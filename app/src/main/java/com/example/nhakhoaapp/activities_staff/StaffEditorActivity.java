package com.example.nhakhoaapp.activities_staff;

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
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StaffEditorActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtPhone, edtEmail, edtSalary, edtAddress;
    private AutoCompleteTextView spPosition;
    private Button btnSave;
    private String staffId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staff_editor);

        initViews();
        setupPositionDropdown();
        checkMode();

        btnSave.setOnClickListener(v -> saveStaff());
    }

    private void initViews() {
        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish()); // Handle Back button click

        edtName = findViewById(R.id.edtName);
        spPosition = findViewById(R.id.spPosition); // Dropdown for position
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtSalary = findViewById(R.id.edtSalary);
        edtAddress = findViewById(R.id.edtAddress);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupPositionDropdown() {
        // Populate the dropdown with predefined roles
        String[] roles = {"Bác sĩ", "Y tá", "Lễ tân", "Quản lý", "Bảo vệ"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, roles);
        spPosition.setAdapter(adapter);
    }

    private void checkMode() {
        if (getIntent().hasExtra("id")) {
            // Edit Mode
            staffId = getIntent().getStringExtra("id");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Cập nhật Nhân viên");
            }
            btnSave.setText("LƯU THAY ĐỔI");

            // Populate fields with existing data
            edtName.setText(getIntent().getStringExtra("name"));
            spPosition.setText(getIntent().getStringExtra("position"), false); // 'false' prevents filtering
            edtPhone.setText(getIntent().getStringExtra("phone"));
            edtEmail.setText(getIntent().getStringExtra("email"));
            edtAddress.setText(getIntent().getStringExtra("address"));

            // Handle salary display (convert double to string)
            double salary = getIntent().getDoubleExtra("salary", 0);
            if (salary > 0) {
                edtSalary.setText(String.valueOf((long)salary)); // Display as integer if needed
            }
        }
    }

    private void saveStaff() {
        String name = edtName.getText().toString().trim();
        String position = spPosition.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();
        String salaryStr = edtSalary.getText().toString().trim();

        // Basic Validation
        if (name.isEmpty() || phone.isEmpty() || position.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên, chức vụ và số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }

        double salary = salaryStr.isEmpty() ? 0 : Double.parseDouble(salaryStr);
        String defaultPassword = "123456"; // Default password for new staff
        String defaultDob = "01/01/2000"; // Placeholder DOB if not captured

        // Create NhanVien object
        NhanVien nv = new NhanVien(name, position, defaultDob, address, phone, email, defaultPassword, salary);

        ApiService api = ApiClient.getApiService();
        btnSave.setEnabled(false); // Disable button to prevent double submission
        btnSave.setText("Đang xử lý...");

        if (staffId == null) {
            // === CREATE NEW ===
            api.createNhanVien(nv).enqueue(new Callback<NhanVien>() {
                @Override
                public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU NHÂN VIÊN");
                    if (response.isSuccessful()) {
                        Toast.makeText(StaffEditorActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(StaffEditorActivity.this, "Lỗi thêm mới: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<NhanVien> call, Throwable t) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU NHÂN VIÊN");
                    Toast.makeText(StaffEditorActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // === UPDATE EXISTING ===
            api.updateNhanVien(staffId, nv).enqueue(new Callback<NhanVien>() {
                @Override
                public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU THAY ĐỔI");
                    if (response.isSuccessful()) {
                        Toast.makeText(StaffEditorActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(StaffEditorActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<NhanVien> call, Throwable t) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU THAY ĐỔI");
                    Toast.makeText(StaffEditorActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}