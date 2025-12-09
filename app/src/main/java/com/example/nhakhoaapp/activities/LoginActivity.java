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

// Lưu ý: Nếu bạn chưa tạo AdminActivity và DoctorActivity, hãy tạm thời comment (//) các dòng import và Intent liên quan để không bị lỗi đỏ.
// import com.example.nhakhoaapp.activities_admin.AdminActivity;
// import com.example.nhakhoaapp.activities_staff.DoctorActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private Button btnLogin;
    private TextView tvForgotPassword, tvRegisterLink;

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

        // 2. Xử lý sự kiện Đăng nhập
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleLogin();
            }
        });

        // 3. Xử lý Đăng ký
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

        // Tạo Request
        LoginRequest request = new LoginRequest(email, password);

        // UI: Vô hiệu hóa nút
        btnLogin.setEnabled(false);
        btnLogin.setText("Đang xử lý...");

        // Gọi API
        ApiClient.getApiService().login(request).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                btnLogin.setEnabled(true);
                btnLogin.setText("Đăng nhập");

                // ✅ SỬA LỖI 1: Kiểm tra response và lấy body ra trước
                if (response.isSuccessful() && response.body() != null) {

                    // --- KHAI BÁO BIẾN loginResponse TẠI ĐÂY ---
                    LoginResponse loginResponse = response.body();
                    // -------------------------------------------

                    if (loginResponse.isSuccess()) {
                        Toast.makeText(LoginActivity.this, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show();

                        // 1. Lưu thông tin chung
                        saveUserSession(loginResponse.getData());

                        // 2. Lấy Role từ server
                        String role = loginResponse.getRole();
                        if (role == null) role = "PATIENT"; // Mặc định tránh crash

                        Intent intent = null;

                        // 3. ĐIỀU HƯỚNG THEO ROLE
                        switch (role) {
                            case "MANAGER":
                                // Nếu chưa có file AdminActivity thì tạm thời dùng DashboardActivity
                                // intent = new Intent(LoginActivity.this, AdminActivity.class);
                                intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                                Toast.makeText(LoginActivity.this, "Chào Quản Lý (Đang trỏ tạm về Dashboard)", Toast.LENGTH_SHORT).show();
                                break;

                            case "DOCTOR":
                                // Nếu chưa có file DoctorActivity thì tạm thời dùng DashboardActivity
                                // intent = new Intent(LoginActivity.this, DoctorActivity.class);
                                intent = new Intent(LoginActivity.this, StaffDashboardActivity.class);
                                Toast.makeText(LoginActivity.this, "Chào Bác Sĩ (Đang trỏ tạm về Dashboard)", Toast.LENGTH_SHORT).show();
                                break;

                            case "PATIENT":
                            default:
                                intent = new Intent(LoginActivity.this, DashboardActivity.class);
                                break;
                        }

                        // 4. Start Activity
                        if (intent != null) {
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        // Server trả về false (Sai pass/email)
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Lỗi 404, 500...
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
        SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        // ✅ SỬA LỖI 2: Lưu _id của MongoDB thay vì Email
        // Để sau này lọc lịch hẹn theo ID người dùng
        // Đảm bảo trong LoginResponse.BenhNhan có hàm get_id()
        if (user.get_id() != null) {
            editor.putString("USER_ID", user.get_id());
        } else {
            // Fallback nếu chưa cấu hình get_id, dùng tạm email nhưng nên sửa sớm
            editor.putString("USER_ID", user.getEmail());
        }

        editor.putString("USER_NAME", user.getHo_ten());
        editor.putBoolean("IS_LOGGED_IN", true);
        editor.apply();
    }
}