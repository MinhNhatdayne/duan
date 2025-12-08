package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DailyAppointmentAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.response.LichHenResponse; // [QUAN TRỌNG] Dùng Model Response
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DailyScheduleActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private TextView tvCurrentDate, tvAppointmentCount;
    private BottomNavigationView bottomNavigationView;
    private ApiService apiService;

    // Adapter
    private DailyAppointmentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);

        apiService = ApiClient.getApiService();

        rvAppointments = findViewById(R.id.rv_daily_appointments);
        tvCurrentDate = findViewById(R.id.tv_current_date);
        tvAppointmentCount = findViewById(R.id.tv_appointment_count);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lịch Khám Hôm Nay");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupRecyclerView();
        updateDateDisplay();

        // Gọi API
        fetchDailySchedule();

        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
        bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_schedule);
        }
        fetchDailySchedule();
    }

    private boolean handleStaffNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_staff_home) {
            startActivity(new Intent(this, StaffDashboardActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (id == R.id.nav_staff_schedule) {
            return true;
        } else if (id == R.id.nav_staff_appointments) {
            startActivity(new Intent(this, AppointmentManagerActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        return false;
    }

    private void setupRecyclerView() {
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        // Khởi tạo Adapter với danh sách rỗng (Kiểu LichHenResponse)
        adapter = new DailyAppointmentAdapter(this, new ArrayList<>());
        rvAppointments.setAdapter(adapter);
    }

    /**
     * Hàm gọi API để lấy danh sách lịch hẹn trong ngày
     */
    private void fetchDailySchedule() {
        tvAppointmentCount.setText("Đang tải lịch hẹn...");

        // Sử dụng Call<List<LichHenResponse>>
        apiService.getTodayAppointments().enqueue(new Callback<List<LichHenResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<LichHenResponse>> call, @NonNull Response<List<LichHenResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LichHenResponse> appointments = response.body();

                    // Cập nhật Adapter
                    if (adapter != null) {
                        adapter.setData(appointments);
                    }

                    updateSummary(appointments);

                } else {
                    Toast.makeText(DailyScheduleActivity.this, "Không có lịch hẹn hôm nay", Toast.LENGTH_SHORT).show();
                    updateSummary(new ArrayList<>());
                    if (adapter != null) adapter.setData(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LichHenResponse>> call, @NonNull Throwable t) {
                Toast.makeText(DailyScheduleActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_LONG).show();
                updateSummary(new ArrayList<>());
            }
        });
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
        String today = dateFormat.format(Calendar.getInstance().getTime());
        tvCurrentDate.setText(String.format("Hôm nay: %s", today));
    }

    private void updateSummary(List<LichHenResponse> appointments) {
        int total = appointments.size();

        // Đếm số lượng "ChoXacNhan" (Tên status trong DB của bạn)
        long pending = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            pending = appointments.stream()
                    .filter(l -> "ChoXacNhan".equals(l.getTrangThai())) // Dùng getTrangThai() mới
                    .count();
        } else {
            for (LichHenResponse item : appointments) {
                if ("ChoXacNhan".equals(item.getTrangThai())) pending++;
            }
        }

        tvAppointmentCount.setText(String.format("Tổng số: %d (Chờ xác nhận: %d)", total, pending));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}