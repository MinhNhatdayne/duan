package com.example.nhakhoaapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtRePassword;
    private Button btnCreate;
    private TextView txtLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_sign_up);

        // Áp insets cho root view (id = SignUp trong layout)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.SignUp), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Ánh xạ view
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtRePassword = findViewById(R.id.edtRePassword);

        btnCreate = findViewById(R.id.btnCreate);
        txtLogin = findViewById(R.id.txtLogin);

        // 2. Xử lý nút Create an Account
        btnCreate.setOnClickListener(v -> {
            if (validateInput()) {
                doSignUpFake();
            }
        });

        // 3. Xử lý nút "Log in"
        txtLogin.setOnClickListener(v -> {
            // Quay về màn Login
            Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();   // đóng màn SignUp
        });
    }

    // ====== HÀM KIỂM TRA DỮ LIỆU ======
    private boolean validateInput() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();
        String rePass = edtRePassword.getText().toString().trim();

        // Name
        if (TextUtils.isEmpty(name)) {
            edtName.setError("Tên không được để trống");
            edtName.requestFocus();
            return false;
        }

        // Email
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email không được để trống");
            edtEmail.requestFocus();
            return false;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edtEmail.setError("Email không đúng định dạng");
            edtEmail.requestFocus();
            return false;
        }

        // Password
        if (TextUtils.isEmpty(pass)) {
            edtPassword.setError("Mật khẩu không được để trống");
            edtPassword.requestFocus();
            return false;
        }
        if (pass.length() < 6) {
            edtPassword.setError("Mật khẩu phải từ 6 ký tự");
            edtPassword.requestFocus();
            return false;
        }

        // RePassword
        if (TextUtils.isEmpty(rePass)) {
            edtRePassword.setError("Vui lòng nhập lại mật khẩu");
            edtRePassword.requestFocus();
            return false;
        }
        if (!rePass.equals(pass)) {
            edtRePassword.setError("Mật khẩu nhập lại không khớp");
            edtRePassword.requestFocus();
            return false;
        }

        return true;
    }

    // ====== ĐĂNG KÝ "FAKE" – CHƯA DÙNG DATABASE ======
    private void doSignUpFake() {
        // Sau này bạn sẽ lưu vào DB / API tại đây
        Toast.makeText(this,
                "Tạo tài khoản tạm thời (fake) thành công",
                Toast.LENGTH_SHORT).show();

        // Tạm thời: sau khi tạo xong quay về màn Login
        Intent intent = new Intent(SignUpActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
