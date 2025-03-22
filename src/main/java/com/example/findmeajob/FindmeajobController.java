package com.example.findmeajob;

import com.example.findmeajob.database.UserEntity;
import com.example.findmeajob.jobsearchlinkedin.LinkedInJobSearchService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping
public class FindmeajobController {
    private final FindmeajobService resumeParserService;
    private final ResumeAnalysisService resumeAnalysisService;
    private final LinkedInJobSearchService linkedInJobSearchService;

    public FindmeajobController(FindmeajobService resumeParserService, ResumeAnalysisService resumeAnalysisService, LinkedInJobSearchService linkedInJobSearchService) {
        this.resumeParserService = resumeParserService;
        this.resumeAnalysisService = resumeAnalysisService;
        this.linkedInJobSearchService = linkedInJobSearchService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadResume(@RequestParam("file") MultipartFile file) {
        try {
            String extractedText = resumeParserService.extractText(file);
            return ResponseEntity.ok(extractedText);
        } catch (IOException e) {
            return ResponseEntity.status(500).body("Error processing resume: " + e.getMessage());
        }
    }
    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeResume(@RequestParam("file") MultipartFile file,
                                                             @RequestParam("email") String userEmail) {
        try {
            String extractedText = resumeParserService.extractText(file);
            Map<String, Object> analysis = resumeAnalysisService.analyzeResume(extractedText, userEmail);
            return ResponseEntity.ok(analysis);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error processing resume: " + e.getMessage()));
        }
    }

    @PostMapping("/user")
    public ResponseEntity<?> getUserResume(@RequestParam("email") String email) {
        Optional<UserEntity> userResume = resumeAnalysisService.getUserResume(email);

        if (userResume.isPresent()) {
            return ResponseEntity.ok(userResume.get());
        } else {
            return ResponseEntity.ok(Map.of("message", "No resume data found."));
        }
    }
    @GetMapping("/search-jobs")
    public Map<String, Object> searchJobs(@RequestParam String jobTitle, @RequestParam String location) throws IOException, InterruptedException {
        return linkedInJobSearchService.searchJobs(jobTitle, location);
    }
}


