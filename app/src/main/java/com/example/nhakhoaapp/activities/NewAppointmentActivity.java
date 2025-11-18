package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhakhoaapp.R;

public class NewAppointmentActivity extends AppCompatActivity {

    private EditText etPatientInfo;
    private Spinner spinnerService;
    private Button btnSelectDoctor, btnSelectTime, btnConfirmAppointment;
    private TextView tvSelectedPatient, tvSelectedDoctorTime;
    
    // Giả lập dữ liệu đã chọn
    private String selectedDoctorId = null; 
    private String selectedTimeSlot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        // Thiết lập Toolbar
        setSupportActionBar(findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
        
        // Giả lập đã có bệnh nhân được chọn
        tvSelectedPatient.setText("Bệnh nhân đã chọn: Nguyễn Thị Lan (090xxxxxxx)");
        tvSelectedPatient.setVisibility(View.VISIBLE);
    }
    
    private void setupServiceSpinner() {
        String[] services = {"Khám Tổng Quát", "Chỉnh Nha", "Tẩy Trắng Răng", "Điều Trị Tủy"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, 
            android.R.layout.simple_spinner_dropdown_item, services);
        spinnerService.setAdapter(adapter);
    }

    private void setupClickListeners() {
        
        // 1. Tìm kiếm Bệnh nhân
        etPatientInfo.setOnClickListener(v -> {
            Toast.makeText(this, "Mở màn hình tìm kiếm/chọn bệnh nhân", Toast.LENGTH_SHORT).show();
            // TODO: Mở Intent đến màn hình tìm kiếm/chọn BN
        });

        // 2. Chọn Bác sĩ (Tham khảo image_dca43b.png)
        btnSelectDoctor.setOnClickListener(v -> {
            Toast.makeText(this, "Chuyển đến màn hình Chọn Bác sĩ", Toast.LENGTH_SHORT).show();
            // TODO: Mở Intent đến màn hình Chọn Bác sĩ
            
            // Giả lập sau khi chọn xong
            selectedDoctorId = "Dr001";
            updateDoctorTimeView("BS. Đoàn Hồng Lê", null);
        });

        // 3. Chọn Ngày & Giờ (Tham khảo image_e689a3.png)
        btnSelectTime.setOnClickListener(v -> {
            if (selectedDoctorId == null) {
                 Toast.makeText(this, "Vui lòng chọn Bác sĩ trước!", Toast.LENGTH_SHORT).show();
                 return;
            }
            Toast.makeText(this, "Chuyển đến màn hình Chọn Thời gian cho Bác sĩ " + selectedDoctorId, Toast.LENGTH_SHORT).show();
            // TODO: Mở Intent đến màn hình Chọn Thời gian
            
            // Giả lập sau khi chọn xong
            selectedTimeSlot = "14:00, 14/10/2024";
            updateDoctorTimeView("BS. Đoàn Hồng Lê", selectedTimeSlot);
        });
        
        // 4. Xác nhận Tạo Cuộc Hẹn
        btnConfirmAppointment.setOnClickListener(v -> {
            if (tvSelectedPatient.getVisibility() != View.VISIBLE || selectedDoctorId == null || selectedTimeSlot == null) {
                Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin (Bệnh nhân, Bác sĩ, Thời gian)!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Đã tạo cuộc hẹn thành công cho " + tvSelectedPatient.getText().toString().substring(23), Toast.LENGTH_LONG).show();
                // TODO: Gọi API lưu dữ liệu
                finish(); // Đóng màn hình sau khi tạo thành công
            }
        });
    }

    private void updateDoctorTimeView(String doctorName, String timeSlot) {
        String currentText = tvSelectedDoctorTime.getText().toString();
        
        // Cập nhật TextView sau khi có đủ thông tin
        if (selectedDoctorId != null && selectedTimeSlot != null) {
             tvSelectedDoctorTime.setText(String.format("Đã chọn: %s - %s", doctorName, timeSlot));
             tvSelectedDoctorTime.setVisibility(View.VISIBLE);
        } else if (selectedDoctorId != null) {
             tvSelectedDoctorTime.setText(String.format("Đã chọn: %s", doctorName));
             tvSelectedDoctorTime.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}