package com.example.da1_android.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.da1_android.R;
import com.example.da1_android.data.api.AuthService;
import com.example.da1_android.data.prefs.UserPrefsManager;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.LoginRequest;
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

    // Inyectamos el UserPrefsManager para manejar el almacenamiento encriptado
    @Inject
    UserPrefsManager userPrefsManager;

    private EditText emailEditText, passwordEditText;
    private Button loginButton, registerButton;
    private TextView forgotPasswordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        forgotPasswordText = findViewById(R.id.forgotPasswordText);

        loginButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();
            String password = passwordEditText.getText().toString();
            LoginRequest request = new LoginRequest(email, password);

            authService.login(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        AuthResponse authResponse = response.body();
                        // Guardamos el token y el userId utilizando UserPrefsManager
                        userPrefsManager.saveAuthResponse(authResponse);

                        Toast.makeText(LoginActivity.this, "Login exitoso. Token guardado.", Toast.LENGTH_LONG).show();
                        // Aquí podrías navegar a otra pantalla, por ejemplo, la MainActivity
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
