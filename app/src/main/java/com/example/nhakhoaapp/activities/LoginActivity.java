package com.example.nhakhoaapp.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities_customer.DashboardActivity;
import com.example.nhakhoaapp.activities_staff.ManageActivity;
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
    private CheckBox cbRemember;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // 1. Ánh xạ View
        etUsername = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
        tvForgotPassword = findViewById(R.id.tv_forgot_password);
        tvRegisterLink = findViewById(R.id.tv_register_link);
        cbRemember = findViewById(R.id.cb_remember);

        // 2. Kiểm tra ghi nhớ
        checkRememberedUser();

        // 3. Sự kiện Click
        btnLogin.setOnClickListener(v -> handleLogin());

        tvRegisterLink.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        tvForgotPassword.setOnClickListener(v -> {
            Intent intent = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
        });
    }

    private void checkRememberedUser() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        boolean isRemembered = prefs.getBoolean("REMEMBER_ME", false);

        if (isRemembered) {
            String savedEmail = prefs.getString("SAVED_EMAIL", "");
            String savedPass = prefs.getString("SAVED_PASS", "");

            etUsername.setText(savedEmail);
            etPassword.setText(savedPass);
            cbRemember.setChecked(true);
        }
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
                        processRememberMe(email, password);
                        saveUserSession(loginResponse.getData());

                        // === PHẦN CHỈNH SỬA QUAN TRỌNG ===

                        // 1. Lấy role, xử lý null
                        String role = loginResponse.getRole();
                        if (role == null) role = "PATIENT";

                        // 2. Chuẩn hóa chuỗi: Xóa khoảng trắng thừa và Chuyển hết về CHỮ IN HOA
                        // Để tránh lỗi "Manager" khác "MANAGER"
                        role = role.trim().toUpperCase();

                        // 3. (DEBUG) Hiện thông báo xem Server thực sự trả về role gì
                        // Nếu nó hiện "DOCTOR" -> Lỗi do Server gán sai quyền.
                        // Nếu nó hiện "MANAGER" -> Code Switch chạy đúng.
                        Toast.makeText(LoginActivity.this, "Role: " + role, Toast.LENGTH_LONG).show();

                        Intent intent;
                        switch (role) {
                            case "MANAGER": // Quản lý
                                intent = new Intent(LoginActivity.this, ManageActivity.class);
                                break;

                            case "DOCTOR":  // Bác sĩ
                                intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                                break;

                            case "STAFF":   // Nhân viên (Nếu có)
                                // Bạn có thể trỏ về ManageActivity hoặc StaffDashboard tùy logic
                                intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                                break;

                            case "PATIENT": // Bệnh nhân
                            default:        // Mặc định
                                intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                if (loginResponse.getData() != null) {
                                    intent.putExtra("PATIENT_NAME", loginResponse.getData().getHo_ten());
                                    intent.putExtra("PATIENT_ID", loginResponse.getData().get_id());
                                }
                                break;
                        }

                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        // =================================

                    } else {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if (response.code() == 404 || response.code() == 400 || response.code() == 401) {
                        Toast.makeText(LoginActivity.this, "Sai tài khoản hoặc mật khẩu!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(LoginActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");
                Toast.makeText(LoginActivity.this, "Lỗi kết nối mạng!", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void processRememberMe(String email, String password) {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        if (cbRemember.isChecked()) {
            editor.putBoolean("REMEMBER_ME", true);
            editor.putString("SAVED_EMAIL", email);
            editor.putString("SAVED_PASS", password);
        } else {
            editor.remove("REMEMBER_ME");
            editor.remove("SAVED_EMAIL");
            editor.remove("SAVED_PASS");
        }
        editor.apply();
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