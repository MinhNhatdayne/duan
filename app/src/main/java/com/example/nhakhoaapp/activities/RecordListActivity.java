package com.example.nhakhoaapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapter.RecordAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.HoSoBenhAn;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordListActivity extends AppCompatActivity {

    private RecyclerView rcvRecord;
    private RecordAdapter adapter;
    private List<HoSoBenhAn> originalList;
    private ProgressBar progressBar;
    private FloatingActionButton fabAdd;
    private String currentSearchText = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);

        initViews();
        rcvRecord.setLayoutManager(new LinearLayoutManager(this));
        originalList = new ArrayList<>();

        fabAdd.setOnClickListener(v -> {
            Intent intent = new Intent(this, RecordEditorActivity.class);
            startActivity(intent);
        });
    }

    private void initViews() {
        rcvRecord = findViewById(R.id.rcvRecord);
        progressBar = findViewById(R.id.progressBar);
        fabAdd = findViewById(R.id.fabAddRecord);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setQueryHint("Tìm theo chẩn đoán...");

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
        List<HoSoBenhAn> filtered = new ArrayList<>();
        for (HoSoBenhAn item : originalList) {
            // Lọc theo Chẩn đoán
            if (item.getChan_doan() != null && item.getChan_doan().toLowerCase().contains(currentSearchText.toLowerCase())) {
                filtered.add(item);
            }
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
        ApiClient.getApiService().getAllHoSoBenhAn().enqueue(new Callback<List<HoSoBenhAn>>() {
            @Override
            public void onResponse(Call<List<HoSoBenhAn>> call, Response<List<HoSoBenhAn>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    originalList = response.body();
                    filterData();

                    if (adapter == null) {
                        adapter = new RecordAdapter(RecordListActivity.this, originalList, new RecordAdapter.OnItemClickListener() {
                            @Override
                            public void onEdit(HoSoBenhAn rec) {
                                Intent intent = new Intent(RecordListActivity.this, RecordEditorActivity.class);
                                intent.putExtra("id", rec.get_id());
                                intent.putExtra("patientId", rec.getId_benh_nhan());
                                intent.putExtra("doctorId", rec.getId_bac_si_kham());
                                intent.putExtra("date", rec.getNgay_kham());
                                intent.putExtra("diagnosis", rec.getChan_doan());
                                intent.putExtra("result", rec.getKet_qua_xet_nghiem());
                                intent.putExtra("img", rec.getImg());
                                startActivity(intent);
                            }
                            @Override
                            public void onDelete(String id) { confirmDelete(id); }
                        });
                        rcvRecord.setAdapter(adapter);
                    }
                }
            }
            @Override
            public void onFailure(Call<List<HoSoBenhAn>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(RecordListActivity.this, "Lỗi tải dữ liệu", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void confirmDelete(String id) {
        new AlertDialog.Builder(this).setTitle("Xóa").setMessage("Xóa hồ sơ này?")
                .setPositiveButton("Xóa", (d, w) -> {
                    ApiClient.getApiService().deleteHoSoBenhAn(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if(response.isSuccessful()) { loadData(); Toast.makeText(RecordListActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show(); }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {}
                    });
                }).setNegativeButton("Hủy", null).show();
    }
}