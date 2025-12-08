package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.SingleAppointmentAdapter;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.LichHen;
import com.example.nhakhoaapp.models_adapter.AppointmentHeader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentManagerActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private Button btnNewAppointment;
    private TextView tvTitle;
    private BottomNavigationView bottomNavigationView;

    // Khai báo Retrofit ApiService
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_manager);

        rvAppointments = findViewById(R.id.recycler_appointments);
        btnNewAppointment = findViewById(R.id.btn_new_appointment);
        tvTitle = findViewById(R.id.tv_title);

        tvTitle.setText("Quản lý cuộc hẹn");

        // Khởi tạo ApiService: Sử dụng ApiClient
        apiService = ApiClient.getApiService();

        // Thay thế loadAppointments() cũ bằng logic gọi API
        loadAppointments();

        btnNewAppointment.setOnClickListener(v -> {
            // Chuyển sang NewAppointmentActivity (Không có BottomNav)
            Intent intent = new Intent(AppointmentManagerActivity.this, NewAppointmentActivity.class);
            startActivity(intent);
        });

        // Xử lý Navigation Staff
        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
        bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Highlight tab DS Hẹn
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_appointments);
        }
    }

    private boolean handleStaffNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_staff_home) {
            startActivity(new Intent(this, StaffDashboardActivity.class));
            overridePendingTransition(0, 0);
            return true;

        } else if (id == R.id.nav_staff_schedule) {
            startActivity(new Intent(this, DailyScheduleActivity.class));
            overridePendingTransition(0, 0);
            return true;

        } else if (id == R.id.nav_staff_appointments) {
            return true; // Đang ở đây rồi
        }
        return false;
    }

    /**
     * Thực hiện gọi API để tải danh sách lịch hẹn
     */
    private void loadAppointments() {
        // Khởi tạo RecyclerView trước để tránh lỗi NullPointer nếu API gọi thất bại
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));

        apiService.getAllLichHen().enqueue(new Callback<List<LichHen>>() {
            @Override
            public void onResponse(@NonNull Call<List<LichHen>> call, @NonNull Response<List<LichHen>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LichHen> fetchedAppointments = response.body();

                    // Xử lý dữ liệu và hiển thị lên RecyclerView
                    populateRecyclerView(fetchedAppointments);

                    Toast.makeText(AppointmentManagerActivity.this, "Tải danh sách lịch hẹn thành công!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(AppointmentManagerActivity.this, "Lỗi tải dữ liệu: " + response.code(), Toast.LENGTH_LONG).show();
                    populateRecyclerView(new ArrayList<>()); // Hiển thị danh sách rỗng
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LichHen>> call, @NonNull Throwable t) {
                Toast.makeText(AppointmentManagerActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_LONG).show();
                populateRecyclerView(new ArrayList<>()); // Hiển thị danh sách rỗng
            }
        });
    }

    /**
     * Xử lý danh sách LichHen và đưa vào Adapter (bao gồm cả Header)
     */
    private void populateRecyclerView(List<LichHen> fetchedAppointments) {
        List<Object> combinedList = createCombinedListWithHeaders(fetchedAppointments);
        SingleAppointmentAdapter adapter = new SingleAppointmentAdapter(this, combinedList);
        rvAppointments.setAdapter(adapter);
    }

    /**
     * Chuyển đổi List<LichHen> thành List<Object> có AppointmentHeader
     * NOTE: Logic nhóm theo ngày cần được phát triển thêm để xử lý chính xác hơn
     */
    private List<Object> createCombinedListWithHeaders(List<LichHen> appointments) {
        List<Object> list = new ArrayList<>();

        if (appointments == null || appointments.isEmpty()) {
            list.add(new AppointmentHeader("Không tìm thấy lịch hẹn nào."));
            return list;
        }

        // Tạm thời, thêm một header chung và tất cả các mục lịch hẹn
        list.add(new AppointmentHeader("Tất cả Lịch hẹn Đã Tải (" + appointments.size() + ")"));
        list.addAll(appointments);

        return list;
    }
}