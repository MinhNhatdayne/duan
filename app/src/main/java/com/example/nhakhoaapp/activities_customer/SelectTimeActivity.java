package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DateSlotAdapter;
import com.example.nhakhoaapp.adapters.TimeSlotAdapter;
import com.example.nhakhoaapp.models_adapter.DateSlot;
import com.example.nhakhoaapp.models_adapter.TimeSlot;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class SelectTimeActivity extends AppCompatActivity {

    private RecyclerView rvDateSlots, rvTimeSlots;
    private Button btnContinue;
    private DateSlotAdapter dateAdapter;
    private TimeSlotAdapter timeAdapter;

    private List<DateSlot> dateSlotList;
    private List<TimeSlot> timeSlotList;

    private DateSlot selectedDateSlot = null;
    private TimeSlot selectedTimeSlot = null;

    private String serviceName, doctorName, doctorId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        // Nhận dữ liệu từ màn hình trước
        serviceName = getIntent().getStringExtra("SERVICE_NAME");
        doctorName = getIntent().getStringExtra("DOCTOR_NAME");
        doctorId = getIntent().getStringExtra("DOCTOR_ID"); // ID Bác sĩ để gửi API

        rvDateSlots = findViewById(R.id.rv_date_slots);
        rvTimeSlots = findViewById(R.id.rv_time_slots);
        btnContinue = findViewById(R.id.btn_continue);
        TextView tvServiceName = findViewById(R.id.tv_service_name);
        TextView tvDoctorDetails = findViewById(R.id.tv_doctor_details);
        ImageView imgBack = findViewById(R.id.img_back_button);
        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);

        tvServiceName.setText(serviceName);
        tvDoctorDetails.setText(doctorName);
        imgBack.setOnClickListener(v -> finish());
        
        // Setup Bottom Nav
        bottomNav.setSelectedItemId(R.id.nav_booking);
        bottomNav.setOnItemSelectedListener(this::handleBottomNav);

        setupDateRecyclerView();
        setupTimeRecyclerView();
        updateContinueButtonState();

        btnContinue.setOnClickListener(v -> {
            if (selectedDateSlot == null || selectedTimeSlot == null) {
                Toast.makeText(this, "Vui lòng chọn ngày và giờ.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tạo chuỗi thời gian chuẩn: yyyy-MM-dd HH:mm
            String fullDateTime = selectedDateSlot.getFullDateString() + " " + selectedTimeSlot.getTime();

            // Chuyển sang màn hình xác nhận
            // Lưu ý: ConfirmationActivity cần nằm ở package activities_customer hoặc shared
            Intent intent = new Intent(SelectTimeActivity.this, com.example.nhakhoaapp.activities_staff.ConfirmationActivity.class);
            
            intent.putExtra("SERVICE_NAME", serviceName);
            intent.putExtra("DOCTOR_NAME", doctorName);
            intent.putExtra("DOCTOR_ID", doctorId);
            intent.putExtra("FULL_DATETIME", fullDateTime); // Gửi chuỗi thời gian đã gộp
            
            startActivity(intent);
        });
    }

    private void setupDateRecyclerView() {
        // Tự động tạo 14 ngày tiếp theo (Ngày thật)
        dateSlotList = generateRealDateSlots();
        
        dateAdapter = new DateSlotAdapter(dateSlotList, (slot, position) -> {
            selectedDateSlot = slot;
            updateContinueButtonState();
        });
        rvDateSlots.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDateSlots.setAdapter(dateAdapter);
    }

    private void setupTimeRecyclerView() {
        timeSlotList = generateDummyTimeSlots(); // Giờ cố định
        timeAdapter = new TimeSlotAdapter(timeSlotList, (slot, position) -> {
            selectedTimeSlot = slot;
            updateContinueButtonState();
        });
        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 3));
        rvTimeSlots.setAdapter(timeAdapter);
    }

    private void updateContinueButtonState() {
        boolean isReady = selectedDateSlot != null && selectedTimeSlot != null;
        btnContinue.setEnabled(isReady);
        int colorRes = isReady ? R.color.purple_700 : android.R.color.darker_gray;
        btnContinue.setBackgroundTintList(ContextCompat.getColorStateList(this, colorRes));
    }

    // Hàm tạo ngày thực tế
    private List<DateSlot> generateRealDateSlots() {
        List<DateSlot> slots = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat dayFormat = new SimpleDateFormat("EEE", new Locale("vi", "VN")); // Thứ (T2, T3...)
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault()); // Ngày (01, 02...)
        SimpleDateFormat fullDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()); // Năm-Tháng-Ngày

        // Tạo 14 ngày tiếp theo
        for (int i = 0; i < 14; i++) {
            String dayOfWeek = dayFormat.format(calendar.getTime());
            int dayOfMonth = Integer.parseInt(dateFormat.format(calendar.getTime()));
            String fullDate = fullDateFormat.format(calendar.getTime());

            // Bạn cần cập nhật Model DateSlot để có thêm trường fullDateString
            // Ví dụ: new DateSlot(dayOfWeek, dayOfMonth, fullDate)
            // Ở đây tôi giả định bạn dùng constructor cũ, bạn cần sửa Model DateSlot
            DateSlot slot = new DateSlot(dayOfWeek, dayOfMonth); 
            slot.setFullDateString(fullDate); // Cần thêm method này vào Model
            
            slots.add(slot);
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        return slots;
    }

    private List<TimeSlot> generateDummyTimeSlots() {
        List<TimeSlot> slots = new ArrayList<>();
        // Giờ làm việc cố định
        String[] times = {"08:00", "08:30", "09:00", "09:30", "10:00", "10:30", "14:00", "14:30", "15:00", "15:30", "16:00"};
        for (String t : times) {
            slots.add(new TimeSlot(t, true));
        }
        return slots;
    }
    
    private boolean handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivity.class));
            return true;
        } else if (id == R.id.nav_booking) {
            startActivity(new Intent(this, BookingActivity.class));
            return true;
        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return false;
    }
}