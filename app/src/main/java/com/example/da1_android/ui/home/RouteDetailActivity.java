package com.example.da1_android.ui.home;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.da1_android.R;
import com.example.da1_android.data.model.RouteDetailDTO;

public class RouteDetailActivity extends AppCompatActivity {

    private TextView textViewRouteId;
    private TextView textViewRouteStatus;
    private TextView textViewPackageDescription;
    private TextView textViewRouteZone;
    private static final String TAG = "RouteDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        textViewRouteId = findViewById(R.id.textViewRouteId);
        textViewRouteStatus = findViewById(R.id.textViewRouteStatus);
        textViewPackageDescription = findViewById(R.id.textViewPackageDescription);
        textViewRouteZone = findViewById(R.id.textViewRouteZone);

        RouteDetailDTO routeDetail = (RouteDetailDTO) getIntent().getSerializableExtra("routeDetail");
        if (routeDetail != null) {
            // ID
            SpannableStringBuilder idText = new SpannableStringBuilder("ID de Ruta: ");
            idText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, idText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            idText.append(String.valueOf(routeDetail.getId()));
            textViewRouteId.setText(idText);

            // Zona
            SpannableStringBuilder zoneText = new SpannableStringBuilder("Zona: ");
            zoneText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, zoneText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            zoneText.append(routeDetail.getZone());
            textViewRouteZone.setText(zoneText);

            // Estado
            SpannableStringBuilder statusText = new SpannableStringBuilder("Estado: ");
            statusText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, statusText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            statusText.append(routeDetail.getStatus());
            textViewRouteStatus.setText(statusText);

            // Ubicación del paquete
            if (routeDetail.getPackageDTO() != null) {
                SpannableStringBuilder packageText = new SpannableStringBuilder("Ubicación del Paquete en el Depósito: ");
                packageText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, packageText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                packageText.append(routeDetail.getPackageDTO().getDepositSector());
                textViewPackageDescription.setText(packageText);
            }

            Log.d(TAG, "Route details received: " + routeDetail.toString());
        } else {
            Log.e(TAG, "Route details are null");
        }

        ImageButton backButton = findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> finish());
    }
}
