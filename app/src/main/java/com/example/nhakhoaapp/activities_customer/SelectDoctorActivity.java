package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DoctorAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.NhanVien;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SelectDoctorActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private TextView tvSelectedService;
    private RecyclerView rvListDoctors;
    private DoctorAdapter doctorAdapter;
    private List<NhanVien> listBacSi;
    private EditText etSearchDoctor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            setContentView(R.layout.activity_select_doctor);
        } catch (Exception e) {
            Log.e("SelectDoctorActivity", "Lỗi nạp XML: " + e.getMessage());
            Toast.makeText(this, "Lỗi giao diện: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        imgBackButton = findViewById(R.id.img_back_button);
        tvSelectedService = findViewById(R.id.tv_selected_service);
        rvListDoctors = findViewById(R.id.rv_list_doctors);
        etSearchDoctor = findViewById(R.id.et_search_doctor);

        if (rvListDoctors == null) {
            Toast.makeText(this, "Không tìm thấy rv_list_doctors", Toast.LENGTH_LONG).show();
            return;
        }

        String serviceName = getIntent().getStringExtra("SERVICE_NAME");
        String patientName = getIntent().getStringExtra("PATIENT_NAME");

        tvSelectedService.setText(serviceName != null ? serviceName : "Dịch vụ");

        imgBackButton.setOnClickListener(v -> finish());

        rvListDoctors.setLayoutManager(new LinearLayoutManager(this));
        listBacSi = new ArrayList<>();

        doctorAdapter = new DoctorAdapter(this, listBacSi, doctor -> {
            Intent intent = new Intent(SelectDoctorActivity.this, SelectTimeActivity.class);
            intent.putExtra("SERVICE_NAME", serviceName);
            intent.putExtra("PATIENT_NAME", patientName);
            intent.putExtra("DOCTOR_NAME", doctor.getHo_ten());
            intent.putExtra("DOCTOR_ID", doctor.get_id());
            startActivity(intent);
        });
        rvListDoctors.setAdapter(doctorAdapter);

        loadDoctorsFromApi();
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
                            String role = nv.getChuc_vu().trim().toLowerCase();
                            if (role.contains("bác sĩ") || role.contains("bac si") || role.contains("doctor") || role.contains("nha sĩ")) {
                                listBacSi.add(nv);
                            }
                        }
                    }

                    if (listBacSi.isEmpty()) {
                        NhanVien dummy = new NhanVien();
                        dummy.setHo_ten("Bác sĩ Demo");
                        dummy.setChuc_vu("Trưởng khoa");
                        dummy.set_id("demo_id");
                        listBacSi.add(dummy);

                        Toast.makeText(SelectDoctorActivity.this, "Hiện dữ liệu mẫu", Toast.LENGTH_SHORT).show();
                    }

                    doctorAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(SelectDoctorActivity.this, "Không tải được danh sách", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                Toast.makeText(SelectDoctorActivity.this, "Lỗi kết nối API", Toast.LENGTH_SHORT).show();
            }
        });
    }
}