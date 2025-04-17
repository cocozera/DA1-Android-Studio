package com.example.da1_android.remote.interceptor;

import android.content.Context;
import android.content.Intent;

import com.example.da1_android.data.prefs.UserPrefsManager;
import com.example.da1_android.ui.login.LoginActivity;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AuthInterceptor implements Interceptor {

    private final Context context;
    private final UserPrefsManager userPrefsManager;

    public AuthInterceptor(Context context, UserPrefsManager userPrefsManager) {
        this.context = context;
        this.userPrefsManager = userPrefsManager;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();
        String token = userPrefsManager.getToken();

        Request requestWithToken = originalRequest.newBuilder()
                .header("Authorization", "Bearer " + token)
                .build();

        Response response = chain.proceed(requestWithToken);

        if (response.code() == 401) {
            userPrefsManager.clearAuthData();

            // Redirigir al login
            Intent intent = new Intent(context, LoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            context.startActivity(intent);
        }

        return response;
    }
}
