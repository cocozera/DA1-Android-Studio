package com.example.da1_android;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import com.example.da1_android.LoginActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Redirigir directamente al login
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish(); // Opcional, para que no vuelva al MainActivity al presionar atrás
    }
}
