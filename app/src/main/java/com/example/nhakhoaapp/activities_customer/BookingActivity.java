package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.ServiceAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.DanhMucDichVu;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookingActivity extends AppCompatActivity {

    private RecyclerView rvServices;
    private BottomNavigationView bottomNavigationView;
    private ProgressBar progressBar;

    private ServiceAdapter adapter;
    private List<DanhMucDichVu> listService;

    // ⭐ Nhận từ DashboardActivity
    private String patientName = "";
    private String patientId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        receivePatientData();

        rvServices = findViewById(R.id.rv_services);
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        progressBar = findViewById(R.id.progress_bar);

        rvServices.setLayoutManager(new LinearLayoutManager(this));
        listService = new ArrayList<>();

        adapter = new ServiceAdapter(this, listService, service -> {

            // ⭐ Chuyển sang chọn bác sĩ
            Intent intent = new Intent(BookingActivity.this, SelectDoctorActivity.class);

            // Truyền dữ liệu dịch vụ
            intent.putExtra("SERVICE_NAME", service.getTen_dich_vu());
            intent.putExtra("SERVICE_ID", service.get_id());

            // ⭐ Truyền bệnh nhân tiếp theo luồng
            intent.putExtra("PATIENT_NAME", patientName);
            intent.putExtra("PATIENT_ID", patientId);

            startActivity(intent);
        });

        rvServices.setAdapter(adapter);

        loadServicesFromApi();

        bottomNavigationView.setOnItemSelectedListener(this::handleBottomNav);
    }

    // ⭐ Nhận PATIENT_NAME + PATIENT_ID từ DashboardActivity
    private void receivePatientData() {
        Intent intent = getIntent();
        if (intent != null) {
            patientName = intent.getStringExtra("PATIENT_NAME");
            patientId = intent.getStringExtra("PATIENT_ID");
        }

        if (patientName == null) patientName = "";
        if (patientId == null) patientId = "";
    }

    private void loadServicesFromApi() {
        progressBar.setVisibility(View.VISIBLE);

        ApiClient.getApiService().getAllDanhMucDichVu().enqueue(new Callback<List<DanhMucDichVu>>() {
            @Override
            public void onResponse(Call<List<DanhMucDichVu>> call, Response<List<DanhMucDichVu>> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null) {
                    listService.clear();
                    listService.addAll(response.body());
                    adapter.notifyDataSetChanged();

                    if (listService.isEmpty()) {
                        Toast.makeText(BookingActivity.this, "Chưa có dịch vụ nào", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(BookingActivity.this, "Lỗi tải dịch vụ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<DanhMucDichVu>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BookingActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        bottomNavigationView.setSelectedItemId(R.id.nav_booking);
    }

    private boolean handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.nav_home) {
            Intent intent = new Intent(this, DashboardActivity.class);
            intent.putExtra("PATIENT_NAME", patientName);
            intent.putExtra("PATIENT_ID", patientId);
            startActivity(intent);
            return true;
        }

        if (id == R.id.nav_booking) return true;

        if (id == R.id.nav_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return true;
        }

        if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }

        return false;
    }
}
