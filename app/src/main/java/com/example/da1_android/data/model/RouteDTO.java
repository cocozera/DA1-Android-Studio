package com.example.da1_android.data.model;

import com.google.gson.annotations.SerializedName;

public class RouteDTO {
    @SerializedName("id")
    private Long id;

    @SerializedName("address")  // Asegúrate de que este campo coincide con la API
    private String address;

    @SerializedName("status")
    private String status;

    @SerializedName("started_at")
    private String startedAt;

    public Long getId() { return id; }
    public String getAddress() { return address; }
    public String getStatus() { return status; }
    public String getStartedAt() { return startedAt; }
}
