package com.example.nhakhoaapp.models_data;

public class TimeSlot {
    private String time;        
    private boolean isAvailable; 
    private boolean isSelected;  

    public TimeSlot(String time, boolean isAvailable) {
        this.time = time;
        this.isAvailable = isAvailable;
        this.isSelected = false; 
    }

    // Getters and Setters
    public String getTime() { return time; }
    public boolean isAvailable() { return isAvailable; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}