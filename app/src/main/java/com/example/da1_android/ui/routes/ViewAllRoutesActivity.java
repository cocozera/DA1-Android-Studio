package com.example.da1_android.ui.routes;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

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
public class ViewAllRoutesActivity extends AppCompatActivity {

    @Inject
    RouteService routeService;

    @Inject
    UserPrefsManager userPrefsManager;

    private ListView listViewRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_routes);

        listViewRoutes = findViewById(R.id.listViewRoutes);
        fetchRoutes();

        ImageButton buttonBack = findViewById(R.id.buttonBack);
        buttonBack.setOnClickListener(v -> finish());

    }

    private void fetchRoutes() {
        String token = userPrefsManager.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "No se encontró el token. Inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
            return;
        }

        routeService.getAllRoutes("Bearer " + token).enqueue(new Callback<List<RouteDTO>>() {
            @Override
            public void onResponse(Call<List<RouteDTO>> call, Response<List<RouteDTO>> response) {
                if (response.isSuccessful()) {
                    List<RouteDTO> routeList = response.body();
                    RouteAdapter routeAdapter = new RouteAdapter(ViewAllRoutesActivity.this, routeList);
                    listViewRoutes.setAdapter(routeAdapter);

                    listViewRoutes.setOnItemClickListener((parent, view, position, id) -> {
                        RouteDTO selectedRoute = routeList.get(position);
                        fetchRouteDetails(selectedRoute.getId());
                    });
                } else {
                    Toast.makeText(ViewAllRoutesActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RouteDTO>> call, Throwable t) {
                Log.e("ViewAllRoutes", "Error: " + t.getMessage());
                Toast.makeText(ViewAllRoutesActivity.this, "Error al cargar las rutas", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchRouteDetails(Long routeId) {
        String token = userPrefsManager.getToken();
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "No se encontró el token. Inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
            return;
        }

        routeService.getRouteDetails(routeId, "Bearer " + token).enqueue(new Callback<RouteDetailDTO>() {
            @Override
            public void onResponse(Call<RouteDetailDTO> call, Response<RouteDetailDTO> response) {
                if (response.isSuccessful()) {
                    RouteDetailDTO detail = response.body();
                    Intent intent = new Intent(ViewAllRoutesActivity.this, RouteDetailActivity.class);
                    intent.putExtra("routeDetail", detail);
                    startActivity(intent);
                } else {
                    Toast.makeText(ViewAllRoutesActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<RouteDetailDTO> call, Throwable t) {
                Log.e("ViewAllRoutes", "Error: " + t.getMessage());
                Toast.makeText(ViewAllRoutesActivity.this, "Error al cargar los detalles", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
