package com.example.da1_android.data.api;

import com.example.da1_android.data.model.UserDTO;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Query;

public interface UserService {

    @GET("/api/users/me")
    Call<UserDTO> getMe(@Query("userId") Long userId);
}
