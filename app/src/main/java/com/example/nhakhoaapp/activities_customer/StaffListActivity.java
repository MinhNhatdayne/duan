package com.example.nhakhoaapp.activities_customer;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
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
    private List<NhanVien> originalList; // Danh sách gốc từ API
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private ApiService apiService;
    private Toolbar toolbar;
    private Spinner spinnerRole;

    // Biến lưu trạng thái lọc hiện tại
    private String currentSearchText = "";
    private String currentRoleFilter = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_list);

        initViews();
        setupSpinner(); // Cài đặt dữ liệu cho Spinner lọc

        apiService = ApiClient.getApiService();
        rcvStaff.setLayoutManager(new LinearLayoutManager(this));
        originalList = new ArrayList<>();

        // Sự kiện nút Thêm mới
        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(StaffListActivity.this, StaffEditorActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        rcvStaff = findViewById(R.id.rcvStaff);
        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAddStaff);
        spinnerRole = findViewById(R.id.spinnerRole);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Danh sách Nhân viên");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    // === 1. CẤU HÌNH SPINNER LỌC ROLE ===
    private void setupSpinner() {
        // Danh sách các vai trò (Bạn có thể thêm tùy ý)
        String[] roles = {"Tất cả", "Bác sĩ", "Nhân viên", "Quản lý"};

        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, roles);
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerRole.setAdapter(adapterSpinner);

        // Bắt sự kiện chọn item trong Spinner
        spinnerRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                currentRoleFilter = roles[position]; // Lưu role người dùng chọn
                filterData(); // Gọi hàm lọc lại dữ liệu
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    // === 2. CẤU HÌNH MENU TÌM KIẾM ===
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Nhập tên nhân viên...");

        // Bắt sự kiện gõ chữ
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchText = newText; // Lưu từ khóa tìm kiếm
                filterData(); // Gọi hàm lọc lại dữ liệu
                return true;
            }
        });
        return true;
    }

    // === 3. LOGIC LỌC TỔNG HỢP (QUAN TRỌNG NHẤT) ===
    private void filterData() {
        if (originalList == null) return;

        List<NhanVien> filteredList = new ArrayList<>();

        for (NhanVien item : originalList) {
            // Điều kiện 1: Tên chứa từ khóa tìm kiếm (Không phân biệt hoa thường)
            boolean matchName = item.getHo_ten().toLowerCase().contains(currentSearchText.toLowerCase());

            // Điều kiện 2: Chức vụ trùng với bộ lọc Spinner
            boolean matchRole = false;
            if (currentRoleFilter.equals("Tất cả")) {
                matchRole = true;
            } else {
                // Kiểm tra xem chức vụ trong DB có chứa từ khóa lọc không
                // Ví dụ: DB là "Bác sĩ nha khoa" -> Lọc "Bác sĩ" vẫn nhận
                if (item.getChuc_vu() != null && item.getChuc_vu().toLowerCase().contains(currentRoleFilter.toLowerCase())) {
                    matchRole = true;
                }
            }

            // Nếu thỏa mãn CẢ HAI điều kiện -> Thêm vào list hiển thị
            if (matchName && matchRole) {
                filteredList.add(item);
            }
        }

        // Cập nhật lại Adapter
        if (adapter != null) {
            adapter.updateList(filteredList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData(); // Tải lại dữ liệu khi quay lại màn hình
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getAllNhanVien().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    originalList = response.body();

                    // Setup adapter lần đầu
                    if (adapter == null) {
                        setupAdapter(originalList);
                    }

                    // Lọc ngay lập tức (để giữ trạng thái nếu đang lọc dở)
                    filterData();
                }
            }

            @Override
            public void onFailure(Call<List<NhanVien>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(StaffListActivity.this, "Lỗi tải dữ liệu: " + t.getMessage(), Toast.LENGTH_SHORT).show();
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
                confirmDelete(id, name);
            }
        });
        rcvStaff.setAdapter(adapter);
    }

    private void confirmDelete(String id, String name) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa nhân viên " + name + " không?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteStaff(id))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteStaff(String id) {
        apiService.deleteNhanVien(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(StaffListActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                    loadData();
                } else {
                    Toast.makeText(StaffListActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(StaffListActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}