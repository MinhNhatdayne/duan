package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.BenhNhanAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientListActivity extends AppCompatActivity {

    private RecyclerView rcvPatient;
    private BenhNhanAdapter adapter;
    private List<BenhNhan> originalList = new ArrayList<>(); // Khởi tạo tránh null
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private AppCompatSpinner spinnerGender;
    private EditText etSearch; // Thêm biến này để khớp XML
    private LinearLayout layoutEmptyState;

    private String currentSearchText = "";
    private String currentGenderFilter = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        initViews();
        setupSpinner();
        setupSearch(); // Hàm xử lý tìm kiếm mới

        rcvPatient.setLayoutManager(new LinearLayoutManager(this));

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, PatientEditorActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        rcvPatient = findViewById(R.id.rcvPatient);
        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAddPatient);
        spinnerGender = findViewById(R.id.spinnerGender);
        etSearch = findViewById(R.id.etSearch); // Ánh xạ từ XML
        layoutEmptyState = findViewById(R.id.layoutEmptyState);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false); // Ẩn title mặc định để hiện layout custom
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    // Xử lý tìm kiếm real-time khi gõ vào EditText trong XML
    private void setupSearch() {
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentSearchText = s.toString();
                filterData();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void setupSpinner() {
        String[] genders = {"Tất cả", "Nam", "Nữ"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, genders);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterSpinner);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentGenderFilter = genders[position];
                filterData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    // Logic lọc dữ liệu
    private void filterData() {
        if (originalList == null) return;
        List<BenhNhan> filtered = new ArrayList<>();

        for (BenhNhan item : originalList) {
            // Lọc tên HOẶC số điện thoại (để tìm kiếm tiện hơn)
            String name = item.getHo_ten() != null ? item.getHo_ten().toLowerCase() : "";
            String phone = item.getSo_dien_thoai() != null ? item.getSo_dien_thoai() : "";

            boolean matchSearch = name.contains(currentSearchText.toLowerCase()) ||
                    phone.contains(currentSearchText);

            // Lọc giới tính
            boolean matchGender = currentGenderFilter.equals("Tất cả") ||
                    (item.getGioi_tinh() != null && item.getGioi_tinh().equalsIgnoreCase(currentGenderFilter));

            if (matchSearch && matchGender) filtered.add(item);
        }

        if (adapter != null) {
            adapter.updateList(filtered);
        }

        // Hiển thị thông báo nếu không có dữ liệu
        if (filtered.isEmpty()) {
            layoutEmptyState.setVisibility(View.VISIBLE);
            rcvPatient.setVisibility(View.GONE);
        } else {
            layoutEmptyState.setVisibility(View.GONE);
            rcvPatient.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        ApiClient.getApiService().getAllBenhNhan().enqueue(new Callback<List<BenhNhan>>() {
            @Override
            public void onResponse(Call<List<BenhNhan>> call, Response<List<BenhNhan>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    originalList = response.body();

                    if (adapter == null) {
                        adapter = new BenhNhanAdapter(PatientListActivity.this, new ArrayList<>(originalList), new BenhNhanAdapter.OnItemClickListener() {
                            @Override
                            public void onEdit(BenhNhan bn) {
                                Intent intent = new Intent(PatientListActivity.this, PatientEditorActivity.class);
                                intent.putExtra("id", bn.get_id());
                                intent.putExtra("name", bn.getHo_ten());
                                intent.putExtra("dob", bn.getNgay_sinh());
                                intent.putExtra("gender", bn.getGioi_tinh());
                                intent.putExtra("phone", bn.getSo_dien_thoai());
                                intent.putExtra("address", bn.getDia_chi());
                                intent.putExtra("email", bn.getEmail());
                                startActivity(intent);
                            }

                            @Override
                            public void onDelete(String id, String name) {
                                confirmDelete(id, name);
                            }
                        });
                        rcvPatient.setAdapter(adapter);
                    } else {
                        // Nếu adapter đã có, chỉ cần cập nhật data gốc và lọc lại
                        filterData();
                    }
                    // Gọi filter ngay lần đầu để hiển thị đúng
                    filterData();
                }
            }

            @Override
            public void onFailure(Call<List<BenhNhan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PatientListActivity.this, "Lỗi kết nối server", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete(String id, String name) {
        new AlertDialog.Builder(this)
                .setTitle("Xóa bệnh nhân")
                .setMessage("Bạn có chắc chắn muốn xóa bệnh nhân " + name + " không?")
                .setPositiveButton("Xóa", (d, w) -> {
                    ApiClient.getApiService().deleteBenhNhan(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(PatientListActivity.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                                loadData(); // Tải lại danh sách
                            } else {
                                Toast.makeText(PatientListActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(PatientListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}