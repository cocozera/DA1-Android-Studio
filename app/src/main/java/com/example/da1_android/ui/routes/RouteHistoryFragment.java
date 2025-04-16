package com.example.da1_android.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.da1_android.R;
import com.example.da1_android.data.api.RouteService;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.CompletedRouteDTO;
import com.example.da1_android.data.prefs.UserPrefsManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class RouteHistoryFragment extends Fragment {

    private static final String TAG = "RouteHistoryFragment";
    private ListView listViewRoutes;
    private CompletedRoutesAdapter adapter;

    @Inject
    RouteService routeService;

    @Inject
    UserPrefsManager userPrefsManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_history, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ImageButton buttonBack = view.findViewById(R.id.buttonBack);
        listViewRoutes = view.findViewById(R.id.listViewRoutes);

        // Animación simple sin usar archivos res/anim
        buttonBack.setOnClickListener(v -> {
            ScaleAnimation scaleDown = new ScaleAnimation(
                    1f, 0.85f, 1f, 0.85f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f,
                    ScaleAnimation.RELATIVE_TO_SELF, 0.5f
            );
            scaleDown.setDuration(100);
            scaleDown.setFillAfter(true);

            v.startAnimation(scaleDown);

            v.postDelayed(() -> {
                v.clearAnimation();
                requireActivity().onBackPressed();
            }, 150);
        });

        adapter = new CompletedRoutesAdapter(requireContext());
        listViewRoutes.setAdapter(adapter);

        String token = userPrefsManager.getToken();
        Long userId = userPrefsManager.getUserId();

        Log.d(TAG, "Token obtenido: " + token);
        Log.d(TAG, "UserId obtenido: " + userId);

        if (token == null || userId == null) {
            Log.e(TAG, "No se encontró token o userId. Asegúrate de haber guardado la respuesta de autenticación.");

            AuthResponse dummyAuth = new AuthResponse();
            dummyAuth.setToken("dummy_token");
            dummyAuth.setUserId(1L);
            userPrefsManager.saveAuthResponse(dummyAuth);

            token = userPrefsManager.getToken();
            userId = userPrefsManager.getUserId();
            Log.d(TAG, "Valores dummy guardados: Token = " + token + ", UserId = " + userId);
            Toast.makeText(requireContext(), "Se han guardado valores de prueba", Toast.LENGTH_SHORT).show();
        }

        routeService.getCompletedRoutes(userId, "Bearer " + token).enqueue(new Callback<List<CompletedRouteDTO>>() {
            @Override
            public void onResponse(Call<List<CompletedRouteDTO>> call, Response<List<CompletedRouteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CompletedRouteDTO> routes = response.body();
                    Log.d(TAG, "Número de rutas completadas recibidas: " + routes.size());
                    adapter.setRoutes(routes);
                } else {
                    Log.e(TAG, "Respuesta no exitosa: " + response.message());
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompletedRouteDTO>> call, Throwable t) {
                Log.e(TAG, "Fallo de red: " + t.getMessage(), t);
                Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
