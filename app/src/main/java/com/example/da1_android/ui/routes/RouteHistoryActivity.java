package com.example.da1_android.ui.routes;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
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
public class RouteHistoryActivity extends AppCompatActivity {

    private static final String TAG = "RouteHistoryActivity";
    private ListView listViewRoutes;
    private CompletedRoutesAdapter adapter;

    // Inyectamos el servicio de rutas y el gestor de preferencias
    @Inject
    RouteService routeService;

    @Inject
    UserPrefsManager userPrefsManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Usamos el layout que mantiene el mismo diseño visual
        setContentView(R.layout.activity_route_history);

        // Referencias de los componentes del layout
        ImageButton buttonBack = findViewById(R.id.buttonBack);
        listViewRoutes = findViewById(R.id.listViewRoutes);

        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        adapter = new CompletedRoutesAdapter(this);
        listViewRoutes.setAdapter(adapter);

        // Obtenemos el token y userId desde UserPrefsManager
        String token = userPrefsManager.getToken();
        Long userId = userPrefsManager.getUserId();

        Log.d(TAG, "Token obtenido: " + token);
        Log.d(TAG, "UserId obtenido: " + userId);

        // Si token o userId son null, significa que probablemente no se guardó la respuesta de autenticación.
        if (token == null || userId == null) {
            Log.e(TAG, "No se encontró token o userId. Asegúrate de haber guardado la respuesta de autenticación.");
            // Para pruebas, guardamos valores dummy:
            AuthResponse dummyAuth = new AuthResponse();
            dummyAuth.setToken("dummy_token");
            dummyAuth.setUserId(1L);
            userPrefsManager.saveAuthResponse(dummyAuth);

            token = userPrefsManager.getToken();
            userId = userPrefsManager.getUserId();
            Log.d(TAG, "Valores dummy guardados: Token = " + token + ", UserId = " + userId);
            Toast.makeText(this, "Se han guardado valores de prueba", Toast.LENGTH_SHORT).show();
        }

        // Realizamos la llamada a la API para obtener el historial de rutas completadas
        routeService.getCompletedRoutes(userId, "Bearer " + token).enqueue(new Callback<List<CompletedRouteDTO>>() {
            @Override
            public void onResponse(Call<List<CompletedRouteDTO>> call, Response<List<CompletedRouteDTO>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<CompletedRouteDTO> routes = response.body();
                    Log.d(TAG, "Número de rutas completadas recibidas: " + routes.size());
                    adapter.setRoutes(routes);
                } else {
                    Log.e(TAG, "Respuesta no exitosa: " + response.message());
                    Toast.makeText(RouteHistoryActivity.this, "Error: " + response.message(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<CompletedRouteDTO>> call, Throwable t) {
                Log.e(TAG, "Fallo de red: " + t.getMessage(), t);
                Toast.makeText(RouteHistoryActivity.this, "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
