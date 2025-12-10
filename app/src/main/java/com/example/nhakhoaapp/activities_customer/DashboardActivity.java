package com.example.nhakhoaapp.activities_customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnBookAppointment;
    private BottomNavigationView bottomNavigationView;

    // ⭐ Lưu lại để truyền qua BookingActivity
    private String patientName = "";
    private String patientId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        setupListeners();

        // Lấy dữ liệu từ LoginActivity nếu có
        receivePatientFromLogin();

        // Load từ API để chắc chắn dữ liệu luôn đúng
        loadUserDataFromApi();
    }

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

    // ⭐ Nhận PATIENT_NAME và PATIENT_ID từ LoginActivity
    private void receivePatientFromLogin() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("PATIENT_NAME");
            String id = intent.getStringExtra("PATIENT_ID");

            if (name != null) patientName = name;
            if (id != null) patientId = id;

            if (patientName != null && !patientName.isEmpty()) {
                tvUserName.setText(patientName);
            }
        }
    }

    private void loadUserDataFromApi() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        // Nếu chưa có ID trong biến local thì lấy từ SharedPreferences
        if (patientId == null || patientId.isEmpty()) {
            patientId = prefs.getString("USER_ID", null);
        }

        // Hiện tên tạm trước
        if (patientName == null || patientName.isEmpty()) {
            patientName = prefs.getString("USER_NAME", "Khách hàng");
        }

        tvUserName.setText(patientName);

        if (patientId == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy ID bệnh nhân!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Gọi API lấy dữ liệu chính xác
        ApiClient.getApiService().getBenhNhanById(patientId).enqueue(new Callback<BenhNhan>() {
            @Override
            public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BenhNhan bn = response.body();
                    patientName = bn.getHo_ten();
                    tvUserName.setText(patientName);
                }
            }

            @Override
            public void onFailure(Call<BenhNhan> call, Throwable t) {
                // Không cập nhật, dùng dữ liệu local
            }
        });
    }

    private void setupListeners() {

        // ⭐ Truyền tên + ID sang BookingActivity
        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("PATIENT_NAME", patientName);
            intent.putExtra("PATIENT_ID", patientId);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNav);
    }

    private boolean handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) return true;

        Intent intent = null;

        if (id == R.id.nav_booking) {
            intent = new Intent(this, BookingActivity.class);
            intent.putExtra("PATIENT_NAME", patientName);
            intent.putExtra("PATIENT_ID", patientId);

        } else if (id == R.id.nav_notifications) {
            intent = new Intent(this, NotificationsActivity.class);

        } else if (id == R.id.nav_profile) {
            intent = new Intent(this, ProfileActivity.class);
        }

        if (intent != null) startActivity(intent);
        return true;
    }
}
