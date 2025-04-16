package com.example.da1_android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.da1_android.R;
import com.example.da1_android.ui.routes.RouteHistoryActivity;
import com.example.da1_android.ui.routes.ViewAllRoutesFragment;
import com.google.android.material.card.MaterialCardView;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HomeActivity extends AppCompatActivity {

    private ImageButton profileButton;
    private MaterialCardView availableRoutesCard, routeHistoryCard;
    // Elementos de la UI que se mostrarán/ocultarán
    private View topBar, titleTextView;
    // Contenedor para cargar el fragment a pantalla completa
    private FrameLayout fragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Referencias de la UI
        profileButton = findViewById(R.id.profileButton);
        availableRoutesCard = findViewById(R.id.availableRoutesCard);
        routeHistoryCard = findViewById(R.id.routeHistoryCard);
        topBar = findViewById(R.id.topBar);
        titleTextView = findViewById(R.id.titleTextView);
        fragmentContainer = findViewById(R.id.fragment_container);

        // Aseguramos que el contenedor de fragment esté inicialmente oculto
        fragmentContainer.setVisibility(View.GONE);

        profileButton.setOnClickListener(v -> {
            // Aquí iría la actividad de Profile (futura)
            startActivity(new Intent(this, ProfileActivity.class));
        });

        availableRoutesCard.setOnClickListener(v -> {
            // Oculta los elementos principales de la Home
            topBar.setVisibility(View.GONE);
            titleTextView.setVisibility(View.GONE);
            availableRoutesCard.setVisibility(View.GONE);
            routeHistoryCard.setVisibility(View.GONE);

            // Muestra el contenedor para el fragment (ahora a pantalla completa)
            fragmentContainer.setVisibility(View.VISIBLE);

            // Carga el fragment "ViewAllRoutesFragment" en el contenedor
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new ViewAllRoutesFragment())
                    .addToBackStack(null) // Permite volver atrás
                    .commit();
        });

        routeHistoryCard.setOnClickListener(v -> {
            startActivity(new Intent(this, RouteHistoryActivity.class));
        });

        // Listener para restaurar la UI de Home cuando se retire el fragment
        getSupportFragmentManager().addOnBackStackChangedListener(() -> {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                // No quedan fragments en la pila, se restaura la pantalla Home
                topBar.setVisibility(View.VISIBLE);
                titleTextView.setVisibility(View.VISIBLE);
                availableRoutesCard.setVisibility(View.VISIBLE);
                routeHistoryCard.setVisibility(View.VISIBLE);
                fragmentContainer.setVisibility(View.GONE);
            }
        });
    }
}
