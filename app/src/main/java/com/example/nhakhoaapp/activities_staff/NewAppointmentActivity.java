package com.example.nhakhoaapp.activities_staff;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView; // [QUAN TRỌNG]
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.example.nhakhoaapp.models.entity.DanhMucDichVu;
import com.example.nhakhoaapp.models.request.LichHenRequest;
import com.example.nhakhoaapp.models.response.LichHenResponse;
import com.example.nhakhoaapp.models.entity.NhanVien;

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
    private EditText etPatientName, etNote, etDateTime;
    private AutoCompleteTextView actvPatientPhone; // [ĐỔI] EditText -> AutoCompleteTextView
    private CheckBox cbNoPhone;
    private Spinner spinnerService, spinnerDoctor;
    private Button btnConfirmAppointment;

    // API & Data
    private ApiService apiService;
    private List<DanhMucDichVu> serviceList = new ArrayList<>();
    private List<NhanVien> doctorList = new ArrayList<>();
    
    // [MỚI] Danh sách bệnh nhân để gợi ý
    private List<BenhNhan> allPatientsList = new ArrayList<>();
    private List<String> patientPhoneList = new ArrayList<>();

    // Selected Data
    private String selectedServiceId = null;
    private String selectedServiceName = null;
    private String selectedDoctorId = null;
    
    // [MỚI] ID của bệnh nhân nếu chọn từ gợi ý AutoComplete
    private String preSelectedPatientId = null; 

    // Date Time Handling
    private final Calendar calendar = Calendar.getInstance();
    private String selectedIsoDateTime = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        apiService = ApiClient.getApiService();

        setupToolbar();
        initViews();
        setupEmptySpinners();

        // Load dữ liệu
        fetchServices();
        fetchDoctors();
        fetchPatientsForAutoComplete(); // [MỚI] Load danh sách bệnh nhân để gợi ý

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
        etNote = findViewById(R.id.et_note);
        etDateTime = findViewById(R.id.et_date_time);
        
        // [MỚI] AutoCompleteTextView
        actvPatientPhone = findViewById(R.id.actv_patient_phone);
        cbNoPhone = findViewById(R.id.cb_no_phone);

        spinnerService = findViewById(R.id.spinner_service);
        spinnerDoctor = findViewById(R.id.spinner_doctor);
        btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);
    }

    private void setupEmptySpinners() {
        // Tạo adapter rỗng để tránh lỗi null khi chưa load xong
        List<String> emptyList = new ArrayList<>();
        emptyList.add("Đang tải...");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, emptyList);
        spinnerService.setAdapter(adapter);
        spinnerDoctor.setAdapter(adapter);
    }
    
    // --- [MỚI] HÀM LOAD BỆNH NHÂN ĐỂ GỢI Ý ---
    private void fetchPatientsForAutoComplete() {
        apiService.getAllBenhNhan().enqueue(new Callback<List<BenhNhan>>() {
            @Override
            public void onResponse(@NonNull Call<List<BenhNhan>> call, @NonNull Response<List<BenhNhan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allPatientsList = response.body();
                    patientPhoneList.clear();
                    
                    for (BenhNhan bn : allPatientsList) {
                        // Chỉ thêm những người có SĐT thực (không phải null, ko phải chuỗi rỗng)
                        if (bn.getSo_dien_thoai() != null && !bn.getSo_dien_thoai().isEmpty() && !bn.getSo_dien_thoai().startsWith("GUEST_")) {
                            patientPhoneList.add(bn.getSo_dien_thoai());
                        }
                    }

                    // Cài đặt Adapter cho AutoCompleteTextView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(
                            NewAppointmentActivity.this,
                            android.R.layout.simple_dropdown_item_1line,
                            patientPhoneList
                    );
                    actvPatientPhone.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<BenhNhan>> call, @NonNull Throwable t) {
                // Fail silently, người dùng vẫn nhập tay được
            }
        });
    }

    private void setupEvents() {
        // 1. Sự kiện chọn SĐT từ gợi ý
        actvPatientPhone.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPhone = (String) parent.getItemAtPosition(position);
            
            // Tìm bệnh nhân tương ứng để điền tên
            for (BenhNhan bn : allPatientsList) {
                if (selectedPhone.equals(bn.getSo_dien_thoai())) {
                    etPatientName.setText(bn.getHo_ten());
                    preSelectedPatientId = bn.get_id(); // Lưu ID lại để dùng luôn
                    Toast.makeText(this, "Đã chọn bệnh nhân cũ: " + bn.getHo_ten(), Toast.LENGTH_SHORT).show();
                    break;
                }
            }
        });

        // 2. Reset ID nếu người dùng sửa lại số điện thoại sau khi chọn
        actvPatientPhone.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Nếu người dùng gõ thêm/xóa bớt, ID cũ không còn đúng nữa -> Reset về null
                preSelectedPatientId = null;
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // 3. Sự kiện Checkbox "Không dùng ĐT"
        cbNoPhone.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                actvPatientPhone.setText("");
                actvPatientPhone.setEnabled(false);
                actvPatientPhone.setHint("Hệ thống tự tạo mã ID");
                preSelectedPatientId = null; // Reset ID vì đang tạo mới Guest
            } else {
                actvPatientPhone.setEnabled(true);
                actvPatientPhone.setHint("Nhập SĐT để tìm...");
            }
        });

        // 4. Các sự kiện Spinner (Copy từ code cũ)
        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { selectedServiceId = null; selectedServiceName = null; return; }
                int realPos = position - 1;
                if (realPos >= 0 && realPos < serviceList.size()) {
                    selectedServiceId = serviceList.get(realPos).get_id();
                    selectedServiceName = serviceList.get(realPos).getTen_dich_vu();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { selectedDoctorId = null; return; }
                int realPos = position - 1;
                if (realPos >= 0 && realPos < doctorList.size()) {
                    selectedDoctorId = doctorList.get(realPos).get_id();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        etDateTime.setOnClickListener(v -> showDateTimePicker());
        btnConfirmAppointment.setOnClickListener(v -> processAppointment());
    }
    
    // --- CÁC HÀM LOAD DATA (Copy code cũ) ---
    private void fetchServices() {
        apiService.getAllDanhMucDichVu().enqueue(new Callback<List<DanhMucDichVu>>() {
            @Override
            public void onResponse(@NonNull Call<List<DanhMucDichVu>> call, @NonNull Response<List<DanhMucDichVu>> response) {
                List<String> names = new ArrayList<>();
                names.add("-- Chọn Dịch vụ --");
                if (response.isSuccessful() && response.body() != null) {
                    serviceList = response.body();
                    for (DanhMucDichVu s : serviceList) names.add(s.getTen_dich_vu());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewAppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, names);
                spinnerService.setAdapter(adapter);
            }
            @Override public void onFailure(@NonNull Call<List<DanhMucDichVu>> call, @NonNull Throwable t) {}
        });
    }

    private void fetchDoctors() {
        apiService.getAllNhanVien().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(@NonNull Call<List<NhanVien>> call, @NonNull Response<List<NhanVien>> response) {
                List<String> names = new ArrayList<>();
                names.add("-- Chọn Bác sĩ --");
                if (response.isSuccessful() && response.body() != null) {
                    doctorList = response.body();
                    for (NhanVien nv : doctorList) names.add(nv.getHo_ten());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<>(NewAppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, names);
                spinnerDoctor.setAdapter(adapter);
            }
            @Override public void onFailure(@NonNull Call<List<NhanVien>> call, @NonNull Throwable t) {}
        });
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

    // --- LOGIC XỬ LÝ CHÍNH ---
    private void processAppointment() {
        String pName = etPatientName.getText().toString().trim();
        String pPhone;

        if (cbNoPhone.isChecked()) {
            pPhone = "GUEST_" + System.currentTimeMillis();
        } else {
            pPhone = actvPatientPhone.getText().toString().trim();
        }

        if (pName.isEmpty()) { Toast.makeText(this, "Nhập tên bệnh nhân!", Toast.LENGTH_SHORT).show(); return; }
        if (!cbNoPhone.isChecked() && pPhone.isEmpty()) { Toast.makeText(this, "Nhập SĐT!", Toast.LENGTH_SHORT).show(); return; }
        if (selectedDoctorId == null) { Toast.makeText(this, "Chọn Bác sĩ!", Toast.LENGTH_SHORT).show(); return; }
        if (selectedIsoDateTime == null) { Toast.makeText(this, "Chọn Thời gian!", Toast.LENGTH_SHORT).show(); return; }

        btnConfirmAppointment.setEnabled(false);
        btnConfirmAppointment.setText("ĐANG XỬ LÝ...");

        // TỐI ƯU: Nếu đã chọn từ danh sách gợi ý (preSelectedPatientId != null) -> Bỏ qua bước check API
        if (!cbNoPhone.isChecked() && preSelectedPatientId != null) {
            postLichHenToApi(preSelectedPatientId, pName, pPhone);
            return;
        }

        // Nếu là khách vãng lai (No Phone) -> Tạo mới luôn
        if (cbNoPhone.isChecked()) {
            createNewPatientAndAppointment(pName, pPhone);
            return;
        }

        // Trường hợp còn lại: Nhập SĐT tay (không chọn gợi ý) -> Check API xem tồn tại chưa
        String finalPhone = pPhone;
        apiService.getAllBenhNhan().enqueue(new Callback<List<BenhNhan>>() {
            @Override
            public void onResponse(@NonNull Call<List<BenhNhan>> call, @NonNull Response<List<BenhNhan>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<BenhNhan> list = response.body();
                    String foundId = null;
                    for (BenhNhan bn : list) {
                        if (bn.getSo_dien_thoai() != null && bn.getSo_dien_thoai().equals(finalPhone)) {
                            foundId = bn.get_id(); break;
                        }
                    }
                    if (foundId != null) {
                        postLichHenToApi(foundId, pName, finalPhone);
                    } else {
                        createNewPatientAndAppointment(pName, finalPhone);
                    }
                } else {
                    createNewPatientAndAppointment(pName, finalPhone);
                }
            }
            @Override
            public void onFailure(@NonNull Call<List<BenhNhan>> call, @NonNull Throwable t) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN");
                Toast.makeText(NewAppointmentActivity.this, "Lỗi kết nối kiểm tra", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createNewPatientAndAppointment(String name, String phone) {
        BenhNhan newPatient = new BenhNhan();
        newPatient.setHo_ten(name);
        newPatient.setSo_dien_thoai(phone);
        newPatient.setDia_chi(phone.startsWith("GUEST_") ? "Khách vãng lai (Không SĐT)" : "Khách vãng lai");
        newPatient.setGioi_tinh("Khac");
        newPatient.setNgay_sinh("2024");
        newPatient.setPassword("123456");

        apiService.createBenhNhan(newPatient).enqueue(new Callback<BenhNhan>() {
            @Override
            public void onResponse(@NonNull Call<BenhNhan> call, @NonNull Response<BenhNhan> response) {
                if (response.isSuccessful() && response.body() != null) {
                    String newId = response.body().get_id();
                    postLichHenToApi(newId, name, phone);
                } else {
                    btnConfirmAppointment.setEnabled(true);
                    btnConfirmAppointment.setText("XÁC NHẬN");
                    Toast.makeText(NewAppointmentActivity.this, "Lỗi tạo hồ sơ", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(@NonNull Call<BenhNhan> call, @NonNull Throwable t) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN");
                Toast.makeText(NewAppointmentActivity.this, "Lỗi mạng tạo hồ sơ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void postLichHenToApi(String patientId, String pName, String pPhone) {
        String note = etNote.getText().toString().trim();
        String phoneDisplay = pPhone.startsWith("GUEST_") ? "Không SĐT" : pPhone;
        String finalReason = pName + " (" + phoneDisplay + ")";
        if (selectedServiceName != null) finalReason += " - DV: " + selectedServiceName;
        if (!note.isEmpty()) finalReason += " - Note: " + note;

        LichHenRequest request = new LichHenRequest();
        request.setId_benh_nhan(patientId);
        request.setId_bac_si(selectedDoctorId);
        request.setThoi_gian_hen(selectedIsoDateTime);
        request.setLy_do_kham(finalReason);
        request.setTrang_thai("ChoXacNhan");

        apiService.createLichHen(request).enqueue(new Callback<LichHenResponse>() {
            @Override
            public void onResponse(@NonNull Call<LichHenResponse> call, @NonNull Response<LichHenResponse> response) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN TẠO CUỘC HẸN");
                if (response.isSuccessful()) {
                    Toast.makeText(NewAppointmentActivity.this, "✅ Đặt lịch thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(NewAppointmentActivity.this, "Lỗi Server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(@NonNull Call<LichHenResponse> call, @NonNull Throwable t) {
                // Xử lý lỗi JsonSyntaxException nếu có
                String msg = t.getMessage();
                if (msg != null && (msg.contains("JsonSyntax") || msg.contains("IllegalState"))) {
                    Toast.makeText(NewAppointmentActivity.this, "✅ Đặt lịch thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    btnConfirmAppointment.setEnabled(true);
                    btnConfirmAppointment.setText("XÁC NHẬN");
                    Toast.makeText(NewAppointmentActivity.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}