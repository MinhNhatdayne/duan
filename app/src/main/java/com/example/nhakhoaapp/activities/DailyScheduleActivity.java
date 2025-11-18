package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.widget.TextView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DailyAppointmentAdapter;
import com.example.nhakhoaapp.models.LichHen;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class DailyScheduleActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private TextView tvCurrentDate, tvAppointmentCount;

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
        
        // 1. Giả lập dữ liệu và hiển thị
        loadDailySchedule();
    }

    private void loadDailySchedule() {
        // Tạo danh sách lịch hẹn giả lập
        List<LichHen> appointments = createDummyAppointments();

        // 2. Cập nhật thông tin tổng quan
        updateSummary(appointments);

        // 3. Thiết lập Adapter cho RecyclerView
        DailyAppointmentAdapter adapter = new DailyAppointmentAdapter(this, appointments);
        rvAppointments.setAdapter(adapter);
        
        // LinearLayoutManager đã được khai báo trong XML
    }

    private List<LichHen> createDummyAppointments() {
        List<LichHen> list = new ArrayList<>();
        Date now = Calendar.getInstance().getTime();

        // Giả lập 5 lịch hẹn
        list.add(new LichHen(101, 1, 1, now, "Khám định kỳ", "Đã khám", "Nguyễn Mạnh Toàn", "08:30"));
        list.add(new LichHen(102, 2, 1, now, "Điều trị tủy răng", "Đang chờ", "Trần Thị Lan", "09:30"));
        list.add(new LichHen(103, 3, 1, now, "Nhổ răng khôn", "Đang chờ", "Lê Văn Hùng", "10:30"));
        list.add(new LichHen(104, 4, 1, now, "Tái khám chỉnh nha", "Chưa khám", "Phạm Thị Thúy", "14:00"));
        list.add(new LichHen(105, 5, 1, now, "Khám tổng quát", "Dời lịch", "Vũ Minh Đức", "15:30"));

        return list;
    }
    
    private void updateSummary(List<LichHen> appointments) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
        String today = dateFormat.format(Calendar.getInstance().getTime());
        
        tvCurrentDate.setText(String.format("Hôm nay: %s", today));

        int total = appointments.size();
        long pending = appointments.stream().filter(l -> l.getTrang_thai().equals("Đang chờ")).count();
        
        tvAppointmentCount.setText(String.format("Tổng số lịch hẹn: %d (Đang chờ: %d)", total, pending));
    }
}