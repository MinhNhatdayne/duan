package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhakhoaapp.R;

public class NotificationsActivity extends AppCompatActivity {

    private ImageView imgSearchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // 1. Ánh xạ cơ bản
        imgSearchButton = findViewById(R.id.img_search_button);
        
        // 1b. Ánh xạ các thẻ <include>
        View notif1 = findViewById(R.id.notification_1);
        View notif2 = findViewById(R.id.notification_2);
        View notif3 = findViewById(R.id.notification_3);
        View notif4 = findViewById(R.id.notification_4);

        // 2. Gán dữ liệu cho các item
        setupNotificationView(notif1, 
            "Dự đoán sẽ chỉ có thể giảm hô 60% so với bạn đầu...", 
            "9 tháng trước", true); // true: chưa đọc
            
        setupNotificationView(notif2, 
            "Cải thiện ngay góc nghiêng, cằm lẹm và nhận lại một chiếc cằm vline sau niềng răng.", 
            "một năm trước", false); // false: đã đọc
            
        setupNotificationView(notif3, 
            "Nhân ngày Phụ nữ Việt Nam 20/10, Niềng răng Chuyên sâu Skylake xin được gửi lời chúc...", 
            "một năm trước", true);
            
        setupNotificationView(notif4, 
            "📣 THÔNG BÁO THAY ĐỔI TÊN FANPAGE 📣 Tên mới: Niềng răng chuyên sâu Skylake...", 
            "một năm trước", false);


        // 3. Xử lý sự kiện Tìm kiếm
        imgSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(NotificationsActivity.this, "Chuyển sang chức năng tìm kiếm thông báo", Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    /**
     * Hàm hỗ trợ gán dữ liệu và trạng thái cho item thông báo (sử dụng ID trong item_notification.xml).
     */
    private void setupNotificationView(View parentView, String content, String time, boolean isUnread) {
        TextView tvContent = parentView.findViewById(R.id.tv_notification_content);
        TextView tvTime = parentView.findViewById(R.id.tv_notification_time);
        ImageView imgDot = parentView.findViewById(R.id.img_status_dot);
        
        if (tvContent != null) {
            tvContent.setText(content);
        }
        if (tvTime != null) {
            tvTime.setText(time);
        }
        // Hiển thị/Ẩn dấu chấm đỏ
        if (imgDot != null) {
            imgDot.setVisibility(isUnread ? View.VISIBLE : View.GONE);
        }
    }
}