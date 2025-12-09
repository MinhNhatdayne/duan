package com.example.nhakhoaapp.models_adapter;

public class TimeSlot {
    private String time;        // Hiển thị và gửi đi (Nên để định dạng HH:mm, vd: "09:30")
    private boolean isAvailable; // Có trống hay không
    private boolean isSelected;  // (Mới) Hỗ trợ đổi màu khi click

    public TimeSlot(String time, boolean isAvailable) {
        this.time = time;
        this.isAvailable = isAvailable;
        this.isSelected = false; // Mặc định chưa chọn
    }

    // Getters
    public String getTime() {
        return time;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public boolean isSelected() {
        return isSelected;
    }

    // Setters (Cần thiết để Adapter cập nhật trạng thái)
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }
    
    // Hàm tiện ích để lấy giờ chuẩn gửi API (Nếu time bạn lưu là "09:30" thì return luôn)
    @Override
    public String toString() {
        return time;
    }
}