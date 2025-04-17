package com.example.da1_android.ui.routes.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.da1_android.R;
import com.example.da1_android.data.api.RouteService;
import com.example.da1_android.data.model.AuthResponse;
import com.example.da1_android.data.model.InProgressRouteDTO;
import com.example.da1_android.data.prefs.UserPrefsManager;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class InProgressRouteFragment extends Fragment {

    private static final String TAG = "InProgressRouteFragment";

    private View noRoutesMessageContainer;
    private View routeDetailContainer;
    private TextView txtDireccion, txtZona, txtConductor, txtInicio;

    @Inject
    RouteService routeService;

    @Inject
    UserPrefsManager userPrefsManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_in_progress_route, container, false);

        ImageButton buttonBack = view.findViewById(R.id.buttonBack);
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

        noRoutesMessageContainer = view.findViewById(R.id.noRoutesMessageContainer);
        routeDetailContainer = view.findViewById(R.id.routeDetailContainer);
        txtDireccion = view.findViewById(R.id.txtDireccion);
        txtZona = view.findViewById(R.id.txtZona);
        txtConductor = view.findViewById(R.id.txtConductor);
        txtInicio = view.findViewById(R.id.txtInicio);

        cargarRutaEnProgreso();

        return view;
    }

    private void cargarRutaEnProgreso() {
        Long userId = userPrefsManager.getUserId();

        routeService.getInProgressRoutes(userId)
                .enqueue(new Callback<List<InProgressRouteDTO>>() {
                    @Override
                    public void onResponse(Call<List<InProgressRouteDTO>> call, Response<List<InProgressRouteDTO>> response) {
                        if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                            InProgressRouteDTO ruta = response.body().get(0);

                            noRoutesMessageContainer.setVisibility(View.GONE);
                            routeDetailContainer.setVisibility(View.VISIBLE);

                            txtDireccion.setText(ruta.getAddress());
                            txtZona.setText("Zona: " + ruta.getZone());
                            txtConductor.setText("Asignado a: " + ruta.getAssignedUser());
                            txtInicio.setText("Inicio: " + ruta.getStartedAt());

                        } else {
                            noRoutesMessageContainer.setVisibility(View.VISIBLE);
                            routeDetailContainer.setVisibility(View.GONE);
                        }
                    }

                    @Override
                    public void onFailure(Call<List<InProgressRouteDTO>> call, Throwable t) {
                        Log.e(TAG, "Error al obtener ruta en progreso", t);
                        Toast.makeText(requireContext(), "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        noRoutesMessageContainer.setVisibility(View.VISIBLE);
                        routeDetailContainer.setVisibility(View.GONE);
                    }
                });
    }
}
