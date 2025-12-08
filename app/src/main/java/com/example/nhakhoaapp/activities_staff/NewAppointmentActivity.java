package com.example.nhakhoaapp.activities_staff;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.BenhNhan;
import com.example.nhakhoaapp.models.DanhMucDichVu;
import com.example.nhakhoaapp.models.request.LichHenRequest; // [QUAN TRỌNG] Dùng Request để gửi
import com.example.nhakhoaapp.models.response.LichHenResponse; // [QUAN TRỌNG] Dùng Response để nhận
import com.example.nhakhoaapp.models.NhanVien;
import okhttp3.ResponseBody;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentActivity extends AppCompatActivity {

    // Views
    private EditText etPatientName, etPatientPhone, etNote, etDateTime;
    private Spinner spinnerService, spinnerDoctor;
    private Button btnConfirmAppointment;

    // API & Data
    private ApiService apiService;
    private List<DanhMucDichVu> serviceList = new ArrayList<>();
    private List<NhanVien> doctorList = new ArrayList<>();

    // Selected Data
    private String selectedServiceId = null;
    private String selectedServiceName = null;
    private String selectedDoctorId = null;

    // Date Time Handling
    private final Calendar calendar = Calendar.getInstance();
    private String selectedIsoDateTime = null; // Chuỗi ISO gửi lên API

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        apiService = ApiClient.getApiService();

        setupToolbar();
        initViews();

        // Khởi tạo Adapter rỗng trước để tránh Spinner bị null
        setupEmptySpinners();

        // Gọi API lấy dữ liệu đổ vào 2 Spinner
        fetchServices();
        fetchDoctors();

        setupEvents();
    }

    private void setupToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Tạo Cuộc Hẹn Mới");
        }
    }

    private void initViews() {
        etPatientName = findViewById(R.id.et_patient_name);
        etPatientPhone = findViewById(R.id.et_patient_phone);
        etNote = findViewById(R.id.et_note);
        etDateTime = findViewById(R.id.et_date_time);

        spinnerService = findViewById(R.id.spinner_service);
        spinnerDoctor = findViewById(R.id.spinner_doctor);

        btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);
    }

    private void setupEmptySpinners() {
        List<String> loadingList = new ArrayList<>();
        loadingList.add("Đang tải dữ liệu...");
        
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, loadingList);
        spinnerService.setAdapter(adapter);
        spinnerDoctor.setAdapter(adapter);
    }

    // --- API 1: Lấy danh sách Dịch vụ ---
    private void fetchServices() {
        apiService.getAllDanhMucDichVu().enqueue(new Callback<List<DanhMucDichVu>>() {
            @Override
            public void onResponse(@NonNull Call<List<DanhMucDichVu>> call, @NonNull Response<List<DanhMucDichVu>> response) {
                List<String> names = new ArrayList<>();
                names.add("-- Chọn Dịch vụ --");

                if (response.isSuccessful() && response.body() != null) {
                    serviceList = response.body();
                    for (DanhMucDichVu s : serviceList) {
                        names.add(s.getTen_dich_vu());
                    }
                } else {
                    names.add("Không có dịch vụ nào");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewAppointmentActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, names);
                spinnerService.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<DanhMucDichVu>> call, @NonNull Throwable t) {
                Toast.makeText(NewAppointmentActivity.this, "Lỗi tải Dịch vụ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- API 2: Lấy danh sách Bác sĩ ---
    private void fetchDoctors() {
        apiService.getAllNhanVien().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(@NonNull Call<List<NhanVien>> call, @NonNull Response<List<NhanVien>> response) {
                List<String> names = new ArrayList<>();
                names.add("-- Chọn Bác sĩ --");

                if (response.isSuccessful() && response.body() != null) {
                    doctorList = response.body();
                    for (NhanVien nv : doctorList) {
                        names.add(nv.getHo_ten());
                    }
                } else {
                    names.add("Không có bác sĩ nào");
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewAppointmentActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, names);
                spinnerDoctor.setAdapter(adapter);
            }

            @Override
            public void onFailure(@NonNull Call<List<NhanVien>> call, @NonNull Throwable t) {
                Toast.makeText(NewAppointmentActivity.this, "Lỗi tải Bác sĩ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupEvents() {
        // 1. Sự kiện chọn Spinner Dịch vụ
        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedServiceId = null;
                    selectedServiceName = null;
                    return;
                }
                
                int realPos = position - 1;
                if (realPos >= 0 && realPos < serviceList.size()) {
                    selectedServiceId = serviceList.get(realPos).get_id();
                    selectedServiceName = serviceList.get(realPos).getTen_dich_vu();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 2. Sự kiện chọn Spinner Bác sĩ
        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedDoctorId = null;
                    return;
                }
                
                int realPos = position - 1;
                if (realPos >= 0 && realPos < doctorList.size()) {
                    selectedDoctorId = doctorList.get(realPos).get_id();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        // 3. Sự kiện chọn Ngày Giờ (Dialog)
        etDateTime.setOnClickListener(v -> showDateTimePicker());

        // 4. Nút Xác nhận
        btnConfirmAppointment.setOnClickListener(v -> checkPatientAndCreateAppointment());
    }

    private void showDateTimePicker() {
        DatePickerDialog datePicker = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            calendar.set(Calendar.YEAR, year);
            calendar.set(Calendar.MONTH, month);
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

            TimePickerDialog timePicker = new TimePickerDialog(this, (timeView, hourOfDay, minute) -> {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                calendar.set(Calendar.SECOND, 0);

                SimpleDateFormat sdfDisplay = new SimpleDateFormat("HH:mm - dd/MM/yyyy", Locale.getDefault());
                etDateTime.setText(sdfDisplay.format(calendar.getTime()));

                SimpleDateFormat sdfIso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
                selectedIsoDateTime = sdfIso.format(calendar.getTime());

            }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
            timePicker.show();

        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

        datePicker.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePicker.show();
    }

    /**
     * LOGIC CHÍNH: Kiểm tra SĐT -> (Tạo mới hoặc Lấy cũ) -> Tạo Lịch Hẹn
     */
    private void checkPatientAndCreateAppointment() {
        // Validate dữ liệu
        String pName = etPatientName.getText().toString().trim();
        String pPhone = etPatientPhone.getText().toString().trim();

        if (pName.isEmpty() || pPhone.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập Tên và SĐT bệnh nhân!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedDoctorId == null) {
            Toast.makeText(this, "Vui lòng chọn Bác sĩ!", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedIsoDateTime == null) {
            Toast.makeText(this, "Vui lòng chọn Thời gian hẹn!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnConfirmAppointment.setEnabled(false);
        btnConfirmAppointment.setText("ĐANG KIỂM TRA SĐT...");

        // 2. Gọi API lấy danh sách bệnh nhân để kiểm tra SĐT
        apiService.getAllBenhNhan().enqueue(new Callback<List<BenhNhan>>() {
            @Override
            public void onResponse(@NonNull Call<List<BenhNhan>> call, @NonNull Response<List<BenhNhan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BenhNhan> list = response.body();
                    
                    String foundId = null;
                    for (BenhNhan bn : list) {
                        if (bn.getSo_dien_thoai() != null && bn.getSo_dien_thoai().equals(pPhone)) {
                            foundId = bn.get_id();
                            break;
                        }
                    }

                    if (foundId != null) {
                        // KHÁCH CŨ -> Dùng ID cũ
                        Log.d("CHECK_PATIENT", "Khách cũ, ID: " + foundId);
                        btnConfirmAppointment.setText("KHÁCH CŨ - ĐANG TẠO LỊCH...");
                        postLichHenToApi(foundId, pName, pPhone);
                    } else {
                        // KHÁCH MỚI -> Tạo hồ sơ mới
                        Log.d("CHECK_PATIENT", "Khách mới, đang tạo hồ sơ...");
                        btnConfirmAppointment.setText("KHÁCH MỚI - ĐANG TẠO HỒ SƠ...");
                        createNewPatientAndAppointment(pName, pPhone);
                    }
                } else {
                    // Lỗi server (hoặc danh sách rỗng) -> Fallback tạo mới
                    createNewPatientAndAppointment(pName, pPhone);
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<BenhNhan>> call, @NonNull Throwable t) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN TẠO CUỘC HẸN");
                Toast.makeText(NewAppointmentActivity.this, "Lỗi kết nối khi kiểm tra SĐT", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Tạo Bệnh nhân mới ---
    private void createNewPatientAndAppointment(String name, String phone) {
        BenhNhan newPatient = new BenhNhan();
        newPatient.setHo_ten(name);
        newPatient.setSo_dien_thoai(phone);
        // Các trường mặc định
        newPatient.setGioi_tinh("Khac");
        newPatient.setNgay_sinh("2024");
        newPatient.setDia_chi("Khách vãng lai");
        newPatient.setPassword("123456"); // Mật khẩu mặc định

        apiService.createBenhNhan(newPatient).enqueue(new Callback<BenhNhan>() {
            @Override
            public void onResponse(@NonNull Call<BenhNhan> call, @NonNull Response<BenhNhan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String newId = response.body().get_id();
                    postLichHenToApi(newId, name, phone);
                } else {
                    btnConfirmAppointment.setEnabled(true);
                    btnConfirmAppointment.setText("XÁC NHẬN TẠO CUỘC HẸN");
                    
                    // Log lỗi chi tiết
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown Error";
                        Log.e("API_ERROR", "Tạo BN thất bại: " + errorBody);
                        Toast.makeText(NewAppointmentActivity.this, "Lỗi tạo hồ sơ: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }

            @Override
            public void onFailure(@NonNull Call<BenhNhan> call, @NonNull Throwable t) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN TẠO CUỘC HẸN");
                Toast.makeText(NewAppointmentActivity.this, "Lỗi tạo hồ sơ!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- Gửi Lịch Hẹn lên API ---
    private void postLichHenToApi(String patientId, String pName, String pPhone) {
        String note = etNote.getText().toString().trim();
        String finalReason = pName + " (" + pPhone + ")";
        if (selectedServiceName != null) finalReason += " - DV: " + selectedServiceName;
        if (!note.isEmpty()) finalReason += " - Note: " + note;

        // [QUAN TRỌNG] Sử dụng LichHenRequest để gửi String ID
        LichHenRequest request = new LichHenRequest();
        request.setIdBenhNhan(patientId);
        request.setIdBacSi(selectedDoctorId);
        request.setThoiGianHen(selectedIsoDateTime);
        request.setLyDoKham(finalReason);
        request.setTrangThai("ChoXacNhan");

        apiService.createLichHen(request).enqueue(new Callback<LichHenResponse>() {
            @Override
            public void onResponse(@NonNull Call<LichHenResponse> call, @NonNull Response<LichHenResponse> response) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN TẠO CUỘC HẸN");

                if (response.isSuccessful()) {
                    Toast.makeText(NewAppointmentActivity.this, "✅ Đặt lịch thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    // Log lỗi chi tiết
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown Error";
                        Log.e("API_ERROR", "Đặt lịch thất bại: " + errorBody);
                        Toast.makeText(NewAppointmentActivity.this, "Lỗi đặt lịch: " + response.code(), Toast.LENGTH_SHORT).show();
                    } catch (Exception e) { e.printStackTrace(); }
                }
            }

            @Override
            public void onFailure(@NonNull Call<LichHenResponse> call, @NonNull Throwable t) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN TẠO CUỘC HẸN");
                Toast.makeText(NewAppointmentActivity.this, "Lỗi kết nối đặt lịch!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}