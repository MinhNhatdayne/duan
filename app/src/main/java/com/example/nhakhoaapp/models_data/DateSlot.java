package com.example.nhakhoaapp.models_data;

public class DateSlot {
    private String dayOfWeek; 
    private String date;      
    private boolean isSelected;

    public DateSlot(String dayOfWeek, String date) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.isSelected = false;
    }

    // Getters and Setters
    public String getDayOfWeek() { return dayOfWeek; }
    public String getDate() { return date; }
    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}