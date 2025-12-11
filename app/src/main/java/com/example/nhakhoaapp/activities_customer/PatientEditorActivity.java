package com.example.nhakhoaapp.activities_customer; // Hoặc activities_staff tùy cấu trúc

import android.app.DatePickerDialog;
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
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientEditorActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtDob, edtPhone, edtEmail, edtAddress;
    private AutoCompleteTextView spGender;
    private Button btnSave;
    private String patientId = null;
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_editor);

        initViews();
        setupGenderDropdown();
        setupDatePicker(); // Cấu hình lịch
        checkMode();

        btnSave.setOnClickListener(v -> savePatient());
    }

    private void initViews() {
        // Cấu hình Toolbar thay vì ImageView Back
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        edtName = findViewById(R.id.edtName);
        edtDob = findViewById(R.id.edtDob);
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

    // Cấu hình chọn ngày sinh
    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            String myFormat = "dd/MM/yyyy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            edtDob.setText(sdf.format(myCalendar.getTime()));
        };

        edtDob.setOnClickListener(v -> {
            new DatePickerDialog(PatientEditorActivity.this, date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void checkMode() {
        if (getIntent().hasExtra("id")) {
            patientId = getIntent().getStringExtra("id");
            if (getSupportActionBar() != null) {
                getSupportActionBar().setTitle("Cập nhật Bệnh nhân");
            }
            // Điền dữ liệu cũ
            edtName.setText(getIntent().getStringExtra("name"));
            edtDob.setText(getIntent().getStringExtra("dob"));
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

        // Mật khẩu mặc định
        String defaultPassword = "123456";

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và SĐT!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo object (Lưu ý: Format ngày gửi lên server nên là yyyy-MM-dd nếu backend yêu cầu)
        // Ở đây đang gửi dd/MM/yyyy, nếu server lỗi thì cần thêm hàm convert
        BenhNhan bn = new BenhNhan(name, dob, gender, address, phone, email, defaultPassword);

        ApiService api = ApiClient.getApiService();

        if (patientId == null) {
            // THÊM MỚI
            api.createBenhNhan(bn).enqueue(new Callback<BenhNhan>() {
                @Override
                public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PatientEditorActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PatientEditorActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<BenhNhan> call, Throwable t) {
                    Toast.makeText(PatientEditorActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // CẬP NHẬT
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
                    Toast.makeText(PatientEditorActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}