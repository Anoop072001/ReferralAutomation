package com.example.findmeajob;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletion;
import com.openai.models.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LinkedInReferralService {
    private final OpenAIClient client;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LinkedInReferralService(@Value("${openai.api.key}") String apiKey) {
        this.client = new OpenAIOkHttpClient.Builder().apiKey(apiKey).build();
    }

    public String generateReferralMessage(Map<String, Object> resumeData,String customMsg) throws JsonProcessingException {
        String prompt;
        if(customMsg != null) {
            prompt = """
                    Act like you are""" + resumeData.get("name") + """
                    and you are writing this message based on the following resume details about yourself, generate a professional Linkedin message asking for a referral in their company XYZ:
                    with Experience:""" + resumeData.get("experience") + """
                    and Skills:""" + resumeData.get("skills") + """
                    with Education:""" + resumeData.get("education") + """
                    and"""+resumeData.get("name")+"""
                    also wanted this message added in this"""+customMsg+""" 
                    The message should be engaging, concise, and professional.
                    """;
        }
        else{
            prompt = """
                    Act like you are""" + resumeData.get("name") + """
                    and you are writing this message based on the following resume details about yourself, generate a professional Linkedin message asking for a referral in their company XYZ:
                    with Experience:""" + resumeData.get("experience") + """
                    and Skills:""" + resumeData.get("skills") + """
                    with Education:""" + resumeData.get("education") + """
                    
                    The message should be engaging, concise, and professional.
                    """;
        }
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("gpt-3.5-turbo")
                .addUserMessage(prompt)
                .build();

        var response = client.chat().completions().create(request);
        return getContent(response);
    }

    public String generateColdEmail(Map<String, Object> resumeData,String customMsg) throws JsonProcessingException {
        String prompt;
        if(customMsg != null) {
            prompt = """
                    Act like you are""" + resumeData.get("name") + """
                    and you are writing this cold email based on the following resume details about yourself, generate a professional cold mail asking for a referral in their company XYZ:
                    with Experience:""" + resumeData.get("experience") + """
                    and Skills:""" + resumeData.get("skills") + """
                    with Education:""" + resumeData.get("education") + """
                    and"""+resumeData.get("name")+"""
                    also wanted this message added in this"""+customMsg+""" 
                    The email should be engaging, concise, and professional.
                    """;
        }
        else{
            prompt = """
                    Act like you are""" + resumeData.get("name") + """
                    and you are writing this cold email based on the following resume details about yourself, generate a professional cold mail asking for a referral in their company XYZ:
                    with Experience:""" + resumeData.get("experience") + """
                    and Skills:""" + resumeData.get("skills") + """
                    with Education:""" + resumeData.get("education") + """
                    
                    The email should be engaging, concise, and professional.
                    """;
        }
        ChatCompletionCreateParams request = ChatCompletionCreateParams.builder()
                .model("gpt-3.5-turbo")
                .addUserMessage(prompt)
                .build();

        var response = client.chat().completions().create(request);
        return getContent(response);
    }

    public String getContent(ChatCompletion response) throws JsonProcessingException {
        String responseJson = objectMapper.writeValueAsString(response);
        System.out.println(responseJson);
        Map<String, Object> responseMap = objectMapper.readValue(responseJson, Map.class);
        List<Map<String, Object>> choices = (List<Map<String, Object>>) responseMap.get("choices");
        if (choices == null || choices.isEmpty()) {
            throw new RuntimeException("No choices in OpenAI response: " + responseJson);
        }
        Map<String, Object> choice = choices.get(0);
        Map<String, Object> message = (Map<String, Object>) choice.get("message");
        if (message == null) {
            throw new RuntimeException("No message in OpenAI response: " + responseJson);
        }
        String content = (String) message.get("content");
        if (content == null) {
            throw new RuntimeException("No content in OpenAI response: " + responseJson);
        }
        return content;
    }
}
