package com.example.nhakhoaapp.activities_customer;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientEditorActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtDob, edtPhone, edtEmail, edtAddress;
    private AutoCompleteTextView spGender;
    private Button btnSave;
    private String patientId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_editor);

        initViews();
        setupGenderDropdown();
        checkMode();

        btnSave.setOnClickListener(v -> savePatient());
    }

    private void initViews() {
        // Nút Back thủ công
        ImageView btnBack = findViewById(R.id.btnBackCustom);
        btnBack.setOnClickListener(v -> finish());

        edtName = findViewById(R.id.edtName);
        edtDob = findViewById(R.id.edtDob); // Ánh xạ trường Ngày sinh
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        spGender = findViewById(R.id.spGender);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupGenderDropdown() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        spGender.setAdapter(adapter);
    }

    private void checkMode() {
        if (getIntent().hasExtra("id")) {
            // Chế độ Sửa
            patientId = getIntent().getStringExtra("id");
            TextView tvTitle = findViewById(R.id.tvHeaderTitle);
            tvTitle.setText("Cập nhật Bệnh nhân");

            // Điền dữ liệu cũ
            edtName.setText(getIntent().getStringExtra("name"));
            edtDob.setText(getIntent().getStringExtra("dob")); // Lấy ngày sinh
            edtPhone.setText(getIntent().getStringExtra("phone"));
            edtEmail.setText(getIntent().getStringExtra("email"));
            edtAddress.setText(getIntent().getStringExtra("address"));
            spGender.setText(getIntent().getStringExtra("gender"), false);
        }
    }

    private void savePatient() {
        String name = edtName.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String gender = spGender.getText().toString();
        String email = edtEmail.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        // Mật khẩu mặc định khi tạo mới (Backend xử lý mã hóa nếu cần)
        String defaultPassword = "123456";

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và SĐT!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo object theo Constructor của model bạn cung cấp
        BenhNhan bn = new BenhNhan(name, dob, gender, address, phone, email, defaultPassword);

        ApiService api = ApiClient.getApiService();

        if (patientId == null) {
            // === THÊM MỚI ===
            api.createBenhNhan(bn).enqueue(new Callback<BenhNhan>() {
                @Override
                public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PatientEditorActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PatientEditorActivity.this, "Lỗi thêm mới", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<BenhNhan> call, Throwable t) {
                    Toast.makeText(PatientEditorActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // === CẬP NHẬT ===
            api.updateBenhNhan(patientId, bn).enqueue(new Callback<BenhNhan>() {
                @Override
                public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PatientEditorActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PatientEditorActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<BenhNhan> call, Throwable t) {
                    Toast.makeText(PatientEditorActivity.this, "Lỗi mạng: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}