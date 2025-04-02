package com.example.da1_android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.R;
import com.example.da1_android.data.api.AuthService;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.LoginRequest;
import com.example.da1_android.data.prefs.UserPrefsManager;
import com.example.da1_android.ui.home.HomeActivity;
import com.example.da1_android.ui.password.RecoverPasswordActivity;
import com.example.da1_android.ui.register.RegisterActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    AuthService authService;

    @Inject
    UserPrefsManager userPrefsManager;

    private EditText emailEditText, passwordEditText;
    private Button loginButton;
    private TextView forgotPasswordText, registerText; // CAMBIO: usar TextView en vez de Button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);
        registerText = findViewById(R.id.registerText); // CAMBIO: ID actualizado

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString().trim();
            String password = passwordEditText.getText().toString().trim();
            LoginRequest request = new LoginRequest(email, password);

            authService.login(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse authResponse = response.body();
                        userPrefsManager.saveAuthResponse(authResponse);

                        Toast.makeText(LoginActivity.this, "Login exitoso", Toast.LENGTH_LONG).show();

                        // Redirigir a HomeActivity
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); // Cierra la actividad de login para que no pueda volver atrás con el botón de retroceso
                    } else {
                        Toast.makeText(LoginActivity.this, "Error de login", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(LoginActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

        registerText.setOnClickListener(v -> { // CAMBIO: nuevo listener para el TextView
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
            startActivity(intent);
        });
    }
}
