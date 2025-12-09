package com.example.nhakhoaapp.activities_customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient; // Dùng ApiClient cho giống LoginActivity
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnBookAppointment;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        setupListeners();
        
        // --- BẮT ĐẦU PHẦN SỬA ---
        loadUserDataFromApi();
        // --- KẾT THÚC PHẦN SỬA ---
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

    private void loadUserDataFromApi() {
        // 1. Lấy SharedPreferences (Tên file "UserPrefs" khớp với LoginActivity)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        
        // 2. Lấy ID bằng key "USER_ID" (KHỚP VỚI LOGIN ACTIVITY BẠN GỬI)
        String userId = prefs.getString("USER_ID", null);
        
        // Lấy tên tạm thời hiển thị trước khi API tải xong
        String tempName = prefs.getString("USER_NAME", "Khách hàng");
        tvUserName.setText(tempName);

        if (userId == null) {
            Toast.makeText(this, "Chưa đăng nhập", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Gọi API lấy thông tin chi tiết
        ApiClient.getApiService().getBenhNhanById(userId).enqueue(new Callback<BenhNhan>() {
            @Override
            public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BenhNhan bn = response.body();
                    tvUserName.setText(bn.getHo_ten()); // Cập nhật tên chính xác từ Server
                }
            }

            @Override
            public void onFailure(Call<BenhNhan> call, Throwable t) {
                // Nếu lỗi mạng thì vẫn giữ tên lấy từ SharedPreferences
            }
        });
    }

    private void setupListeners() {
        btnBookAppointment.setOnClickListener(v -> {
            startActivity(new Intent(this, BookingActivity.class));
        });

        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNav);
    }

    private boolean handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) return true;
        
        Intent intent = null;
        if (id == R.id.nav_booking) intent = new Intent(this, BookingActivity.class);
        else if (id == R.id.nav_notifications) intent = new Intent(this, NotificationsActivity.class);
        else if (id == R.id.nav_profile) intent = new Intent(this, ProfileActivity.class);

        if (intent != null) startActivity(intent);
        return true;
    }
}