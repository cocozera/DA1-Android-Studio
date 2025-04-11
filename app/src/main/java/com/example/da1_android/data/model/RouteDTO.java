package com.example.da1_android.data.model;

import com.google.gson.annotations.SerializedName;

public class RouteDTO {
    @SerializedName("id")
    private Long id;

    @SerializedName("zone")
    private String zone;

    @SerializedName("status")
    private String status;

    @SerializedName("started_at")
    private String startedAt;

    public Long getId() { return id; }
    public String getZone() { return zone; }
    public String getStatus() { return status; }
    public String getStartedAt() { return startedAt; }
}
