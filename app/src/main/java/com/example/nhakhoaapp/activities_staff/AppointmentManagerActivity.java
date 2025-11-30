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
import java.util.Date;
import java.util.List;

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
        Date dummyDate = new Date();
        list.add(new AppointmentHeader("Hôm nay, 10/10/2024"));
        list.add(new LichHen(1, 101, 1, dummyDate, "Khám định kỳ", "Đang chờ", "Nguyễn Thị Lan", "10:00"));
        list.add(new LichHen(2, 102, 1, dummyDate, "Chỉnh nha", "Đã khám", "Lê Quốc Huy", "11:00"));
        list.add(new AppointmentHeader("Ngày mai, 11/10/2024"));
        list.add(new LichHen(4, 104, 2, dummyDate, "Hẹn tái khám", "Chưa khám", "Phạm Kim Chi", "16:00"));
        return list;
    }
}