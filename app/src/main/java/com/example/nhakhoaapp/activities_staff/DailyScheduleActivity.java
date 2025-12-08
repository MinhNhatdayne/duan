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
import com.example.nhakhoaapp.models.LichHen;
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

    // Khai báo API Service
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);

        // Khởi tạo API Service
        apiService = ApiClient.getApiService();

        // Ánh xạ View
        rvAppointments = findViewById(R.id.rv_daily_appointments);
        tvCurrentDate = findViewById(R.id.tv_current_date);
        tvAppointmentCount = findViewById(R.id.tv_appointment_count);

        // Thiết lập Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lịch Khám Hôm Nay");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        // Thiết lập RecyclerView và hiển thị ngày tháng
        setupRecyclerView();
        updateDateDisplay();

        // Gọi API để tải dữ liệu lịch hẹn thực tế
        fetchDailySchedule();

        // Xử lý Bottom Navigation Staff
        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
        bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);
    }

    // Highlight tab "Lịch ngày" khi mở màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_schedule);
        }
        // Gọi lại API khi quay lại màn hình để đảm bảo dữ liệu mới nhất
        fetchDailySchedule();
    }

    // Hàm điều hướng chung cho Staff
    private boolean handleStaffNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_staff_home) {
            startActivity(new Intent(this, StaffDashboardActivity.class));
            overridePendingTransition(0, 0);
            return true;

        } else if (id == R.id.nav_staff_schedule) {
            return true; // Đang ở màn hình này rồi

        } else if (id == R.id.nav_staff_appointments) {
            startActivity(new Intent(this, AppointmentManagerActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }
        return false;
    }

    // Hàm thiết lập RecyclerView ban đầu
    private void setupRecyclerView() {
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        // Khởi tạo Adapter với danh sách rỗng
        DailyAppointmentAdapter adapter = new DailyAppointmentAdapter(this, new ArrayList<>());
        rvAppointments.setAdapter(adapter);
    }

    /**
     * Hàm gọi API để lấy danh sách lịch hẹn trong ngày
     */
    private void fetchDailySchedule() {
        // Cập nhật trạng thái loading
        tvAppointmentCount.setText("Đang tải lịch hẹn...");

        apiService.getTodayAppointments().enqueue(new Callback<List<LichHen>>() {
            @Override
            public void onResponse(@NonNull Call<List<LichHen>> call, @NonNull Response<List<LichHen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LichHen> appointments = response.body();

                    // Cập nhật Adapter (sử dụng method updateData đã thêm ở file DailyAppointmentAdapter)
                    DailyAppointmentAdapter adapter = (DailyAppointmentAdapter) rvAppointments.getAdapter();
                    if (adapter != null) {
                        adapter.updateData(appointments);
                    }

                    // Cập nhật thông tin tổng quan
                    updateSummary(appointments);

                } else {
                    Toast.makeText(DailyScheduleActivity.this, "Lỗi khi tải lịch hẹn: " + response.code(), Toast.LENGTH_SHORT).show();
                    updateSummary(new ArrayList<>()); // Hiển thị 0
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LichHen>> call, @NonNull Throwable t) {
                Toast.makeText(DailyScheduleActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_LONG).show();
                updateSummary(new ArrayList<>()); // Hiển thị 0
            }
        });
    }

    // Hàm cập nhật hiển thị ngày tháng
    private void updateDateDisplay() {
        // Định dạng ngày tháng bằng tiếng Việt
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
        String today = dateFormat.format(Calendar.getInstance().getTime());
        tvCurrentDate.setText(String.format("Hôm nay: %s", today));
    }

    /**
     * Hàm cập nhật thông tin tổng quan số lượng lịch hẹn.
     * @param appointments Danh sách lịch hẹn đã tải từ API.
     */
    private void updateSummary(List<LichHen> appointments) {
        int total = appointments.size();

        // Đếm số lượng lịch hẹn có trạng thái "Chờ khám"
        long pending = appointments.stream()
                .filter(l -> "Chờ khám".equals(l.getTrang_thai()))
                .count();

        tvAppointmentCount.setText(String.format("Tổng số lịch hẹn: %d (Chờ khám: %d)", total, pending));
    }
}