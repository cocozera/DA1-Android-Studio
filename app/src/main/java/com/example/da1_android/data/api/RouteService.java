package com.example.da1_android.data.api;

import com.example.da1_android.data.model.RouteDTO;
import com.example.da1_android.data.model.RouteDetailDTO;
import com.example.da1_android.data.model.CompletedRouteDTO;
import com.example.da1_android.data.model.InProgressRouteDTO;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface RouteService {

    // Obtener todas las rutas
    @GET("/api/routes/")
    Call<List<RouteDTO>> getAllRoutes(@Header("Authorization") String token);

    // Obtener los detalles de una ruta
    @GET("/api/routes/{routeId}")
    Call<RouteDetailDTO> getRouteDetails(@Path("routeId") Long routeId, @Header("Authorization") String token);

    @POST("/api/routes/assign")
    Call<String> assignUserToRoute(@Query("routeId") Long routeId,
                                   @Query("userId") Long userId,
                                   @Header("Authorization") String token);

    // Completar una ruta (endpoint POST /complete)
    @POST("/api/routes/complete")
    Call<String> completeRoute(@Query("routeId") Long routeId,
                               @Header("Authorization") String token);

    // Obtener las rutas completadas de un usuario
    @GET("/api/routes/{userId}/completed-routes")
    Call<List<CompletedRouteDTO>> getCompletedRoutes(@Path("userId") Long userId,
                                                     @Header("Authorization") String token);

    // Obtener las rutas en progreso de un usuario
    @GET("/api/routes/{userId}/inprogress-routes")
    Call<List<InProgressRouteDTO>> getInProgressRoutes(@Path("userId") Long userId,
                                                       @Header("Authorization") String token);

    // Actualizar la zona de una ruta (endpoint PUT /{id}/zone)
    @PUT("/api/routes/{id}/zone")
    Call<RouteDTO> updateRouteZone(@Path("id") Long id,
                                   @Query("zone") String zone,
                                   @Header("Authorization") String token);
}
