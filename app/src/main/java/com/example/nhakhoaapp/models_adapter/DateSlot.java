package com.example.nhakhoaapp.models_adapter;

public class DateSlot {
    private String dayOfWeek;
    private int date;

    public DateSlot(String dayOfWeek, int date) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getDate() {
        return date;
    }
}
