package com.example.da1_android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.da1_android.R;
import com.example.da1_android.data.model.UserDTO;
import com.example.da1_android.data.prefs.UserPrefsManager;
import com.example.da1_android.data.api.UserService;
import com.example.da1_android.ui.login.LoginActivity;
import javax.inject.Inject;
import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "ProfileActivity";

    @Inject
    UserPrefsManager userPrefsManager; // Inyección de SharedPreferences

    @Inject
    UserService userService; // Inyección del servicio de usuario (Retrofit)

    private TextView textName;
    private TextView textEmail;
    private TextView textPhoneNumber;
    private Button buttonLogout;
    private ImageButton buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Log.d(TAG, "onCreate: Iniciando ProfileActivity");

        // Vinculamos las vistas del layout
        textName = findViewById(R.id.textName);
        textEmail = findViewById(R.id.textEmail);
        textPhoneNumber = findViewById(R.id.textPhoneNumber); // Nuevo TextView
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonBack = findViewById(R.id.buttonBack);

        // Obtenemos el userId desde SharedPreferences
        Long userId = userPrefsManager.getUserId();
        String jwt_token = "Bearer " + userPrefsManager.getToken();
        Log.d(TAG, "onCreate: userId obtenido = " + userId);
        if (userId != null & jwt_token != null) {
            // Llamamos al endpoint /api/user/me pasando el userId
            userService.getMe(userId, jwt_token).enqueue(new Callback<UserDTO>() {
                @Override
                public void onResponse(Call<UserDTO> call, Response<UserDTO> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        UserDTO user = response.body();
                        Log.d(TAG, "onResponse: Datos del usuario recibidos: " + user.toString());
                        // Actualizamos la UI con los datos del usuario obtenido
                        textName.setText("Nombre: " + user.getName());
                        textEmail.setText("Email: " + user.getEmail());
                        textPhoneNumber.setText("Teléfono: " + user.getPhoneNumber()); // Mostrar número de teléfono
                    } else {
                        textName.setText("Error al obtener los datos");
                        textEmail.setText("");
                        textPhoneNumber.setText("");
                    }
                }

                @Override
                public void onFailure(Call<UserDTO> call, Throwable t) {
                    Log.e(TAG, "onFailure: Error de conexión al obtener los datos del usuario", t);
                    textName.setText("Error en la conexión");
                    textEmail.setText("");
                    textPhoneNumber.setText("");
                }
            });
        } else {
            Log.e(TAG, "onCreate: No se encontró userId en SharedPreferences");
            textName.setText("Usuario no identificado");
            textEmail.setText("");
            textPhoneNumber.setText("");
        }

        // Configuración del botón para cerrar sesión
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });

        // Configuración del botón de volver (back) para regresar a la pantalla anterior
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void logout() {
        // Antes de limpiar, logueamos el token actual
        String currentToken = userPrefsManager.getToken();
        Log.d(TAG, "logout: Token antes del logout = " + currentToken);

        // Limpia los datos del usuario de SharedPreferences
        userPrefsManager.clearAuth();

        // Después del logout, logueamos el token que debería estar limpio (null)
        String tokenAfterLogout = userPrefsManager.getToken();
        Log.d(TAG, "logout: Token después del logout = " + tokenAfterLogout);

        // Redirige al LoginActivity y limpia el stack de actividades
        Intent intent = new Intent(ProfileActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
