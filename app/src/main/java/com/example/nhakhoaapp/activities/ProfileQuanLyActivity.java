package com.example.nhakhoaapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities.LoginActivity;
import com.example.nhakhoaapp.activities_customer.ChangePasswordActivity;
import com.example.nhakhoaapp.activities_staff.AppointmentManagerActivity;
import com.example.nhakhoaapp.activities_staff.DailyScheduleAdminActivity; // [MỚI]
import com.example.nhakhoaapp.activities_staff.ManageActivity; // [MỚI]
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.bottomnavigation.BottomNavigationView; // [MỚI]
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileQuanLyActivity extends AppCompatActivity {

    private TextInputEditText etHoTen, etEmail, etPhone, etDiaChi;
    private TextView tvRole;
    private Button btnSave, btnLogout, btnChangePass;
    private ImageView btnBack;

    // [MỚI] Bottom Nav
    private BottomNavigationView bottomNavigationView;

    private ApiService apiService;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_quan_ly);

        apiService = ApiClient.getApiService();

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = prefs.getString("USER_ID", null);

        if (currentUserId == null) {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            // performLogout();
            return;
        }

        initView();
        loadManagerProfile();

        // [MỚI] Setup Bottom Navigation
        setupBottomNavigation();

        btnSave.setOnClickListener(v -> updateProfile());
        btnLogout.setOnClickListener(v -> performLogout());
        btnChangePass.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));

        // Nút Back trên header (cũng tương đương việc chuyển về Home)
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(this, ManageActivity.class));
            finish();
        });
    }

    // [MỚI] Highlight tab "Tôi" khi vào màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_profile);
        }
    }

    private void initView() {
        etHoTen = findViewById(R.id.etHoTen);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDiaChi = findViewById(R.id.etDiaChi);
        tvRole = findViewById(R.id.tvRole);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangePass = findViewById(R.id.btnChangePass);
        btnBack = findViewById(R.id.btnBack);

        // [MỚI]
        bottomNavigationView = findViewById(R.id.bottom_navigation_admin);
    }

    // [MỚI] Hàm xử lý chuyển Tab
    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_staff_home) {
                // Về trang chủ Admin
                startActivity(new Intent(this, ManageActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (id == R.id.nav_staff_appointments) {
                // Về trang Quản lý DS Hẹn
                startActivity(new Intent(this, AppointmentManagerActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;

            } else if (id == R.id.nav_staff_profile) {
                // Đang ở đây rồi
                return true;
            }
            return false;
        });
    }

    // ... (Các hàm loadProfile, updateProfile, performLogout giữ nguyên như cũ) ...
    // Bạn copy lại code cũ vào đây

    private void loadManagerProfile() { /* Code cũ... */
        apiService.getNhanVienById(currentUserId).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NhanVien nv = response.body();
                    etHoTen.setText(nv.getHo_ten());
                    etEmail.setText(nv.getEmail());
                    etPhone.setText(nv.getSo_dien_thoai());
                    etDiaChi.setText(nv.getDia_chi());
                    tvRole.setText(nv.getChuc_vu() != null ? nv.getChuc_vu().toUpperCase() : "QUẢN LÝ");
                }
            }
            @Override public void onFailure(Call<NhanVien> call, Throwable t) {}
        });
    }

    private void updateProfile() { /* Code cũ... */
        // ... (Logic update giống bài trước)
        // Lưu ý: apiService.updateNhanVien(...)
        // ...
        NhanVien updateInfo = new NhanVien();
        updateInfo.setHo_ten(etHoTen.getText().toString());
        updateInfo.setSo_dien_thoai(etPhone.getText().toString());
        updateInfo.setDia_chi(etDiaChi.getText().toString());

        apiService.updateNhanVien(currentUserId, updateInfo).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if(response.isSuccessful()) Toast.makeText(ProfileQuanLyActivity.this, "Cập nhật xong!", Toast.LENGTH_SHORT).show();
            }
            @Override public void onFailure(Call<NhanVien> call, Throwable t) {}
        });
    }

    private void performLogout() { /* Code cũ... */
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}