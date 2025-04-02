package com.example.da1_android.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import dagger.hilt.android.AndroidEntryPoint;
import javax.inject.Inject;
import com.example.da1_android.R;
import com.example.da1_android.data.api.RouteService;
import com.example.da1_android.data.model.RouteDTO;
import com.example.da1_android.data.prefs.UserPrefsManager;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    @Inject
    RouteService routeService;  // Inyección de dependencias con Hilt

    @Inject
    UserPrefsManager userPrefsManager; // Agregar UserPrefsManager para obtener el token


    private ListView listViewRoutes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        listViewRoutes = findViewById(R.id.listViewRoutes);

        fetchRoutes();
    }

    private void fetchRoutes() {
        String token = userPrefsManager.getToken(); // Obtener el token almacenado
        if (token == null || token.isEmpty()) {
            Toast.makeText(this, "No se encontró el token. Inicie sesión nuevamente.", Toast.LENGTH_LONG).show();
            return;
        }

        Call<List<RouteDTO>> call = routeService.getAllRoutes("Bearer " + token); // Pasar el token en la cabecera

        call.enqueue(new Callback<List<RouteDTO>>() {
            @Override
            public void onResponse(Call<List<RouteDTO>> call, Response<List<RouteDTO>> response) {
                if (response.isSuccessful()) {
                    List<RouteDTO> routeList = response.body();
                    RouteAdapter routeAdapter = new RouteAdapter(HomeActivity.this, routeList);
                    listViewRoutes.setAdapter(routeAdapter);
                } else {
                    Toast.makeText(HomeActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<RouteDTO>> call, Throwable t) {
                Log.e("HomeActivity", "Error: " + t.getMessage());
                Toast.makeText(HomeActivity.this, "Error al cargar las rutas", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
