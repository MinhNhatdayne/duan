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
import com.example.nhakhoaapp.activities_customer.PatientEditorActivity;
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
    private List<BenhNhan> originalList;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private Spinner spinnerGender;

    private String currentSearchText = "";
    private String currentGenderFilter = "Tất cả";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_list);

        initViews();
        setupSpinner();

        rcvPatient.setLayoutManager(new LinearLayoutManager(this));
        originalList = new ArrayList<>();

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

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Quản lý Bệnh nhân");
        }
        toolbar.setNavigationOnClickListener(v -> finish());
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Tìm tên bệnh nhân...");

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) { return false; }
            @Override
            public boolean onQueryTextChange(String newText) {
                currentSearchText = newText;
                filterData();
                return true;
            }
        });
        return true;
    }

    private void filterData() {
        if (originalList == null) return;
        List<BenhNhan> filtered = new ArrayList<>();

        for (BenhNhan item : originalList) {
            // Lọc tên
            boolean matchName = item.getHo_ten().toLowerCase().contains(currentSearchText.toLowerCase());

            // Lọc giới tính
            boolean matchGender = currentGenderFilter.equals("Tất cả") ||
                    (item.getGioi_tinh() != null && item.getGioi_tinh().equalsIgnoreCase(currentGenderFilter));

            if (matchName && matchGender) filtered.add(item);
        }

        if (adapter != null) adapter.updateList(filtered);
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }

    private void loadData() {
        progressBar.setVisibility(View.VISIBLE);
        // Lưu ý: Gọi đúng API getAllBenhNhan()
        ApiClient.getApiService().getAllBenhNhan().enqueue(new Callback<List<BenhNhan>>() {
            @Override
            public void onResponse(Call<List<BenhNhan>> call, Response<List<BenhNhan>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    originalList = response.body();
                    filterData(); // Lọc lại dữ liệu theo trạng thái hiện tại

                    if (adapter == null) {
                        adapter = new BenhNhanAdapter(PatientListActivity.this, originalList, new BenhNhanAdapter.OnItemClickListener() {
                            @Override
                            public void onEdit(BenhNhan bn) {
                                Intent intent = new Intent(PatientListActivity.this, PatientEditorActivity.class);
                                intent.putExtra("id", bn.get_id());
                                intent.putExtra("name", bn.getHo_ten());
                                intent.putExtra("dob", bn.getNgay_sinh()); // SỬA: Key là dob (date of birth)
                                intent.putExtra("gender", bn.getGioi_tinh());
                                intent.putExtra("phone", bn.getSo_dien_thoai());
                                intent.putExtra("address", bn.getDia_chi());
                                intent.putExtra("email", bn.getEmail());
                                startActivity(intent);
                            }
                            @Override
                            public void onDelete(String id, String name) { confirmDelete(id, name); }
                        });
                        rcvPatient.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<BenhNhan>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(PatientListActivity.this, "Lỗi tải: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete(String id, String name) {
        new AlertDialog.Builder(this).setTitle("Xóa").setMessage("Xóa bệnh nhân " + name + "?")
                .setPositiveButton("Xóa", (d, w) -> {
                    ApiClient.getApiService().deleteBenhNhan(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(PatientListActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                                loadData();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {}
                    });
                }).setNegativeButton("Hủy", null).show();
    }
}