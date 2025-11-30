package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem; // Thêm
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull; // Thêm
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView; // Thêm

public class SelectDoctorActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private TextView tvSelectedService;
    private TextView btnDatLich1;
    private TextView btnDatLich2;

    // 1. THÊM BIẾN NÀY
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);

        // --- CODE CŨ CỦA BẠN (GIỮ NGUYÊN) ---
        imgBackButton = findViewById(R.id.img_back_button);
        tvSelectedService = findViewById(R.id.tv_selected_service);

        String serviceName = getIntent().getStringExtra("SERVICE_NAME");
        if (serviceName != null) {
            tvSelectedService.setText(serviceName);
        } else {
            tvSelectedService.setText("Chỉnh nha");
        }

        View doctorItem1 = findViewById(R.id.doctor_item_1);
        View doctorItem2 = findViewById(R.id.doctor_item_2);

        btnDatLich1 = doctorItem1.findViewById(R.id.btn_dat_lich);
        btnDatLich2 = doctorItem2.findViewById(R.id.btn_dat_lich);

        imgBackButton.setOnClickListener(v -> onBackPressed());

        btnDatLich1.setOnClickListener(v -> {
            Intent intent = new Intent(SelectDoctorActivity.this, SelectTimeActivity.class);
            intent.putExtra("SERVICE_NAME", serviceName != null ? serviceName : "Chỉnh nha");
            intent.putExtra("DOCTOR_NAME", "Đoàn Hồng Lê");
            startActivity(intent);
        });

        btnDatLich2.setOnClickListener(v -> {
            Intent intent = new Intent(SelectDoctorActivity.this, SelectTimeActivity.class);
            intent.putExtra("SERVICE_NAME", serviceName != null ? serviceName : "Chỉnh nha");
            intent.putExtra("DOCTOR_NAME", "Đoàn Bích Ngọc");
            startActivity(intent);
        });
        // ------------------------------------

        // 2. THÊM ĐOẠN NÀY: Xử lý Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            handleBottomNav(item);
            return true;
        });
    }

    // 3. THÊM ĐOẠN NÀY: Highlight tab "Lịch hẹn" vì màn hình này thuộc luồng đặt lịch
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_booking);
        }
    }

    // 4. THÊM ĐOẠN NÀY: Hàm điều hướng
    private void handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivity.class));
            return;

        } else if (id == R.id.nav_booking) {
            // Đang ở trong luồng booking rồi, có thể return hoặc mở lại BookingActivity gốc
            startActivity(new Intent(this, BookingActivity.class));
            return;

        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return;

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return;
        }
    }
}