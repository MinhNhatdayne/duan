package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.DanhMucDichVu;
import com.example.nhakhoaapp.models.LichHen;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentActivity extends AppCompatActivity {

    // Request Codes cho ActivityResult
    private static final int REQUEST_CODE_SELECT_PATIENT = 1;
    private static final int REQUEST_CODE_SELECT_DOCTOR = 2;
    private static final int REQUEST_CODE_SELECT_TIME = 3;

    private EditText etPatientInfo, etNote;
    private Spinner spinnerService;
    private Button btnSelectDoctor, btnSelectTime, btnConfirmAppointment;
    private TextView tvSelectedPatient, tvSelectedDoctorTime;

    // API
    private ApiService apiService;
    private List<DanhMucDichVu> serviceList;

    // Dữ liệu quan trọng
    private String selectedPatientId = null;
    private String selectedPatientName = null;
    private String selectedDoctorId = null;
    private String selectedDoctorName = null;
    private String selectedTimeDisplay = null;
    private String selectedIsoDateTime = null; // Chuỗi ISO 8601 Date/Time cho API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        apiService = ApiClient.getApiService();

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
        etNote = findViewById(R.id.et_note);

        btnSelectDoctor = findViewById(R.id.btn_select_doctor);
        btnSelectTime = findViewById(R.id.btn_select_time);
        btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);

        tvSelectedPatient = findViewById(R.id.tv_selected_patient);
        tvSelectedDoctorTime = findViewById(R.id.tv_selected_doctor_time);
        tvSelectedPatient.setVisibility(View.GONE);
        tvSelectedDoctorTime.setVisibility(View.GONE);

        // Khởi tạo Spinner Dịch vụ (gọi API)
        fetchServiceList();

        // Xử lý sự kiện
        setupClickListeners();
    }

    /**
     * Gọi API để lấy danh sách dịch vụ và populate Spinner
     */
    private void fetchServiceList() {
        apiService.getAllDanhMucDichVu().enqueue(new Callback<List<DanhMucDichVu>>() {
            @Override
            public void onResponse(@NonNull Call<List<DanhMucDichVu>> call, @NonNull Response<List<DanhMucDichVu>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    serviceList = response.body();
                    // Lấy ra danh sách tên dịch vụ để hiển thị
                    List<String> serviceNames = serviceList.stream()
                            .map(DanhMucDichVu::getTen_dich_vu)
                            .collect(Collectors.toList());

                    // Thêm một item mặc định (ví dụ: "Chọn dịch vụ")
                    // serviceNames.add(0, "Chọn dịch vụ"); 
                    
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(NewAppointmentActivity.this,
                            R.layout.item_spinner_selected, // Layout tùy chỉnh chữ đen
                            serviceNames);

                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerService.setAdapter(adapter);

                } else {
                    Toast.makeText(NewAppointmentActivity.this, "Không thể tải danh mục dịch vụ: " + response.code(), Toast.LENGTH_SHORT).show();
                    // Tạo danh sách giả lập nếu thất bại
                    setupDummyServiceSpinner(); 
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<DanhMucDichVu>> call, @NonNull Throwable t) {
                Toast.makeText(NewAppointmentActivity.this, "Lỗi kết nối API dịch vụ: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                // Tạo danh sách giả lập nếu lỗi
                setupDummyServiceSpinner(); 
            }
        });
    }
    
    // Sử dụng list giả lập nếu API lỗi
    private void setupDummyServiceSpinner() {
        String[] services = {"Khám Tổng Quát", "Chỉnh Nha", "Tẩy Trắng Răng", "Điều Trị Tủy", "Nhổ Răng"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_spinner_selected,
                services);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerService.setAdapter(adapter);
    }

    private void setupClickListeners() {

        // 1. Chọn Bệnh nhân (Mở màn hình tìm kiếm)
        etPatientInfo.setOnClickListener(v -> {
            // Thay thế bằng Intent thực tế tới PatientSearchActivity
            // Intent intent = new Intent(this, PatientSearchActivity.class);
            // startActivityForResult(intent, REQUEST_CODE_SELECT_PATIENT);

            // --- GIẢ LẬP KẾT QUẢ TỪ SEARCH ACTIVITY ---
            selectedPatientId = "6570c915f013d20a02b1c001"; // ID Bệnh nhân giả lập
            selectedPatientName = "Nguyễn Thị Lan (0909.123.456)";
            tvSelectedPatient.setText(String.format("👤 Bệnh nhân: %s", selectedPatientName));
            tvSelectedPatient.setVisibility(View.VISIBLE);
            Toast.makeText(this, "Đã chọn Bệnh nhân (Demo)", Toast.LENGTH_SHORT).show();
        });

        // 2. Chọn Bác sĩ (Mở màn hình chọn Bác sĩ)
        btnSelectDoctor.setOnClickListener(v -> {
            // Thay thế bằng Intent thực tế tới SelectDoctorActivity
            // Intent intent = new Intent(this, SelectDoctorActivity.class);
            // startActivityForResult(intent, REQUEST_CODE_SELECT_DOCTOR);
            
            // --- GIẢ LẬP KẾT QUẢ TỪ DOCTOR ACTIVITY ---
            selectedDoctorId = "6570c915f013d20a02b1c101"; // ID Bác sĩ giả lập
            selectedDoctorName = "Đoàn Hồng Lê";
            updateDoctorTimeView();
            Toast.makeText(this, "Đã chọn Bác sĩ (Demo)", Toast.LENGTH_SHORT).show();
        });

        // 3. Chọn Ngày & Giờ (Mở màn hình chọn Thời gian)
        btnSelectTime.setOnClickListener(v -> {
            if (selectedDoctorId == null) {
                 Toast.makeText(this, "Vui lòng chọn Bác sĩ phụ trách trước!", Toast.LENGTH_SHORT).show();
                 return;
            }
            // Thay thế bằng Intent thực tế tới SelectTimeActivity
            // Intent intent = new Intent(this, SelectTimeActivity.class);
            // intent.putExtra("DOCTOR_ID", selectedDoctorId);
            // startActivityForResult(intent, REQUEST_CODE_SELECT_TIME);
            
            // --- GIẢ LẬP KẾT QUẢ TỪ TIME ACTIVITY ---
            selectedTimeDisplay = "14:00 - 14/10/2024";
            selectedIsoDateTime = "2024-10-14T14:00:00.000Z"; // Chuỗi ISO 8601 giả lập
            updateDoctorTimeView();
            Toast.makeText(this, "Đã chọn Thời gian (Demo)", Toast.LENGTH_SHORT).show();
        });

        // 4. Xác nhận Tạo Cuộc Hẹn (GỌI API)
        btnConfirmAppointment.setOnClickListener(v -> createAppointment());
    }

    /**
     * Hàm gọi API để tạo lịch hẹn
     */
    private void createAppointment() {
        // 1. Validate
        if (selectedPatientId == null || selectedDoctorId == null || selectedIsoDateTime == null) {
            Toast.makeText(this, "Vui lòng chọn đầy đủ Bệnh nhân, Bác sĩ và Thời gian!", Toast.LENGTH_LONG).show();
            return;
        }

        String lyDoKham = etNote.getText().toString().trim();
        String selectedServiceName = spinnerService.getSelectedItem().toString();
        
        // 2. Tạo Request Body (LichHen model)
        LichHen request = new LichHen(); 
        request.setId_benh_nhan(selectedPatientId);
        request.setId_bac_si(selectedDoctorId);
        request.setThoi_gian_hen(selectedIsoDateTime);
        // Kết hợp Dịch vụ và Ghi chú vào trường lý do khám
        request.setLy_do_kham("Dịch vụ: " + selectedServiceName + " - Ghi chú: " + lyDoKham); 

        // 3. Gọi API
        apiService.createLichHen(request).enqueue(new Callback<LichHen>() {
            @Override
            public void onResponse(@NonNull Call<LichHen> call, @NonNull Response<LichHen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(NewAppointmentActivity.this, "✅ Đặt lịch thành công! Mã: " + response.body().get_id(), Toast.LENGTH_LONG).show();
                    // Chuyển sang màn hình xác nhận hoặc đóng
                    // Intent intent = new Intent(NewAppointmentActivity.this, ConfirmationActivity.class);
                    // startActivity(intent);
                    finish(); 
                    
                } else {
                    Toast.makeText(NewAppointmentActivity.this, "Lỗi khi tạo lịch hẹn: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LichHen> call, @NonNull Throwable t) {
                Toast.makeText(NewAppointmentActivity.this, "Lỗi kết nối API: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    /**
     * Cập nhật giao diện hiển thị kết quả chọn Bác sĩ và Thời gian
     */
    private void updateDoctorTimeView() {
        StringBuilder displayText = new StringBuilder();

        if (selectedDoctorName != null && !selectedDoctorName.isEmpty()) {
            displayText.append("👨‍⚕️ Bác sĩ: ").append(selectedDoctorName);
        }

        if (selectedTimeDisplay != null) {
            if (displayText.length() > 0) displayText.append("\n");
            displayText.append("📅 Thời gian: ").append(selectedTimeDisplay);
        }

        if (displayText.length() > 0) {
            tvSelectedDoctorTime.setText(displayText.toString());
            tvSelectedDoctorTime.setVisibility(View.VISIBLE);
        } else {
            tvSelectedDoctorTime.setVisibility(View.GONE);
        }
    }

    // Xử lý kết quả trả về từ các Activity khác (Patient/Doctor/Time selection)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == REQUEST_CODE_SELECT_PATIENT) {
                selectedPatientId = data.getStringExtra("PATIENT_ID");
                selectedPatientName = data.getStringExtra("PATIENT_NAME_DISPLAY");
                tvSelectedPatient.setText(String.format("👤 Bệnh nhân: %s", selectedPatientName));
                tvSelectedPatient.setVisibility(View.VISIBLE);

            } else if (requestCode == REQUEST_CODE_SELECT_DOCTOR) {
                selectedDoctorId = data.getStringExtra("DOCTOR_ID");
                selectedDoctorName = data.getStringExtra("DOCTOR_NAME");
                updateDoctorTimeView();

            } else if (requestCode == REQUEST_CODE_SELECT_TIME) {
                selectedTimeDisplay = data.getStringExtra("TIME_DISPLAY"); // Ví dụ: "14:00 - 14/10/2024"
                selectedIsoDateTime = data.getStringExtra("ISO_DATE_TIME"); // Ví dụ: "2024-10-14T14:00:00.000Z"
                updateDoctorTimeView();
            }
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