package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.nhakhoaapp.models.request.LichHenRequest;
import com.example.nhakhoaapp.models.response.LichHenResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConfirmationActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private Button btnConfirmBooking;
    private EditText etNotes;

    private View detailService, detailDoctor, detailPatient, detailTime;

    private ApiService apiService;

    private String patientId;
    private String patientName;
    private String doctorId;
    private String isoDateTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation);

        apiService = ApiClient.getApiService();

        imgBackButton = findViewById(R.id.img_back_button);
        btnConfirmBooking = findViewById(R.id.btn_confirm_booking);
        etNotes = findViewById(R.id.et_notes);

        detailService = findViewById(R.id.detail_service);
        detailDoctor = findViewById(R.id.detail_doctor);
        detailPatient = findViewById(R.id.detail_patient);
        detailTime = findViewById(R.id.detail_time);

        // =============================
        // ⭐ NHẬN DỮ LIỆU TỪ MÀN TRƯỚC
        // =============================
        Intent intent = getIntent();

        String serviceName = intent.getStringExtra("SERVICE_NAME");
        String doctorName = intent.getStringExtra("DOCTOR_NAME");
        String selectedDate = intent.getStringExtra("SELECTED_DATE");
        String selectedTime = intent.getStringExtra("SELECTED_TIME");

        doctorId = intent.getStringExtra("DOCTOR_ID");
        isoDateTime = intent.getStringExtra("FULL_ISO_DATE");

        // =============================================
        // ⭐ LẤY ĐÚNG ID BỆNH NHÂN TỪ SharedPreferences
        // =============================================
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        patientId = prefs.getString("USER_ID", null);  // API REQUIRE ID
        patientName = prefs.getString("USER_NAME", "Không xác định"); // CHO GIAO DIỆN

        // =============================
        // ⭐ SETUP UI
        // =============================
        String fullTime = (selectedDate != null && selectedTime != null)
                ? selectedDate + " • " + selectedTime
                : "Chưa chọn";

        setupDetailView(detailService, "Dịch vụ", serviceName);
        setupDetailView(detailDoctor, "Bác sĩ", doctorName);
        setupDetailView(detailPatient, "Tên bệnh nhân", patientName);
        setupDetailView(detailTime, "Thời gian", fullTime);

        imgBackButton.setOnClickListener(v -> finish());

        btnConfirmBooking.setOnClickListener(v ->
                createAppointment(patientId, doctorId, isoDateTime, etNotes.getText().toString()));
    }

    private void createAppointment(String idBenhNhan, String idBacSi, String isoDateTime, String notes) {

        // =============================
        // ⭐ DEBUG — KIỂM TRA GIÁ TRỊ
        // =============================
        System.out.println("PATIENT_ID = " + idBenhNhan);
        System.out.println("DOCTOR_ID  = " + idBacSi);
        System.out.println("ISO_DATE   = " + isoDateTime);

        if (idBenhNhan == null || idBacSi == null || isoDateTime == null) {
            Toast.makeText(this,
                    "Lỗi: Thiếu thông tin Bệnh nhân/Bác sĩ/Thời gian",
                    Toast.LENGTH_LONG).show();
            return;
        }

        btnConfirmBooking.setEnabled(false);

        LichHenRequest request = new LichHenRequest();
        request.setId_benh_nhan(idBenhNhan);
        request.setId_bac_si(idBacSi);
        request.setThoi_gian_hen(isoDateTime);
        request.setLy_do_kham(notes);
        request.setTrang_thai("ChoXacNhan");

        apiService.createLichHen(request).enqueue(new Callback<LichHenResponse>() {
            @Override
            public void onResponse(@NonNull Call<LichHenResponse> call,
                                   @NonNull Response<LichHenResponse> response) {

                btnConfirmBooking.setEnabled(true);

                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(ConfirmationActivity.this,
                            "Đặt lịch thành công!", Toast.LENGTH_LONG).show();

                    finish();
                } else {
                    Toast.makeText(ConfirmationActivity.this,
                            "Lỗi tạo lịch: " + response.code(),
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<LichHenResponse> call, @NonNull Throwable t) {
                btnConfirmBooking.setEnabled(true);
                Toast.makeText(ConfirmationActivity.this,
                        "Lỗi kết nối: " + t.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setupDetailView(View parent, String title, String value) {
        TextView tvTitle = parent.findViewById(R.id.tv_detail_title);
        TextView tvValue = parent.findViewById(R.id.tv_detail_value);

        tvTitle.setText(title);
        tvValue.setText(value != null ? value : "Không có");
    }
}
