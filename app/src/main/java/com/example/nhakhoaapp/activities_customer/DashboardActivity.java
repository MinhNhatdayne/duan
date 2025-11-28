package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnBookAppointment;
    private BottomNavigationView bottomNavigationView;

    // Role từ User login — tạm gán cứng
    private String userRole = "PATIENT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        loadUserData();
        applyRoleBasedUI(userRole);
        setupListeners();
    }

    /** Mỗi lần quay lại Dashboard → tab Home luôn được highlight */
    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        btnBookAppointment = findViewById(R.id.btn_book_appointment);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    /** Giả lập load dữ liệu user */
    private void loadUserData() {
        String userName = "Nguyễn Mạnh Toàn"; // test thôi
        tvUserName.setText(userName);
    }

    /** Phân quyền UI dựa theo Role */
    private void applyRoleBasedUI(String role) {
        if ("PATIENT".equals(role)) {
            btnBookAppointment.setVisibility(View.VISIBLE);
        } else {
            btnBookAppointment.setVisibility(View.GONE);
        }

        Toast.makeText(this, "Vai trò: " + role, Toast.LENGTH_SHORT).show();
    }

    /** Gán sự kiện click */
    private void setupListeners() {

        // Nút “Đặt ngay”
        btnBookAppointment.setOnClickListener(v -> {
            startActivity(new Intent(this, BookingActivity.class));
        });

        // Xử lý navigation
        bottomNavigationView.setOnItemSelectedListener(item -> {
            handleBottomNav(item);
            return true;
        });
    }

    /** Điều hướng BottomNavigation – tránh mở Activity đang mở */
    private void handleBottomNav(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            return;

        } else if (id == R.id.nav_booking) {
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
