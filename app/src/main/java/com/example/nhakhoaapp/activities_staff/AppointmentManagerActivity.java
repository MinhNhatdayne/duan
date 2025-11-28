package com.example.nhakhoaapp.activities_staff;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.SingleAppointmentAdapter;
import com.example.nhakhoaapp.models.LichHen;
import com.example.nhakhoaapp.models_adapter.AppointmentHeader;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AppointmentManagerActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private Button btnNewAppointment;
    private TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_manager);

        rvAppointments = findViewById(R.id.recycler_appointments);
        btnNewAppointment = findViewById(R.id.btn_new_appointment);
        tvTitle = findViewById(R.id.tv_title);

        tvTitle.setText("Cuộc hẹn");

        loadAppointments();

        btnNewAppointment.setOnClickListener(v -> {
            // Giả định chuyển sang màn hình SelectTimeActivity
            // Intent intent = new Intent(this, SelectTimeActivity.class);
            // startActivity(intent);
            Toast.makeText(this, "Chuyển đến màn hình Tạo Cuộc Hẹn Mới", Toast.LENGTH_SHORT).show();
        });
    }

    private void loadAppointments() {
        // Tạo danh sách kết hợp Model Header và Detail
        List<Object> combinedList = createCombinedAppointmentList();

        SingleAppointmentAdapter adapter = new SingleAppointmentAdapter(this, combinedList);

        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        rvAppointments.setAdapter(adapter);
    }

    /**
     * Hàm giả lập dữ liệu: Tạo danh sách List<Object> chứa cả Header và LichHen
     */
    private List<Object> createCombinedAppointmentList() {
        // Phải là List<Object>
        List<Object> list = new ArrayList<>();
        Date dummyDate = new Date();

        // --- NGÀY 1 ---
        // Thêm Header (Model UI/Adapter)
        list.add(new AppointmentHeader("Hôm nay, Thứ Tư, 9 tháng 10"));

        // Thêm Detail (Model Nghiệp vụ)
        list.add(new LichHen(1, 101, 1, dummyDate, "Khám định kỳ", "Đang chờ", "Nguyễn Thị Lan", "10:00"));
        list.add(new LichHen(2, 102, 1, dummyDate, "Chỉnh nha", "Đã khám", "Lê Quốc Huy", "11:00"));

        // --- NGÀY 2 ---
        // Thêm Header (Model UI/Adapter)
        list.add(new AppointmentHeader("Ngày mai, Thứ Năm, 10 tháng 10"));

        // Thêm Detail (Model Nghiệp vụ)
        list.add(new LichHen(4, 104, 2, dummyDate, "Hẹn tái khám", "Chưa khám", "Phạm Kim Chi", "16:00"));

        return list;
    }
}