package com.example.nhakhoaapp.models_adapter;

public class TimeSlot {
    private String time;
    private boolean isAvailable;

    public TimeSlot(String time, boolean isAvailable) {
        this.time = time;
        this.isAvailable = isAvailable;
    }

    public String getTime() {
        return time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}
