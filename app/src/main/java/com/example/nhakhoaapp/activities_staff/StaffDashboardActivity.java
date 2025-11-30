package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StaffDashboardActivity extends AppCompatActivity {

    // Khai báo các Button chức năng cốt lõi
    private Button btnViewScheduleAppointments;
    private Button btnQuickPatientRecord;
    private Button btnExamNotesUpdate;
    private Button btnEPrescription;

    // Khai báo BottomNavigation
    private BottomNavigationView bottomNavigationView;

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
            // Nút "Xem chi tiết Lịch hẹn" trong CardView -> Chuyển sang AppointmentManagerActivity
            startActivity(new Intent(this, AppointmentManagerActivity.class))
        );
            
        // 4. Cập nhật dữ liệu động (Giả lập)
        TextView tvTotal = findViewById(R.id.tv_total_appointments);
        TextView tvPending = findViewById(R.id.tv_pending_appointments);
        if(tvTotal != null) tvTotal.setText("12");
        if(tvPending != null) tvPending.setText("3");

        // 5. XỬ LÝ BOTTOM NAVIGATION
        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
        bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);
    }
    
    // Highlight tab TỔNG QUAN khi vào màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_home);
        }
    }

    // Hàm điều hướng Staff Menu
    private boolean handleStaffNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_staff_home) {
            return true; // Đang ở Home rồi

        } else if (id == R.id.nav_staff_schedule) {
            startActivity(new Intent(this, DailyScheduleActivity.class));
            overridePendingTransition(0, 0);
            return true;

        } else if (id == R.id.nav_staff_appointments) {
            startActivity(new Intent(this, AppointmentManagerActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        return false;
    }

    private void setupCoreModules() {
        
        // 1. Xem lịch làm việc & lịch hẹn bệnh nhân
        btnViewScheduleAppointments.setOnClickListener(v -> {
             startActivity(new Intent(this, AppointmentManagerActivity.class));
        });

        // 2. Xem hồ sơ bệnh nhân nhanh
        btnQuickPatientRecord.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng: Tra cứu Hồ sơ bệnh nhân", Toast.LENGTH_SHORT).show();
        });

        // 3. Ghi chú, cập nhật kết quả khám
        btnExamNotesUpdate.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng: Ghi chú & Kết quả khám", Toast.LENGTH_SHORT).show();
        });

        // 4. Kê đơn thuốc điện tử
        btnEPrescription.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng: Kê đơn thuốc", Toast.LENGTH_SHORT).show();
        });
    }
}