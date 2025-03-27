package com.example.da1_android.data.api;

import com.example.da1_android.data.model.LoginRequest;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.RegisterRequest;

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
    Call<AuthResponse> verifyToken(
            @Query("email") String email,
            @Query("code") String code
    );

    @POST("/api/auth/recover-password")
    Call<AuthResponse> recoverPassword(
            @Query("email") String email
    );

    @POST("/api/auth/change-password")
    Call<AuthResponse> changePasswordWithCode(
            @Query("email") String email,
            @Query("code") String code,
            @Query("newPassword") String newPassword
    );

}
