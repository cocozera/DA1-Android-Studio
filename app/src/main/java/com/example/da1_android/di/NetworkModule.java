package com.example.da1_android.di;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.da1_android.data.api.AuthService;
import com.example.da1_android.data.api.RouteService;
import com.example.da1_android.data.api.UserService;
import com.example.da1_android.data.prefs.UserPrefsManager;
import com.example.da1_android.remote.interceptor.AuthInterceptor;

import java.io.IOException;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {

    private static final String BASE_URL = "http://10.0.2.2:8080";

    @Provides
    @Singleton
    public Retrofit provideRetrofit(OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Provides
    @Singleton
    public OkHttpClient provideOkHttpClient(
            @ApplicationContext Context context,
            UserPrefsManager userPrefsManager
    ) {
        return new OkHttpClient.Builder()
                .addInterceptor(new AuthInterceptor(context, userPrefsManager))
                .build();
    }

    @Provides
    @Singleton
    public AuthService provideAuthService(Retrofit retrofit) {
        return retrofit.create(AuthService.class);
    }

    @Provides
    @Singleton
    public RouteService provideRouteService(Retrofit retrofit) {
        return retrofit.create(RouteService.class);
    }

    @Provides
    @Singleton
    public UserService provideUserService(Retrofit retrofit) {
        return retrofit.create(UserService.class);
    }

}

