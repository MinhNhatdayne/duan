package com.example.nhakhoaapp.activities_customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.PhanHoi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private EditText etContent;
    private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initViews();

        btnSend.setOnClickListener(v -> handleSendFeedback());
    }

    private void initViews() {
        etContent = findViewById(R.id.et_feedback_content);
        btnSend = findViewById(R.id.btn_send_feedback);
    }

    private void handleSendFeedback() {
        String content = etContent.getText().toString().trim();

        if (content.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 1. Lấy thông tin người gửi từ Session
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);
        String userName = prefs.getString("USER_NAME", "Ẩn danh");

        if (userId == null) {
            Toast.makeText(this, "Bạn cần đăng nhập lại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 2. Tạo object
        PhanHoi feedback = new PhanHoi(userId, userName, content);

        // 3. Khóa nút để tránh spam
        btnSend.setEnabled(false);
        btnSend.setText("Đang gửi...");

        // 4. Gọi API
        ApiClient.getApiService().sendFeedback(feedback).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnSend.setEnabled(true);
                btnSend.setText("Gửi đi");

                if (response.isSuccessful()) {
                    Toast.makeText(FeedbackActivity.this, "Cảm ơn đóng góp của bạn!", Toast.LENGTH_LONG).show();
                    etContent.setText(""); // Xóa nội dung đã nhập
                    finish(); // Đóng màn hình
                } else {
                    Toast.makeText(FeedbackActivity.this, "Gửi thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnSend.setEnabled(true);
                btnSend.setText("Gửi đi");
                Toast.makeText(FeedbackActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}