package com.example.nhakhoaapp.activities;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.request.ForgotRequest;
import com.example.nhakhoaapp.models.response.ForgotResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends AppCompatActivity {
    private ImageView btnBackIcon;
    private TextView tvBackLogins;
    private EditText etEmail;
    private Button btnSend;
    private TextView tvBackLogin;
    // Nên thêm ProgressBar vào XML để xoay khi đang gửi mail
    // private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        etEmail = findViewById(R.id.et_email_forgot);
        btnSend = findViewById(R.id.btn_send_reset);
        tvBackLogin = findViewById(R.id.tv_back_login);
        btnBackIcon = findViewById(R.id.btn_back_icon);
        btnSend.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            if (TextUtils.isEmpty(email)) {
                Toast.makeText(ForgotPasswordActivity.this, "Vui lòng nhập email!", Toast.LENGTH_SHORT).show();
            } else {
                sendResetRequest(email);
            }
        });
        btnBackIcon.setOnClickListener(v -> finish());
        tvBackLogin.setOnClickListener(v -> finish());
    }

    private void sendResetRequest(String email) {
        // UI: Khóa nút bấm để tránh spam
        btnSend.setEnabled(false);
        btnSend.setText("Đang gửi...");

        ForgotRequest request = new ForgotRequest(email);

        ApiClient.getApiService().forgotPassword(request).enqueue(new Callback<ForgotResponse>() {
            @Override
            public void onResponse(Call<ForgotResponse> call, Response<ForgotResponse> response) {
                btnSend.setEnabled(true);
                btnSend.setText("Gửi yêu cầu");

                if (response.isSuccessful() && response.body() != null) {
                    if (response.body().isSuccess()) {
                        Toast.makeText(ForgotPasswordActivity.this, "Thành công! Vui lòng kiểm tra email.", Toast.LENGTH_LONG).show();
                        finish(); // Quay lại trang đăng nhập
                    } else {
                        Toast.makeText(ForgotPasswordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ForgotPasswordActivity.this, "Email không tồn tại hoặc lỗi server.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ForgotResponse> call, Throwable t) {
                btnSend.setEnabled(true);
                btnSend.setText("Gửi yêu cầu");
                Toast.makeText(ForgotPasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}