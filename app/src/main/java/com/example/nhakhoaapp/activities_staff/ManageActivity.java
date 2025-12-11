package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.activities.CategoryListActivity;
import com.example.nhakhoaapp.activities.LoginActivity;
import com.example.nhakhoaapp.activities.ProfileQuanLyActivity;
import com.example.nhakhoaapp.activities.QuanLyHoaDonActivity;
import com.example.nhakhoaapp.activities.RecordListActivity;
import com.example.nhakhoaapp.activities_customer.PatientListActivity; // Đảm bảo bạn đã có Activity này
import com.example.nhakhoaapp.activities_customer.ProfileActivity;
// import com.example.nhakhoaapp.activities_staff.RecordListActivity; // Import khi đã tạo file
// import com.example.nhakhoaapp.activities_staff.ServiceListActivity; // Import khi đã tạo file
// import com.example.nhakhoaapp.activities_staff.InvoiceListActivity; // Import khi đã tạo file

public class ManageActivity extends AppCompatActivity {

    private CardView cardStaff, cardPatient, cardRecord, cardService, cardInvoice;
    private ImageView imgLog;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage); // Tên file XML của bạn

        initViews();
        setupListeners();
        loadUserInfo();
    }

    private void initViews() {
        cardStaff = findViewById(R.id.cardStaff);
        cardPatient = findViewById(R.id.cardPatient);
        cardRecord = findViewById(R.id.cardRecord);
        cardService = findViewById(R.id.cardService);
        cardInvoice = findViewById(R.id.cardInvoice);
        imgLog = findViewById(R.id.imgUser);

        // Nếu bạn muốn hiển thị tên Admin lên header (cần đặt ID cho TextView "Xin chào..." trong XML trước)
        // tvWelcome = findViewById(R.id.tvWelcomeName);
    }

    private void loadUserInfo() {
        // Lấy tên người dùng từ SharedPreferences để hiển thị (nếu cần)
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String userName = prefs.getString("USER_NAME", "Admin");

        // if (tvWelcome != null) tvWelcome.setText("Xin chào, " + userName);
    }

    private void setupListeners() {
        // 1. Quản lý Nhân viên
        cardStaff.setOnClickListener(v -> {
            Intent intent = new Intent(ManageActivity.this, com.example.nhakhoaapp.activities_customer.StaffListActivity.class);
            startActivity(intent);
        });

        // 2. Quản lý Bệnh nhân
        cardPatient.setOnClickListener(v -> {
            // Kiểm tra xem bạn đã tạo PatientListActivity chưa, nếu chưa hãy tạo nhé
            Intent intent = new Intent(ManageActivity.this, PatientListActivity.class); // Hoặc tên file danh sách bệnh nhân của bạn
            startActivity(intent);
        });

        // 3. Quản lý Hồ sơ bệnh án
        cardRecord.setOnClickListener(v -> {
            Intent intent = new Intent(ManageActivity.this, RecordListActivity.class);
            startActivity(intent);
        });

        // 4. Quản lý Dịch vụ (Nếu chưa làm thì hiện thông báo)
        cardService.setOnClickListener(v -> {
             Intent intent = new Intent(ManageActivity.this, CategoryListActivity.class);
             startActivity(intent);
            Toast.makeText(this, "Chức năng Dịch vụ đang phát triển", Toast.LENGTH_SHORT).show();
        });

        // 5. Quản lý Hóa đơn (Nếu chưa làm thì hiện thông báo)
        cardInvoice.setOnClickListener(v -> {
             Intent intent = new Intent(ManageActivity.this, QuanLyHoaDonActivity.class);
             startActivity(intent);
            Toast.makeText(this, "Chức năng Hóa đơn đang phát triển", Toast.LENGTH_SHORT).show();
        });

        imgLog.setOnClickListener(v -> {
            Intent intent = new Intent(ManageActivity.this, ProfileQuanLyActivity.class);
            startActivity(intent);
        });

    }


}