package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.TextView; // Cần import TextView để gán dữ liệu
import android.widget.Toast;

import com.example.nhakhoaapp.R;

public class ConfirmationActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private Button btnConfirmBooking;
    private EditText etNotes;
    
    // Khai báo các View để ánh xạ các thẻ <include>
    private View detailService;
    private View detailDoctor;
    private View detailLocation;
    private View detailTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // 1. Ánh xạ cơ bản
        imgBackButton = findViewById(R.id.img_back_button);
        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
        etNotes = findViewById(R.id.et_notes);
        
        // 1b. Ánh xạ các thẻ <include> bằng ID mới
        detailService = findViewById(R.id.detail_service);
        detailDoctor = findViewById(R.id.detail_doctor);
        detailLocation = findViewById(R.id.detail_location);
        detailTime = findViewById(R.id.detail_time);

        // TODO: Lấy dữ liệu Service, Doctor, Time từ Intent (Nên lấy từ màn hình trước)
        
        // 2. Gán dữ liệu cho các chi tiết (Hardcode tạm thời)
        setupDetailView(detailService, "Dịch vụ", "Chỉnh nha");
        setupDetailView(detailDoctor, "Bác sĩ", "Đoàn Hồng Lê");
        setupDetailView(detailLocation, "Cơ sở", "Cơ sở 2: 67 Phạm Tuấn Tài");
        setupDetailView(detailTime, "Ngày & Giờ", "Thứ Hai, 14/10/2024 - 9:30h");
        

        // 3. Xử lý sự kiện Quay lại
        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Trở về màn hình trước
            }
        });

        // 4. Xử lý sự kiện Xác nhận Đặt lịch
        btnConfirmBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String notes = etNotes.getText().toString();
                
                // TODO: Gửi yêu cầu Đặt lịch lên API/Database
                
                Toast.makeText(ConfirmationActivity.this, 
                    "Đã xác nhận đặt lịch thành công! Ghi chú: " + notes, 
                    Toast.LENGTH_LONG).show();
                
                // Chuyển về màn hình Trang chủ hoặc Màn hình Thông báo xác nhận
                // finish(); 
            }
        });
    }

    /**
     * Hàm hỗ trợ để tìm TextView trong layout Include (item_confirmation_detail.xml) 
     * và gán giá trị Title và Value.
     */
    private void setupDetailView(View parentView, String title, String value) {
        // Tìm TextView trong layout con (item_confirmation_detail)
        TextView tvTitle = parentView.findViewById(R.id.tv_detail_title);
        TextView tvValue = parentView.findViewById(R.id.tv_detail_value);
        
        if (tvTitle != null) {
            tvTitle.setText(title);
        }
        if (tvValue != null) {
            tvValue.setText(value);
        }
    }
}