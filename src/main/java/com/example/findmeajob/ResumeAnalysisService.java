package com.example.findmeajob;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;
import java.util.Optional;

@Service
public class ResumeAnalysisService {
    private final OpenAIClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ResumeAnalysisService(@Value("${openai.api.key}") String apiKey) {
        this.client = new OpenAIOkHttpClient.Builder().apiKey(apiKey).build();
    }

    public Map<String, Object> analyzeResume(String resumeText){
        String prompt = """
                Extract the following details from the resume and return as a JSON object:
                {
                  "name": "Full Name",
                  "experience": [
                    { "company": "Company Name", "role": "Job Title", "duration": "Start - End" }
                  ],
                  "projects":[{"project":"Project Name 1", "tools":"Tools used"},{"project":"Project Name 2", "tools":"Tools used"}],
                  "skills": ["Skill1", "Skill2"],
                  "education": [
                    { "degree": "Degree Name", "institution": "Institution Name", "year": "Year of Graduation" }
                  ]
                }
                
                Resume:
                """ + resumeText;

        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("gpt-3.5-turbo")
                .addUserMessage(prompt)
                .build();

        var response = client.chat().completions().create(request);

        Optional<String> analysis = response.choices().get(0).message().content();
        try {
            return objectMapper.readValue(analysis.orElse("").replaceFirst("^Optional\\[(.*)]$", "$1"), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse JSON response", e);
        }
    }
}
