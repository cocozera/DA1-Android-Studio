package com.example.da1_android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.R;
import com.example.da1_android.data.api.RouteService;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.InProgressRouteDTO;
import com.example.da1_android.data.prefs.UserPrefsManager;
import com.example.da1_android.ui.routes.InProgressRouteFragment;
import com.example.da1_android.ui.routes.RouteHistoryFragment;
import com.example.da1_android.ui.routes.ViewAllRoutesFragment;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private static final String TAG = "HomeActivity";

    private ImageButton profileButton;
    private MaterialCardView availableRoutesCard, routeHistoryCard;
    private FloatingActionButton btnInProgress;
    private View topBar, titleTextView;
    private FrameLayout fragmentContainer;

    @Inject
    RouteService routeService;

    @Inject
    UserPrefsManager userPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Referencias UI
        profileButton = findViewById(R.id.profileButton);
        availableRoutesCard = findViewById(R.id.availableRoutesCard);
        routeHistoryCard = findViewById(R.id.routeHistoryCard);
        btnInProgress = findViewById(R.id.btnInProgress);
        topBar = findViewById(R.id.topBar);
        titleTextView = findViewById(R.id.titleTextView);
        fragmentContainer = findViewById(R.id.fragment_container);

        fragmentContainer.setVisibility(View.GONE);

        // Botón de perfil
        profileButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class));
        });

        // Rutas disponibles
        availableRoutesCard.setOnClickListener(v -> {
            ocultarHomeUI();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ViewAllRoutesFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Historial de rutas
        routeHistoryCard.setOnClickListener(v -> {
            ocultarHomeUI();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RouteHistoryFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Rutas en progreso
        btnInProgress.setOnClickListener(v -> {
            ocultarHomeUI();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InProgressRouteFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Volver al Home si no hay más fragments
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                mostrarHomeUI();
            }
        });

        verificarRutaEnProgreso();
    }

    private void verificarRutaEnProgreso() {
        String token = userPrefsManager.getToken();
        Long userId = userPrefsManager.getUserId();

        if (token == null || userId == null) {
            Log.e(TAG, "No se encontró token o userId. Asignando dummy para testing.");

            AuthResponse dummyAuth = new AuthResponse();
            dummyAuth.setToken("dummy_token");
            dummyAuth.setUserId(1L);
            userPrefsManager.saveAuthResponse(dummyAuth);

            token = userPrefsManager.getToken();
            userId = userPrefsManager.getUserId();
            Toast.makeText(this, "Se asignaron valores de prueba", Toast.LENGTH_SHORT).show();
        }

        routeService.getInProgressRoutes(userId, "Bearer " + token)
                .enqueue(new Callback<List<InProgressRouteDTO>>() {
                    @Override
                    public void onResponse(Call<List<InProgressRouteDTO>> call, Response<List<InProgressRouteDTO>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            btnInProgress.setVisibility(View.VISIBLE);
                        } else {
                            btnInProgress.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<InProgressRouteDTO>> call, Throwable t) {
                        Log.e(TAG, "Error al verificar ruta en progreso: " + t.getMessage(), t);
                        btnInProgress.setVisibility(View.GONE);
                    }
                });
    }

    private void ocultarHomeUI() {
        topBar.setVisibility(View.GONE);
        titleTextView.setVisibility(View.GONE);
        availableRoutesCard.setVisibility(View.GONE);
        routeHistoryCard.setVisibility(View.GONE);
        btnInProgress.setVisibility(View.GONE);
        fragmentContainer.setVisibility(View.VISIBLE);
    }

    private void mostrarHomeUI() {
        topBar.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.VISIBLE);
        availableRoutesCard.setVisibility(View.VISIBLE);
        routeHistoryCard.setVisibility(View.VISIBLE);
        verificarRutaEnProgreso(); // <-- Volvemos a verificar al volver al Home
        fragmentContainer.setVisibility(View.GONE);
    }
}
