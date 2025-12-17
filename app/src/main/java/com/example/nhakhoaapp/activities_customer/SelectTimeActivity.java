package com.example.nhakhoaapp.activities_customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DateSlotAdapter;
import com.example.nhakhoaapp.adapters.TimeSlotAdapter; 
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models_adapter.DateSlot;
import com.example.nhakhoaapp.models_adapter.TimeSlot; 

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectTimeActivity extends AppCompatActivity {

    private RecyclerView rvDateSlots, rvTimeSlots;
    private Button btnContinue;
    private ImageView btn_back_screen;

    private DateSlotAdapter dateAdapter;
    private TimeSlotAdapter timeAdapter;

    private List<DateSlot> dateSlotList = new ArrayList<>();
    private List<TimeSlot> timeSlotList = new ArrayList<>();

    private DateSlot selectedDateSlot = null;
    private String selectedTimeSlot = null;

    private Calendar currentCalendar = Calendar.getInstance();

    private String serviceName, doctorName, doctorId, patientId, patientName;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        // Nhận extras
        serviceName = getIntent().getStringExtra("SERVICE_NAME");
        doctorName = getIntent().getStringExtra("DOCTOR_NAME");
        doctorId = getIntent().getStringExtra("DOCTOR_ID");
        patientId = getIntent().getStringExtra("PATIENT_ID");
        patientName = getIntent().getStringExtra("PATIENT_NAME");

        rvDateSlots = findViewById(R.id.rvDateSlots);
        rvTimeSlots = findViewById(R.id.rvTimeSlots);
        btnContinue = findViewById(R.id.btn_continue);
        btn_back_screen = findViewById(R.id.btn_back_screen);

        setupDateRecyclerView();
        setupTimeRecyclerView(); 
        updateContinueButtonState();

        btn_back_screen.setOnClickListener(v -> finish());

        btnContinue.setOnClickListener(v -> {
            if (selectedDateSlot == null || selectedTimeSlot == null) {
                return;
            }

            // Gọi hàm generate ISO đã sửa múi giờ
            String iso = generateISODateTime(selectedDateSlot.getFullDateString(), selectedTimeSlot);

            Intent intent = new Intent(SelectTimeActivity.this, ConfirmationActivity.class);
            intent.putExtra("SERVICE_NAME", serviceName);
            intent.putExtra("DOCTOR_NAME", doctorName);
            intent.putExtra("DOCTOR_ID", doctorId);

            if (patientId != null) intent.putExtra("PATIENT_ID", patientId);
            if (patientName != null) intent.putExtra("PATIENT_NAME", patientName);

            intent.putExtra("SELECTED_DATE", selectedDateSlot.getFullDateString());
            intent.putExtra("SELECTED_TIME", selectedTimeSlot);
            intent.putExtra("FULL_ISO_DATE", iso);

            startActivity(intent);
        });
    }

    private void setupDateRecyclerView() {
        dateSlotList = generateDaysOfMonth();

        dateAdapter = new DateSlotAdapter(dateSlotList, (slot, pos) -> {
            selectedDateSlot = slot;
            selectedTimeSlot = null;
            timeAdapter.clearSelection();
            updateContinueButtonState();
            loadTimeSlotsForDate(slot.getFullDateString());
        });

        rvDateSlots.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDateSlots.setAdapter(dateAdapter);

        ImageView btnNext = findViewById(R.id.btn_next_month);
        ImageView btnPrev = findViewById(R.id.btn_prev_month);

        updateMonthHeader();

        btnNext.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            refreshDates();
        });

        btnPrev.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            refreshDates();
        });
    }

    private void setupTimeRecyclerView() {
        timeSlotList = generateDefaultTimeSlots();

        timeAdapter = new TimeSlotAdapter(timeSlotList, slot -> {
            if (slot.isAvailable()) {
                selectedTimeSlot = slot.getTime();
                updateContinueButtonState();
            } else {
                Toast.makeText(this, "Khung giờ này đã kín!", Toast.LENGTH_SHORT).show();
            }
        });

        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 3));
        rvTimeSlots.setAdapter(timeAdapter);
    }

    private List<TimeSlot> generateDefaultTimeSlots() {
        List<TimeSlot> list = new ArrayList<>();
        String[] times = {"08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "14:00", "14:30", "15:00", "15:30", "16:00"};

        for (String t : times) {
            list.add(new TimeSlot(t, true));
        }
        return list;
    }

    private void loadTimeSlotsForDate(String dateString) {
        timeSlotList.clear();
        timeSlotList.addAll(generateDefaultTimeSlots());
        timeAdapter.notifyDataSetChanged(); 

        ApiClient.getApiService().getBusySlots(doctorId, dateString).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> busySlots = response.body(); 

                    for (TimeSlot slot : timeSlotList) {
                        if (busySlots.contains(slot.getTime())) {
                            slot.setAvailable(false); 
                        }
                    }
                    timeAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {
                Toast.makeText(SelectTimeActivity.this, "Lỗi kiểm tra lịch: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateContinueButtonState() {
        boolean ready = selectedDateSlot != null && selectedTimeSlot != null;
        btnContinue.setEnabled(ready);
        if (ready) {
            btnContinue.setBackgroundTintList(getResources().getColorStateList(R.color.primary_blue));
        } else {
            btnContinue.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
        }
    }

    private void updateMonthHeader() {
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM yyyy", new Locale("vi", "VN"));
        TextView tv = findViewById(R.id.tv_month_year);
        tv.setText(fmt.format(currentCalendar.getTime()));
    }

    private List<DateSlot> generateDaysOfMonth() {
        List<DateSlot> list = new ArrayList<>();
        Calendar temp = (Calendar) currentCalendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int max = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dowFormat = new SimpleDateFormat("EEE", new Locale("vi", "VN"));
        SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        for (int i = 1; i <= max; i++) {
            temp.set(Calendar.DAY_OF_MONTH, i);
            DateSlot slot = new DateSlot(dowFormat.format(temp.getTime()), i);
            slot.setFullDateString(fullFormat.format(temp.getTime()));
            list.add(slot);
        }
        return list;
    }

    private void refreshDates() {
        dateSlotList.clear();
        dateSlotList.addAll(generateDaysOfMonth());
        dateAdapter.notifyDataSetChanged();
        selectedDateSlot = null;
        selectedTimeSlot = null;
        timeAdapter.clearSelection();
        updateContinueButtonState();
        updateMonthHeader();
    }

    // ===============================================
    // ⭐ ĐÃ SỬA: SỬ DỤNG MÚI GIỜ HỆ THỐNG (LOCAL)
    // ===============================================
    private String generateISODateTime(String date, String time) {
        try {
            String input = date + " " + time;
            // 1. Parse theo giờ địa phương
            SimpleDateFormat localFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date parsed = localFmt.parse(input);

            // 2. Format ISO nhưng giữ nguyên múi giờ hiện tại (không chuyển sang UTC)
            SimpleDateFormat isoFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            
            // QUAN TRỌNG: Sử dụng TimeZone mặc định của thiết bị (Asia/Ho_Chi_Minh)
            isoFmt.setTimeZone(TimeZone.getDefault()); 

            return isoFmt.format(parsed);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}