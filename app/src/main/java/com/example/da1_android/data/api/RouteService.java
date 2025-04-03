package com.example.da1_android.data.api;

import com.example.da1_android.data.model.RouteDTO;
import com.example.da1_android.data.model.RouteDetailDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Path;

public interface RouteService {

    // Método para obtener una ruta con todos sus detalles
    @GET("/api/routes/{routeId}")
    Call<RouteDetailDTO> getRouteDetails(@Path("routeId") Long routeId, @Header("Authorization") String token);

    @GET("/api/routes/")
    Call<List<RouteDTO>> getAllRoutes(@Header("Authorization") String token);
}
