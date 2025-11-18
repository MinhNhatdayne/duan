package com.example.nhakhoaapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nhakhoaapp.R;

public class SelectDoctorActivity extends AppCompatActivity {

    private ImageView imgBackButton;
    private TextView tvSelectedService;
    private TextView btnDatLich1; // Đặt lịch cho bác sĩ 1
    private TextView btnDatLich2; // Đặt lịch cho bác sĩ 2
    // TODO: Khi dùng RecyclerView, logic sẽ khác

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_doctor);

        // 1. Ánh xạ
        imgBackButton = findViewById(R.id.img_back_button);
        tvSelectedService = findViewById(R.id.tv_selected_service);
        
        // Giả lập lấy thông tin dịch vụ từ màn hình trước (BookingActivity)
        String serviceName = getIntent().getStringExtra("SERVICE_NAME");
        if (serviceName != null) {
             tvSelectedService.setText(serviceName);
        } else {
             tvSelectedService.setText("Chỉnh nha"); // Mặc định nếu không có dữ liệu
        }
        
        // Lấy nút Đặt lịch từ các item include (Dùng ID của TextView bên trong item_doctor_list.xml)
        View doctorItem1 = findViewById(R.id.doctor_item_1);
        View doctorItem2 = findViewById(R.id.doctor_item_2);
        
        btnDatLich1 = doctorItem1.findViewById(R.id.btn_dat_lich);
        btnDatLich2 = doctorItem2.findViewById(R.id.btn_dat_lich);


        // 2. Xử lý sự kiện Quay lại
        imgBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed(); // Trở về màn hình trước
            }
        });
        
        // 3. Xử lý sự kiện Chọn bác sĩ (Đặt lịch)
        btnDatLich1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Chuyển sang màn hình chọn ngày giờ cho bác sĩ Đoàn Hồng Lê
                Toast.makeText(SelectDoctorActivity.this, "Chọn bác sĩ Đoàn Hồng Lê. Chuyển sang chọn ngày/giờ", Toast.LENGTH_SHORT).show();
            }
        });

        btnDatLich2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Chuyển sang màn hình chọn ngày giờ cho bác sĩ Đoàn Bích Ngọc
                Toast.makeText(SelectDoctorActivity.this, "Chọn bác sĩ Đoàn Bích Ngọc. Chuyển sang chọn ngày/giờ", Toast.LENGTH_SHORT).show();
            }
        });
        
        // TODO: Xử lý tìm kiếm (et_search_doctor)
    }
}