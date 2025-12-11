package com.example.nhakhoaapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities.CategoryEditorActivity;
import com.example.nhakhoaapp.adapters.CategoryAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.DanhMucDichVu;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryListActivity extends AppCompatActivity {

    private RecyclerView rcvList;
    private CategoryAdapter adapter;
    private List<DanhMucDichVu> originalList = new ArrayList<>();
    private ApiService apiService;
    private EditText etSearch;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sử dụng lại layout activity_staff_list.xml hoặc tạo layout mới tương tự
        setContentView(R.layout.activity_staff_list);

        initViews();
        apiService = ApiClient.getApiService();
        rcvList.setLayoutManager(new LinearLayoutManager(this));

        // Nút Thêm mới (dùng ID fabAddStaff nếu dùng layout staff)
        findViewById(R.id.fabAddStaff).setOnClickListener(v -> {
            startActivity(new Intent(CategoryListActivity.this, CategoryEditorActivity.class));
        });

        // Tìm kiếm
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filterData(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Danh mục Dịch vụ");
        }

        rcvList = findViewById(R.id.rcvStaff);
        etSearch = findViewById(R.id.etSearchStaff);
        etSearch.setHint("Tìm tên dịch vụ...");
        progressBar = findViewById(R.id.progressBar);

        // Ẩn Spinner lọc Role đi vì màn hình này không cần
        View spinnerRole = findViewById(R.id.spinnerRole);
        if (spinnerRole != null) spinnerRole.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        apiService.getAllDanhMucDichVu().enqueue(new Callback<List<DanhMucDichVu>>() {
            @Override
            public void onResponse(Call<List<DanhMucDichVu>> call, Response<List<DanhMucDichVu>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    originalList = response.body();
                    setupAdapter(originalList);
                }
            }
            @Override
            public void onFailure(Call<List<DanhMucDichVu>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CategoryListActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupAdapter(List<DanhMucDichVu> list) {
        adapter = new CategoryAdapter(this, list, new CategoryAdapter.OnItemClickListener() {
            @Override
            public void onEdit(DanhMucDichVu item) {
                Intent intent = new Intent(CategoryListActivity.this, CategoryEditorActivity.class);
                intent.putExtra("id", item.get_id());
                intent.putExtra("name", item.getTen_dich_vu());
                intent.putExtra("type", item.getLoai_dich_vu());
                intent.putExtra("price", item.getGia_co_ban());
                intent.putExtra("unit", item.getDon_vi());
                startActivity(intent);
            }

            @Override
            public void onDelete(String id, String name) {
                new AlertDialog.Builder(CategoryListActivity.this)
                        .setTitle("Xóa danh mục")
                        .setMessage("Bạn có chắc muốn xóa '" + name + "' không?")
                        .setPositiveButton("Xóa", (dialog, which) -> deleteItem(id))
                        .setNegativeButton("Hủy", null).show();
            }
        });
        rcvList.setAdapter(adapter);
    }

    private void deleteItem(String id) {
        apiService.deleteDanhMucDichVu(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(CategoryListActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                    loadData();
                } else {
                    Toast.makeText(CategoryListActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(CategoryListActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filterData(String text) {
        List<DanhMucDichVu> filtered = new ArrayList<>();
        for (DanhMucDichVu item : originalList) {
            if (item.getTen_dich_vu().toLowerCase().contains(text.toLowerCase())) {
                filtered.add(item);
            }
        }
        if (adapter != null) adapter.updateList(filtered);
    }
}