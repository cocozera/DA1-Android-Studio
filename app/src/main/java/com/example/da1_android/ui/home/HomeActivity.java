package com.example.da1_android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.da1_android.R;
import com.example.da1_android.data.api.RouteService;
import com.example.da1_android.data.api.UserService;
import com.example.da1_android.data.model.InProgressRouteDTO;
import com.example.da1_android.data.model.UserDTO;
import com.example.da1_android.data.prefs.UserPrefsManager;
import com.example.da1_android.ui.login.LoginActivity;
import com.example.da1_android.ui.routes.fragments.InProgressRouteFragment;
import com.example.da1_android.ui.routes.fragments.RouteHistoryFragment;
import com.example.da1_android.ui.routes.fragments.ViewAllRoutesFragment;
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
    private View topBar;
    private TextView titleTextView;
    private FrameLayout fragmentContainer;
    private SwipeRefreshLayout swipeRefreshLayout;

    @Inject RouteService routeService;
    @Inject UserService userService;
    @Inject UserPrefsManager userPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Si no hay token, redirige al login
        if (userPrefsManager.getToken() == null) {
            redirigirAlLogin();
            return;
        }

        setContentView(R.layout.activity_home);

        // Vinculación de vistas
        swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        profileButton = findViewById(R.id.profileButton);
        availableRoutesCard = findViewById(R.id.availableRoutesCard);
        routeHistoryCard = findViewById(R.id.routeHistoryCard);
        btnInProgress = findViewById(R.id.btnInProgress);
        topBar = findViewById(R.id.topBar);
        titleTextView = findViewById(R.id.titleTextView);
        fragmentContainer = findViewById(R.id.fragment_container);

        // Configurar Swipe to Refresh solo en Home
        swipeRefreshLayout.setOnRefreshListener(this::verificarRutaEnProgreso);
        swipeRefreshLayout.setEnabled(true);

        // Cargar nombre desde el endpoint y saludar
        Long userId = userPrefsManager.getUserId();
        if (userId != null) {
            userService.getMe(userId).enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        String nombre = response.body().getName();
                        titleTextView.setText("¡Bienvenido, " + nombre + "!");
                    } else {
                        titleTextView.setText("¡Bienvenido!");
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Log.e(TAG, "Error al obtener usuario: " + t.getMessage(), t);
                    titleTextView.setText("¡Bienvenido!");
                }
            });
        } else {
            titleTextView.setText("¡Bienvenido!");
        }

        fragmentContainer.setVisibility(View.GONE);

        // Navegación
        profileButton.setOnClickListener(v -> startActivity(new Intent(this, ProfileActivity.class)));
        availableRoutesCard.setOnClickListener(v -> {
            ocultarHomeUI();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ViewAllRoutesFragment())
                    .addToBackStack(null)
                    .commit();
        });
        routeHistoryCard.setOnClickListener(v -> {
            ocultarHomeUI();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new RouteHistoryFragment())
                    .addToBackStack(null)
                    .commit();
        });
        btnInProgress.setOnClickListener(v -> {
            ocultarHomeUI();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new InProgressRouteFragment())
                    .addToBackStack(null)
                    .commit();
        });

        // Listener para mostrar home al volver de fragments
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                mostrarHomeUI();
            }
        });

        // Verificar rutas en progreso al inicio
        verificarRutaEnProgreso();
    }

    private void verificarRutaEnProgreso() {
        Long userId = userPrefsManager.getUserId();
        routeService.getInProgressRoutes(userId)
                .enqueue(new Callback<List<InProgressRouteDTO>>() {
                    @Override
                    public void onResponse(Call<List<InProgressRouteDTO>> call, Response<List<InProgressRouteDTO>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            btnInProgress.setVisibility(View.VISIBLE);
                        } else {
                            btnInProgress.setVisibility(View.GONE);
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onFailure(Call<List<InProgressRouteDTO>> call, Throwable t) {
                        Log.e(TAG, "Error al verificar ruta en progreso: " + t.getMessage(), t);
                        btnInProgress.setVisibility(View.GONE);
                        swipeRefreshLayout.setRefreshing(false);
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
        swipeRefreshLayout.setEnabled(false);  // Desactivar refresh en fragments
    }

    private void mostrarHomeUI() {
        topBar.setVisibility(View.VISIBLE);
        titleTextView.setVisibility(View.VISIBLE);
        availableRoutesCard.setVisibility(View.VISIBLE);
        routeHistoryCard.setVisibility(View.VISIBLE);
        swipeRefreshLayout.setEnabled(true);   // Reactivar refresh en home
        verificarRutaEnProgreso();
        fragmentContainer.setVisibility(View.GONE);
    }

    private void redirigirAlLogin() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
}
}