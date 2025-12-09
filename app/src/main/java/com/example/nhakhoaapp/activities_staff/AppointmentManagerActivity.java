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
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.response.LichHenResponse; // [QUAN TRỌNG] Dùng Model Response
import com.example.nhakhoaapp.models_adapter.AppointmentHeader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentManagerActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private Button btnNewAppointment;
    private TextView tvTitle;
    private BottomNavigationView bottomNavigationView;

    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_manager);

        // Ánh xạ Views
        rvAppointments = findViewById(R.id.recycler_appointments);
        btnNewAppointment = findViewById(R.id.btn_new_appointment);
        tvTitle = findViewById(R.id.tv_title);
        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);

        tvTitle.setText("Quản lý cuộc hẹn");

        // Khởi tạo API & RecyclerView
        apiService = ApiClient.getApiService();
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));

        // 1. [FIX LỖI NAV] Cài đặt trạng thái BottomNav TRƯỚC KHI gán listener
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_appointments);
            bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);
        }

        // Tải dữ liệu lần đầu
        loadAppointments();

        btnNewAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentManagerActivity.this, NewAppointmentActivity.class);
            startActivity(intent);
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Load lại danh sách khi quay lại màn hình này
        loadAppointments();

        // [FIX LỖI NAV] KHÔNG gọi setSelectedItemId ở đây nữa để tránh kích hoạt lại listener
    }

    private boolean handleStaffNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();

        // Nếu bấm vào chính tab hiện tại thì không làm gì
        if (id == R.id.nav_staff_appointments) {
            return true;
        }

        if (id == R.id.nav_staff_home) {
            startActivity(new Intent(this, StaffDashboardActivity.class));
            overridePendingTransition(0, 0);
            return true;
        } else if (id == R.id.nav_staff_schedule) {
            startActivity(new Intent(this, DailyScheduleActivity.class));
            overridePendingTransition(0, 0);
            return true;
        }

        return false;
    }

    /**
     * Gọi API getAllLichHen và hứng dữ liệu bằng LichHenResponse
     */
    private void loadAppointments() {
        // Sử dụng Call<List<LichHenResponse>> thay vì Call<List<LichHen>>
        apiService.getAllLichHen().enqueue(new Callback<List<LichHenResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<LichHenResponse>> call, @NonNull Response<List<LichHenResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LichHenResponse> fetchedAppointments = response.body();

                    // Đảo ngược để cái mới nhất lên đầu
                    Collections.reverse(fetchedAppointments);

                    populateRecyclerView(fetchedAppointments);
                    // Bỏ Toast mỗi lần load để đỡ phiền user
                } else {
                    Toast.makeText(AppointmentManagerActivity.this, "Lỗi tải dữ liệu: " + response.code(), Toast.LENGTH_SHORT).show();
                    populateRecyclerView(new ArrayList<>());
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LichHenResponse>> call, @NonNull Throwable t) {
                Toast.makeText(AppointmentManagerActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                populateRecyclerView(new ArrayList<>());
            }
        });
    }

    /**
     * Xử lý danh sách LichHenResponse và đưa vào Adapter
     */
    private void populateRecyclerView(List<LichHenResponse> fetchedAppointments) {
        List<Object> combinedList = createCombinedListWithHeaders(fetchedAppointments);
        SingleAppointmentAdapter adapter = new SingleAppointmentAdapter(this, combinedList);
        rvAppointments.setAdapter(adapter);
    }

    /**
     * Chuyển đổi List<LichHenResponse> thành List<Object> có Header
     */
    private List<Object> createCombinedListWithHeaders(List<LichHenResponse> appointments) {
        List<Object> list = new ArrayList<>();

        if (appointments == null || appointments.isEmpty()) {
            list.add(new AppointmentHeader("Chưa có lịch hẹn nào."));
            return list;
        }

        list.add(new AppointmentHeader("Danh sách Lịch hẹn (" + appointments.size() + ")"));
        list.addAll(appointments);

        return list;
    }
}