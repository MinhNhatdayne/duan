package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhakhoaapp.R;

public class StaffDashboardActivity extends AppCompatActivity {

    // Khai báo các Button chức năng cốt lõi
    private Button btnViewScheduleAppointments;
    private Button btnQuickPatientRecord;
    private Button btnExamNotesUpdate;
    private Button btnEPrescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);
        
        // 1. Ánh xạ 4 Button chức năng cốt lõi
        btnViewScheduleAppointments = findViewById(R.id.btn_view_schedule_appointments);
        btnQuickPatientRecord = findViewById(R.id.btn_quick_patient_record);
        btnExamNotesUpdate = findViewById(R.id.btn_exam_notes_update);
        btnEPrescription = findViewById(R.id.btn_e_prescription);
        
        // 2. Gán sự kiện cho 4 chức năng
        setupCoreModules();

        // 3. Xử lý các sự kiện phụ (Notifications và Xem lịch hẹn hôm nay)
        findViewById(R.id.img_notifications).setOnClickListener(v -> 
            Toast.makeText(this, "Mở màn hình thông báo hệ thống", Toast.LENGTH_SHORT).show());
            
        findViewById(R.id.btn_view_schedule).setOnClickListener(v -> 
            Toast.makeText(this, "Mở lịch hẹn chi tiết hôm nay", Toast.LENGTH_SHORT).show());
            
        // 4. Cập nhật dữ liệu động (Giả lập)
        ((TextView) findViewById(R.id.tv_total_appointments)).setText("12");
        ((TextView) findViewById(R.id.tv_pending_appointments)).setText("3");
    }
    
    /**
     * Hàm thiết lập các hành động click cho 4 chức năng cốt lõi.
     */
    private void setupCoreModules() {
        
        // 1. Xem lịch làm việc & lịch hẹn bệnh nhân
        btnViewScheduleAppointments.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình Quản lý Lịch hẹn/Lịch làm việc", Toast.LENGTH_SHORT).show();
            // TODO: Thêm Intent để chuyển sang màn hình Lịch làm việc
        });

        // 2. Xem hồ sơ bệnh nhân nhanh
        btnQuickPatientRecord.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình Tra cứu Hồ sơ bệnh nhân", Toast.LENGTH_SHORT).show();
            // TODO: Thêm Intent để chuyển sang màn hình Tra cứu Hồ sơ
        });

        // 3. Ghi chú, cập nhật kết quả khám
        btnExamNotesUpdate.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình Ghi chú & Cập nhật kết quả khám", Toast.LENGTH_SHORT).show();
            // TODO: Thêm Intent để chuyển sang màn hình Cập nhật khám
        });

        // 4. Kê đơn thuốc điện tử
        btnEPrescription.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình Kê đơn thuốc điện tử (Rút gọn)", Toast.LENGTH_SHORT).show();
            // TODO: Thêm Intent để chuyển sang màn hình Kê đơn
        });
    }
}