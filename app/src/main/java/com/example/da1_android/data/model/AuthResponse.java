package com.example.da1_android.data.model;

public class AuthResponse {
    private String token;
    private String refreshToken;
    private String message;
    private Long userId;

    public AuthResponse() {}

    public AuthResponse(String token, String refreshToken, String message, Long userId) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.message = message;
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}

