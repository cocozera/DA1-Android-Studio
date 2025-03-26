package com.example.da1_android.data.api;


import com.example.da1_android.data.model.LoginRequest;
import com.example.da1_android.data.model.AuthResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {

    @POST("/api/auth/login")
    Call<AuthResponse> login(@Body LoginRequest request);

}
