package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

public class SelectTimeActivity extends AppCompatActivity {

    private RecyclerView rvDateSlots, rvTimeSlots;
    private Button btnContinue;

    private DateSlotAdapter dateAdapter;
    private TimeSlotAdapter timeAdapter;

    private List<DateSlot> dateSlotList;
    private List<TimeSlot> timeSlotList;

    private DateSlot selectedDateSlot = null;
    private TimeSlot selectedTimeSlot = null;

    private TextView tvServiceName;
    private TextView tvDoctorDetails;

    private String serviceName;
    private String doctorName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);

        // read extras from intent
        serviceName = getIntent().getStringExtra("SERVICE_NAME");
        doctorName = getIntent().getStringExtra("DOCTOR_NAME");

        rvDateSlots = findViewById(R.id.rv_date_slots);
        rvTimeSlots = findViewById(R.id.rv_time_slots);
        btnContinue = findViewById(R.id.btn_continue);
        ImageView imgBack = findViewById(R.id.img_back_button);
        imgBack.setOnClickListener(v -> finish());

        tvServiceName = findViewById(R.id.tv_service_name);
        tvDoctorDetails = findViewById(R.id.tv_doctor_details);

        if (serviceName != null) tvServiceName.setText(serviceName);
        if (doctorName != null) tvDoctorDetails.setText(doctorName);

        setupDateRecyclerView();
        setupTimeRecyclerView();
        setupDefaultSelection();

        btnContinue.setOnClickListener(v -> {
            if (selectedDateSlot == null || selectedTimeSlot == null) {
                Toast.makeText(this, "Vui lòng chọn ngày và giờ.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Start ConfirmationActivity with appointment details
            Intent intent = new Intent(SelectTimeActivity.this, com.example.nhakhoaapp.activities_staff.ConfirmationActivity.class);
            intent.putExtra("SERVICE_NAME", serviceName != null ? serviceName : "Chỉnh nha");
            intent.putExtra("DOCTOR_NAME", doctorName != null ? doctorName : "Bác sĩ");
            intent.putExtra("SELECTED_DATE", selectedDateSlot.getDate() + ""); // depends on DateSlot implementation
            intent.putExtra("SELECTED_TIME", selectedTimeSlot.getTime());
            startActivity(intent);
        });
    }

    private void setupDateRecyclerView() {
        dateSlotList = generateDummyDateSlots();
        dateAdapter = new DateSlotAdapter(dateSlotList, (slot, position) -> {
            selectedDateSlot = slot;
            updateContinueButtonState();
        });

        rvDateSlots.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDateSlots.setAdapter(dateAdapter);
    }

    private void setupTimeRecyclerView() {
        timeSlotList = generateDummyTimeSlots();
        timeAdapter = new TimeSlotAdapter(timeSlotList, (slot, position) -> {
            selectedTimeSlot = slot;
            updateContinueButtonState();
        });

        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 3));
        rvTimeSlots.setAdapter(timeAdapter);
    }

    private void setupDefaultSelection() {
        // Chọn ngày mặc định (vị trí 4 - ngày 14)
        if (dateSlotList != null && dateSlotList.size() > 4) {
            dateAdapter.selectPosition(4);
            selectedDateSlot = dateSlotList.get(4);
            rvDateSlots.scrollToPosition(4);
        }

        // Chọn giờ mặc định (vị trí 2 - 9h30)
        if (timeSlotList != null && timeSlotList.size() > 2) {
            TimeSlot defaultTime = timeSlotList.get(2);
            if (defaultTime.isAvailable()) {
                timeAdapter.selectPosition(2);
                selectedTimeSlot = defaultTime;
                rvTimeSlots.scrollToPosition(2);
            }
        }

        updateContinueButtonState();
    }

    private void updateContinueButtonState() {
        boolean isReady = selectedDateSlot != null && selectedTimeSlot != null;
        btnContinue.setEnabled(isReady);
        int colorRes = isReady ? R.color.purple_700 : android.R.color.darker_gray;
        btnContinue.setBackgroundTintList(ContextCompat.getColorStateList(this, colorRes));
    }

    private List<DateSlot> generateDummyDateSlots() {
        List<DateSlot> slots = new ArrayList<>();
        slots.add(new DateSlot("THU", 10));
        slots.add(new DateSlot("FRI", 11));
        slots.add(new DateSlot("SAT", 12));
        slots.add(new DateSlot("SUN", 13));
        slots.add(new DateSlot("MON", 14));
        slots.add(new DateSlot("TUE", 15));
        slots.add(new DateSlot("WED", 16));
        slots.add(new DateSlot("THU", 17));
        slots.add(new DateSlot("FRI", 18));
        return slots;
    }


    private List<TimeSlot> generateDummyTimeSlots() {
        List<TimeSlot> slots = new ArrayList<>();
        slots.add(new TimeSlot("8h:30p", true));
        slots.add(new TimeSlot("9h", true));
        slots.add(new TimeSlot("9h:30p", true));
        slots.add(new TimeSlot("10h", true));
        slots.add(new TimeSlot("10h:30p", true));
        slots.add(new TimeSlot("11h", true));
        slots.add(new TimeSlot("11h:30p", true));
        slots.add(new TimeSlot("14h", true));
        slots.add(new TimeSlot("14h:30p", true));
        slots.add(new TimeSlot("15h", true));
        slots.add(new TimeSlot("15h:30p", false));
        slots.add(new TimeSlot("16h", false));
        slots.add(new TimeSlot("16h:30p", true));
        return slots;
    }
}
