//package com.example.nhakhoaapp;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.TextView;
//
//import androidx.activity.EdgeToEdge;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.core.graphics.Insets;
//import androidx.core.view.ViewCompat;
//import androidx.core.view.WindowInsetsCompat;
//
//public class LoginActivity extends AppCompatActivity {
//    private EditText edtEmail, edtPassword;
//    private Button btnSignIn;
//    private TextView txtCreate, txtForgot;
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        EdgeToEdge.enable(this);
//        setContentView(R.layout.activity_login);
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Login), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });
//
//    }
//
//}
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

public class LoginActivity extends AppCompatActivity {

    private EditText edtEmail, edtPassword;
    private Button btnSignIn;
    private TextView txtCreate, txtForgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Áp insets cho view gốc (id trong layout là main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.Login), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Ánh xạ view
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        btnSignIn = findViewById(R.id.btnSignIn);
        txtCreate = findViewById(R.id.txtCreate);
        txtForgot = findViewById(R.id.txtForgot);

        // 2. Nút Sign in
        btnSignIn.setOnClickListener(v -> {
            if (validateInput()) {
                doLoginFake();   // hiện tại login fake, sau này thay bằng DB
            }
        });

        // 3. Nút Create account
        txtCreate.setOnClickListener(v -> {
//            Toast.makeText(
//                    LoginActivity.this,
//                    "Đi tới màn tạo tài khoản (sẽ làm sau)",
//                    Toast.LENGTH_SHORT
//            ).show();

//             Sau này bạn mở RegisterActivity ở đây
             Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
             startActivity(intent);
        });

        // 4. Nút Forgot password
        txtForgot.setOnClickListener(v -> {
            Toast.makeText(
                    LoginActivity.this,
                    "Chức năng quên mật khẩu (sẽ làm sau)",
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    // Kiểm tra input cơ bản
    private boolean validateInput() {
        String email = edtEmail.getText().toString().trim();
        String pass = edtPassword.getText().toString().trim();

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
        return true;
    }

    // Login “giả” – chỉ chuyển sang MainActivity
    private void doLoginFake() {
        Toast.makeText(this,
                "Đăng nhập tạm thời (fake) thành công",
                Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish(); // đóng màn Login
    }
}
