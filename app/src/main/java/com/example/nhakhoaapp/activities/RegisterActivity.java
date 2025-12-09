//package com.example.nhakhoaapp.activities;
//
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import com.example.nhakhoaapp.R;
//
//public class RegisterActivity extends AppCompatActivity {
//
//    // Khai báo các component
//    private EditText fullNameEditText;
//    private EditText emailEditText;
//    private EditText passwordEditText;
//    private EditText confirmPasswordEditText;
//    private Button registerButton;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_register);
//
//        // 1. Ánh xạ các component từ XML
//        fullNameEditText = findViewById(R.id.et_full_name);
//        emailEditText = findViewById(R.id.et_email_register);
//        passwordEditText = findViewById(R.id.et_password_register);
//        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
//        registerButton = findViewById(R.id.btn_register);
//
//        // 2. Thiết lập Listener cho nút Đăng ký
//        registerButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                handleRegistration();
//            }
//        });
//    }
//
//    /**
//     * Hàm xử lý logic khi nút Đăng ký được nhấn
//     */
//    private void handleRegistration() {
//        String fullName = fullNameEditText.getText().toString().trim();
//        String email = emailEditText.getText().toString().trim();
//        String password = passwordEditText.getText().toString().trim();
//        String confirmPassword = confirmPasswordEditText.getText().toString().trim();
//
//        // Kiểm tra validation cơ bản
//        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
//            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        if (!password.equals(confirmPassword)) {
//            Toast.makeText(this, "Mật khẩu và Xác nhận Mật khẩu không khớp.", Toast.LENGTH_LONG).show();
//            return;
//        }
//
//        // TODO: Thực hiện Logic Đăng ký (Lưu vào CSDL)
//        // Ví dụ: databaseHelper.registerUser(fullName, email, password);
//
//        Toast.makeText(this, "Đăng ký thành công! Vui lòng Đăng nhập.", Toast.LENGTH_LONG).show();
//        // Sau khi đăng ký thành công, thường chuyển về màn hình Login hoặc Main
//        // finish();
//    }
//}
package com.example.nhakhoaapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etFullname, etEmail, etPassword, etPhone, etDob;
    private RadioGroup rgGender;
    private Button btnRegister;
    private TextView btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register); // Đảm bảo bạn đã tạo layout này

        // 1. Ánh xạ View
        etFullname = findViewById(R.id.et_fullname);
        etEmail = findViewById(R.id.et_email);
        etPassword = findViewById(R.id.et_password);
        etPhone = findViewById(R.id.et_phone);
        etDob = findViewById(R.id.et_dob); // Nhập ngày sinh (ví dụ dạng text 1990-01-01)
        rgGender = findViewById(R.id.rg_gender);
        btnRegister = findViewById(R.id.btn_register);
        btnBackToLogin = findViewById(R.id.btn_back_login);

        // 2. Xử lý nút Đăng ký
        btnRegister.setOnClickListener(v -> handleRegister());

        // 3. Quay lại đăng nhập
        btnBackToLogin.setOnClickListener(v -> finish());
    }

    private void handleRegister() {
        String name = etFullname.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String pass = etPassword.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String dob = etDob.getText().toString().trim();

        // Lấy giới tính
        String gender = "Khác";
        int selectedId = rgGender.getCheckedRadioButtonId();
        if (selectedId != -1) {
            RadioButton rb = findViewById(selectedId);
            gender = rb.getText().toString();
        }

        // Validate cơ bản
        if (name.isEmpty() || email.isEmpty() || pass.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
            return;
        }

        // Tạo request
        RegisterRequest request = new RegisterRequest(name, email, pass, phone, gender, dob);

        // Gọi API
        btnRegister.setEnabled(false);
        btnRegister.setText("Đang đăng ký...");

        ApiClient.getApiService().register(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnRegister.setEnabled(true);
                btnRegister.setText("Đăng ký");

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(RegisterActivity.this, "Đăng ký thành công! Vui lòng đăng nhập.", Toast.LENGTH_LONG).show();

                        // Chuyển về màn hình đăng nhập
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Email có thể đã tồn tại hoặc lỗi server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnRegister.setEnabled(true);
                btnRegister.setText("Đăng ký");
                Toast.makeText(RegisterActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}