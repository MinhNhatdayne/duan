package com.example.nhakhoaapp.activities_customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtPhone, edtDob, edtAddress;
    private AutoCompleteTextView spGender;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        initViews();
        setupGenderDropdown();
        loadCurrentData();

        btnSave.setOnClickListener(v -> saveProfile());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish()); // Nút back

        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtDob = findViewById(R.id.edt_dob);
        edtAddress = findViewById(R.id.edt_address);
        spGender = findViewById(R.id.sp_gender);
        btnSave = findViewById(R.id.btn_save);
    }

    private void setupGenderDropdown() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        spGender.setAdapter(adapter);
    }

    private void loadCurrentData() {
        // Lấy dữ liệu được truyền từ ProfileActivity
        if (getIntent() != null) {
            edtName.setText(getIntent().getStringExtra("NAME"));
            edtPhone.setText(getIntent().getStringExtra("PHONE"));
            edtDob.setText(getIntent().getStringExtra("DOB"));
            edtAddress.setText(getIntent().getStringExtra("ADDRESS"));
            spGender.setText(getIntent().getStringExtra("GENDER"), false);
        }
    }

    private void saveProfile() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        String gender = spGender.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên và Số điện thoại", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lấy ID người dùng
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);

        if (userId == null) {
            Toast.makeText(this, "Lỗi xác thực", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo object BenhNhan mới (Chỉ chứa thông tin cần update)
        // Lưu ý: Password để null hoặc chuỗi rỗng để Server biết không update pass
        BenhNhan updatedUser = new BenhNhan();
        updatedUser.setHo_ten(name);
        updatedUser.setSo_dien_thoai(phone);
        updatedUser.setNgay_sinh(dob);
        updatedUser.setGioi_tinh(gender);
        updatedUser.setDia_chi(address);

        // Gọi API cập nhật
        ApiClient.getApiService().updateBenhNhan(userId, updatedUser).enqueue(new Callback<BenhNhan>() {
            @Override
            public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay lại màn hình Profile và nó sẽ tự load lại dữ liệu mới
                } else {
                    Toast.makeText(EditProfileActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<BenhNhan> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}