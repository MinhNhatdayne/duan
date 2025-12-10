package com.example.nhakhoaapp.models.request;

public class ForgotRequest {
    private String email;

    public ForgotRequest(String email) {
        this.email = email;
    }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}