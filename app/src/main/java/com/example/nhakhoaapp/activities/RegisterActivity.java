package com.example.nhakhoaapp.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;

public class RegisterActivity extends AppCompatActivity {

    // Khai báo các component
    private EditText fullNameEditText;
    private EditText emailEditText;
    private EditText passwordEditText;
    private EditText confirmPasswordEditText;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 1. Ánh xạ các component từ XML
        fullNameEditText = findViewById(R.id.et_full_name);
        emailEditText = findViewById(R.id.et_email_register);
        passwordEditText = findViewById(R.id.et_password_register);
        confirmPasswordEditText = findViewById(R.id.et_confirm_password);
        registerButton = findViewById(R.id.btn_register);

        // 2. Thiết lập Listener cho nút Đăng ký
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleRegistration();
            }
        });
    }

    /**
     * Hàm xử lý logic khi nút Đăng ký được nhấn
     */
    private void handleRegistration() {
        String fullName = fullNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();
        String confirmPassword = confirmPasswordEditText.getText().toString().trim();

        // Kiểm tra validation cơ bản
        if (fullName.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            Toast.makeText(this, "Vui lòng điền đầy đủ thông tin.", Toast.LENGTH_LONG).show();
            return;
        }

        if (!password.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu và Xác nhận Mật khẩu không khớp.", Toast.LENGTH_LONG).show();
            return;
        }

        // TODO: Thực hiện Logic Đăng ký (Lưu vào CSDL)
        // Ví dụ: databaseHelper.registerUser(fullName, email, password);

        Toast.makeText(this, "Đăng ký thành công! Vui lòng Đăng nhập.", Toast.LENGTH_LONG).show();
        // Sau khi đăng ký thành công, thường chuyển về màn hình Login hoặc Main
        // finish();
    }
}