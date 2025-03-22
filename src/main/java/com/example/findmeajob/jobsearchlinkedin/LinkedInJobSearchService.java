package com.example.findmeajob.jobsearchlinkedin;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;


import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LinkedInJobSearchService {

    @Value("${rapidapi.key}")
    private String rapidApiKey;
    Map<String, String> geoIds = new HashMap<>(){{
        put("Bengaluru","105214831");
        put("Kochi","100100512");
        put("Chennai","106888327");
        put("Mumbai","106164952");
        put("Delhi","106187582");
        put("Kolkata","111795395");
    }};

    public Map<String,Object> searchJobs(String jobTitle, String location) throws IOException, InterruptedException {


        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://linkedin-api8.p.rapidapi.com/search-jobs?keywords=" + jobTitle + "&locationId=" + geoIds.get(location) + "&datePosted=anyTime&sort=mostRelevant"))
                .header("x-rapidapi-key", rapidApiKey)
                .header("x-rapidapi-host", "linkedin-api8.p.rapidapi.com")
                .method("GET", HttpRequest.BodyPublishers.noBody())
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());


        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode rootNode = objectMapper.readTree(response.body());

        JsonNode bodyContent = rootNode.get("data");

        // Extract 'data' array

        List<Map<String,String>> companyDetails = new ArrayList<>();
        for (JsonNode job : bodyContent) {
            Map<String,String> mp = new HashMap<>();
            String companyName = job.get("company").get("name").asText();
            String url = job.get("url").asText();
            String companyLocation = job.get("location").asText();
            mp.put("name",companyName);
            mp.put("applyUrl",url);
            mp.put("location",companyLocation);
            companyDetails.add(mp);
        }
        Map<String,Object> root = new HashMap<>();
        root.put("companies",companyDetails);
       return root;

    }
}
