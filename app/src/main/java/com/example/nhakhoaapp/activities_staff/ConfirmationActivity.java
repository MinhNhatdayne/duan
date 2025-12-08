package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.LichHen; // Tái sử dụng model LichHen

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private Button btnConfirmBooking;
    private EditText etNotes;
    
    private View detailService, detailDoctor, detailLocation, detailTime;

    private ApiService apiService; 

    // Dữ liệu cần thiết cho API (Giả định được truyền qua Intent)
    private String patientId; 
    private String doctorId; 
    private String isoDateTime; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        apiService = ApiClient.getApiService();

        // Ánh xạ cơ bản
        imgBackButton = findViewById(R.id.img_back_button);
        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
        etNotes = findViewById(R.id.et_notes);
        
        detailService = findViewById(R.id.detail_service);
        detailDoctor = findViewById(R.id.detail_doctor);
        detailLocation = findViewById(R.id.detail_location);
        detailTime = findViewById(R.id.detail_time);

        // Lấy dữ liệu từ Intent
        Intent intent = getIntent();
        String serviceName = intent.getStringExtra("SERVICE_NAME");
        String doctorName = intent.getStringExtra("DOCTOR_NAME");
        String selectedDate = intent.getStringExtra("SELECTED_DATE"); 
        String selectedTime = intent.getStringExtra("SELECTED_TIME"); 
        
        patientId = intent.getStringExtra("PATIENT_ID");
        doctorId = intent.getStringExtra("DOCTOR_ID");
        isoDateTime = intent.getStringExtra("FULL_ISO_DATE"); 

        String fullTime = (selectedDate != null && selectedTime != null) 
                          ? String.format("Ngày %s - %s", selectedDate, selectedTime) 
                          : "Chưa chọn thời gian";

        // Gán dữ liệu lên giao diện
        setupDetailView(detailService, "Dịch vụ", serviceName != null ? serviceName : "Khám tổng quát");
        setupDetailView(detailDoctor, "Bác sĩ", doctorName != null ? doctorName : "Bác sĩ trực");
        setupDetailView(detailLocation, "Cơ sở", "Cơ sở 2: 67 Phạm Tuấn Tài");
        setupDetailView(detailTime, "Thời gian", fullTime);

        // Xử lý sự kiện Quay lại
        imgBackButton.setOnClickListener(v -> finish());

        // Xử lý sự kiện Xác nhận Đặt lịch (GỌI API)
        btnConfirmBooking.setOnClickListener(v -> {
            createAppointment(patientId, doctorId, isoDateTime, etNotes.getText().toString());
        });
    }
    
    /**
     * Phương thức gọi API để tạo lịch hẹn
     */
    private void createAppointment(String idBenhNhan, String idBacSi, String isoDateTime, String notes) {
        if (idBenhNhan == null || idBacSi == null || isoDateTime == null) {
            Toast.makeText(this, "Lỗi: Thiếu thông tin Bệnh nhân/Bác sĩ/Thời gian.", Toast.LENGTH_LONG).show();
            return;
        }

        // Tái sử dụng model LichHen để tạo request body
        LichHen request = new LichHen(); 
        request.setId_benh_nhan(idBenhNhan); // set id_benh_nhan (String)
        request.setId_bac_si(idBacSi);       // set id_bac_si (String)
        request.setThoi_gian_hen(isoDateTime); // set thoi_gian_hen (String ISO 8601)
        request.setLy_do_kham(notes);        // set ly_do_kham
        // Trường trang_thai sẽ mặc định là "Chờ khám" trên backend

        // Gọi API
        apiService.createLichHen(request).enqueue(new Callback<LichHen>() {
            @Override
            public void onResponse(@NonNull Call<LichHen> call, @NonNull Response<LichHen> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String newId = response.body().get_id();
                    Toast.makeText(ConfirmationActivity.this, "Đặt lịch thành công! Mã: " + newId, Toast.LENGTH_LONG).show();
                    
                    // Chuyển về Dashboard và xóa stack
                    Intent dashboardIntent = new Intent(ConfirmationActivity.this, StaffDashboardActivity.class);
                    dashboardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(dashboardIntent);
                    
                } else {
                    Toast.makeText(ConfirmationActivity.this, "Lỗi khi tạo lịch hẹn: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LichHen> call, @NonNull Throwable t) {
                Toast.makeText(ConfirmationActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }


    private void setupDetailView(View parentView, String title, String value) {
        TextView tvTitle = parentView.findViewById(R.id.tv_detail_title);
        TextView tvValue = parentView.findViewById(R.id.tv_detail_value);
        
        if (tvTitle != null) tvTitle.setText(title);
        if (tvValue != null) tvValue.setText(value);
    }
}