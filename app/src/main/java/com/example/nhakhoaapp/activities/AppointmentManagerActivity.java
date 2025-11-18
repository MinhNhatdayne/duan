//package com.example.nhakhoaapp.activities;
//
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.RecyclerView;
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.Toast;
//
//import com.example.nhakhoaapp.R;
//import com.example.nhakhoaapp.adapters.AppointmentGroupAdapter;
//import com.example.nhakhoaapp.models_data. AppointmentGroup;
//import com.example.nhakhoaapp.models.LichHen;
//
//import java.util.ArrayList;
//import java.util.Date;
//import java.util.List;
//
//public class AppointmentManagerActivity extends AppCompatActivity {
//
//    private RecyclerView rvAppointmentGroups;
//    private Button btnCreateNewAppointment;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_appointment_manager);
//
//        rvAppointmentGroups = findViewById(R.id.rv_appointment_groups);
//        btnCreateNewAppointment = findViewById(R.id.btn_create_new_appointment);
//
//        // 1. Giả lập dữ liệu và hiển thị
//        loadAppointments();
//
//        // 2. Xử lý sự kiện nút Tạo Cuộc Hẹn Mới
//        btnCreateNewAppointment.setOnClickListener(v -> {
//            // Chuyển sang màn hình tạo cuộc hẹn mới đã làm ở bước trước
//            Toast.makeText(this, "Chuyển đến màn hình Tạo Cuộc Hẹn Mới", Toast.LENGTH_SHORT).show();
//            // TODO: Thêm Intent để chuyển sang NewAppointmentActivity
//        });
//    }
//
//    private void loadAppointments() {
//        // Giả lập dữ liệu cho 2 ngày (Hôm nay và Ngày mai)
//        List<AppointmentGroup> groups = createDummyAppointmentGroups();
//
//        // Thiết lập Adapter
//        AppointmentGroupAdapter adapter = new AppointmentGroupAdapter(this, groups);
//        rvAppointmentGroups.setAdapter(adapter);
//    }
//
//    private List<AppointmentGroup> createDummyAppointmentGroups() {
//        List<AppointmentGroup> groups = new ArrayList<>();
//        Date dummyDate = new Date(); // Sử dụng chung ngày giả lập
//
//        // --- NHÓM NGÀY 1: Hôm nay, Thứ Tư, 9 tháng 10 ---
//        List<LichHen> todayAppointments = new ArrayList<>();
//        // Lưu ý: Đã thêm tên bệnh nhân và giờ khám vào LichHen model
//        todayAppointments.add(new LichHen(1, 101, 1, dummyDate, "Khám định kỳ", "Đang chờ", "Nguyễn Thị Lan", "10:00"));
//        todayAppointments.add(new LichHen(2, 102, 1, dummyDate, "Chỉnh nha", "Đã khám", "Lê Quốc Huy", "11:00"));
//
//        groups.add(new AppointmentGroup("Hôm nay, Thứ Tư, 9 tháng 10", todayAppointments));
//
//        // --- NHÓM NGÀY 2: Ngày mai, Thứ Năm, 10 tháng 10 ---
//        List<LichHen> tomorrowAppointments = new ArrayList<>();
//        tomorrowAppointments.add(new LichHen(3, 103, 2, dummyDate, "Hẹn tái khám", "Chưa khám", "hạm Kim Chi", "16:00"));
//
//        groups.add(new AppointmentGroup("Ngày mai, Thứ Năm, 10 tháng 10", tomorrowAppointments));
//
//        return groups;
//    }
//}