package com.example.da1_android.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import com.example.da1_android.R;
import com.example.da1_android.ui.routes.RouteHistoryActivity;
import com.example.da1_android.ui.routes.ViewAllRoutesActivity;
import com.google.android.material.card.MaterialCardView;

public class HomeActivity extends AppCompatActivity {

    private ImageButton profileButton;
    private MaterialCardView availableRoutesCard, routeHistoryCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        profileButton = findViewById(R.id.profileButton);
        availableRoutesCard = findViewById(R.id.availableRoutesCard);
        routeHistoryCard = findViewById(R.id.routeHistoryCard);

        profileButton.setOnClickListener(v -> {
            startActivity(new Intent(this, ProfileActivity.class)); // futura
        });

        availableRoutesCard.setOnClickListener(v -> {
            startActivity(new Intent(this, ViewAllRoutesActivity.class));
        });

        routeHistoryCard.setOnClickListener(v -> {
            startActivity(new Intent(this, RouteHistoryActivity.class)); // futura
        });
    }
}
