package com.example.da1_android;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.data.api.AuthService;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.LoginRequest;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {

    @Inject
    AuthService authService;

    private EditText emailEditText, passwordEditText;
    private Button loginButton,registerButton;
    private TextView forgotPasswordText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton); // Esta línea falta
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            LoginRequest request = new LoginRequest(email, password);

            authService.login(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String token = response.body().getToken();
                        Toast.makeText(LoginActivity.this, "Login exitoso. Token: " + token, Toast.LENGTH_LONG).show();
                        // Guardar token o navegar a otra pantalla
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
        registerButton.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
        forgotPasswordText.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RecoverPasswordActivity.class);
            startActivity(intent);
        });



    }

}