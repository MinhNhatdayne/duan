package com.example.nhakhoaapp.activities_customer;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.PhanHoi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackActivity extends AppCompatActivity {

    private EditText etContent;
    private Button btnSend;
    private RatingBar ratingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        initViews();

        btnSend.setOnClickListener(v -> handleSendFeedback());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());

        etContent = findViewById(R.id.et_feedback_content);
        btnSend = findViewById(R.id.btn_send_feedback);
        ratingBar = findViewById(R.id.rating_bar);
    }

    private void handleSendFeedback() {
        // 1. Lấy dữ liệu
        String rawContent = etContent.getText().toString().trim();
        float rating = ratingBar.getRating(); // Lấy số sao (ví dụ: 3.0, 4.0)

        // 2. Validate (Bắt buộc phải chọn sao)
        if (rating == 0) {
            Toast.makeText(this, "Vui lòng chấm điểm sao!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (rawContent.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập nội dung góp ý!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 3. Lấy thông tin User
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userId = prefs.getString("USER_ID", null);
        String userName = prefs.getString("USER_NAME", "Ẩn danh");

        if (userId == null) {
            Toast.makeText(this, "Phiên đăng nhập hết hạn!", Toast.LENGTH_SHORT).show();
            return;
        }

        // 4. GHÉP SỐ SAO VÀO NỘI DUNG (Để lưu vào database mà không cần sửa bảng DB)
        // Kết quả sẽ dạng: "Đánh giá: 5/5 sao. Nội dung: Bác sĩ rất tận tình..."
        String finalContent = "Đánh giá: " + (int)rating + "/5 sao.\nNội dung: " + rawContent;

        // 5. Tạo Object gửi đi
        PhanHoi feedback = new PhanHoi(userId, userName, finalContent);

        // 6. Gọi API
        btnSend.setEnabled(false);
        btnSend.setText("Đang gửi...");

        ApiClient.getApiService().sendFeedback(feedback).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                btnSend.setEnabled(true);
                btnSend.setText("GỬI PHẢN HỒI");

                if (response.isSuccessful()) {
                    Toast.makeText(FeedbackActivity.this, "Cảm ơn đánh giá " + (int)rating + " sao của bạn!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(FeedbackActivity.this, "Gửi thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnSend.setEnabled(true);
                btnSend.setText("GỬI PHẢN HỒI");
                Toast.makeText(FeedbackActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }
}