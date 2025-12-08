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
import com.example.nhakhoaapp.models.request.LichHenRequest; // [SỬA] Dùng Request
import com.example.nhakhoaapp.models.response.LichHenResponse; // [SỬA] Dùng Response

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private Button btnConfirmBooking;
    private EditText etNotes;

    private View detailService, detailDoctor, detailLocation, detailTime;

    private ApiService apiService;

    // Dữ liệu cần thiết cho API
    private String patientId;
    private String doctorId;
    private String isoDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        apiService = ApiClient.getApiService();

        // Ánh xạ
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

        btnConfirmBooking.setEnabled(false); // Khóa nút để tránh bấm nhiều lần

        // [SỬA QUAN TRỌNG] Sử dụng LichHenRequest thay vì LichHen
        LichHenRequest request = new LichHenRequest();
        request.setIdBenhNhan(idBenhNhan);   // Gửi String ID
        request.setIdBacSi(idBacSi);         // Gửi String ID
        request.setThoiGianHen(isoDateTime); // Gửi String ISO Date
        request.setLyDoKham(notes);
        request.setTrangThai("ChoXacNhan"); // Set trạng thái mặc định

        // Gọi API (Hứng kết quả là LichHenResponse)
        apiService.createLichHen(request).enqueue(new Callback<LichHenResponse>() {
            @Override
            public void onResponse(@NonNull Call<LichHenResponse> call, @NonNull Response<LichHenResponse> response) {
                btnConfirmBooking.setEnabled(true); // Mở lại nút

                if (response.isSuccessful() && response.body() != null) {
                    // response.body() trả về LichHenResponse (chứa ID mới tạo)
                    String newId = response.body().getId();
                    Toast.makeText(ConfirmationActivity.this, "Đặt lịch thành công!", Toast.LENGTH_LONG).show();

                    // Chuyển về Dashboard và xóa stack (ngăn quay lại màn hình này)
                    Intent dashboardIntent = new Intent(ConfirmationActivity.this, StaffDashboardActivity.class);
                    dashboardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(dashboardIntent);

                } else {
                    Toast.makeText(ConfirmationActivity.this, "Lỗi tạo lịch: " + response.code(), Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LichHenResponse> call, @NonNull Throwable t) {
                btnConfirmBooking.setEnabled(true);
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