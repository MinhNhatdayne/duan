package com.example.nhakhoaapp.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.request.RegisterRequest;
import com.example.nhakhoaapp.models.response.LoginResponse;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullname, etEmail, etPassword, etPhone, etDob;
    private RadioGroup rgGender;
    private Button btnRegister;
    private TextView btnBackToLogin;

    // Biến Calendar để lưu ngày người dùng chọn
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
        setupDatePicker(); // Cài đặt sự kiện hiển thị lịch

        btnRegister.setOnClickListener(v -> handleRegister());
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void initViews() {
        etFullname = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob);
        rgGender = findViewById(R.id.rg_gender);
        btnRegister = findViewById(R.id.btn_register);
        btnBackToLogin = findViewById(R.id.btn_back_login);
    }

    // --- CẤU HÌNH DATE PICKER (LỊCH) ---
    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, month);
                myCalendar.set(Calendar.DAY_OF_MONTH, day);
                updateLabel(); // Cập nhật text hiển thị
            }
        };

        // Khi bấm vào ô ngày sinh thì hiện lịch
        etDob.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(RegisterActivity.this, date,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    // Hiển thị ngày lên màn hình theo định dạng Việt Nam (dd/MM/yyyy)
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat dateFormat = new SimpleDateFormat(myFormat, Locale.US);
        etDob.setText(dateFormat.format(myCalendar.getTime()));
    }

    // --- HÀM CHUYỂN ĐỔI NGÀY ĐỂ GỬI LÊN SERVER ---
    // Chuyển từ "30/11/2005" -> "2005-11-30" để Server hiểu
    private String convertDateToISO(String dateStr) {
        try {
            // Định dạng đầu vào (khớp với hiển thị trên màn hình)
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);

            // Định dạng đầu ra (Chuẩn SQL/MongoDB)
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            Date date = inputFormat.parse(dateStr);
            return outputFormat.format(date);
        } catch (Exception e) {
            e.printStackTrace();
            return dateStr; // Nếu lỗi thì trả về nguyên gốc
        }
    }

    // --- XỬ LÝ ĐĂNG KÝ ---
    private void handleRegister() {
        String name = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dobRaw = etDob.getText().toString().trim(); // Lấy ngày thô (dd/MM/yyyy)

        // Lấy giới tính
        String gender = "Khác";
        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton rb = findViewById(selectedId);
            gender = rb.getText().toString();
        }

        if (name.isEmpty() || email.isEmpty() || pass.isEmpty() || dobRaw.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        // BƯỚC QUAN TRỌNG: Chuyển đổi ngày trước khi gửi
        String dobFormatted = convertDateToISO(dobRaw);

        // Tạo request với ngày đã format
        RegisterRequest request = new RegisterRequest(name, email, pass, phone, gender, dobFormatted);

        btnRegister.setEnabled(false);
        btnRegister.setText("Đang xử lý...");

        ApiClient.getApiService().register(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnRegister.setEnabled(true);
                btnRegister.setText("ĐĂNG KÝ");

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();
                        finish(); // Quay lại màn hình đăng nhập
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Hiển thị mã lỗi nếu có (ví dụ 500)
                    Toast.makeText(RegisterActivity.this, "Lỗi đăng ký: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText("ĐĂNG KÝ");
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}