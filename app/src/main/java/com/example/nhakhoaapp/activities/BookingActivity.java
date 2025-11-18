package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhakhoaapp.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class BookingActivity extends AppCompatActivity {

    private TextView btnDatLichChinhNha;
    private TextView btnDatLichTongQuat;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        // 1. Ánh xạ
        btnDatLichChinhNha = findViewById(R.id.btn_dat_lich_chinh_nha);
        btnDatLichTongQuat = findViewById(R.id.btn_dat_lich_tong_quat);
        bottomNavigationView = findViewById(R.id.bottom_navigation);

        // 2. Xử lý sự kiện Đặt lịch cho Dịch vụ 1 (Chỉnh nha)
        btnDatLichChinhNha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình chọn bác sĩ và truyền tên dịch vụ
                Intent intent = new Intent(BookingActivity.this, SelectDoctorActivity.class);
                intent.putExtra("SERVICE_NAME", "Chỉnh nha");
                startActivity(intent);
            }
        });

        // 3. Xử lý sự kiện Đặt lịch cho Dịch vụ 2 (Điều trị tổng quát)
        btnDatLichTongQuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Chuyển sang màn hình chọn bác sĩ và truyền tên dịch vụ
                Intent intent = new Intent(BookingActivity.this, SelectDoctorActivity.class);
                intent.putExtra("SERVICE_NAME", "Điều trị tổng quát");
                startActivity(intent);
            }
        });

        // 4. Xử lý Bottom Navigation
        // ...
    }
}