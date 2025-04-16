package com.example.da1_android.ui.routes;

import android.content.Intent;
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
import com.example.da1_android.data.model.RouteDTO;
import com.example.da1_android.data.model.RouteDetailDTO;
import com.example.da1_android.data.prefs.UserPrefsManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ViewAllRoutesFragment extends Fragment {

    @Inject
    RouteService routeService;

    @Inject
    UserPrefsManager userPrefsManager;

    private ListView listViewRoutes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_all_routes, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        listViewRoutes = view.findViewById(R.id.listViewRoutes);
        ImageButton buttonBack = view.findViewById(R.id.buttonBack);

        // ✅ Agregamos animación como en historial
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

        fetchRoutes();
    }

    private void fetchRoutes() {
        String token = userPrefsManager.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "No se encontró el token. Inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
            return;
        }

        routeService.getAllRoutes("Bearer " + token).enqueue(new Callback<List<RouteDTO>>() {
            @Override
            public void onResponse(Call<List<RouteDTO>> call, Response<List<RouteDTO>> response) {
                if (response.isSuccessful()) {
                    List<RouteDTO> routeList = response.body();
                    RouteAdapter routeAdapter = new RouteAdapter(requireContext(), routeList);
                    listViewRoutes.setAdapter(routeAdapter);
                    listViewRoutes.setOnItemClickListener((parent, view, position, id) -> {
                        RouteDTO selectedRoute = routeList.get(position);
                        fetchRouteDetails(selectedRoute.getId());
                    });
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RouteDTO>> call, Throwable t) {
                Log.e("ViewAllRoutesFragment", "Error: " + t.getMessage());
                Toast.makeText(requireContext(), "Error al cargar las rutas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRouteDetails(Long routeId) {
        String token = userPrefsManager.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "No se encontró el token. Inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
            return;
        }

        routeService.getRouteDetails(routeId, "Bearer " + token).enqueue(new Callback<RouteDetailDTO>() {
            @Override
            public void onResponse(Call<RouteDetailDTO> call, Response<RouteDetailDTO> response) {
                if (response.isSuccessful()) {
                    RouteDetailDTO detail = response.body();
                    Intent intent = new Intent(requireContext(), RouteDetailActivity.class);
                    intent.putExtra("routeDetail", detail);
                    startActivity(intent);
                } else {
                    Toast.makeText(requireContext(), "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RouteDetailDTO> call, Throwable t) {
                Log.e("ViewAllRoutesFragment", "Error: " + t.getMessage());
                Toast.makeText(requireContext(), "Error al cargar los detalles", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
