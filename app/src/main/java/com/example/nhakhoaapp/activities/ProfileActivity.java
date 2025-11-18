package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhakhoaapp.R;

public class ProfileActivity extends AppCompatActivity {
    
    // Khai báo các View cho chi tiết người dùng
    private View detailPhone, detailDob, detailGender, detailAddress;
    // Khai báo các View cho menu
    private View menuEditInfo, menuFeedback, menuChangePassword, menuLogout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1. Ánh xạ các chi tiết người dùng
        detailPhone = findViewById(R.id.detail_phone);
        detailDob = findViewById(R.id.detail_dob);
        detailGender = findViewById(R.id.detail_gender);
        detailAddress = findViewById(R.id.detail_address);
        
        // 2. Gán dữ liệu cho chi tiết người dùng
        // Sử dụng icon mặc định của Android để minh họa (Nếu bạn có icon riêng, thay R.drawable.my_icon vào)
        setupDetailItem(detailPhone, android.R.drawable.ic_menu_call, "0978948571");
        setupDetailItem(detailDob, android.R.drawable.ic_menu_today, "13/11/1989");
        setupDetailItem(detailGender, android.R.drawable.ic_menu_compass, "Nam"); 
        setupDetailItem(detailAddress, android.R.drawable.ic_menu_myplaces, "TDP An Phú, Thị trấn Kiến Xương...");
        
        // 3. Ánh xạ các mục Menu
        menuEditInfo = findViewById(R.id.menu_edit_info);
        menuFeedback = findViewById(R.id.menu_feedback);
        menuChangePassword = findViewById(R.id.menu_change_password);
        menuLogout = findViewById(R.id.menu_logout);
        
        // 4. Gán dữ liệu và sự kiện cho Menu
        setupMenuItem(menuEditInfo, android.R.drawable.ic_menu_edit, "Chỉnh sửa thông tin", "EditInfo");
        setupMenuItem(menuFeedback, android.R.drawable.btn_star_big_off, "Phản hồi", "Feedback");
        setupMenuItem(menuChangePassword, android.R.drawable.ic_lock_power_off, "Đổi mật khẩu", "ChangePassword");
        setupMenuItem(menuLogout, android.R.drawable.ic_delete, "Đăng xuất", "Logout");
    }

    // Hàm hỗ trợ gán dữ liệu cho item_profile_detail
    private void setupDetailItem(View parentView, int iconResId, String value) {
        ImageView imgIcon = parentView.findViewById(R.id.img_detail_icon); // Giả sử ID icon
        TextView tvValue = parentView.findViewById(R.id.tv_detail_value); // Giả sử ID value
        
        if (imgIcon != null) imgIcon.setImageResource(iconResId);
        if (tvValue != null) tvValue.setText(value);
    }

    // Hàm hỗ trợ gán dữ liệu và xử lý click cho item_profile_menu
    private void setupMenuItem(View parentView, int iconResId, String title, String action) {
        ImageView imgIcon = parentView.findViewById(R.id.img_menu_icon); // Giả sử ID icon
        TextView tvTitle = parentView.findViewById(R.id.tv_menu_title); // Giả sử ID title
        
        if (imgIcon != null) imgIcon.setImageResource(iconResId);
        if (tvTitle != null) tvTitle.setText(title);

        parentView.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình: " + action, Toast.LENGTH_SHORT).show();
            // TODO: Thêm Intent để chuyển màn hình thực tế
        });
    }
}