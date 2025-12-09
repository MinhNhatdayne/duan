package com.example.nhakhoaapp.models_adapter;

public class DateSlot {
    private String dayOfWeek;      // Ví dụ: "T2", "CN"
    private int date;              // Ví dụ: 12, 13
    private String fullDateString; // Ví dụ: "2023-12-20" (Dùng để gửi API)

    public DateSlot(String dayOfWeek, int date) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
    }
    
    // Constructor đầy đủ (nên dùng cái này khi tạo dữ liệu thật)
    public DateSlot(String dayOfWeek, int date, String fullDateString) {
        this.dayOfWeek = dayOfWeek;
        this.date = date;
        this.fullDateString = fullDateString;
    }

    public String getDayOfWeek() {
        return dayOfWeek;
    }

    public int getDate() {
        return date;
    }
    
    public String getFullDateString() {
        return fullDateString;
    }

    public void setFullDateString(String fullDateString) {
        this.fullDateString = fullDateString;
    }
}