package com.example.da1_android.data.model;

import java.io.Serializable;

public class RouteDetailDTO implements Serializable {
    private Long id;
    private String address;
    private String status;
    private String startedAt;
    private String finishedAt;
    private String zone;
    private Long assignedUserId;
    private PackageDTO packageDTO;  // Solo un único paquete asociado a la ruta

    // Getters y Setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getFinishedAt() {
        return finishedAt;
    }

    public void setFinishedAt(String finishedAt) {
        this.finishedAt = finishedAt;
    }


    public String getZone() {return zone;}

    public void setZone(String zone) {this.zone = zone;}


    public Long getAssignedUserId() {
        return assignedUserId;
    }

    public void setAssignedUserId(Long assignedUserId) {
        this.assignedUserId = assignedUserId;
    }

    public PackageDTO getPackageDTO() {
        return packageDTO;
    }

    public void setPackageDTO(PackageDTO packageDTO) {
        this.packageDTO = packageDTO;
    }
}