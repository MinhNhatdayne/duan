package com.example.nhakhoaapp.activities_staff;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditStaffProfileActivity extends AppCompatActivity {

    // Views theo layout mới (Material Components)
    private TextInputEditText edtName, edtPhone, edtDob, edtAddress;
    private AutoCompleteTextView spGender;
    private Button btnSave;
    private ImageView imgAvatarEdit;
    private Toolbar toolbar;

    // Data
    private String userId;
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_staff_profile); // Đảm bảo tên file XML đúng

        initViews();
        setupToolbar();
//        setupGenderDropdown();
        setupDatePicker();

        loadCurrentData();

        btnSave.setOnClickListener(v -> saveChanges());
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);

        edtName = findViewById(R.id.edt_name);
        edtPhone = findViewById(R.id.edt_phone);
        edtDob = findViewById(R.id.edt_dob);
        edtAddress = findViewById(R.id.edt_address);
        spGender = findViewById(R.id.sp_gender);
        btnSave = findViewById(R.id.btn_save);
        imgAvatarEdit = findViewById(R.id.img_avatar_edit); // Icon ảnh đại diện

        // Lấy ID User từ SharedPreferences
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        userId = prefs.getString("USER_ID", null);
    }

    private void setupToolbar() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(true);
        }
    }

    // Cấu hình Dropdown Giới tính (Material Exposed Dropdown)
    private void setupGenderDropdown() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        spGender.setAdapter(adapter);
        // Mặc định không hiện bàn phím khi bấm vào
        spGender.setKeyListener(null);
    }

    // Cấu hình DatePicker cho ngày sinh
    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();
        };

        edtDob.setOnClickListener(v -> {
            // Cố gắng parse ngày hiện tại để set mặc định cho lịch
            try {
                // Giả sử định dạng hiển thị là dd/MM/yyyy
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                if (edtDob.getText() != null && !edtDob.getText().toString().isEmpty()) {
                    myCalendar.setTime(sdf.parse(edtDob.getText().toString()));
                }
            } catch (Exception e) { e.printStackTrace(); }

            new DatePickerDialog(EditStaffProfileActivity.this, date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        // Định dạng hiển thị: dd/MM/yyyy (Việt Nam)
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDob.setText(sdf.format(myCalendar.getTime()));
    }

    private void loadCurrentData() {
        if (getIntent() != null) {
            edtName.setText(getIntent().getStringExtra("NAME"));
            edtPhone.setText(getIntent().getStringExtra("PHONE"));
            edtDob.setText(getIntent().getStringExtra("DOB"));
            edtAddress.setText(getIntent().getStringExtra("ADDRESS"));

            String gender = getIntent().getStringExtra("GENDER");
            if (gender != null) spGender.setText(gender, false); // false để không hiện dropdown list ngay lập tức
        }
    }

    private void saveChanges() {
        String name = edtName.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String dob = edtDob.getText().toString().trim();
        String gender = spGender.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên và SĐT", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo object cập nhật
        NhanVien updatedStaff = new NhanVien();
        updatedStaff.setHo_ten(name);
        updatedStaff.setSo_dien_thoai(phone);

        // Lưu ý: Nếu Server cần định dạng yyyy-MM-dd thì bạn cần convert lại 'dob' trước khi gửi
        // Ở đây giả định server nhận chuỗi thô hoặc bạn đã xử lý ở backend
        updatedStaff.setNgay_sinh(dob);
        updatedStaff.setDia_chi(address);

        // Gọi API Update
        ApiClient.getApiService().updateNhanVien(userId, updatedStaff).enqueue(new Callback<NhanVien>() {
            @Override
            public void onResponse(Call<NhanVien> call, Response<NhanVien> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(EditStaffProfileActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Quay về màn hình trước
                } else {
                    Toast.makeText(EditStaffProfileActivity.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<NhanVien> call, Throwable t) {
                Toast.makeText(EditStaffProfileActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}