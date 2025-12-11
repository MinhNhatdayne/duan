package com.example.nhakhoaapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities_customer.ChangePasswordActivity;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileQuanLyActivity extends AppCompatActivity {

    // Khai báo các biến giao diện
    private TextInputEditText etHoTen, etEmail, etPhone, etDiaChi;
    private TextView tvRole;
    private Button btnSave, btnLogout,btnChangePass;
    private ImageView btnBack;
    // API variables
    private ApiService apiService;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_quan_ly);

        // Khởi tạo API
        apiService = ApiClient.getApiService();

        // 1. Lấy ID từ SharedPreferences (Giả sử bạn đã lưu khi Login)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = prefs.getString("USER_ID", null);

        // --- CODE TEST (Xóa dòng này khi chạy thật) ---
        // currentUserId = "ID_CUA_QUAN_LY_TAI_DAY";
        // ----------------------------------------------

        if (currentUserId == null) {
            Toast.makeText(this, "Chưa đăng nhập!", Toast.LENGTH_SHORT).show();
            // Điều hướng về Login
            return;
        }

        initView();
        loadManagerProfile();

        btnSave.setOnClickListener(v -> updateProfile());
        btnLogout.setOnClickListener(v -> performLogout());
        btnChangePass.setOnClickListener(v -> startActivity(new Intent(this, ChangePasswordActivity.class)));
        btnBack.setOnClickListener(v -> finish());
    }

    private void initView() {
        etHoTen = findViewById(R.id.etHoTen);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etDiaChi = findViewById(R.id.etDiaChi);
        tvRole = findViewById(R.id.tvRole);
        btnSave = findViewById(R.id.btnSave);
        btnLogout = findViewById(R.id.btnLogout);
        btnChangePass=findViewById(R.id.btnChangePass);
        btnBack=findViewById(R.id.btnBack);


    }

    // --- Lấy thông tin Quản lý ---
    private void loadManagerProfile() {
        apiService.getNhanVienById(currentUserId).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NhanVien nv = response.body();

                    etHoTen.setText(nv.getHo_ten());
                    etEmail.setText(nv.getEmail());
                    etPhone.setText(nv.getSo_dien_thoai());
                    etDiaChi.setText(nv.getDia_chi());

                    // Hiển thị chức vụ
                    tvRole.setText(nv.getChuc_vu() != null ? nv.getChuc_vu().toUpperCase() : "QUẢN LÝ");
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable t) {
                Toast.makeText(ProfileQuanLyActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Cập nhật thông tin ---
    private void updateProfile() {
        String name = etHoTen.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String address = etDiaChi.getText().toString().trim();

        if (name.isEmpty()) {
            etHoTen.setError("Không được để trống");
            return;
        }

        // Tạo object chứa thông tin cần update
        // (Constructor này phải có trong Model NhanVien của bạn)
        NhanVien updateInfo = new NhanVien();
        updateInfo.setHo_ten(name);
        updateInfo.setSo_dien_thoai(phone);
        updateInfo.setDia_chi(address);

        apiService.updateNhanVien(currentUserId, updateInfo).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful()) {
                    finish();
                    Toast.makeText(ProfileQuanLyActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ProfileQuanLyActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable t) {
                Toast.makeText(ProfileQuanLyActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Đăng xuất ---
    private void performLogout() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        prefs.edit().clear().apply(); // Xóa token/id
//         Chuyển về màn hình đăng nhập
         Intent intent = new Intent(this, LoginActivity.class);
         intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
         startActivity(intent);
        Toast.makeText(this, "Đã đăng xuất", Toast.LENGTH_SHORT).show();
        finish();
    }
}