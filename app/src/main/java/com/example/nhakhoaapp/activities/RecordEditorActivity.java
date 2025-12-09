package com.example.nhakhoaapp.activities;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.HoSoBenhAn;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecordEditorActivity extends AppCompatActivity {

    private TextInputEditText edtPatientId, edtDoctorId, edtDate, edtDiagnosis, edtResult, edtImg;
    private Button btnSave;
    private String recordId = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_editor);

        initViews();
        checkMode();

        btnSave.setOnClickListener(v -> saveRecord());
    }

    private void initViews() {
        ImageView btnBack = findViewById(R.id.btnBackCustom);
        btnBack.setOnClickListener(v -> finish());

        edtPatientId = findViewById(R.id.edtPatientId);
        edtDoctorId = findViewById(R.id.edtDoctorId);
        edtDate = findViewById(R.id.edtDate);
        edtDiagnosis = findViewById(R.id.edtDiagnosis);
        edtResult = findViewById(R.id.edtResult);
        edtImg = findViewById(R.id.edtImg);
        btnSave = findViewById(R.id.btnSave);
    }

    private void checkMode() {
        if (getIntent().hasExtra("id")) {
            recordId = getIntent().getStringExtra("id");
            TextView tvTitle = findViewById(R.id.tvHeaderTitle);
            tvTitle.setText("Cập nhật Hồ Sơ");

            edtPatientId.setText(getIntent().getStringExtra("patientId"));
            edtDoctorId.setText(getIntent().getStringExtra("doctorId"));
            edtDate.setText(getIntent().getStringExtra("date"));
            edtDiagnosis.setText(getIntent().getStringExtra("diagnosis"));
            edtResult.setText(getIntent().getStringExtra("result"));
            edtImg.setText(getIntent().getStringExtra("img"));
        }
    }

    private void saveRecord() {
        String pId = edtPatientId.getText().toString().trim();
        String dId = edtDoctorId.getText().toString().trim();
        String date = edtDate.getText().toString().trim();
        String diag = edtDiagnosis.getText().toString().trim();
        String res = edtResult.getText().toString().trim();
        String img = edtImg.getText().toString().trim();

        if (pId.isEmpty() || diag.isEmpty()) {
            Toast.makeText(this, "Nhập ID Bệnh nhân và Chẩn đoán!", Toast.LENGTH_SHORT).show();
            return;
        }

        HoSoBenhAn record = new HoSoBenhAn(pId, dId, date, diag, res, img);
        ApiService api = ApiClient.getApiService();

        if (recordId == null) {
            api.createHoSoBenhAn(record).enqueue(new Callback<HoSoBenhAn>() {
                @Override
                public void onResponse(Call<HoSoBenhAn> call, Response<HoSoBenhAn> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RecordEditorActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else Toast.makeText(RecordEditorActivity.this, "Lỗi thêm", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<HoSoBenhAn> call, Throwable t) {
                    Toast.makeText(RecordEditorActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            api.updateHoSoBenhAn(recordId, record).enqueue(new Callback<HoSoBenhAn>() {
                @Override
                public void onResponse(Call<HoSoBenhAn> call, Response<HoSoBenhAn> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(RecordEditorActivity.this, "Cập nhật xong!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else Toast.makeText(RecordEditorActivity.this, "Lỗi cập nhật", Toast.LENGTH_SHORT).show();
                }
                @Override
                public void onFailure(Call<HoSoBenhAn> call, Throwable t) {}
            });
        }
    }
}