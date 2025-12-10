package com.example.nhakhoaapp.models_adapter;

public class DateSlot {

    private String dayOfWeek;
    private int day;
    private String fullDateString;

    public DateSlot(String dayOfWeek, int day) {
        this.dayOfWeek = dayOfWeek;
        this.day = day;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getDay() {
        return day;
    }

    public String getFullDateString() {
        return fullDateString;
    }

    public void setFullDateString(String fullDateString) {
        this.fullDateString = fullDateString;
    }
}
