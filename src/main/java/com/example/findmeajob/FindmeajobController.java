package com.example.findmeajob;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping
@CrossOrigin(origins = "https://job-matcher-frontend.vercel.app")
public class FindmeajobController {
    private final FindmeajobService resumeParserService;
    private final ResumeAnalysisService resumeAnalysisService;

    public FindmeajobController(FindmeajobService resumeParserService, ResumeAnalysisService resumeAnalysisService) {
        this.resumeParserService = resumeParserService;
        this.resumeAnalysisService = resumeAnalysisService;
    }

    @PostMapping("/analyze")
    public ResponseEntity<Map<String, Object>> analyzeResume(@RequestParam("file") MultipartFile file,
                                                             @RequestParam("location") String jobLocation) {
        try {
            String extractedText = resumeParserService.extractText(file);
            Map<String, Object> analysis = resumeAnalysisService.analyzeResume(extractedText, jobLocation);
            return ResponseEntity.ok(analysis);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error processing resume: " + e.getMessage()));
        }
    }
//
//    @PostMapping("/user")
//    public ResponseEntity<?> getUserResume(@RequestParam("email") String email) {
//        Optional<UserEntity> userResume = resumeAnalysisService.getUserResume(email);
//
//        if (userResume.isPresent()) {
//            return ResponseEntity.ok(userResume.get());
//        } else {
//            return ResponseEntity.ok(Map.of("message", "No resume data found."));
//        }
//    }

}


