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
import com.example.nhakhoaapp.models.LichHen;
import com.example.nhakhoaapp.models_adapter.AppointmentHeader;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;
// import java.util.Date; // KHÔNG CẦN DÙNG Date NỮA

public class AppointmentManagerActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private Button btnNewAppointment;
    private TextView tvTitle;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_manager);

        rvAppointments = findViewById(R.id.recycler_appointments);
        btnNewAppointment = findViewById(R.id.btn_new_appointment);
        tvTitle = findViewById(R.id.tv_title);

        tvTitle.setText("Quản lý cuộc hẹn");

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

    private void loadAppointments() {
        List<Object> combinedList = createCombinedAppointmentList();
        SingleAppointmentAdapter adapter = new SingleAppointmentAdapter(this, combinedList);
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvAppointments.setAdapter(adapter);
    }

    private List<Object> createCombinedAppointmentList() {
        List<Object> list = new ArrayList<>();
        // Giả lập thoi_gian_hen bằng String ISO Date để khớp với model mới
        String dummyDateIso = "2025-10-10T10:00:00.000Z";

        list.add(new AppointmentHeader("Hôm nay, 10/10/2024"));

        // Sử dụng constructor mới:
        // LichHen(_id: String, id_benh_nhan: String, id_bac_si: String, thoi_gian_hen: String, ly_do_kham: String, trang_thai: String, ten_benh_nhan: String, gio_kham: String)
        list.add(new LichHen("6570c915f013d20a02b1c3e1", "6570c915f013d20a02b1c001", "6570c915f013d20a02b1c101", dummyDateIso, "Khám định kỳ", "Đang chờ", "Nguyễn Thị Lan", "10:00"));
        list.add(new LichHen("6570c915f013d20a02b1c3e2", "6570c915f013d20a02b1c002", "6570c915f013d20a02b1c101", dummyDateIso, "Chỉnh nha", "Đã khám", "Lê Quốc Huy", "11:00"));

        list.add(new AppointmentHeader("Ngày mai, 11/10/2024"));

        list.add(new LichHen("6570c915f013d20a02b1c3e4", "6570c915f013d20a02b1c004", "6570c915f013d20a02b1c102", dummyDateIso, "Hẹn tái khám", "Chưa khám", "Phạm Kim Chi", "16:00"));

        return list;
    }
}