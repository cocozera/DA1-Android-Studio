package com.example.da1_android.ui.password;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.R;
import com.example.da1_android.data.api.AuthService;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.RecoverPasswordRequest;
import com.example.da1_android.ui.login.ChangePasswordActivity;
import com.example.da1_android.ui.login.LoginActivity;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RecoverPasswordActivity extends AppCompatActivity {

    @Inject
    AuthService authService;

    private EditText emailEditText;
    private Button sendCodeButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recover_password);

        emailEditText = findViewById(R.id.emailEditText);
        sendCodeButton = findViewById(R.id.sendCodeButton);

        sendCodeButton.setOnClickListener(v -> {
            String email = emailEditText.getText().toString();

            RecoverPasswordRequest request = new RecoverPasswordRequest(email);

            authService.recoverPassword(request).enqueue(new Callback<AuthResponse>() {
                @Override
                public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RecoverPasswordActivity.this, "Código enviado al correo", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(RecoverPasswordActivity.this, ChangePasswordActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RecoverPasswordActivity.this, "Correo no registrado", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<AuthResponse> call, Throwable t) {
                    Toast.makeText(RecoverPasswordActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });


        // Asociar el texto clickeable al método goToLogin
        TextView loginText = findViewById(R.id.loginText);
        loginText.setOnClickListener(v -> goToLogin());
    }

    // Método para navegar a la actividad de inicio de sesión
    public void goToLogin() {
        Intent intent = new Intent(RecoverPasswordActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}
