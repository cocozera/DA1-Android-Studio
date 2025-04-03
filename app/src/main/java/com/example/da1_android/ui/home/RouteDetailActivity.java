package com.example.da1_android.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.example.da1_android.R;
import com.example.da1_android.data.model.RouteDetailDTO;

public class RouteDetailActivity extends AppCompatActivity {

    private TextView textViewRouteId;
    private TextView textViewRouteAddress;
    private TextView textViewRouteStatus;
    private TextView textViewRouteStartedAt;
    private TextView textViewRouteFinishedAt;
    private TextView textViewPackageName;
    private TextView textViewPackageDescription;
    private static final String TAG = "RouteDetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_detail);

        textViewRouteId = findViewById(R.id.textViewRouteId);
        textViewRouteAddress = findViewById(R.id.textViewRouteAddress);
        textViewRouteStatus = findViewById(R.id.textViewRouteStatus);
        textViewRouteStartedAt = findViewById(R.id.textViewRouteStartedAt);
        textViewRouteFinishedAt = findViewById(R.id.textViewRouteFinishedAt);
        textViewPackageName = findViewById(R.id.textViewPackageName);
        textViewPackageDescription = findViewById(R.id.textViewPackageDescription);

        RouteDetailDTO routeDetail = (RouteDetailDTO) getIntent().getSerializableExtra("routeDetail");
        if (routeDetail != null) {
            textViewRouteId.setText("ID: " + routeDetail.getId());
            textViewRouteAddress.setText("Dirección: " + routeDetail.getAddress());
            textViewRouteStatus.setText("Estado: " + routeDetail.getStatus());
            textViewRouteStartedAt.setText("Inicio: " + routeDetail.getStartedAt());
            textViewRouteFinishedAt.setText("Fin: " + routeDetail.getFinishedAt());
            if (routeDetail.getPackageDTO() != null) {
                textViewPackageName.setText("Nombre del receptor: " + routeDetail.getPackageDTO().getReceptor());
                textViewPackageDescription.setText("Ubicacion del Paquete: " + routeDetail.getPackageDTO().getDepositSector());
            }
            Log.d(TAG, "Route details received: " + routeDetail.toString());
        } else {
            Log.e(TAG, "Route details are null");
        }
    }
}