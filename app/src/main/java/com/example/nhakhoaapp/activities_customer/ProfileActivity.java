package com.example.nhakhoaapp.activities_customer;

import android.content.Intent; // THÊM MỚI
import android.os.Bundle;
import android.view.MenuItem; // THÊM MỚI
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull; // THÊM MỚI
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView; // THÊM MỚI

public class ProfileActivity extends AppCompatActivity {

    // Khai báo các View cho chi tiết người dùng
    private View detailPhone, detailDob, detailGender, detailAddress;
    // Khai báo các View cho menu
    private View menuEditInfo, menuFeedback, menuChangePassword, menuLogout;

    // 1. THÊM MỚI: Khai báo biến BottomNavigation
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // --- CODE CŨ CỦA BẠN (GIỮ NGUYÊN) ---
        detailPhone = findViewById(R.id.detail_phone);
        detailDob = findViewById(R.id.detail_dob);
        detailGender = findViewById(R.id.detail_gender);
        detailAddress = findViewById(R.id.detail_address);

        setupDetailItem(detailPhone, android.R.drawable.ic_menu_call, "0978948571");
        setupDetailItem(detailDob, android.R.drawable.ic_menu_today, "13/11/1989");
        setupDetailItem(detailGender, android.R.drawable.ic_menu_compass, "Nam");
        setupDetailItem(detailAddress, android.R.drawable.ic_menu_myplaces, "TDP An Phú, Thị trấn Kiến Xương...");

        menuEditInfo = findViewById(R.id.menu_edit_info);
        menuFeedback = findViewById(R.id.menu_feedback);
        menuChangePassword = findViewById(R.id.menu_change_password);
        menuLogout = findViewById(R.id.menu_logout);

        setupMenuItem(menuEditInfo, android.R.drawable.ic_menu_edit, "Chỉnh sửa thông tin", "EditInfo");
        setupMenuItem(menuFeedback, android.R.drawable.btn_star_big_off, "Phản hồi", "Feedback");
        setupMenuItem(menuChangePassword, android.R.drawable.ic_lock_power_off, "Đổi mật khẩu", "ChangePassword");
        setupMenuItem(menuLogout, android.R.drawable.ic_delete, "Đăng xuất", "Logout");
        // ------------------------------------

        // 2. THÊM MỚI: Ánh xạ và xử lý sự kiện Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            handleBottomNav(item);
            return true;
        });
    }

    // 3. THÊM MỚI: Highlight icon Profile khi mở màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }
    }

    // 4. THÊM MỚI: Hàm xử lý chuyển màn hình
    private void handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivity.class));
            return;

        } else if (id == R.id.nav_booking) {
            startActivity(new Intent(this, BookingActivity.class));
            return;

        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return;

        } else if (id == R.id.nav_profile) {
            return; // Đang ở màn hình Profile rồi
        }
    }

    // --- CÁC HÀM CŨ CỦA BẠN (GIỮ NGUYÊN) ---
    private void setupDetailItem(View parentView, int iconResId, String value) {
        ImageView imgIcon = parentView.findViewById(R.id.img_detail_icon);
        TextView tvValue = parentView.findViewById(R.id.tv_detail_value);

        if (imgIcon != null) imgIcon.setImageResource(iconResId);
        if (tvValue != null) tvValue.setText(value);
    }

    private void setupMenuItem(View parentView, int iconResId, String title, String action) {
        ImageView imgIcon = parentView.findViewById(R.id.img_menu_icon);
        TextView tvTitle = parentView.findViewById(R.id.tv_menu_title);

        if (imgIcon != null) imgIcon.setImageResource(iconResId);
        if (tvTitle != null) tvTitle.setText(title);

        parentView.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình: " + action, Toast.LENGTH_SHORT).show();
        });
    }
}