package com.example.findmeajob;

import com.example.findmeajob.jobsearchlinkedin.LinkedInJobSearchService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ResumeAnalysisService {
    private final OpenAIClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();
//    private final UserResumeRepository userResumeRepository;
    private final LinkedInReferralService linkedInReferralService;
    private final LinkedInJobSearchService linkedInJobSearchService;

    public ResumeAnalysisService(@Value("${openai.api.key}") String apiKey,LinkedInReferralService linkedInReferralService,LinkedInJobSearchService linkedInJobSearchService) {
        this.client = new OpenAIOkHttpClient.Builder().apiKey(apiKey).build();
        this.linkedInReferralService = linkedInReferralService;
        this.linkedInJobSearchService = linkedInJobSearchService;
    }

    public Map<String, Object> analyzeResume(String resumeText,String jobLocation) throws JsonProcessingException {
        String prompt = """
                Extract the following details from the resume and return as a JSON object:
                {
                  "name": "Full Name",
                  "experience": [
                    { "company": "Company Name", "role": "Job Title", "duration": "Start - End" }
                  ],
                  "relevantYrOfExperience": "No. of years of relevant non- internship experience I have in this resume",
                  "projects":[{"project":"Project Name 1", "tools":"Tools used"},{"project":"Project Name 2", "tools":"Tools used"}],
                  "skills": ["Skill1", "Skill2"],
                  "education": [
                    { "degree": "Degree Name", "institution": "Institution Name", "year": "Year of Graduation" }
                  ],
                  "experienceLevel":"According to experience level(don't consider internship while taking) in this resume give value in this field from anyone of these : internship(if in college), associate(experience from 1 to 2), director(10yrs+), entryLevel(0-1 yrs), midSeniorLevel(3+)",
                  "role":"role apt for this resume according to experience provided"
                }
                
                Resume:
                """ + resumeText;

        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("gpt-3.5-turbo")
                .addUserMessage(prompt)
                .build();

        var response = client.chat().completions().create(request);
        String content = linkedInReferralService.getContent(response);


        Map<String, Object> parsedResume = objectMapper.readValue(content, Map.class);
        try {
            String referralMessage = linkedInReferralService.generateReferralMessage(parsedResume, "");
            String coldEmail = linkedInReferralService.generateColdEmail(parsedResume,"");
            Object jobs = linkedInJobSearchService.searchJobs(parsedResume.get("role").toString(),jobLocation).get("companies");
//            UserEntity userResume = userResumeRepository.findByEmail(userEmail).orElse(new UserEntity());
//            userResume.setEmail(userEmail);
//            userResume.setParsedResumeJson(parsedResumeJson);
//            userResume.setReferralMessage(referralMessage);
//            userResumeRepository.save(userResume);
            parsedResume.put("referralMessage", referralMessage);
            parsedResume.put("coldEmail",coldEmail);
            parsedResume.put("jobs",jobs);
            return parsedResume;
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }


}
