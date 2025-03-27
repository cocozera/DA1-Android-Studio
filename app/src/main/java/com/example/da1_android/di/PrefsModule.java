package com.example.da1_android.di;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

import java.io.IOException;
import java.security.GeneralSecurityException;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.qualifiers.ApplicationContext;
import dagger.hilt.components.SingletonComponent;

@Module
@InstallIn(SingletonComponent.class)
public class PrefsModule {

    private static final String PREFS_FILENAME = "secure_prefs";

    @Provides
    @Singleton
    public SharedPreferences provideEncryptedSharedPreferences(@ApplicationContext Context context) {
        try {
            // Crear u obtener el MasterKey para cifrado
            MasterKey masterKey = new MasterKey.Builder(context)
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build();

            // Crear la instancia de EncryptedSharedPreferences
            return EncryptedSharedPreferences.create(
                    context,
                    PREFS_FILENAME,
                    masterKey,
                    EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                    EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            );
        } catch (GeneralSecurityException | IOException e) {
            throw new RuntimeException("Error al inicializar EncryptedSharedPreferences", e);
        }
    }
}
