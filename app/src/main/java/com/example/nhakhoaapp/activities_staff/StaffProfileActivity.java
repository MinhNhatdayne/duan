package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities.LoginActivity;
import com.example.nhakhoaapp.activities_customer.ChangePasswordActivity;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserId;
    // Khai báo các View item chi tiết
    private View detailPhone, detailDob, detailGender, detailAddress;
    // Khai báo các View menu chức năng
    private View menuEditInfo, menuChangePassword, menuLogout;

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_profile);

        initViews();
        setupMenuActions();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
//            bottomNavigationView.setSelectedItemId(R.id.nav_staff_profile);
        }
        loadUserProfile();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserId = findViewById(R.id.tv_user_id);

        detailPhone = findViewById(R.id.detail_phone);
        detailDob = findViewById(R.id.detail_dob);
        detailGender = findViewById(R.id.detail_gender); // Đảm bảo trong XML đã bỏ comment phần này
        detailAddress = findViewById(R.id.detail_address);

        // [MỚI] Ánh xạ nút Sửa thông tin
        menuEditInfo = findViewById(R.id.menu_edit_info);

        menuChangePassword = findViewById(R.id.menu_change_password);
        menuLogout = findViewById(R.id.menu_logout);

        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);

        if (userId == null) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            handleLogout();
            return;
        }

        // Gọi API lấy thông tin Nhân viên
        ApiClient.getApiService().getNhanVienById(userId).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NhanVien nv = response.body();
                    updateUI(nv);
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable t) {
                Toast.makeText(StaffProfileActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(NhanVien user) {
        tvUserName.setText(user.getHo_ten());

        String role = "Nhân viên";
        if ("BacSi".equals(user.getChuc_vu())) role = "Bác sĩ";
        else if ("LeTan".equals(user.getChuc_vu())) role = "Lễ tân";
        else if ("QuanLy".equals(user.getChuc_vu())) role = "Quản lý";

        tvUserId.setText(role + " - ID: " + (user.get_id().length() > 6 ? user.get_id().substring(0, 6) : user.get_id()));

        setupDetailItem(detailPhone, android.R.drawable.ic_menu_call, user.getSo_dien_thoai());
        setupDetailItem(detailDob, android.R.drawable.ic_menu_today, formatDate(user.getNgay_sinh()));
//        setupDetailItem(detailGender, android.R.drawable.ic_menu_compass, user.getGioi_tinh());
        setupDetailItem(detailAddress, android.R.drawable.ic_menu_myplaces, user.getDia_chi());
    }

    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return "Chưa cập nhật";
        // Logic format ngày
        if (dateString.matches("\\d{8}")) {
            return dateString.substring(0, 2) + "/" + dateString.substring(2, 4) + "/" + dateString.substring(4);
        }
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(dateString);
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return displayFormat.format(date);
        } catch (Exception e) {
            try {
                SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = sqlFormat.parse(dateString);
                SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                return displayFormat.format(date);
            } catch (Exception ex) { return dateString; }
        }
    }

    private void setupDetailItem(View parentView, int iconResId, String value) {
        if (parentView == null) return; // Check null nếu view bị comment trong XML
        ImageView imgIcon = parentView.findViewById(R.id.img_detail_icon);
        TextView tvValue = parentView.findViewById(R.id.tv_detail_value);
        if (imgIcon != null) imgIcon.setImageResource(iconResId);
        if (tvValue != null) tvValue.setText(value != null && !value.isEmpty() ? value : "Chưa cập nhật");
    }

    private void setupMenuActions() {
        // [CẬP NHẬT] Code xử lý nút Sửa thông tin (Giống bên ProfileActivity)
        setupMenuItem(menuEditInfo, android.R.drawable.ic_menu_edit, "Chỉnh sửa thông tin", v -> {
            Intent intent = new Intent(this, EditStaffProfileActivity.class);

            // Lấy dữ liệu hiện tại từ UI để truyền sang màn hình sửa
            // Lưu ý: Phải chắc chắn các view detailPhone, detailDob... đã được ánh xạ thành công
            TextView tvPhoneVal = detailPhone.findViewById(R.id.tv_detail_value);
            TextView tvDobVal = detailDob.findViewById(R.id.tv_detail_value);
            TextView tvAddressVal = detailAddress.findViewById(R.id.tv_detail_value);

            // Xử lý riêng cho Gender vì trong XML bạn có thể đã comment nó lại
            String genderVal = "";
            if (detailGender != null) {
                TextView tvGenderVal = detailGender.findViewById(R.id.tv_detail_value);
                if (tvGenderVal != null) genderVal = tvGenderVal.getText().toString();
            }

            intent.putExtra("NAME", tvUserName.getText().toString());
            intent.putExtra("PHONE", tvPhoneVal.getText().toString());
            intent.putExtra("DOB", tvDobVal.getText().toString());
            intent.putExtra("GENDER", genderVal);
            intent.putExtra("ADDRESS", tvAddressVal.getText().toString());

            startActivity(intent);
        });

        setupMenuItem(menuChangePassword, android.R.drawable.ic_lock_power_off, "Đổi mật khẩu", v -> {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        });

        setupMenuItem(menuLogout, android.R.drawable.ic_delete, "Đăng xuất", v -> showLogoutConfirm());
    }

    private void setupMenuItem(View parentView, int iconResId, String title, View.OnClickListener listener) {
        if (parentView == null) return;
        ImageView imgIcon = parentView.findViewById(R.id.img_menu_icon);
        TextView tvTitle = parentView.findViewById(R.id.tv_menu_title);
        if (imgIcon != null) imgIcon.setImageResource(iconResId);
        if (tvTitle != null) tvTitle.setText(title);
        parentView.setOnClickListener(listener);
    }

    private void showLogoutConfirm() {
        new AlertDialog.Builder(this)
                .setTitle("Đăng xuất")
                .setMessage("Bạn có chắc chắn muốn đăng xuất?")
                .setPositiveButton("Đồng ý", (dialog, which) -> handleLogout())
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void handleLogout() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.clear();
        editor.apply();

        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    private void setupBottomNav() {
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_staff_home) {
                startActivity(new Intent(this, StaffDashboardActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_staff_schedule) {
                startActivity(new Intent(this, DailyScheduleActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (id == R.id.nav_staff_appointments) {
                startActivity(new Intent(this, AppointmentManagerActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
//            else if (id == R.id.nav_staff_profile) {
//                return true;
//            }
            return false;
        });
    }
}