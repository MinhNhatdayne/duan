package com.example.nhakhoaapp.models_data;

import com.example.nhakhoaapp.models.LichHen;

import java.util.List;

public class AppointmentGroup {
    private String headerDate; // Ví dụ: "Hôm nay, Thứ Tư, 9 tháng 10"
    private List<LichHen> appointments;

    public AppointmentGroup(String headerDate, List<LichHen> appointments) {
        this.headerDate = headerDate;
        this.appointments = appointments;
    }

    public String getHeaderDate() {
        return headerDate;
    }

    public List<LichHen> getAppointments() {
        return appointments;
    }
}