package com.example.nhakhoaapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities_customer.DashboardActivity;
import com.example.nhakhoaapp.activities_staff.StaffDashboardActivity;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.request.LoginRequest;
import com.example.nhakhoaapp.models.response.LoginResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegisterLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegisterLink = findViewById(R.id.tv_register_link);

        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });
    }

    private void handleLogin() {
        String email = etUsername.getText().toString().trim();
        String password = etPassword.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Email và Mật khẩu.", Toast.LENGTH_SHORT).show();
            return;
        }

        LoginRequest request = new LoginRequest(email, password);

        btnLogin.setEnabled(false);
        btnLogin.setText("Đang xử lý...");

        ApiClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");

                if (response.isSuccessful() && response.body() != null) {

                    LoginResponse loginResponse = response.body();

                    if (loginResponse.isSuccess()) {

                        // Lưu session
                        saveUserSession(loginResponse.getData());

                        String role = loginResponse.getRole();
                        if (role == null) role = "PATIENT";

                        Intent intent;

                        switch (role) {
                            case "MANAGER":
                            case "DOCTOR":
                                intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                                break;

                            case "PATIENT":
                            default:
                                intent = new Intent(LoginActivity.this, DashboardActivity.class);

                                // ⭐⭐ Gửi TÊN BỆNH NHÂN qua để dùng cho toàn bộ flow đặt lịch
                                intent.putExtra("PATIENT_NAME", loginResponse.getData().getHo_ten());
                                intent.putExtra("PATIENT_ID", loginResponse.getData().get_id());
                                break;
                        }

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();

                    } else {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(LoginActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");
                Toast.makeText(LoginActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private void saveUserSession(LoginResponse.BenhNhan user) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putString("USER_ID", user.get_id());
        editor.putString("USER_NAME", user.getHo_ten());
        editor.putBoolean("IS_LOGGED_IN", true);

        editor.apply();
    }
}
