package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DoctorAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectDoctorActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private TextView tvSelectedService;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView rvListDoctors;
    private DoctorAdapter doctorAdapter;
    private List<NhanVien> listBacSi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 1. Nạp giao diện (Có bắt lỗi Crash)
        try {
            setContentView(R.layout.activity_select_doctor);
        } catch (Exception e) {
            Log.e("SelectDoctorActivity", "Lỗi nạp XML: " + e.getMessage());
            Toast.makeText(this, "Lỗi giao diện: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // 2. Ánh xạ View
        try {
            imgBackButton = findViewById(R.id.img_back_button);
            tvSelectedService = findViewById(R.id.tv_selected_service);
            bottomNavigationView = findViewById(R.id.bottom_navigation);
            rvListDoctors = findViewById(R.id.rv_list_doctors);

            // Kiểm tra ID quan trọng
            if (rvListDoctors == null) {
                Toast.makeText(this, "Lỗi: Không tìm thấy rv_list_doctors", Toast.LENGTH_LONG).show();
                return;
            }

            // 3. Nhận dữ liệu
            String serviceName = getIntent().getStringExtra("SERVICE_NAME");
            tvSelectedService.setText(serviceName != null ? serviceName : "Dịch vụ");

            // 4. Xử lý sự kiện
            imgBackButton.setOnClickListener(v -> finish());

            if (bottomNavigationView != null) {
                bottomNavigationView.setOnItemSelectedListener(this::handleBottomNav);
            }

            // 5. Cấu hình RecyclerView
            rvListDoctors.setLayoutManager(new LinearLayoutManager(this));
            listBacSi = new ArrayList<>();

            doctorAdapter = new DoctorAdapter(this, listBacSi, doctor -> {
                // Chuyển sang màn hình chọn giờ
                Intent intent = new Intent(SelectDoctorActivity.this, SelectTimeActivity.class);
                intent.putExtra("SERVICE_NAME", serviceName);
                intent.putExtra("DOCTOR_NAME", doctor.getHo_ten());
                intent.putExtra("DOCTOR_ID", doctor.get_id());
                startActivity(intent);
            });
            rvListDoctors.setAdapter(doctorAdapter);

            // 6. Gọi API
            loadDoctorsFromApi();

        } catch (Exception e) {
            Toast.makeText(this, "Lỗi Java: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    private void loadDoctorsFromApi() {
        ApiClient.getApiService().getAllNhanVien().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<NhanVien> allStaff = response.body();
                    listBacSi.clear();

                    for (NhanVien nv : allStaff) {
                        if (nv.getChuc_vu() != null) {
                            String chucVu = nv.getChuc_vu().trim().toLowerCase();
                            // Logic lọc linh hoạt
                            if (chucVu.contains("bác sĩ") || chucVu.contains("bac si") || chucVu.contains("doctor") || chucVu.contains("nha sĩ")) {
                                listBacSi.add(nv);
                            }
                        }
                    }

                    // Nếu không có dữ liệu thật thì hiển thị dữ liệu giả để test giao diện
                    if (listBacSi.isEmpty()) {
                        NhanVien dummy = new NhanVien();
                        dummy.setHo_ten("Bác sĩ Test (Demo)");
                        dummy.setChuc_vu("Trưởng khoa");
                        dummy.set_id("dummy_id");
                        listBacSi.add(dummy);
                        Toast.makeText(SelectDoctorActivity.this, "Đang hiển thị dữ liệu mẫu", Toast.LENGTH_SHORT).show();
                    }

                    doctorAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SelectDoctorActivity.this, "Không tải được danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                Toast.makeText(SelectDoctorActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) {
            bottomNavigationView.setSelectedItemId(R.id.nav_booking);
        }
    }

    private boolean handleBottomNav(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            startActivity(new Intent(this, DashboardActivity.class));
            return true;
        } else if (id == R.id.nav_booking) {
            startActivity(new Intent(this, BookingActivity.class));
            return true;
        } else if (id == R.id.nav_notifications) {
            startActivity(new Intent(this, NotificationsActivity.class));
            return true;
        } else if (id == R.id.nav_profile) {
            startActivity(new Intent(this, ProfileActivity.class));
            return true;
        }
        return false;
    }
}