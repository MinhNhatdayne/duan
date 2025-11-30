package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;

public class ConfirmationActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private Button btnConfirmBooking;
    private EditText etNotes;
    
    // Khai báo các View để ánh xạ các thẻ <include>
    private View detailService, detailDoctor, detailLocation, detailTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        // 1. Ánh xạ cơ bản
        imgBackButton = findViewById(R.id.img_back_button);
        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
        etNotes = findViewById(R.id.et_notes);
        
        detailService = findViewById(R.id.detail_service);
        detailDoctor = findViewById(R.id.detail_doctor);
        detailLocation = findViewById(R.id.detail_location);
        detailTime = findViewById(R.id.detail_time);

        // 2. Lấy dữ liệu từ Intent (Được gửi từ SelectTimeActivity hoặc NewAppointmentActivity)
        Intent intent = getIntent();
        String serviceName = intent.getStringExtra("SERVICE_NAME");
        String doctorName = intent.getStringExtra("DOCTOR_NAME");
        String selectedDate = intent.getStringExtra("SELECTED_DATE"); // Ví dụ: 14
        String selectedTime = intent.getStringExtra("SELECTED_TIME"); // Ví dụ: 09:30
        
        // Xử lý hiển thị thời gian cho đẹp
        String fullTime = (selectedDate != null && selectedTime != null) 
                          ? String.format("Ngày %s - %s", selectedDate, selectedTime) 
                          : "Chưa chọn thời gian";

        // 3. Gán dữ liệu lên giao diện
        setupDetailView(detailService, "Dịch vụ", serviceName != null ? serviceName : "Khám tổng quát");
        setupDetailView(detailDoctor, "Bác sĩ", doctorName != null ? doctorName : "Bác sĩ trực");
        setupDetailView(detailLocation, "Cơ sở", "Cơ sở 2: 67 Phạm Tuấn Tài"); // Tạm fix cứng
        setupDetailView(detailTime, "Thời gian", fullTime);

        // 4. Xử lý sự kiện Quay lại
        imgBackButton.setOnClickListener(v -> finish());

        // 5. Xử lý sự kiện Xác nhận Đặt lịch
        btnConfirmBooking.setOnClickListener(v -> {
            String notes = etNotes.getText().toString();
            
            // TODO: Tại đây gọi API lưu xuống cơ sở dữ liệu (Database)
            
            Toast.makeText(ConfirmationActivity.this, "Đặt lịch thành công!", Toast.LENGTH_SHORT).show();
            
            // Sau khi thành công, quay về Trang chủ (Dashboard) và xóa các màn hình cũ trong stack
            Intent dashboardIntent = new Intent(ConfirmationActivity.this, StaffDashboardActivity.class);
            dashboardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(dashboardIntent);
        });
    }

    private void setupDetailView(View parentView, String title, String value) {
        TextView tvTitle = parentView.findViewById(R.id.tv_detail_title);
        TextView tvValue = parentView.findViewById(R.id.tv_detail_value);
        
        if (tvTitle != null) tvTitle.setText(title);
        if (tvValue != null) tvValue.setText(value);
    }
}