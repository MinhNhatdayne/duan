package com.example.nhakhoaapp.models.dto;

import com.google.gson.annotations.SerializedName;

public class AppointmentStats {
    @SerializedName("totalAppointments")
    private int totalAppointments;

    @SerializedName("pendingAppointments")
    private int pendingAppointments;

    public AppointmentStats() {
    }

    // Getters
    public int getTotalAppointments() {
        return totalAppointments;
    }

    public int getPendingAppointments() {
        return pendingAppointments;
    }

    // Setters (Nếu cần)
    public void setTotalAppointments(int totalAppointments) {
        this.totalAppointments = totalAppointments;
    }

    public void setPendingAppointments(int pendingAppointments) {
        this.pendingAppointments = pendingAppointments;
    }
}