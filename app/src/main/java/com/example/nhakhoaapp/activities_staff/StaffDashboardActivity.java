package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.dto.AppointmentStats;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffDashboardActivity extends AppCompatActivity {

    private Button btnViewScheduleAppointments;
    private Button btnQuickPatientRecord;
    private Button btnExamNotesUpdate;
    private Button btnEPrescription;

    private BottomNavigationView bottomNavigationView;

    // Khai báo TextView để cập nhật
    private TextView tvTotal;
    private TextView tvPending;
    
    // Khai báo API Service
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);
        
        // Khởi tạo API Service
        apiService = ApiClient.getApiService();

        // 1. Ánh xạ 4 Button chức năng cốt lõi
        btnViewScheduleAppointments = findViewById(R.id.btn_view_schedule_appointments);
        btnQuickPatientRecord = findViewById(R.id.btn_quick_patient_record);
        btnExamNotesUpdate = findViewById(R.id.btn_exam_notes_update);
        btnEPrescription = findViewById(R.id.btn_e_prescription);
        
        // 2. Ánh xạ TextView cho số liệu thống kê
        tvTotal = findViewById(R.id.tv_total_appointments);
        tvPending = findViewById(R.id.tv_pending_appointments);
        
        // 3. Gán sự kiện cho 4 chức năng
        setupCoreModules();

        // 4. Xử lý các sự kiện phụ (Notifications và Xem lịch hẹn hôm nay)
        findViewById(R.id.img_notifications).setOnClickListener(v -> 
            Toast.makeText(this, "Mở màn hình thông báo hệ thống", Toast.LENGTH_SHORT).show());
            
        findViewById(R.id.btn_view_schedule).setOnClickListener(v -> 
            // Nút "Xem chi tiết Lịch hẹn" trong CardView -> Chuyển sang AppointmentManagerActivity
            startActivity(new Intent(this, AppointmentManagerActivity.class))
        );
            
        // 5. Cập nhật dữ liệu động (Gọi API)
        // Dữ liệu sẽ được gọi trong onResume, nhưng vẫn gọi ở đây nếu onResume chưa được kích hoạt
        fetchAppointmentStats();

        // 6. XỬ LÝ BOTTOM NAVIGATION
        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
        bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);
    }
    
    // Highlight tab TỔNG QUAN và gọi lại API khi vào màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_home);
        }
        // Gọi API để cập nhật số liệu mới nhất khi người dùng quay lại màn hình
        fetchAppointmentStats(); 
    }

    /**
     * Hàm gọi API để lấy số liệu thống kê lịch hẹn hôm nay
     */
    private void fetchAppointmentStats() {
        // Đặt giá trị loading
        if (tvTotal != null) tvTotal.setText("...");
        if (tvPending != null) tvPending.setText("...");

        apiService.getTodayAppointmentStats().enqueue(new Callback<AppointmentStats>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentStats> call, @NonNull Response<AppointmentStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppointmentStats stats = response.body();
                    
                    // Cập nhật giao diện với dữ liệu thực tế
                    if (tvTotal != null) tvTotal.setText(String.valueOf(stats.getTotalAppointments()));
                    if (tvPending != null) tvPending.setText(String.valueOf(stats.getPendingAppointments()));
                } else {
                    Toast.makeText(StaffDashboardActivity.this, "Lỗi khi tải số liệu: " + response.code(), Toast.LENGTH_SHORT).show();
                    if (tvTotal != null) tvTotal.setText("0");
                    if (tvPending != null) tvPending.setText("0");
                }
            }

            @Override
            public void onFailure(@NonNull Call<AppointmentStats> call, @NonNull Throwable t) {
                Toast.makeText(StaffDashboardActivity.this, "Lỗi kết nối API thống kê: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                if (tvTotal != null) tvTotal.setText("0");
                if (tvPending != null) tvPending.setText("0");
            }
        });
    }

    // Hàm điều hướng Staff Menu
    private boolean handleStaffNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_staff_home) {
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
        return false;
    }

    private void setupCoreModules() {
        
        // 1. Xem lịch làm việc & lịch hẹn bệnh nhân
        btnViewScheduleAppointments.setOnClickListener(v -> {
             startActivity(new Intent(this, AppointmentManagerActivity.class));
        });

        // 2. Xem hồ sơ bệnh nhân nhanh
        btnQuickPatientRecord.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng: Tra cứu Hồ sơ bệnh nhân (TODO)", Toast.LENGTH_SHORT).show();
            // TODO: Intent -> PatientSearchActivity hoặc QuickRecordActivity
        });

        // 3. Ghi chú, cập nhật kết quả khám
        btnExamNotesUpdate.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng: Ghi chú & Kết quả khám (TODO)", Toast.LENGTH_SHORT).show();
            // TODO: Intent -> ExamNoteActivity
        });

        // 4. Kê đơn thuốc điện tử
        btnEPrescription.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng: Kê đơn thuốc (TODO)", Toast.LENGTH_SHORT).show();
            // TODO: Intent -> EPrescriptionActivity
        });
    }
}