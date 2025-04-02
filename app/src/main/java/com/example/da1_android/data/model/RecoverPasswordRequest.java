package com.example.da1_android.data.model;

public class RecoverPasswordRequest {
    private String email;

    // Constructor, getters y setters
    public RecoverPasswordRequest(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}

