package com.example.nhakhoaapp.activities_customer;

import android.content.Intent; // Nhớ import cái này
import android.os.Bundle;
import android.view.MenuItem; // Nhớ import cái này
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull; // Nhớ import cái này
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView; // Nhớ import cái này

public class NotificationsActivity extends AppCompatActivity {

    private ImageView imgSearchButton;
    private BottomNavigationView bottomNavigationView; // 1. Khai báo biến này

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // --- CODE CŨ CỦA BẠN ---
        imgSearchButton = findViewById(R.id.img_search_button);

        View notif1 = findViewById(R.id.notification_1);
        View notif2 = findViewById(R.id.notification_2);
        View notif3 = findViewById(R.id.notification_3);
        View notif4 = findViewById(R.id.notification_4);

        setupNotificationView(notif1, "Dự đoán sẽ chỉ có thể giảm hô 60%...", "9 tháng trước", true);
        setupNotificationView(notif2, "Cải thiện ngay góc nghiêng...", "một năm trước", false);
        setupNotificationView(notif3, "Nhân ngày Phụ nữ Việt Nam 20/10...", "một năm trước", true);
        setupNotificationView(notif4, "📣 THÔNG BÁO THAY ĐỔI TÊN FANPAGE...", "một năm trước", false);

        imgSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationsActivity.this, "Chuyển sang tìm kiếm...", Toast.LENGTH_SHORT).show();
            }
        });
        // -----------------------

        // 2. THÊM ĐOẠN NÀY: Ánh xạ và bắt sự kiện Bottom Navigation
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            handleBottomNav(item);
            return true;
        });
    }

    // 3. THÊM ĐOẠN NÀY: Để highlight đúng icon khi vào màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_notifications);
        }
    }

    // 4. THÊM ĐOẠN NÀY: Hàm xử lý chuyển màn hình
    private void handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivity.class));
            return;

        } else if (id == R.id.nav_booking) {
            startActivity(new Intent(this, BookingActivity.class));
            return;

        } else if (id == R.id.nav_notifications) {
            return; // Đang ở đây rồi nên không làm gì cả

        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class)); // Nhớ tạo ProfileActivity nếu chưa có
            return;
        }
    }

    // --- CODE CŨ CỦA BẠN (GIỮ NGUYÊN) ---
    private void setupNotificationView(View parentView, String content, String time, boolean isUnread) {
        TextView tvContent = parentView.findViewById(R.id.tv_notification_content);
        TextView tvTime = parentView.findViewById(R.id.tv_notification_time);
        ImageView imgDot = parentView.findViewById(R.id.img_status_dot);

        if (tvContent != null) tvContent.setText(content);
        if (tvTime != null) tvTime.setText(time);
        if (imgDot != null) imgDot.setVisibility(isUnread ? View.VISIBLE : View.GONE);
    }
}