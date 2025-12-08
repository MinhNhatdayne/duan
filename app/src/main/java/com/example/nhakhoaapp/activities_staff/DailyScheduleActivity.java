package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager; // Thêm import LayoutManager
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DailyAppointmentAdapter;
import com.example.nhakhoaapp.models.LichHen;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyScheduleActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private TextView tvCurrentDate, tvAppointmentCount;

    // 1. Khai báo BottomNavigation
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);

        // Ánh xạ View
        rvAppointments = findViewById(R.id.rv_daily_appointments);
        tvCurrentDate = findViewById(R.id.tv_current_date);
        tvAppointmentCount = findViewById(R.id.tv_appointment_count);

        // Thiết lập Toolbar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lịch Khám Hôm Nay");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        loadDailySchedule();

        // 2. Xử lý Bottom Navigation Staff
        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
        bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);
    }

    // 3. Highlight tab "Lịch ngày" khi mở màn hình này
    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_schedule);
        }
    }

    // 4. Hàm điều hướng chung cho Staff
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

    private void loadDailySchedule() {
        // Tạo danh sách lịch hẹn giả lập
        List<LichHen> appointments = createDummyAppointments();

        // Cập nhật thông tin tổng quan
        updateSummary(appointments);

        // Thiết lập Layout Manager (đã thêm)
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));

        // Thiết lập Adapter cho RecyclerView
        DailyAppointmentAdapter adapter = new DailyAppointmentAdapter(this, appointments);
        rvAppointments.setAdapter(adapter);
    }

    private List<LichHen> createDummyAppointments() {
        List<LichHen> list = new ArrayList<>();

        // Sử dụng String (ISO Date string giả định) thay vì java.util.Date
        String dummyDateIso = "2025-10-10T00:00:00.000Z";

        // Sửa lỗi: Sử dụng constructor mới với String ID và String Date
        // LichHen(_id: String, id_benh_nhan: String, id_bac_si: String, thoi_gian_hen: String, ly_do_kham: String, trang_thai: String, ten_benh_nhan: String, gio_kham: String)
        list.add(new LichHen("6570c915f013d20a02b1c3e1", "6570c915f013d20a02b1c001", "6570c915f013d20a02b1c101", dummyDateIso, "Khám định kỳ", "Đã khám", "Nguyễn Mạnh Toàn", "08:30"));
        list.add(new LichHen("6570c915f013d20a02b1c3e2", "6570c915f013d20a02b1c002", "6570c915f013d20a02b1c101", dummyDateIso, "Điều trị tủy răng", "Đang chờ", "Trần Thị Lan", "09:30"));
        list.add(new LichHen("6570c915f013d20a02b1c3e3", "6570c915f013d20a02b1c003", "6570c915f013d20a02b1c102", dummyDateIso, "Nhổ răng khôn", "Đang chờ", "Lê Văn Hùng", "10:30"));
        list.add(new LichHen("6570c915f013d20a02b1c3e4", "6570c915f013d20a02b1c004", "6570c915f013d20a02b1c102", dummyDateIso, "Tái khám chỉnh nha", "Chưa khám", "Phạm Thị Thúy", "14:00"));
        list.add(new LichHen("6570c915f013d20a02b1c3e5", "6570c915f013d20a02b1c005", "6570c915f013d20a02b1c103", dummyDateIso, "Khám tổng quát", "Dời lịch", "Vũ Minh Đức", "15:30"));

        return list;
    }

    private void updateSummary(List<LichHen> appointments) {
        // Vẫn giữ lại Date và Calendar vì chúng dùng để hiển thị ngày tháng hiện tại (local display)
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
        String today = dateFormat.format(Calendar.getInstance().getTime());

        tvCurrentDate.setText(String.format("Hôm nay: %s", today));

        int total = appointments.size();
        // Sử dụng stream() để đếm các cuộc hẹn đang chờ
        long pending = appointments.stream().filter(l -> "Đang chờ".equals(l.getTrang_thai())).count();

        tvAppointmentCount.setText(String.format("Tổng số lịch hẹn: %d (Đang chờ: %d)", total, pending));
    }
}