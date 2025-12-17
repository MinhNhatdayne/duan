package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities.CategoryListActivity;
import com.example.nhakhoaapp.activities.ProfileQuanLyActivity;
import com.example.nhakhoaapp.activities.QuanLyHoaDonActivity;
import com.example.nhakhoaapp.activities.RecordListActivity;
import com.example.nhakhoaapp.activities_customer.PatientListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ManageActivity extends AppCompatActivity {

    private CardView cardStaff, cardPatient, cardRecord, cardService, cardInvoice;
    private ImageView imgLog;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage);

        initViews();
        setupListeners();
        
        // [MỚI] Thiết lập Bottom Navigation
        setupBottomNavigation();
    }
    
    // [MỚI] Highlight tab "Tổng quan" khi quay lại
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_home); // ID của tab Tổng quan
        }
    }

    private void initViews() {
        cardStaff = findViewById(R.id.cardStaff);
        cardPatient = findViewById(R.id.cardPatient);
        cardRecord = findViewById(R.id.cardRecord);
        cardService = findViewById(R.id.cardService);
        cardInvoice = findViewById(R.id.cardInvoice);
        imgLog = findViewById(R.id.imgUser);
        
        // [MỚI] Ánh xạ Bottom Nav
        bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_staff_home) {
                return true; // Đang ở Trang chủ

            } else if (id == R.id.nav_staff_appointments) {
                startActivity(new Intent(this, AppointmentManagerActivity.class));
                overridePendingTransition(0, 0);
                return true;

            } else if (id == R.id.nav_staff_profile) {
                startActivity(new Intent(this, ProfileQuanLyActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
    }

    private void setupListeners() {
        // ... (Giữ nguyên các sự kiện click CardView cũ của bạn) ...
        
        cardStaff.setOnClickListener(v -> {
            Intent intent = new Intent(ManageActivity.this, com.example.nhakhoaapp.activities_customer.StaffListActivity.class);
            startActivity(intent);
        });

        cardPatient.setOnClickListener(v -> {
            Intent intent = new Intent(ManageActivity.this, PatientListActivity.class);
            startActivity(intent);
        });

        cardRecord.setOnClickListener(v -> {
            Intent intent = new Intent(ManageActivity.this, RecordListActivity.class);
            startActivity(intent);
        });

        cardService.setOnClickListener(v -> {
             Intent intent = new Intent(ManageActivity.this, CategoryListActivity.class);
             startActivity(intent);
        });

        cardInvoice.setOnClickListener(v -> {
             Intent intent = new Intent(ManageActivity.this, QuanLyHoaDonActivity.class);
             startActivity(intent);
        });

        imgLog.setOnClickListener(v -> {
            Intent intent = new Intent(ManageActivity.this, ProfileQuanLyActivity.class);
            startActivity(intent);
        });
    }
}