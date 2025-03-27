package com.example.da1_android.data.prefs;

import android.content.SharedPreferences;
import com.example.da1_android.data.model.AuthResponse;
import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class UserPrefsManager {

    private static final String KEY_TOKEN = "jwt_token";
    private static final String KEY_USER_ID = "user_id";

    private final SharedPreferences encryptedSharedPreferences;

    @Inject
    public UserPrefsManager(SharedPreferences encryptedSharedPreferences) {
        this.encryptedSharedPreferences = encryptedSharedPreferences;
    }

    // Guarda el JWT y el userId en las EncryptedSharedPreferences
    public void saveAuthResponse(AuthResponse authResponse) {
        encryptedSharedPreferences.edit()
                .putString(KEY_TOKEN, authResponse.getToken())
                .putLong(KEY_USER_ID, authResponse.getUserId() != null ? authResponse.getUserId() : -1L)
                .apply();
    }

    // Recupera el JWT
    public String getToken() {
        return encryptedSharedPreferences.getString(KEY_TOKEN, null);
    }

    // Recupera el userId (devuelve null si no está presente)
    public Long getUserId() {
        long userId = encryptedSharedPreferences.getLong(KEY_USER_ID, -1L);
        return userId != -1L ? userId : null;
    }
}
