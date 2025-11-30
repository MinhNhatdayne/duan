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

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;

public class NewAppointmentActivity extends AppCompatActivity {

    private EditText etPatientInfo, etNote;
    private Spinner spinnerService;
    private Button btnSelectDoctor, btnSelectTime, btnConfirmAppointment;
    private TextView tvSelectedPatient, tvSelectedDoctorTime;

    // Giả lập dữ liệu đã chọn
    private String selectedDoctorId = null;
    private String selectedTimeSlot = null;
    private String selectedDoctorName = "";

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

        // 1. Ánh xạ View (Khớp với các ID trong XML mới)
        etPatientInfo = findViewById(R.id.et_patient_info);
        spinnerService = findViewById(R.id.spinner_service);
        etNote = findViewById(R.id.et_note); // Ánh xạ thêm ô Ghi chú

        btnSelectDoctor = findViewById(R.id.btn_select_doctor);
        btnSelectTime = findViewById(R.id.btn_select_time);
        btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);

        tvSelectedPatient = findViewById(R.id.tv_selected_patient);
        tvSelectedDoctorTime = findViewById(R.id.tv_selected_doctor_time);

        // 2. Khởi tạo Spinner Dịch vụ
        setupServiceSpinner();

        // 3. Xử lý sự kiện
        setupClickListeners();

        // --- GIẢ LẬP DEMO ---
        // Giả sử vừa vào màn hình đã có thông tin bệnh nhân (demo logic)
        // Trong thực tế, bạn sẽ setVisible(GONE) mặc định, khi tìm xong mới hiện.
        tvSelectedPatient.setText("Bệnh nhân: Nguyễn Thị Lan (0909.123.456)");
        tvSelectedPatient.setVisibility(View.VISIBLE);
    }

    private void setupServiceSpinner() {
        String[] services = {"Khám Tổng Quát", "Chỉnh Nha", "Tẩy Trắng Răng", "Điều Trị Tủy", "Nhổ Răng"};

        // THAY ĐỔI Ở ĐÂY:
        // Thay 'android.R.layout.simple_spinner_item'
        // Thành 'R.layout.item_spinner_selected' để custom chữ màu đen
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner_selected,
                services);

        // Phần danh sách xổ xuống vẫn dùng mặc định cũng được, hoặc tạo file riêng nếu muốn
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerService.setAdapter(adapter);
    }

    private void setupClickListeners() {

        // 1. Tìm kiếm Bệnh nhân
        etPatientInfo.setOnClickListener(v -> {
            Toast.makeText(this, "Mở chức năng Tìm kiếm Bệnh nhân", Toast.LENGTH_SHORT).show();
            // TODO: Intent -> PatientSearchActivity
        });

        // 2. Chọn Bác sĩ
        btnSelectDoctor.setOnClickListener(v -> {
            // TODO: Intent -> SelectDoctorActivity
            // Giả lập callback trả về dữ liệu sau khi chọn
            Toast.makeText(this, "Đã chọn Bác sĩ", Toast.LENGTH_SHORT).show();

            selectedDoctorId = "Dr001";
            selectedDoctorName = "Đoàn Hồng Lê";
            updateDoctorTimeView();
        });

        // 3. Chọn Ngày & Giờ
        btnSelectTime.setOnClickListener(v -> {
            if (selectedDoctorId == null) {
                 Toast.makeText(this, "Vui lòng chọn Bác sĩ phụ trách trước!", Toast.LENGTH_SHORT).show();
                 return;
            }
            // TODO: Intent -> SelectTimeActivity
            // Giả lập callback trả về dữ liệu
            Toast.makeText(this, "Đã chọn Thời gian", Toast.LENGTH_SHORT).show();

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

            // Lấy dữ liệu từ form
            String service = spinnerService.getSelectedItem().toString();
            String note = etNote.getText().toString();

            // Hiển thị thông báo (Hoặc gọi API)
            String message = String.format("Thành công!\nDV: %s\nBS: %s\nGiờ: %s",
                                            service, selectedDoctorName, selectedTimeSlot);

            Toast.makeText(this, message, Toast.LENGTH_LONG).show();
            finish(); // Đóng màn hình
        });
    }

    /**
     * Cập nhật giao diện hiển thị kết quả chọn Bác sĩ và Thời gian
     * Hiển thị dạng vé (Ticket) với icon sinh động
     */
    private void updateDoctorTimeView() {
        StringBuilder displayText = new StringBuilder();

        if (selectedDoctorName != null && !selectedDoctorName.isEmpty()) {
            displayText.append("👨‍⚕️ Bác sĩ: ").append(selectedDoctorName);
        }

        if (selectedTimeSlot != null) {
            // Xuống dòng nếu đã có tên bác sĩ
            if (displayText.length() > 0) displayText.append("\n");
            displayText.append("📅 Thời gian: ").append(selectedTimeSlot);
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
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}