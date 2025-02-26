package com.example.findmeajob;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@RequestMapping
public class FindmeajobController {
    private final FindmeajobService resumeParserService;
    private final ResumeAnalysisService resumeAnalysisService;
    private final LinkedInReferralService referralGenerationService;

    public FindmeajobController(FindmeajobService resumeParserService, ResumeAnalysisService resumeAnalysisService, LinkedInReferralService referralGenerationService) {
        this.resumeParserService = resumeParserService;
        this.resumeAnalysisService = resumeAnalysisService;
        this.referralGenerationService = referralGenerationService;
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
    public ResponseEntity<Map<String, Object>> analyzeResume(@RequestParam("file") MultipartFile file) {
        try {
            String extractedText = resumeParserService.extractText(file);
            Map<String, Object> analysis = resumeAnalysisService.analyzeResume(extractedText);
            return ResponseEntity.ok(analysis);
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error processing resume: " + e.getMessage()));
        }
    }
    @PostMapping("/generate-referral")
    public ResponseEntity<Map<String, String>> generateReferral(@RequestParam("file") MultipartFile file,@RequestParam("customMsg") String customMsg) {
        try {
            String extractedText = resumeParserService.extractText(file);
            Map<String, Object> analysis = resumeAnalysisService.analyzeResume(extractedText);
            String referralMessage = referralGenerationService.generateReferralMessage(analysis,customMsg);
            return ResponseEntity.ok(Map.of("referralMessage", referralMessage));
        } catch (IOException e) {
            return ResponseEntity.status(500).body(Map.of("error", "Error processing resume: " + e.getMessage()));
        }
    }

}


