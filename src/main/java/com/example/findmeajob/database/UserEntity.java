package com.example.findmeajob.database;

import jakarta.persistence.*;



@Entity
@Table(name = "user_resumes")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(columnDefinition = "TEXT")
    private String parsedResumeJson;

    @Column(columnDefinition = "TEXT")
    private String referralMessage;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParsedResumeJson() {
        return parsedResumeJson;
    }

    public void setParsedResumeJson(String parsedResumeJson) {
        this.parsedResumeJson = parsedResumeJson;
    }

    public String getReferralMessage() {
        return referralMessage;
    }

    public void setReferralMessage(String referralMessage) {
        this.referralMessage = referralMessage;
    }
}
