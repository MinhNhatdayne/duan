package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhakhoaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnBookAppointment;
    private BottomNavigationView bottomNavigationView;

    // Biến lưu trữ Vai trò (Role) của người dùng sau khi Đăng nhập thành công
    private String userRole = "PATIENT"; // Có thể là "STAFF", "DOCTOR", "PATIENT"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        tvUserName = findViewById(R.id.tv_user_name);
        btnBookAppointment = findViewById(R.id.btn_book_appointment);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Giả lập lấy tên người dùng (thực tế lấy từ Intent hoặc SharedPrefs)
        String userName = "Nguyễn Mạnh Toàn";
        tvUserName.setText(userName);

        // 1. Áp dụng Logic Phân quyền
        applyRoleBasedUI(userRole);

        // 2. Thêm Listener cho nút Đặt hẹn
        btnBookAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình Đặt lịch
                Toast.makeText(DashboardActivity.this, "Chuyển đến màn hình Đặt lịch hẹn", Toast.LENGTH_SHORT).show();
            }
        });

        // 3. Xử lý sự kiện cho BottomNavigationView
        // (Bạn cần cài đặt listener cho bottomNavigationView.setOnItemSelectedListener...)
    }

    /**
     * Hàm này điều chỉnh giao diện dựa trên vai trò của người dùng.
     */
    private void applyRoleBasedUI(String role) {
        if ("PATIENT".equals(role)) {
            // Hiện chức năng cho Bệnh nhân
            btnBookAppointment.setVisibility(View.VISIBLE);
            // Có thể load menu/fragment Bệnh nhân tại đây
        } else if ("DOCTOR".equals(role) || "STAFF".equals(role)) {
            // Ẩn/Hiện các chức năng theo vai trò nhân viên
            btnBookAppointment.setVisibility(View.GONE);
            // Có thể load menu/fragment Nhân viên tại đây
        }
        Toast.makeText(this, "Bạn đã đăng nhập với vai trò: " + role, Toast.LENGTH_SHORT).show();
    }
}