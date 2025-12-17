package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffDashboardActivity extends AppCompatActivity {

    // [CẬP NHẬT] Đã mở lại nút này
    private Button btnViewScheduleAppointments;
    private Button btnQuickPatientRecord;
    private Button btnExamNotesUpdate;
    private Button btnEPrescription;

    private BottomNavigationView bottomNavigationView;

    private TextView tvGreeting, tvRole;
    private TextView tvTotal, tvPending;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_dashboard);

        apiService = ApiClient.getApiService();

        initViews();
        setupCoreModules();

        // Xử lý Bottom Navigation
        bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);

        findViewById(R.id.img_notifications).setOnClickListener(v ->
                Toast.makeText(this, "Chức năng thông báo đang phát triển", Toast.LENGTH_SHORT).show());

        findViewById(R.id.btn_view_schedule).setOnClickListener(v ->
                startActivity(new Intent(this, DailyScheduleActivity.class))
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_home);
        }
        loadStaffInfo();
        fetchAppointmentStats();
    }

    private void initViews() {
//        btnViewScheduleAppointments = findViewById(R.id.btn_view_schedule_appointments);

        btnQuickPatientRecord = findViewById(R.id.btn_quick_patient_record);
        btnExamNotesUpdate = findViewById(R.id.btn_exam_notes_update);
        btnEPrescription = findViewById(R.id.btn_e_prescription);

        tvTotal = findViewById(R.id.tv_total_appointments);
        tvPending = findViewById(R.id.tv_pending_appointments);

        tvGreeting = findViewById(R.id.tv_greeting);
        tvRole = findViewById(R.id.tv_role);

        bottomNavigationView = findViewById(R.id.bottom_nav_menu_doctor);
    }

    private void loadStaffInfo() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);
        String cachedName = prefs.getString("USER_NAME", "Nhân viên");
        tvGreeting.setText("Xin chào, " + cachedName);

        if (userId == null) {
            tvGreeting.setText("Xin chào, Khách");
            return;
        }

        apiService.getNhanVienById(userId).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful() && response.body() != null) {
                    NhanVien nv = response.body();
                    tvGreeting.setText("Xin chào, " + nv.getHo_ten());

                    String roleDisplay = "Nhân viên";
                    if ("BacSi".equalsIgnoreCase(nv.getChuc_vu())) roleDisplay = "Bác sĩ chuyên khoa";
                    else if ("LeTan".equalsIgnoreCase(nv.getChuc_vu())) roleDisplay = "Lễ tân tiếp đón";
                    else if ("QuanLy".equalsIgnoreCase(nv.getChuc_vu())) roleDisplay = "Quản lý phòng khám";
                    tvRole.setText(roleDisplay);
                }
            }
            @Override public void onFailure(Call<NhanVien> call, Throwable t) { }
        });
    }

    private void fetchAppointmentStats() {
        if (tvTotal != null) tvTotal.setText("...");
        if (tvPending != null) tvPending.setText("...");

        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);

        apiService.getTodayAppointmentStats(userId).enqueue(new Callback<AppointmentStats>() {
            @Override
            public void onResponse(@NonNull Call<AppointmentStats> call, @NonNull Response<AppointmentStats> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AppointmentStats stats = response.body();
                    if (tvTotal != null) tvTotal.setText(String.valueOf(stats.getTotalAppointments()));
                    if (tvPending != null) tvPending.setText(String.valueOf(stats.getPendingAppointments()));
                } else {
                    handleErrorStats();
                }
            }
            @Override
            public void onFailure(@NonNull Call<AppointmentStats> call, @NonNull Throwable t) {
                handleErrorStats();
            }
        });
    }

    private void handleErrorStats() {
        if (tvTotal != null) tvTotal.setText("0");
        if (tvPending != null) tvPending.setText("0");
    }

    // [CẬP NHẬT] Điều chỉnh lại logic điều hướng theo Menu mới
    private boolean handleStaffNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_staff_home) {
            return true;

        } else if (id == R.id.nav_staff_schedule) {
            // Chuyển sang Lịch ngày
            startActivity(new Intent(this, DailyScheduleActivity.class));
            overridePendingTransition(0, 0);
            return true;

        } else if (id == R.id.nav_staff_profile) {
            // Chuyển sang Profile (Tôi)
            startActivity(new Intent(this, StaffProfileActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }

        // Đã xóa check R.id.nav_staff_appointments vì menu không còn item này
        return false;
    }

    private void setupCoreModules() {
//        btnViewScheduleAppointments.setOnClickListener(v ->
//                startActivity(new Intent(this, AppointmentManagerActivity.class))
//        );

        btnQuickPatientRecord.setOnClickListener(v -> Toast.makeText(this, "Chức năng: Tra cứu Hồ sơ (Coming Soon)", Toast.LENGTH_SHORT).show());
        btnExamNotesUpdate.setOnClickListener(v -> Toast.makeText(this, "Chức năng: Ghi chú khám (Coming Soon)", Toast.LENGTH_SHORT).show());
        btnEPrescription.setOnClickListener(v -> Toast.makeText(this, "Chức năng: Kê đơn (Coming Soon)", Toast.LENGTH_SHORT).show());
    }
}