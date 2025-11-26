package com.example.nhakhoaapp.models_adapter;

// Model này chỉ dành cho mục đích hiển thị Header trong RecyclerView
public class AppointmentHeader {
    private String headerText;

    public AppointmentHeader(String headerText) {
        this.headerText = headerText;
    }

    public String getHeaderText() {
        return headerText;
    }
}