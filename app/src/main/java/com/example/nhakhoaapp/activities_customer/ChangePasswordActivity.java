package com.example.nhakhoaapp.activities_customer;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.nhakhoaapp.R;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        EditText etOld = findViewById(R.id.et_old_pass);
        EditText etNew = findViewById(R.id.et_new_pass);
        EditText etConfirm = findViewById(R.id.et_confirm_pass);
        Button btnSave = findViewById(R.id.btn_save_pass);

        btnSave.setOnClickListener(v -> {
            String oldPass = etOld.getText().toString();
            String newPass = etNew.getText().toString();
            String confirmPass = etConfirm.getText().toString();

            if (oldPass.isEmpty() || newPass.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPass.equals(confirmPass)) {
                Toast.makeText(this, "Mật khẩu mới không khớp", Toast.LENGTH_SHORT).show();
                return;
            }

            // GỌI API ĐỔI MẬT KHẨU Ở ĐÂY
            // ApiClient.getApiService().changePassword(userId, oldPass, newPass)...

            Toast.makeText(this, "Đổi mật khẩu thành công (Demo)", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}