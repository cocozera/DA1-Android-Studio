package com.example.da1_android.ui.login;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.R;
import com.example.da1_android.data.api.AuthService;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.ChangePasswordRequest;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ChangePasswordActivity extends AppCompatActivity {

    @Inject
    AuthService authService;

    private EditText codeEditText, newPasswordEditText, emailEditText;
    private Button changePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        codeEditText = findViewById(R.id.codeEditText);
        newPasswordEditText = findViewById(R.id.newPasswordEditText);
        emailEditText = findViewById(R.id.emailEditText);
        changePasswordButton = findViewById(R.id.changePasswordButton);

        String email = getIntent().getStringExtra("email");
        if (email != null) {
            emailEditText.setText(email);
        }

        changePasswordButton.setOnClickListener(v -> {
            String enteredEmail = emailEditText.getText().toString();
            String code = codeEditText.getText().toString();
            String newPassword = newPasswordEditText.getText().toString();

            ChangePasswordRequest request = new ChangePasswordRequest(enteredEmail, code, newPassword);

            authService.changePasswordWithCode(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(ChangePasswordActivity.this, "Contraseña actualizada ✅", Toast.LENGTH_LONG).show();
                        finish(); // Podés redirigir al login si querés
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Código inválido o expirado ❌", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(ChangePasswordActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });

    }
}
