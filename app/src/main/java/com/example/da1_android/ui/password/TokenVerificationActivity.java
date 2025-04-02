package com.example.da1_android.ui.password;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.R;
import com.example.da1_android.data.api.AuthService;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.VerifyTokenRequest;
import com.example.da1_android.ui.login.LoginActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class TokenVerificationActivity extends AppCompatActivity {

    @Inject
    AuthService authService;

    private EditText emailEditText, tokenEditText;
    private Button verifyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_token_verification);

        emailEditText = findViewById(R.id.emailEditText);
        tokenEditText = findViewById(R.id.tokenEditText);
        verifyButton = findViewById(R.id.verifyButton);

        // Autocompletar email si fue pasado desde el intent
        String email = getIntent().getStringExtra("email");
        if (email != null) {
            emailEditText.setText(email);
        }

        verifyButton.setOnClickListener(v -> {
            String inputEmail = emailEditText.getText().toString();
            String inputCode = tokenEditText.getText().toString();

            VerifyTokenRequest request = new VerifyTokenRequest(inputEmail, inputCode);

            authService.verifyToken(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(TokenVerificationActivity.this, "Cuenta verificada correctamente ✅", Toast.LENGTH_LONG).show();
                        startActivity(new Intent(TokenVerificationActivity.this, LoginActivity.class));
                        finish();
                    } else {
                        Toast.makeText(TokenVerificationActivity.this, "Código inválido ❌", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(TokenVerificationActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
    public void goToLogin(View view) {
        Intent intent = new Intent(TokenVerificationActivity.this, LoginActivity.class);
        startActivity(intent);
    }

}
