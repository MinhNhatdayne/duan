package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.core.content.ContextCompat;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models_data.DateSlot;
import com.example.nhakhoaapp.models_data.TimeSlot;
import com.example.nhakhoaapp.adapters.DateAdapter;
import com.example.nhakhoaapp.adapters.TimeSlotAdapter;

import java.util.ArrayList;
import java.util.List;

public class SelectTimeActivity extends AppCompatActivity {

    private RecyclerView rvDateSlots;
    private RecyclerView rvTimeSlots;
    private Button btnContinue;
    private TimeSlotAdapter timeAdapter;
    private DateAdapter dateAdapter;
    private List<TimeSlot> timeSlotList;
    private List<DateSlot> dateSlotList;

    private TimeSlot selectedTimeSlot = null;
    private DateSlot selectedDateSlot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_time);
        
        rvDateSlots = findViewById(R.id.rv_date_slots);
        rvTimeSlots = findViewById(R.id.rv_time_slots);
        btnContinue = findViewById(R.id.btn_continue);
        
        // Nút quay lại
        ImageView imgBack = findViewById(R.id.img_back_button);
        imgBack.setOnClickListener(v -> finish()); 

        setupDateRecyclerView();
        setupTimeRecyclerView();
        
        // --- Thiết lập trạng thái mặc định ban đầu ---
        if (dateSlotList.size() > 4) {
             selectedDateSlot = dateSlotList.get(4);
        }
        
        if (timeSlotList.size() > 2) {
             // Mô phỏng việc chọn 9h:30p (vị trí 2)
             timeAdapter.onSlotClick(timeSlotList.get(2), 2);
             selectedTimeSlot = timeSlotList.get(2);
        }
        
        btnContinue.setOnClickListener(v -> {
            // Lấy dữ liệu cuối cùng để tạo LichHen và gửi lên API
            Toast.makeText(this, 
                "Đã đặt lịch ngày: " + selectedDateSlot.getDate() + 
                " lúc: " + selectedTimeSlot.getTime(), 
                Toast.LENGTH_LONG).show();
        });

        updateContinueButtonState();
    }

    private void setupDateRecyclerView() {
        dateSlotList = generateDummyDateSlots(); 
        dateAdapter = new DateAdapter(this, dateSlotList, (slot, position) -> {
            selectedDateSlot = slot;
            // TODO: Logic quan trọng: Load lại TimeSlots khi người dùng chọn ngày khác
            // updateTimeSlotsForNewDate(slot);
            updateContinueButtonState();
        });
        
        rvDateSlots.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDateSlots.setAdapter(dateAdapter);
        rvDateSlots.scrollToPosition(4); // Cuộn đến ngày 14
    }

    private void setupTimeRecyclerView() {
        timeSlotList = generateDummyTimeSlots();
        timeAdapter = new TimeSlotAdapter(this, timeSlotList, (slot, position) -> {
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

    // Dữ liệu giả lập cho ngày
    private List<DateSlot> generateDummyDateSlots() {
        List<DateSlot> slots = new ArrayList<>();
        slots.add(new DateSlot("THU", "10")); 
        slots.add(new DateSlot("FRI", "11"));
        slots.add(new DateSlot("SAT", "12"));
        slots.add(new DateSlot("SUN", "13"));
        slots.add(new DateSlot("MON", "14")); 
        slots.add(new DateSlot("TUE", "15"));
        slots.add(new DateSlot("WED", "16"));
        slots.add(new DateSlot("THU", "17"));
        slots.add(new DateSlot("FRI", "18")); 
        return slots;
    }

    // Dữ liệu giả lập cho giờ
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
        slots.add(new TimeSlot("15h:30p", false)); // Đã kín
        slots.add(new TimeSlot("16h", false));   // Đã kín
        slots.add(new TimeSlot("16h:30p", true)); 
        return slots;
    }
}