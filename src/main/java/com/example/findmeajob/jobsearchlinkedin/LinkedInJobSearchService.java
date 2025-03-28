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
        put("bengaluru","105214831");
        put("bangalore","105214831");
        put("kochi","100100512");
        put("chennai","106888327");
        put("mumbai","106164952");
        put("new delhi","106187582");
        put("delhi","106187582");
        put("kolkata","111795395");
    }};

    public Map<String,Object> searchJobs(String jobTitle, String location) throws IOException, InterruptedException {


        jobTitle = jobTitle.replaceAll(" ","%20");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://linkedin-data-api.p.rapidapi.com/search-jobs-v2?keywords=" + jobTitle + "&locationId=" + geoIds.get(location.toLowerCase()) + "&datePosted=anyTime&sort=mostRelevant"))
                .header("x-rapidapi-key", rapidApiKey)
                .header("x-rapidapi-host", "linkedin-data-api.p.rapidapi.com")
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
