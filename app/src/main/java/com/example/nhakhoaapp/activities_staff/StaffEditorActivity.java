package com.example.nhakhoaapp.activities_staff;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffEditorActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtPosition, edtPhone, edtEmail, edtSalary, edtAddress;
    private Button btnSave;
    private ImageView btnBack; // Thay Toolbar bằng ImageView
    private TextView tvHeaderTitle;
    private String nhanVienId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_editor);

        initViews();
        checkMode();

        btnSave.setOnClickListener(v -> saveNhanVien());
    }

    private void initViews() {
        // --- XỬ LÝ HEADER THỦ CÔNG ---
        btnBack = findViewById(R.id.btnBackCustom);
        tvHeaderTitle = findViewById(R.id.tvHeaderTitle);

        // Bắt sự kiện Click cho nút Back: Đóng Activity
        btnBack.setOnClickListener(v -> finish());
        // ------------------------------

        edtName = findViewById(R.id.edtName);
        edtPosition = findViewById(R.id.edtPosition);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtSalary = findViewById(R.id.edtSalary);
        edtAddress = findViewById(R.id.edtAddress);
        btnSave = findViewById(R.id.btnSave);
    }

    private void checkMode() {
        if (getIntent().hasExtra("id")) {
            // Chế độ Sửa
            nhanVienId = getIntent().getStringExtra("id");
            tvHeaderTitle.setText("Cập nhật Nhân viên"); // Sửa tiêu đề

            // Điền dữ liệu cũ
            edtName.setText(getIntent().getStringExtra("name"));
            edtPosition.setText(getIntent().getStringExtra("position"));
            edtPhone.setText(getIntent().getStringExtra("phone"));
            edtEmail.setText(getIntent().getStringExtra("email"));
            edtAddress.setText(getIntent().getStringExtra("address"));
            edtSalary.setText(String.valueOf(getIntent().getDoubleExtra("salary", 0)));
        } else {
            // Chế độ Thêm
            tvHeaderTitle.setText("Thêm Nhân viên");
        }
    }

    private void saveNhanVien() {
        String name = edtName.getText().toString().trim();
        String position = edtPosition.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String salaryStr = edtSalary.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        double salary = salaryStr.isEmpty() ? 0 : Double.parseDouble(salaryStr);

        // Tạo object NhanVien
        NhanVien nv = new NhanVien(name, position, "01/01/2000", address, phone, email, "123456", salary);

        ApiService apiService = ApiClient.getApiService();

        if (nhanVienId == null) {
            // === GỌI API THÊM MỚI ===
            apiService.createNhanVien(nv).enqueue(new Callback<NhanVien>() {
                @Override
                public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(StaffEditorActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(StaffEditorActivity.this, "Thêm thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<NhanVien> call, Throwable t) {
                    Toast.makeText(StaffEditorActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // === GỌI API CẬP NHẬT ===
            apiService.updateNhanVien(nhanVienId, nv).enqueue(new Callback<NhanVien>() {
                @Override
                public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(StaffEditorActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(StaffEditorActivity.this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<NhanVien> call, Throwable t) {
                    Toast.makeText(StaffEditorActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}