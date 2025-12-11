package com.example.nhakhoaapp.activities_customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities.LoginActivity;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.request.ChangePasswordRequest;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    private EditText etOldPass, etNewPass, etConfirmPass;
    private Button btnSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        initViews();

        btnSave.setOnClickListener(v -> handleChangePassword());
    }

    private void initViews() {
        // Cấu hình Toolbar (nút Back)
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish()); // Đóng màn hình khi bấm nút back

        // Ánh xạ View
        etOldPass = findViewById(R.id.et_old_pass);
        etNewPass = findViewById(R.id.et_new_pass);
        etConfirmPass = findViewById(R.id.et_confirm_pass);
        btnSave = findViewById(R.id.btn_save_pass);
    }

    private void handleChangePassword() {
        String oldPass = etOldPass.getText().toString().trim();
        String newPass = etNewPass.getText().toString().trim();
        String confirmPass = etConfirmPass.getText().toString().trim();

        // 1. Kiểm tra dữ liệu đầu vào (Validation)
        if (oldPass.isEmpty() || newPass.isEmpty() || confirmPass.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPass.equals(confirmPass)) {
            Toast.makeText(this, "Mật khẩu xác nhận không khớp!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPass.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải từ 6 ký tự trở lên!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Lấy ID người dùng từ SharedPreferences (Session)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);

        if (userId == null) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn, vui lòng đăng nhập lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Tạo Request gửi lên Server
        ChangePasswordRequest request = new ChangePasswordRequest(userId, oldPass, newPass);

        // UI: Khóa nút bấm để tránh spam
        btnSave.setEnabled(false);
        btnSave.setText("Đang xử lý...");

        // 4. Gọi API
        ApiClient.getApiService().changePassword(request).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnSave.setEnabled(true);
                btnSave.setText("LƯU THAY ĐỔI");

                if (response.isSuccessful()) {
                    Toast.makeText(ChangePasswordActivity.this, "Đổi mật khẩu thành công!", Toast.LENGTH_SHORT).show();
                    finish(); // Đóng màn hình, quay về Profile
                } else {
                    // Xử lý các mã lỗi từ server trả về
                    if (response.code() == 400) {
                        Toast.makeText(ChangePasswordActivity.this, "Mật khẩu cũ không đúng!", Toast.LENGTH_SHORT).show();
                    } else if (response.code() == 404) {
                        Toast.makeText(ChangePasswordActivity.this, "Tài khoản không tồn tại!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ChangePasswordActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnSave.setEnabled(true);
                btnSave.setText("LƯU THAY ĐỔI");
                Toast.makeText(ChangePasswordActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}