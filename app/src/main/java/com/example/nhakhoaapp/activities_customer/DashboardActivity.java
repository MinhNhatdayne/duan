package com.example.nhakhoaapp.activities_customer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.example.nhakhoaapp.models.response.LichHenResponse; // Chú ý Import này
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    private TextView tvUserName;
    private Button btnBookAppointment;
    private BottomNavigationView bottomNavigationView;

    // --- VIEW LỊCH HẸN ---
    private LinearLayout layoutEmptyBooking, layoutBookingInfo;
    private TextView tvBookingDate, tvBookingTime, tvBookingDoctor, tvBookingReason, tvBookingStatus;
    // ---------------------

    private String patientName = "";
    private String patientId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        initViews();
        setupListeners();

        // Lấy dữ liệu ID và Name
        receivePatientFromLogin();

        // Gọi API lấy thông tin chi tiết
        loadUserDataFromApi();

        // Gọi API lấy LỊCH HẸN
        loadUpcomingBooking();
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.nav_home);
        // Load lại lịch hẹn khi quay lại màn hình này (ví dụ vừa đặt lịch xong)
        loadUpcomingBooking();
    }

    private void initViews() {
        tvUserName = findViewById(R.id.tv_user_name);
        btnBookAppointment = findViewById(R.id.btn_book_appointment);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // --- ÁNH XẠ VIEW LỊCH HẸN ---
        layoutEmptyBooking = findViewById(R.id.layout_empty_booking);
        layoutBookingInfo = findViewById(R.id.layout_booking_info);

        tvBookingDate = findViewById(R.id.tv_booking_date);
        tvBookingTime = findViewById(R.id.tv_booking_time);
        tvBookingDoctor = findViewById(R.id.tv_booking_doctor);
        tvBookingReason = findViewById(R.id.tv_booking_reason);
        tvBookingStatus = findViewById(R.id.tv_booking_status);
    }

    private void receivePatientFromLogin() {
        Intent intent = getIntent();
        if (intent != null) {
            String name = intent.getStringExtra("PATIENT_NAME");
            String id = intent.getStringExtra("PATIENT_ID");

            if (name != null) patientName = name;
            if (id != null) patientId = id;

            if (!patientName.isEmpty()) {
                tvUserName.setText(patientName);
            }
        }
    }

    private void loadUserDataFromApi() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);

        if (patientId == null || patientId.isEmpty()) {
            patientId = prefs.getString("USER_ID", null);
        }

        if (patientName == null || patientName.isEmpty()) {
            patientName = prefs.getString("USER_NAME", "Khách hàng");
        }
        tvUserName.setText(patientName);

        if (patientId == null) {
            // Nếu vẫn không có ID thì thôi, không gọi API
            return;
        }

        ApiClient.getApiService().getBenhNhanById(patientId).enqueue(new Callback<BenhNhan>() {
            @Override
            public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    BenhNhan bn = response.body();
                    patientName = bn.getHo_ten();
                    tvUserName.setText(patientName);
                }
            }

            @Override
            public void onFailure(Call<BenhNhan> call, Throwable t) {
                // Fail silently
            }
        });
    }

    // ==========================================
    // HÀM LOAD LỊCH HẸN SẮP TỚI
    // ==========================================
    private void loadUpcomingBooking() {
        if (patientId == null || patientId.isEmpty()) {
            showEmptyBookingState();
            return;
        }

        ApiClient.getApiService().getMyAppointments(patientId).enqueue(new Callback<List<LichHenResponse>>() {
            @Override
            public void onResponse(Call<List<LichHenResponse>> call, Response<List<LichHenResponse>> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().isEmpty()) {
                    List<LichHenResponse> list = response.body();

                    // Lấy phần tử mới nhất (Giả sử API trả về list, lấy phần tử đầu tiên)
                    // Nếu API trả về lịch cũ trước, bạn có thể cần lấy list.get(list.size()-1)
                    LichHenResponse upcoming = list.get(0);

                    // Cập nhật UI
                    showBookingInfoState(upcoming);
                } else {
                    showEmptyBookingState();
                }
            }

            @Override
            public void onFailure(Call<List<LichHenResponse>> call, Throwable t) {
                showEmptyBookingState();
            }
        });
    }

    private void showBookingInfoState(LichHenResponse lh) {
        layoutEmptyBooking.setVisibility(View.GONE);
        layoutBookingInfo.setVisibility(View.VISIBLE);

        // Sử dụng Helper Methods trong LichHenResponse
        tvBookingDate.setText("Ngày: " + lh.getNgayHenFormatted());
        tvBookingTime.setText("Giờ: " + lh.getGioHenFormatted());

        tvBookingDoctor.setText("Bác sĩ: " + lh.getTen_bac_si());
        tvBookingReason.setText("Lý do: " + (lh.getLy_do_kham() != null ? lh.getLy_do_kham() : "Khám tổng quát"));

        // Xử lý màu sắc trạng thái đơn giản (Optional)
        String status = lh.getTrang_thai();
        tvBookingStatus.setText("Trạng thái: " + status);
        if ("Đã duyệt".equals(status)) {
            tvBookingStatus.setTextColor(getResources().getColor(android.R.color.holo_green_dark));
        } else if ("Đã hủy".equals(status)) {
            tvBookingStatus.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
        } else {
            tvBookingStatus.setTextColor(getResources().getColor(android.R.color.holo_orange_dark));
        }
    }

    private void showEmptyBookingState() {
        layoutEmptyBooking.setVisibility(View.VISIBLE);
        layoutBookingInfo.setVisibility(View.GONE);
    }
    // ==========================================

    private void setupListeners() {
        btnBookAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(this, BookingActivity.class);
            intent.putExtra("PATIENT_NAME", patientName);
            intent.putExtra("PATIENT_ID", patientId);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNav);
    }

    private boolean handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) return true;

        Intent intent = null;

        if (id == R.id.nav_booking) {
            intent = new Intent(this, BookingActivity.class);
            intent.putExtra("PATIENT_NAME", patientName);
            intent.putExtra("PATIENT_ID", patientId);

        } else if (id == R.id.nav_notifications) {
            intent = new Intent(this, NotificationsActivity.class);

        } else if (id == R.id.nav_profile) {
            intent = new Intent(this, ProfileActivity.class);
        }

        if (intent != null) startActivity(intent);
        return true;
    }
}