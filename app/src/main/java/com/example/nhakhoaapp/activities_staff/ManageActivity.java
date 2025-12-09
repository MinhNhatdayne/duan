package com.example.nhakhoaapp.activities_staff;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.nhakhoaapp.R;

public class ManageActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView cardStaff, cardDoctor, cardPatient, cardRecord, cardService, cardInvoice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_manage);

        // Setup Window Insets (giữ nguyên code của bạn)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Ánh xạ View
        initViews();

        // 2. Gán sự kiện Click
        setEvents();
    }

    private void initViews() {
        cardStaff = findViewById(R.id.cardStaff);
//        cardDoctor = findViewById(R.id.cardDoctor);
        cardPatient = findViewById(R.id.cardPatient);
        cardRecord = findViewById(R.id.cardRecord);
        cardService = findViewById(R.id.cardService);
        cardInvoice = findViewById(R.id.cardInvoice);
        // Thêm các card khác nếu có
    }

    private void setEvents() {
        cardStaff.setOnClickListener(this);
//        cardDoctor.setOnClickListener(this);
        cardPatient.setOnClickListener(this);
        cardRecord.setOnClickListener(this);
        cardService.setOnClickListener(this);
        cardInvoice.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        int id = v.getId();

        if (id == R.id.cardStaff) {
            intent = new Intent(this, com.example.nhakhoaapp.activities_customer.StaffListActivity.class);
             startActivity(intent);
            Toast.makeText(this, "Mở quản lý Nhân viên", Toast.LENGTH_SHORT).show();

//        } else if (id == R.id.cardDoctor) {
//            // Mở màn hình quản lý bác sĩ
//
//            Toast.makeText(this, "Mở quản lý Bác sĩ", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.cardPatient) {
            intent = new Intent(this, com.example.nhakhoaapp.activities_customer.PatientListActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Mở quản lý Bệnh nhân", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.cardRecord) {
            intent = new Intent(this, com.example.nhakhoaapp.activities.RecordListActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Mở quản lý Hồ sơ", Toast.LENGTH_SHORT).show();
        }
        // Xử lý các case khác tương tự
    }
}