package com.example.nhakhoaapp.activities;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.DanhMucDichVu;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryEditorActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtPrice;
    private AutoCompleteTextView edtType, edtUnit;
    private Button btnSave;
    private String itemId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_editor);

        initViews();
        setupDropdowns();
        checkMode();

        btnSave.setOnClickListener(v -> saveItem());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        edtName = findViewById(R.id.edtName);
        edtType = findViewById(R.id.edtType);
        edtPrice = findViewById(R.id.edtPrice);
        edtUnit = findViewById(R.id.edtUnit);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupDropdowns() {
        String[] types = {"Tổng quát", "Thẩm mỹ", "Phẫu thuật", "Chỉnh nha", "Phục hình"};
        edtType.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, types));

        String[] units = {"Lần", "Răng", "Hàm", "Liệu trình", "Trụ"};
        edtUnit.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, units));
    }

    private void checkMode() {
        if (getIntent().hasExtra("id")) {
            itemId = getIntent().getStringExtra("id");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Cập nhật Danh mục");
            }

            edtName.setText(getIntent().getStringExtra("name"));
            edtType.setText(getIntent().getStringExtra("type"), false); // false để không filter dropdown
            edtPrice.setText(String.valueOf(getIntent().getIntExtra("price", 0)));
            edtUnit.setText(getIntent().getStringExtra("unit"), false);
        } else {
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Thêm Danh mục mới");
            }
        }
    }

    private void saveItem() {
        String name = edtName.getText().toString().trim();
        String type = edtType.getText().toString().trim();
        String priceStr = edtPrice.getText().toString().trim();
        String unit = edtUnit.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và giá!", Toast.LENGTH_SHORT).show();
            return;
        }

        int price;
        try {
            price = Integer.parseInt(priceStr);
        } catch (NumberFormatException e) {
            Toast.makeText(this, "Giá phải là số!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo object
        DanhMucDichVu newItem = new DanhMucDichVu();
        newItem.setTen_dich_vu(name);
        newItem.setLoai_dich_vu(type);
        newItem.setGia_co_ban(price);
        newItem.setDon_vi(unit);

        btnSave.setEnabled(false);
        btnSave.setText("Đang xử lý...");

        ApiService api = ApiClient.getApiService();

        if (itemId == null) {
            // CREATE
            api.createDanhMucDichVu(newItem).enqueue(new Callback<DanhMucDichVu>() {
                @Override
                public void onResponse(Call<DanhMucDichVu> call, Response<DanhMucDichVu> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CategoryEditorActivity.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CategoryEditorActivity.this, "Lỗi thêm mới", Toast.LENGTH_SHORT).show();
                        btnSave.setEnabled(true);
                        btnSave.setText("LƯU DANH MỤC");
                    }
                }
                @Override
                public void onFailure(Call<DanhMucDichVu> call, Throwable t) {
                    handleFailure();
                }
            });
        } else {
            // UPDATE
            api.updateDanhMucDichVu(itemId, newItem).enqueue(new Callback<DanhMucDichVu>() {
                @Override
                public void onResponse(Call<DanhMucDichVu> call, Response<DanhMucDichVu> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(CategoryEditorActivity.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(CategoryEditorActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                        btnSave.setEnabled(true);
                        btnSave.setText("LƯU DANH MỤC");
                    }
                }
                @Override
                public void onFailure(Call<DanhMucDichVu> call, Throwable t) {
                    handleFailure();
                }
            });
        }
    }

    private void handleFailure() {
        Toast.makeText(CategoryEditorActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
        btnSave.setEnabled(true);
        btnSave.setText("LƯU DANH MỤC");
    }
}