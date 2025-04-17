package com.example.da1_android.ui.routes.fragments;

import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.da1_android.R;
import com.example.da1_android.data.model.RouteDetailDTO;

public class RouteDetailFragment extends Fragment {

    private TextView textViewRouteId;
    private TextView textViewRouteStatus;
    private TextView textViewPackageDescription;
    private TextView textViewRouteZone;
    private static final String TAG = "RouteDetailFragment";

    private RouteDetailDTO routeDetail;

    public RouteDetailFragment() {
        // Constructor vacío obligatorio
    }

    public static RouteDetailFragment newInstance(RouteDetailDTO detail) {
        RouteDetailFragment fragment = new RouteDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable("routeDetail", detail);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_route_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        textViewRouteId = view.findViewById(R.id.textViewRouteId);
        textViewRouteStatus = view.findViewById(R.id.textViewRouteStatus);
        textViewPackageDescription = view.findViewById(R.id.textViewPackageDescription);
        textViewRouteZone = view.findViewById(R.id.textViewRouteZone);

        if (getArguments() != null) {
            routeDetail = (RouteDetailDTO) getArguments().getSerializable("routeDetail");
            if (routeDetail != null) {
                mostrarDatos();
            } else {
                Log.e(TAG, "RouteDetail es null");
            }
        }

        ImageButton backButton = view.findViewById(R.id.buttonBack);
        backButton.setOnClickListener(v -> {
            // Animación como en RouteHistoryFragment
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
                requireActivity().getSupportFragmentManager().popBackStack();
            }, 150);
        });
    }

    private void mostrarDatos() {
        SpannableStringBuilder idText = new SpannableStringBuilder("ID de Ruta: ");
        idText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, idText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        idText.append(String.valueOf(routeDetail.getId()));
        textViewRouteId.setText(idText);

        SpannableStringBuilder zoneText = new SpannableStringBuilder("Zona: ");
        zoneText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, zoneText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        zoneText.append(routeDetail.getZone());
        textViewRouteZone.setText(zoneText);

        SpannableStringBuilder statusText = new SpannableStringBuilder("Estado: ");
        statusText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, statusText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        statusText.append(routeDetail.getStatus());
        textViewRouteStatus.setText(statusText);

        if (routeDetail.getPackageDTO() != null) {
            SpannableStringBuilder packageText = new SpannableStringBuilder("Ubicación del Paquete en el Depósito: ");
            packageText.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 0, packageText.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            packageText.append(routeDetail.getPackageDTO().getDepositSector());
            textViewPackageDescription.setText(packageText);
        }

        Log.d(TAG, "RouteDetail cargado: " + routeDetail.toString());
    }
}
