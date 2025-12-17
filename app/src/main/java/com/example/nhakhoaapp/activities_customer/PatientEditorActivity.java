package com.example.nhakhoaapp.activities_customer;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.google.android.material.textfield.TextInputEditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PatientEditorActivity extends AppCompatActivity {

    private TextInputEditText edtName, edtDob, edtPhone, edtEmail, edtAddress;
    private AutoCompleteTextView spGender;
    private Button btnSave;
    private String patientId = null; // Stores the ID for updates
    private final Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_editor);

        initViews();
        setupGenderDropdown();
        setupDatePicker();
        checkMode(); // Determine if we are in Add or Edit mode

        btnSave.setOnClickListener(v -> savePatient());
    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        edtName = findViewById(R.id.edtName);
        edtDob = findViewById(R.id.edtDob);
        edtPhone = findViewById(R.id.edtPhone);
        edtEmail = findViewById(R.id.edtEmail);
        edtAddress = findViewById(R.id.edtAddress);
        spGender = findViewById(R.id.spGender);
        btnSave = findViewById(R.id.btnSave);
    }

    private void setupGenderDropdown() {
        String[] genders = {"Nam", "Nữ", "Khác"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, genders);
        spGender.setAdapter(adapter);
        spGender.setText(genders[0], false); // Default to "Nam"
    }

    private void setupDatePicker() {
        DatePickerDialog.OnDateSetListener date = (view, year, month, dayOfMonth) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        };

        edtDob.setOnClickListener(v -> {
            new DatePickerDialog(PatientEditorActivity.this, date,
                    myCalendar.get(Calendar.YEAR),
                    myCalendar.get(Calendar.MONTH),
                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edtDob.setText(sdf.format(myCalendar.getTime()));
    }

    private void checkMode() {
        // If an ID is passed, we are in Edit mode
        if (getIntent().hasExtra("id")) {
            Log.d("PUT_PATIENT", "ID gửi lên = " + patientId);
            patientId = getIntent().getStringExtra("id");
            Log.d("PatientEditor", "Editing patient with ID: " + patientId);

            edtName.setText(getIntent().getStringExtra("name"));
            edtPhone.setText(getIntent().getStringExtra("phone"));
            edtEmail.setText(getIntent().getStringExtra("email"));
            edtAddress.setText(getIntent().getStringExtra("address"));

            String oldGender = getIntent().getStringExtra("gender");
            if (oldGender != null) spGender.setText(oldGender, false);

            // Convert server date (ISO 8601) to display format (dd/MM/yyyy)
            String rawDate = getIntent().getStringExtra("dob");
            edtDob.setText(formatDateToDisplay(rawDate));

            btnSave.setText("Cập nhật"); // Change button text to "Update"
        }
    }

    private void savePatient() {
        String name = edtName.getText().toString().trim();
        String displayDob = edtDob.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();
        String gender = spGender.getText().toString();
        String email = edtEmail.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (name.isEmpty() || phone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập tên và số điện thoại!", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convert display date (dd/MM/yyyy) back to ISO 8601 for the server
        String serverDob = formatDateToSend(displayDob);
        String defaultPassword = "123456"; // Default password for new users

        // Create the BenhNhan object
        BenhNhan bn = new BenhNhan(name, serverDob, gender, address, phone, email, defaultPassword);
        ApiService api = ApiClient.getApiService();

        if (patientId == null) {
            // --- CREATE NEW PATIENT (POST) ---
            api.createBenhNhan(bn).enqueue(new Callback<BenhNhan>() {
                @Override
                public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PatientEditorActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(PatientEditorActivity.this, "Lỗi thêm: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<BenhNhan> call, Throwable t) {
                    Toast.makeText(PatientEditorActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            // --- UPDATE EXISTING PATIENT (PUT) ---
            api.updateBenhNhan(patientId, bn).enqueue(new Callback<BenhNhan>() {
                @Override
                public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(PatientEditorActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        // If 404, it means the ID wasn't found in the database
                        if (response.code() == 404) {
                            Toast.makeText(PatientEditorActivity.this, "Lỗi: Bệnh nhân không tồn tại!", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(PatientEditorActivity.this, "Lỗi cập nhật: " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<BenhNhan> call, Throwable t) {
                    Toast.makeText(PatientEditorActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    // --- Helper: Convert ISO 8601 (Server) -> dd/MM/yyyy (Display) ---
    private String formatDateToDisplay(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "";
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = inputFormat.parse(isoDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            return isoDate; // Return original if parsing fails
        }
    }

    // --- Helper: Convert dd/MM/yyyy (Display) -> ISO 8601 (Server) ---
    private String formatDateToSend(String displayDate) {
        if (displayDate == null || displayDate.isEmpty()) return null;
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            Date date = inputFormat.parse(displayDate);

            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            outputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            return outputFormat.format(date);
        } catch (Exception e) {
            return displayDate; // Return original if parsing fails
        }
    }
}