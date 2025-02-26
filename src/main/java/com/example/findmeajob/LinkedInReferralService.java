package com.example.findmeajob;


import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.ChatCompletionCreateParams;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class LinkedInReferralService {
    private final OpenAIClient client;

    public LinkedInReferralService(@Value("${openai.api.key}") String apiKey) {
        this.client = new OpenAIOkHttpClient.Builder().apiKey(apiKey).build();
    }

    public String generateReferralMessage(Map<String, Object> resumeData,String customMsg) {
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
        return response.choices().get(0).message().content().orElse("").replaceFirst("^Optional\\[(.*)]$", "$1");
    }
}
