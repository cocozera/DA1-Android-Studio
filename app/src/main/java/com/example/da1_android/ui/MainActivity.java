package com.example.da1_android.ui;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.data.prefs.UserPrefsManager;
import com.example.da1_android.ui.home.HomeActivity;
import com.example.da1_android.ui.login.LoginActivity;

import javax.inject.Inject;

public class MainActivity extends AppCompatActivity {
    @Inject
    UserPrefsManager userPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent;

        if (userPrefsManager.getToken() != null) {
            intent = new Intent(this, HomeActivity.class);
        } else {
            intent = new Intent(this, LoginActivity.class);
        }

        startActivity(intent);
        finish();
    }

}
