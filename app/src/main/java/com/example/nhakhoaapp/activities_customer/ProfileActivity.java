package com.example.nhakhoaapp.activities_customer;

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
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUserName, tvUserId;
    private View detailPhone, detailDob, detailGender, detailAddress;
    private View menuEditInfo, menuFeedback, menuChangePassword, menuLogout;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        initViews();
        setupMenuActions();
        setupBottomNav();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_profile);
        }
        loadUserProfile();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        tvUserId = findViewById(R.id.tv_user_id);

        detailPhone = findViewById(R.id.detail_phone);
        detailDob = findViewById(R.id.detail_dob);
        detailGender = findViewById(R.id.detail_gender);
        detailAddress = findViewById(R.id.detail_address);

        menuEditInfo = findViewById(R.id.menu_edit_info);
        menuFeedback = findViewById(R.id.menu_feedback);
        menuChangePassword = findViewById(R.id.menu_change_password);
        menuLogout = findViewById(R.id.menu_logout);

        bottomNavigationView = findViewById(R.id.bottom_navigation);
    }

    private void loadUserProfile() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);

        if (userId == null) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn", Toast.LENGTH_SHORT).show();
            handleLogout();
            return;
        }

        ApiClient.getApiService().getBenhNhanById(userId).enqueue(new Callback<BenhNhan>() {
            @Override
            public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BenhNhan user = response.body();
                    updateUI(user);
                }
            }

            @Override
            public void onFailure(Call<BenhNhan> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUI(BenhNhan user) {
        tvUserName.setText(user.getHo_ten());
        if (user.get_id() != null && user.get_id().length() > 8) {
            tvUserId.setText("Mã BN: " + user.get_id().substring(0, 8).toUpperCase());
        }

        setupDetailItem(detailPhone, android.R.drawable.ic_menu_call, user.getSo_dien_thoai());

        // === SỬ DỤNG HÀM FORMAT NGÀY SINH Ở ĐÂY ===
        setupDetailItem(detailDob, android.R.drawable.ic_menu_today, formatDate(user.getNgay_sinh()));

        setupDetailItem(detailGender, android.R.drawable.ic_menu_compass, user.getGioi_tinh());
        setupDetailItem(detailAddress, android.R.drawable.ic_menu_myplaces, user.getDia_chi());
    }

    // === HÀM XỬ LÝ ĐỊNH DẠNG NGÀY SINH ===
    private String formatDate(String dateString) {
        if (dateString == null || dateString.isEmpty()) return "Chưa cập nhật";

        // Trường hợp 1: Chuỗi số liền dạng "30102005" (như trong log của bạn)
        if (dateString.matches("\\d{8}")) {
            return dateString.substring(0, 2) + "/" + dateString.substring(2, 4) + "/" + dateString.substring(4);
        }

        // Trường hợp 2: Dạng ISO từ MongoDB (yyyy-MM-ddTHH:mm:ss...)
        try {
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = isoFormat.parse(dateString);
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return displayFormat.format(date);
        } catch (Exception e) {
            // Ignored
        }

        // Trường hợp 3: Dạng yyyy-MM-dd (SQL standard)
        try {
            SimpleDateFormat sqlFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date = sqlFormat.parse(dateString);
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return displayFormat.format(date);
        } catch (Exception e) {
            // Ignored
        }

        // Nếu không khớp định dạng nào thì trả về nguyên gốc
        return dateString;
    }

    private void setupDetailItem(View parentView, int iconResId, String value) {
        ImageView imgIcon = parentView.findViewById(R.id.img_detail_icon);
        TextView tvValue = parentView.findViewById(R.id.tv_detail_value);
        if (imgIcon != null) imgIcon.setImageResource(iconResId);
        if (tvValue != null) tvValue.setText(value != null && !value.isEmpty() ? value : "Chưa cập nhật");
    }

    private void setupMenuActions() {
        setupMenuItem(menuEditInfo, android.R.drawable.ic_menu_edit, "Chỉnh sửa thông tin", v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            // Gửi dữ liệu hiện tại sang màn hình sửa
            TextView tvPhoneVal = detailPhone.findViewById(R.id.tv_detail_value);
            TextView tvDobVal = detailDob.findViewById(R.id.tv_detail_value); // Đã format đẹp
            TextView tvGenderVal = detailGender.findViewById(R.id.tv_detail_value);
            TextView tvAddressVal = detailAddress.findViewById(R.id.tv_detail_value);

            intent.putExtra("NAME", tvUserName.getText().toString());
            intent.putExtra("PHONE", tvPhoneVal.getText().toString());
            intent.putExtra("DOB", tvDobVal.getText().toString()); // Gửi ngày đã format
            intent.putExtra("GENDER", tvGenderVal.getText().toString());
            intent.putExtra("ADDRESS", tvAddressVal.getText().toString());

            startActivity(intent);
        });

        setupMenuItem(menuFeedback, android.R.drawable.btn_star_big_off, "Phản hồi", v -> {
            startActivity(new Intent(this, FeedbackActivity.class));
        });

        setupMenuItem(menuChangePassword, android.R.drawable.ic_lock_power_off, "Đổi mật khẩu", v -> {
            startActivity(new Intent(this, ChangePasswordActivity.class));
        });

        setupMenuItem(menuLogout, android.R.drawable.ic_delete, "Đăng xuất", v -> showLogoutConfirm());
    }

    private void setupMenuItem(View parentView, int iconResId, String title, View.OnClickListener listener) {
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
            if (id == R.id.nav_home) {
                startActivity(new Intent(this, DashboardActivity.class));
                return true;
            } else if (id == R.id.nav_booking) {
                startActivity(new Intent(this, BookingActivity.class));
                return true;
            } else if (id == R.id.nav_notifications) {
                startActivity(new Intent(this, NotificationsActivity.class));
                return true;
            }
            return id == R.id.nav_profile;
        });
    }
}