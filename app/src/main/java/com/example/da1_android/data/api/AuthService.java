package com.example.da1_android.data.api;

import com.example.da1_android.data.model.ChangePasswordRequest;
import com.example.da1_android.data.model.LoginRequest;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.RecoverPasswordRequest;
import com.example.da1_android.data.model.RegisterRequest;
import com.example.da1_android.data.model.VerifyTokenRequest;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AuthService {

    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

    @POST("/api/auth/register")
    Call<AuthResponse> register(@Body RegisterRequest request);

    @POST("/api/auth/verify")
    Call<AuthResponse> verifyToken(@Body VerifyTokenRequest request);

    @POST("/api/auth/recover-password")
    Call<AuthResponse> recoverPassword(@Body RecoverPasswordRequest request);

    @POST("/api/auth/change-password")
    Call<AuthResponse> changePasswordWithCode(@Body ChangePasswordRequest request);
}
