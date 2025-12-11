package com.example.nhakhoaapp.activities_customer;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DateSlotAdapter;
import com.example.nhakhoaapp.adapters.TimeSlotAdapter;
import com.example.nhakhoaapp.models_adapter.DateSlot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SelectTimeActivity extends AppCompatActivity {

    private RecyclerView rvDateSlots, rvTimeSlots;
    private Button btnContinue;
    private  ImageView btn_back_screen;

    private DateSlotAdapter dateAdapter;
    private TimeSlotAdapter timeAdapter;

    private List<DateSlot> dateSlotList = new ArrayList<>();
    private List<String> timeSlotList = new ArrayList<>();

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
        patientName = getIntent().getStringExtra("PATIENT_NAME"); // ⭐ THÊM MỚI

        rvDateSlots = findViewById(R.id.rvDateSlots);
        rvTimeSlots = findViewById(R.id.rvTimeSlots);
        btnContinue = findViewById(R.id.btn_continue);
        btn_back_screen=findViewById(R.id.btn_back_screen);


        setupDateRecyclerView();
        setupTimeRecyclerView();
        updateContinueButtonState();

        btn_back_screen.setOnClickListener(v -> {
            finish();
        });
        btnContinue.setOnClickListener(v -> {
            if (selectedDateSlot == null || selectedTimeSlot == null) {
                return;
            }

            String iso = generateISODateTime(selectedDateSlot.getFullDateString(), selectedTimeSlot);

            Intent intent = new Intent(SelectTimeActivity.this, ConfirmationActivity.class);
            intent.putExtra("SERVICE_NAME", serviceName);
            intent.putExtra("DOCTOR_NAME", doctorName);
            intent.putExtra("DOCTOR_ID", doctorId);

            // Gửi cả ID + NAME
            if (patientId != null) intent.putExtra("PATIENT_ID", patientId);
            if (patientName != null) intent.putExtra("PATIENT_NAME", patientName); // ⭐ THÊM MỚI

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
            updateContinueButtonState();
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
        timeSlotList.clear();
        String[] times = {"08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "14:00", "14:30", "15:00", "15:30", "16:00"};
        for (String t : times) timeSlotList.add(t);

        timeAdapter = new TimeSlotAdapter(timeSlotList, time -> {
            selectedTimeSlot = time;
            updateContinueButtonState();
        });

        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 3));
        rvTimeSlots.setAdapter(timeAdapter);
    }

    private void updateContinueButtonState() {
        boolean ready = selectedDateSlot != null && selectedTimeSlot != null;
        btnContinue.setEnabled(ready);
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
        updateContinueButtonState();
        updateMonthHeader();
    }

    private String generateISODateTime(String date, String time) {
        try {
            String input = date + " " + time;
            SimpleDateFormat localFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date parsed = localFmt.parse(input);

            SimpleDateFormat isoFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
            return isoFmt.format(parsed);

        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
