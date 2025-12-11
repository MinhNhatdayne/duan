package com.example.nhakhoaapp.activities_customer; // Đổi lại package nếu cần

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities_staff.StaffEditorActivity;
import com.example.nhakhoaapp.adapters.NhanVienAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffListActivity extends AppCompatActivity {

    private RecyclerView rcvStaff;
    private NhanVienAdapter adapter;
    private List<NhanVien> originalList;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private ApiService apiService;
    private EditText etSearch;
    private AppCompatSpinner spinnerRole;
    private LinearLayout layoutEmpty;

    // Biến trạng thái lọc
    private String currentSearchText = "";
    private String currentRoleFilter = "Tất cả";

    // Biến lưu ID người đang đăng nhập
    private String currentUserId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);

        // Lấy ID người đang đăng nhập từ Session
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = prefs.getString("USER_ID", "");

        initViews();
        setupSpinner();
        setupSearch();

        apiService = ApiClient.getApiService();
        rcvStaff.setLayoutManager(new LinearLayoutManager(this));
        originalList = new ArrayList<>();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(StaffListActivity.this, StaffEditorActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        rcvStaff = findViewById(R.id.rcvStaff);
        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAddStaff);
        etSearch = findViewById(R.id.etSearchStaff);
        spinnerRole = findViewById(R.id.spinnerRole);
        layoutEmpty = findViewById(R.id.layoutEmptyState);
    }

    private void setupSpinner() {
        String[] roles = {"Tất cả", "Bác sĩ", "Y tá", "Lễ tân", "Quản lý"};
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapterSpinner);

        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentRoleFilter = roles[position];
                filterData();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

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

    private void filterData() {
        if (originalList == null) return;

        List<NhanVien> filteredList = new ArrayList<>();

        for (NhanVien item : originalList) {
            // Lọc theo tên
            boolean matchName = item.getHo_ten().toLowerCase().contains(currentSearchText.toLowerCase());

            // Lọc theo Role
            boolean matchRole = false;
            if (currentRoleFilter.equals("Tất cả")) {
                matchRole = true;
            } else {
                if (item.getChuc_vu() != null && item.getChuc_vu().toLowerCase().contains(currentRoleFilter.toLowerCase())) {
                    matchRole = true;
                }
            }

            if (matchName && matchRole) {
                filteredList.add(item);
            }
        }

        if (adapter != null) {
            adapter.updateList(filteredList);
        }

        // Hiện Empty State nếu không có kết quả
        if (filteredList.isEmpty()) {
            layoutEmpty.setVisibility(View.VISIBLE);
            rcvStaff.setVisibility(View.GONE);
        } else {
            layoutEmpty.setVisibility(View.GONE);
            rcvStaff.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getAllNhanVien().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    originalList = response.body();

                    if (adapter == null) {
                        setupAdapter(originalList);
                    }
                    filterData();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(StaffListActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter(List<NhanVien> listToShow) {
        adapter = new NhanVienAdapter(this, listToShow, new NhanVienAdapter.OnItemClickListener() {
            @Override
            public void onEdit(NhanVien nv) {
                Intent intent = new Intent(StaffListActivity.this, StaffEditorActivity.class);
                intent.putExtra("id", nv.get_id());
                intent.putExtra("name", nv.getHo_ten());
                intent.putExtra("position", nv.getChuc_vu());
                intent.putExtra("phone", nv.getSo_dien_thoai());
                intent.putExtra("email", nv.getEmail());
                intent.putExtra("address", nv.getDia_chi());
                intent.putExtra("salary", nv.getLuong());
                startActivity(intent);
            }

            @Override
            public void onDelete(String id, String name) {
                // Kiểm tra trước khi xóa
                confirmDelete(id, name);
            }
        });
        rcvStaff.setAdapter(adapter);
    }

    // === HÀM XỬ LÝ XÓA AN TOÀN ===
    private void confirmDelete(String idStaffToDelete, String name) {
        // 1. Kiểm tra: Nếu ID muốn xóa trùng với ID đang đăng nhập -> Chặn lại
        if (idStaffToDelete.equals(currentUserId)) {
            new AlertDialog.Builder(this)
                    .setTitle("Cảnh báo")
                    .setMessage("Bạn không thể xóa tài khoản Quản lý đang đăng nhập!")
                    .setPositiveButton("Đã hiểu", null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return;
        }

        // 2. Nếu không trùng -> Hiện dialog xác nhận xóa bình thường
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa nhân viên " + name + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteStaff(idStaffToDelete))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteStaff(String id) {
        apiService.deleteNhanVien(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StaffListActivity.this, "Đã xóa thành công", Toast.LENGTH_SHORT).show();
                    loadData(); // Tải lại danh sách
                } else {
                    Toast.makeText(StaffListActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(StaffListActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }
}