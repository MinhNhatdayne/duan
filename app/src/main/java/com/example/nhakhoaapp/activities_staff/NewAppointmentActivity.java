package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
// Import các Activity khác nếu cần, ví dụ SelectDoctorActivity

public class NewAppointmentActivity extends AppCompatActivity {

    private EditText etPatientInfo;
    private Spinner spinnerService;
    private Button btnSelectDoctor, btnSelectTime, btnConfirmAppointment;
    private TextView tvSelectedPatient, tvSelectedDoctorTime;
    
    // Giả lập dữ liệu đã chọn
    private String selectedDoctorId = null; 
    private String selectedTimeSlot = null;
    private String selectedDoctorName = ""; // Thêm biến lưu tên bác sĩ để hiển thị lại

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        // Thiết lập Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tạo Cuộc Hẹn Mới");
        }

        // Ánh xạ View
        etPatientInfo = findViewById(R.id.et_patient_info);
        spinnerService = findViewById(R.id.spinner_service);
        btnSelectDoctor = findViewById(R.id.btn_select_doctor);
        btnSelectTime = findViewById(R.id.btn_select_time);
        btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);
        tvSelectedPatient = findViewById(R.id.tv_selected_patient);
        tvSelectedDoctorTime = findViewById(R.id.tv_selected_doctor_time);

        // Khởi tạo Spinner Dịch vụ
        setupServiceSpinner();

        // Xử lý sự kiện
        setupClickListeners();
        
        // Giả lập: Người dùng vừa nhập xong tìm kiếm -> Hiển thị bệnh nhân (Demo)
        tvSelectedPatient.setText("Bệnh nhân: Nguyễn Thị Lan (090xxxxxxx)");
        tvSelectedPatient.setVisibility(View.VISIBLE);
    }
    
    private void setupServiceSpinner() {
        String[] services = {"Khám Tổng Quát", "Chỉnh Nha", "Tẩy Trắng Răng", "Điều Trị Tủy", "Nhổ Răng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_dropdown_item, services);
        spinnerService.setAdapter(adapter);
    }

    private void setupClickListeners() {
        
        // 1. Tìm kiếm Bệnh nhân
        etPatientInfo.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng Tìm kiếm Bệnh nhân", Toast.LENGTH_SHORT).show();
            // TODO: Intent -> PatientSearchActivity
        });

        // 2. Chọn Bác sĩ
        btnSelectDoctor.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình Chọn Bác sĩ", Toast.LENGTH_SHORT).show();
            // TODO: Intent -> SelectDoctorActivity
            
            // Giả lập callback trả về dữ liệu
            selectedDoctorId = "Dr001";
            selectedDoctorName = "BS. Đoàn Hồng Lê";
            updateDoctorTimeView();
        });

        // 3. Chọn Ngày & Giờ
        btnSelectTime.setOnClickListener(v -> {
            if (selectedDoctorId == null) {
                 Toast.makeText(this, "Vui lòng chọn Bác sĩ trước!", Toast.LENGTH_SHORT).show();
                 return;
            }
            Toast.makeText(this, "Chọn giờ cho bác sĩ: " + selectedDoctorName, Toast.LENGTH_SHORT).show();
            // TODO: Intent -> SelectTimeActivity
            
            // Giả lập callback trả về dữ liệu
            selectedTimeSlot = "14:00 - 14/10/2024";
            updateDoctorTimeView();
        });
        
        // 4. Xác nhận Tạo Cuộc Hẹn
        btnConfirmAppointment.setOnClickListener(v -> {
            // Validate dữ liệu
            if (selectedDoctorId == null || selectedTimeSlot == null) {
                Toast.makeText(this, "Vui lòng chọn đầy đủ Bác sĩ và Thời gian!", Toast.LENGTH_LONG).show();
                return;
            }
            
            // Success Logic
            String service = spinnerService.getSelectedItem().toString();
            String message = String.format("Đã tạo lịch hẹn: %s\nBác sĩ: %s\nThời gian: %s", 
                                            service, selectedDoctorName, selectedTimeSlot);
                                            
            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            
            // TODO: Gọi API lưu vào Database
            finish(); // Đóng màn hình
        });
    }

    private void updateDoctorTimeView() {
        StringBuilder displayText = new StringBuilder();
        
        if (selectedDoctorName != null && !selectedDoctorName.isEmpty()) {
            displayText.append("Bác sĩ: ").append(selectedDoctorName);
        }
        
        if (selectedTimeSlot != null) {
            displayText.append("\nThời gian: ").append(selectedTimeSlot);
        }
        
        if (displayText.length() > 0) {
            tvSelectedDoctorTime.setText(displayText.toString());
            tvSelectedDoctorTime.setVisibility(View.VISIBLE);
        } else {
            tvSelectedDoctorTime.setVisibility(View.GONE);
        }
    }

    // Xử lý nút Back trên Toolbar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish(); // Đóng Activity thay vì gọi onBackPressed
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}