package com.example.nhakhoaapp.models_adapter;

public class TimeSlot {
    private String time;         // Hiển thị và gửi đi (Nên để định dạng HH:mm, vd: "09:30")
    private boolean isAvailable; // true = Trống, false = Đã đặt/Bận
    private boolean isSelected;  // true = Đang chọn

    public TimeSlot(String time, boolean isAvailable) {
        this.time = time;
        this.isAvailable = isAvailable;
        this.isSelected = false;
    }

    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }

    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean available) { isAvailable = available; }

    public boolean isSelected() { return isSelected; }
    public void setSelected(boolean selected) { isSelected = selected; }
}