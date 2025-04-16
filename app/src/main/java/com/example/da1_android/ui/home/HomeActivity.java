package com.example.da1_android.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.R;
import com.example.da1_android.ui.home.ProfileActivity;
import com.example.da1_android.ui.routes.RouteHistoryFragment;
import com.example.da1_android.ui.routes.ViewAllRoutesFragment;
import com.google.android.material.card.MaterialCardView;

import android.content.Intent;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private ImageButton profileButton;
    private MaterialCardView availableRoutesCard, routeHistoryCard;
    private View topBar, titleTextView;
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Referencias UI
        profileButton = findViewById(R.id.profileButton);
        availableRoutesCard = findViewById(R.id.availableRoutesCard);
        routeHistoryCard = findViewById(R.id.routeHistoryCard);
        topBar = findViewById(R.id.topBar);
        titleTextView = findViewById(R.id.titleTextView);
        fragmentContainer = findViewById(R.id.fragment_container);

        fragmentContainer.setVisibility(View.GONE); // Al inicio oculto

        // Botón de perfil (puede ser pantalla futura)
        profileButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        // Rutas disponibles → ViewAllRoutesFragment
        availableRoutesCard.setOnClickListener(v -> {
            ocultarHomeUI();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ViewAllRoutesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Historial de rutas → RouteHistoryFragment
        routeHistoryCard.setOnClickListener(v -> {
            ocultarHomeUI();

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RouteHistoryFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Cuando no quedan fragments, restauramos la UI
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                mostrarHomeUI();
            }
        });
    }

    private void ocultarHomeUI() {
        topBar.setVisibility(View.GONE);
        titleTextView.setVisibility(View.GONE);
        availableRoutesCard.setVisibility(View.GONE);
        routeHistoryCard.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
    }

    private void mostrarHomeUI() {
        topBar.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.VISIBLE);
        availableRoutesCard.setVisibility(View.VISIBLE);
        routeHistoryCard.setVisibility(View.VISIBLE);
        fragmentContainer.setVisibility(View.GONE);
    }
}
