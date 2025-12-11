package com.example.nhakhoaapp.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.models.entity.HoSoBenhAn;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordEditorActivity extends AppCompatActivity {

    private TextInputEditText edtPatientId, edtDoctorId, edtDate, edtDiagnosis, edtResult, edtImg;
    private Button btnSave;
    private Toolbar toolbar;

    // Biến để lưu ID hồ sơ (nếu là chế độ sửa)
    private String recordId = null;

    // Biến lịch để chọn ngày
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_editor);

        initViews();
        setupToolbar();
        setupDatePicker(); // Cài đặt sự kiện chọn ngày
        checkMode();       // Kiểm tra xem là Thêm mới hay Sửa

        btnSave.setOnClickListener(v -> saveRecord());
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);

        edtPatientId = findViewById(R.id.edtPatientId);
        edtDoctorId = findViewById(R.id.edtDoctorId);
        edtDate = findViewById(R.id.edtDate);
        edtDiagnosis = findViewById(R.id.edtDiagnosis);
        edtResult = findViewById(R.id.edtResult);
        edtImg = findViewById(R.id.edtImg);
        btnSave = findViewById(R.id.btnSave);
    }

    // 1. Cấu hình Toolbar (Nút Back)
    private void setupToolbar() {
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> finish());
    }

    // 2. Cấu hình DatePicker (Lịch)
    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel(); // Cập nhật text hiển thị
        };

        // Khi bấm vào ô ngày -> Hiện lịch
        edtDate.setOnClickListener(v -> {
            new DatePickerDialog(RecordEditorActivity.this, date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    // Hiển thị ngày dạng dd/MM/yyyy (Việt Nam)
    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDate.setText(sdf.format(myCalendar.getTime()));
    }

    // Hàm chuyển đổi ngày từ dd/MM/yyyy -> yyyy-MM-dd (Để gửi lên Server)
    private String convertDateToISO(String dateStr) {
        try {
            SimpleDateFormat displayFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
            SimpleDateFormat isoFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date date = displayFormat.parse(dateStr);
            return isoFormat.format(date);
        } catch (Exception e) {
            return dateStr; // Nếu lỗi (hoặc đã đúng format) thì giữ nguyên
        }
    }

    // 3. Kiểm tra chế độ (Thêm hay Sửa)
    private void checkMode() {
        if (getIntent().hasExtra("id")) {
            // Chế độ SỬA
            recordId = getIntent().getStringExtra("id");
            getSupportActionBar().setTitle("Cập nhật Hồ Sơ"); // Đổi tiêu đề Toolbar
            btnSave.setText("LƯU THAY ĐỔI");

            // Điền dữ liệu cũ vào các ô
            edtPatientId.setText(getIntent().getStringExtra("patientId"));
            edtDoctorId.setText(getIntent().getStringExtra("doctorId"));

            // Xử lý hiển thị ngày cũ (Giả sử server trả về yyyy-MM-dd, cần đổi lại dd/MM/yyyy để hiển thị đẹp nếu muốn)
            edtDate.setText(getIntent().getStringExtra("date"));

            edtDiagnosis.setText(getIntent().getStringExtra("diagnosis"));
            edtResult.setText(getIntent().getStringExtra("result"));
            edtImg.setText(getIntent().getStringExtra("img"));
        }
    }

    // 4. Lưu dữ liệu (Gọi API)
    private void saveRecord() {
        String pId = edtPatientId.getText().toString().trim();
        String dId = edtDoctorId.getText().toString().trim();
        String dateRaw = edtDate.getText().toString().trim();
        String diag = edtDiagnosis.getText().toString().trim();
        String res = edtResult.getText().toString().trim();
        String img = edtImg.getText().toString().trim();

        // Validate cơ bản
        if (pId.isEmpty() || dId.isEmpty() || dateRaw.isEmpty() || diag.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin bắt buộc!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Chuyển đổi ngày cho chuẩn Database
        String dateFormatted = convertDateToISO(dateRaw);

        // Tạo object gửi đi
        HoSoBenhAn record = new HoSoBenhAn(pId, dId, dateFormatted, diag, res, img);

        btnSave.setEnabled(false);
        btnSave.setText("Đang xử lý...");

        if (recordId == null) {
            // --- GỌI API THÊM MỚI ---
            ApiClient.getApiService().createHoSoBenhAn(record).enqueue(new Callback<HoSoBenhAn>() {
                @Override
                public void onResponse(Call<HoSoBenhAn> call, Response<HoSoBenhAn> response) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU HỒ SƠ");

                    if (response.isSuccessful()) {
                        Toast.makeText(RecordEditorActivity.this, "Thêm hồ sơ thành công!", Toast.LENGTH_SHORT).show();
                        finish(); // Đóng màn hình
                    } else {
                        Toast.makeText(RecordEditorActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<HoSoBenhAn> call, Throwable t) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU HỒ SƠ");
                    Toast.makeText(RecordEditorActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // --- GỌI API CẬP NHẬT ---
            ApiClient.getApiService().updateHoSoBenhAn(recordId, record).enqueue(new Callback<HoSoBenhAn>() {
                @Override
                public void onResponse(Call<HoSoBenhAn> call, Response<HoSoBenhAn> response) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU THAY ĐỔI");

                    if (response.isSuccessful()) {
                        Toast.makeText(RecordEditorActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(RecordEditorActivity.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<HoSoBenhAn> call, Throwable t) {
                    btnSave.setEnabled(true);
                    btnSave.setText("LƯU THAY ĐỔI");
                    Toast.makeText(RecordEditorActivity.this, "Lỗi kết nối mạng", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}