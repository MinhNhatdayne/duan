package com.example.nhakhoaapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Đảm bảo tên layout khớp với file XML đã sửa
        setContentView(R.layout.activity_login); 

        // 1. Ánh xạ các View từ layout
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        // 2. Xử lý sự kiện nút Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // 3. Xử lý sự kiện Quên mật khẩu
        tvForgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(LoginActivity.this, "Chuyển đến màn hình Quên mật khẩu", Toast.LENGTH_SHORT).show();
                // TODO: Thêm Intent để chuyển đến ForgotPasswordActivity
            }
        });

        // 4. Xử lý sự kiện Đăng ký
        tvRegisterLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình RegisterActivity
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * Xử lý logic đăng nhập: kiểm tra dữ liệu và gọi API/Database.
     */
    private void handleLogin() {
        String username = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ Tên đăng nhập và Mật khẩu.", Toast.LENGTH_SHORT).show();
            return;
        }

        // TODO: Thực hiện logic kiểm tra đăng nhập (ví dụ: gọi Firebase, Room Database, hoặc API)
        
        // --- GIẢ LẬP ĐĂNG NHẬP THÀNH CÔNG ---
        if (username.equals("test") && password.equals("123")) {
            Toast.makeText(this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();
            
            // Chuyển sang màn hình Dashboard (Trang chủ)
            Intent intent = new Intent(LoginActivity.this, DashboardActivity.class);
            // Đặt cờ để xóa hết Activity trước đó và không cho quay lại màn Login
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); 
            startActivity(intent);
            finish(); 
        } else {
            Toast.makeText(this, "Tên đăng nhập hoặc Mật khẩu không đúng.", Toast.LENGTH_SHORT).show();
        }
    }
}