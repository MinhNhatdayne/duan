package com.example.nhakhoaapp.activities_staff;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DateSlotAdapter;
import com.example.nhakhoaapp.adapters.TimeSlotAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.BenhNhan;
import com.example.nhakhoaapp.models.entity.DanhMucDichVu;
import com.example.nhakhoaapp.models.entity.NhanVien;
import com.example.nhakhoaapp.models.request.LichHenRequest;
import com.example.nhakhoaapp.models.response.LichHenResponse;
import com.example.nhakhoaapp.models_adapter.DateSlot;
import com.example.nhakhoaapp.models_adapter.TimeSlot;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewAppointmentActivity extends AppCompatActivity {

    // Views
    private EditText etPatientName, etNote;
    private AutoCompleteTextView actvPatientPhone;
    private CheckBox cbNoPhone;
    private Spinner spinnerService, spinnerDoctor;
    private Button btnConfirmAppointment;

    // Date & Time Views
    private RecyclerView rvDateSlots, rvTimeSlots;
    private TextView tvMonthYear;
    private ImageView btnNextMonth, btnPrevMonth;

    // Adapters & Lists
    private DateSlotAdapter dateAdapter;
    private TimeSlotAdapter timeAdapter;
    private List<DateSlot> dateSlotList = new ArrayList<>();
    private List<TimeSlot> timeSlotList = new ArrayList<>();

    // Data
    private ApiService apiService;
    private List<DanhMucDichVu> serviceList = new ArrayList<>();
    private List<NhanVien> doctorList = new ArrayList<>();
    private List<BenhNhan> allPatientsList = new ArrayList<>();
    private List<String> patientPhoneList = new ArrayList<>();

    // Selected Data
    private String selectedServiceId = null;
    private String selectedServiceName = null;
    private String selectedDoctorId = null;
    private String preSelectedPatientId = null;

    // Time Logic
    private Calendar currentCalendar = Calendar.getInstance();
    private DateSlot selectedDateSlot = null;
    private String selectedTimeSlot = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);

        apiService = ApiClient.getApiService();

        setupToolbar();
        initViews();
        setupEmptySpinners();

        setupDateRecyclerView();
        setupTimeRecyclerView();

        fetchServices();
        fetchDoctors();
        fetchPatientsForAutoComplete();

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
        actvPatientPhone = findViewById(R.id.actv_patient_phone);
        cbNoPhone = findViewById(R.id.cb_no_phone);
        spinnerService = findViewById(R.id.spinner_service);
        spinnerDoctor = findViewById(R.id.spinner_doctor);
        btnConfirmAppointment = findViewById(R.id.btn_confirm_appointment);

        rvDateSlots = findViewById(R.id.rvDateSlots);
        rvTimeSlots = findViewById(R.id.rvTimeSlots);
        tvMonthYear = findViewById(R.id.tv_month_year);
        btnNextMonth = findViewById(R.id.btn_next_month);
        btnPrevMonth = findViewById(R.id.btn_prev_month);
    }

    // --- SETUP DATE & TIME ---

    private void setupDateRecyclerView() {
        dateSlotList = generateDaysOfMonth();
        dateAdapter = new DateSlotAdapter(dateSlotList, (slot, pos) -> {
            selectedDateSlot = slot;
            selectedTimeSlot = null;
            timeAdapter.clearSelection();
            updateConfirmButtonState();

            if (selectedDoctorId == null) {
                Toast.makeText(this, "Vui lòng chọn Bác sĩ trước!", Toast.LENGTH_SHORT).show();
                resetTimeSlots();
            } else {
                loadTimeSlotsForDate(slot.getFullDateString());
            }
        });

        rvDateSlots.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        rvDateSlots.setAdapter(dateAdapter);
        updateMonthHeader();
    }

    private void setupTimeRecyclerView() {
        timeSlotList = generateDefaultTimeSlots();
        timeAdapter = new TimeSlotAdapter(timeSlotList, slot -> {
            if (slot.isAvailable()) {
                selectedTimeSlot = slot.getTime();
                updateConfirmButtonState();
            } else {
                Toast.makeText(this, "Giờ này bác sĩ bận!", Toast.LENGTH_SHORT).show();
            }
        });

        rvTimeSlots.setLayoutManager(new GridLayoutManager(this, 3));
        rvTimeSlots.setAdapter(timeAdapter);
    }

    private void resetTimeSlots() {
        timeSlotList.clear();
        timeSlotList.addAll(generateDefaultTimeSlots());
        timeAdapter.notifyDataSetChanged();
    }

    private void loadTimeSlotsForDate(String dateString) {
        resetTimeSlots();
        apiService.getBusySlots(selectedDoctorId, dateString).enqueue(new Callback<List<String>>() {
            @Override
            public void onResponse(Call<List<String>> call, Response<List<String>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<String> busySlots = response.body();
                    for (TimeSlot slot : timeSlotList) {
                        if (busySlots.contains(slot.getTime())) {
                            slot.setAvailable(false);
                        }
                    }
                    timeAdapter.notifyDataSetChanged();
                }
            }
            @Override
            public void onFailure(Call<List<String>> call, Throwable t) {}
        });
    }

    private void updateConfirmButtonState() {
        boolean isReady = selectedDateSlot != null && selectedTimeSlot != null && selectedDoctorId != null;
        btnConfirmAppointment.setEnabled(isReady);
        if (isReady) {
            btnConfirmAppointment.setBackgroundTintList(getResources().getColorStateList(R.color.primary_blue));
        } else {
            btnConfirmAppointment.setBackgroundTintList(getResources().getColorStateList(android.R.color.darker_gray));
        }
    }

    private void updateMonthHeader() {
        SimpleDateFormat fmt = new SimpleDateFormat("MMMM yyyy", new Locale("vi", "VN"));
        tvMonthYear.setText(fmt.format(currentCalendar.getTime()));
    }

    private void refreshDates() {
        dateSlotList.clear();
        dateSlotList.addAll(generateDaysOfMonth());
        dateAdapter.notifyDataSetChanged();
        selectedDateSlot = null;
        selectedTimeSlot = null;
        timeAdapter.clearSelection();
        updateConfirmButtonState();
        updateMonthHeader();
    }

    private List<DateSlot> generateDaysOfMonth() {
        List<DateSlot> list = new ArrayList<>();
        Calendar temp = (Calendar) currentCalendar.clone();
        temp.set(Calendar.DAY_OF_MONTH, 1);
        int max = temp.getActualMaximum(Calendar.DAY_OF_MONTH);
        SimpleDateFormat dowFormat = new SimpleDateFormat("EEE", new Locale("vi", "VN"));
        SimpleDateFormat fullFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

        for (int i = 1; i <= max; i++) {
            temp.set(Calendar.DAY_OF_MONTH, i);
            DateSlot slot = new DateSlot(dowFormat.format(temp.getTime()), i);
            slot.setFullDateString(fullFormat.format(temp.getTime()));
            list.add(slot);
        }
        return list;
    }

    private List<TimeSlot> generateDefaultTimeSlots() {
        List<TimeSlot> list = new ArrayList<>();
        String[] times = {"08:00", "08:30", "09:00", "09:30", "10:00", "10:30",
                "14:00", "14:30", "15:00", "15:30", "16:00"};
        for (String t : times) list.add(new TimeSlot(t, true));
        return list;
    }

    private String generateISODateTime(String date, String time) {
        try {
            String input = date + " " + time;
            SimpleDateFormat localFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault());
            Date parsed = localFmt.parse(input);
            SimpleDateFormat isoFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            isoFmt.setTimeZone(TimeZone.getTimeZone("UTC"));
            return isoFmt.format(parsed);
        } catch (ParseException e) { return null; }
    }

    // --- SETUP SPINNERS & EVENTS ---

    private void setupEmptySpinners() {
        List<String> empty = new ArrayList<>(); empty.add("Đang tải...");
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, empty);
        spinnerService.setAdapter(adapter); spinnerDoctor.setAdapter(adapter);
    }

    private void fetchServices() {
        apiService.getAllDanhMucDichVu().enqueue(new Callback<List<DanhMucDichVu>>() {
            @Override
            public void onResponse(Call<List<DanhMucDichVu>> call, Response<List<DanhMucDichVu>> response) {
                List<String> names = new ArrayList<>(); names.add("-- Chọn Dịch vụ --");
                if (response.body() != null) {
                    serviceList = response.body();
                    for(DanhMucDichVu s : serviceList) names.add(s.getTen_dich_vu());
                }
                spinnerService.setAdapter(new ArrayAdapter<>(NewAppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, names));
            }
            @Override public void onFailure(Call<List<DanhMucDichVu>> call, Throwable t) {}
        });
    }

    private void fetchDoctors() {
        apiService.getAllNhanVien().enqueue(new Callback<List<NhanVien>>() {
            @Override
            public void onResponse(Call<List<NhanVien>> call, Response<List<NhanVien>> response) {
                List<String> names = new ArrayList<>(); names.add("-- Chọn Bác sĩ --");
                if (response.body() != null) {
                    doctorList = response.body();
                    for(NhanVien nv : doctorList) names.add(nv.getHo_ten());
                }
                spinnerDoctor.setAdapter(new ArrayAdapter<>(NewAppointmentActivity.this, android.R.layout.simple_spinner_dropdown_item, names));
            }
            @Override public void onFailure(Call<List<NhanVien>> call, Throwable t) {}
        });
    }

    private void fetchPatientsForAutoComplete() {
        apiService.getAllBenhNhan().enqueue(new Callback<List<BenhNhan>>() {
            @Override
            public void onResponse(Call<List<BenhNhan>> call, Response<List<BenhNhan>> response) {
                if (response.body() != null) {
                    allPatientsList = response.body();
                    patientPhoneList.clear();
                    for(BenhNhan bn : allPatientsList) {
                        if (bn.getSo_dien_thoai() != null && !bn.getSo_dien_thoai().isEmpty() && !bn.getSo_dien_thoai().startsWith("GUEST_")) {
                            patientPhoneList.add(bn.getSo_dien_thoai());
                        }
                    }
                    actvPatientPhone.setAdapter(new ArrayAdapter<>(NewAppointmentActivity.this, android.R.layout.simple_dropdown_item_1line, patientPhoneList));
                }
            }
            @Override public void onFailure(Call<List<BenhNhan>> call, Throwable t) {}
        });
    }

    private void setupEvents() {
        actvPatientPhone.setOnItemClickListener((parent, view, position, id) -> {
            String selectedPhone = (String) parent.getItemAtPosition(position);
            for (BenhNhan bn : allPatientsList) {
                if (selectedPhone.equals(bn.getSo_dien_thoai())) {
                    etPatientName.setText(bn.getHo_ten());
                    preSelectedPatientId = bn.get_id();
                    break;
                }
            }
        });

        actvPatientPhone.addTextChangedListener(new TextWatcher() {
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            public void onTextChanged(CharSequence s, int start, int before, int count) { preSelectedPatientId = null; }
            public void afterTextChanged(Editable s) {}
        });

        cbNoPhone.setOnCheckedChangeListener((bv, isChecked) -> {
            if (isChecked) { actvPatientPhone.setText(""); actvPatientPhone.setEnabled(false); actvPatientPhone.setHint("Tự tạo ID"); preSelectedPatientId = null; }
            else { actvPatientPhone.setEnabled(true); actvPatientPhone.setHint("Nhập SĐT..."); }
        });

        spinnerService.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) { selectedServiceId = null; selectedServiceName = null; }
                else {
                    selectedServiceId = serviceList.get(position - 1).get_id();
                    selectedServiceName = serviceList.get(position - 1).getTen_dich_vu();
                }
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        spinnerDoctor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    selectedDoctorId = null;
                    resetTimeSlots();
                } else {
                    selectedDoctorId = doctorList.get(position - 1).get_id();
                    if (selectedDateSlot != null) loadTimeSlotsForDate(selectedDateSlot.getFullDateString());
                }
                updateConfirmButtonState();
            }
            @Override public void onNothingSelected(AdapterView<?> parent) {}
        });

        btnNextMonth.setOnClickListener(v -> { currentCalendar.add(Calendar.MONTH, 1); refreshDates(); });
        btnPrevMonth.setOnClickListener(v -> { currentCalendar.add(Calendar.MONTH, -1); refreshDates(); });

        btnConfirmAppointment.setOnClickListener(v -> processAppointment());
    }

    private void processAppointment() {
        String pName = etPatientName.getText().toString().trim();
        String pPhone = cbNoPhone.isChecked() ? "GUEST_" + System.currentTimeMillis() : actvPatientPhone.getText().toString().trim();

        if (pName.isEmpty() || (!cbNoPhone.isChecked() && pPhone.isEmpty())) {
            Toast.makeText(this, "Thiếu thông tin bệnh nhân!", Toast.LENGTH_SHORT).show();
            return;
        }

        String isoDateTime = generateISODateTime(selectedDateSlot.getFullDateString(), selectedTimeSlot);
        if (isoDateTime == null) {
            Toast.makeText(this, "Lỗi thời gian!", Toast.LENGTH_SHORT).show();
            return;
        }

        btnConfirmAppointment.setEnabled(false);
        btnConfirmAppointment.setText("ĐANG XỬ LÝ...");

        if (!cbNoPhone.isChecked() && preSelectedPatientId != null) {
            postLichHenToApi(preSelectedPatientId, pName, pPhone, isoDateTime);
        } else if (cbNoPhone.isChecked()) {
            createNewPatientAndAppointment(pName, pPhone, isoDateTime);
        } else {
            checkAndCreate(pName, pPhone, isoDateTime);
        }
    }

    private void checkAndCreate(String name, String phone, String isoTime) {
        apiService.getAllBenhNhan().enqueue(new Callback<List<BenhNhan>>() {
            @Override
            public void onResponse(Call<List<BenhNhan>> call, Response<List<BenhNhan>> response) {
                String foundId = null;
                if (response.body() != null) {
                    for (BenhNhan bn : response.body()) {
                        if (phone.equals(bn.getSo_dien_thoai())) { foundId = bn.get_id(); break; }
                    }
                }
                if (foundId != null) postLichHenToApi(foundId, name, phone, isoTime);
                else createNewPatientAndAppointment(name, phone, isoTime);
            }
            @Override public void onFailure(Call<List<BenhNhan>> call, Throwable t) {
                createNewPatientAndAppointment(name, phone, isoTime);
            }
        });
    }

    private void createNewPatientAndAppointment(String name, String phone, String isoTime) {
        BenhNhan newPatient = new BenhNhan();
        newPatient.setHo_ten(name); newPatient.setSo_dien_thoai(phone);
        newPatient.setDia_chi(phone.startsWith("GUEST_") ? "Khách vãng lai (Không SĐT)" : "Khách vãng lai");
        newPatient.setGioi_tinh("Khac"); newPatient.setNgay_sinh("2024"); newPatient.setPassword("123456");

        apiService.createBenhNhan(newPatient).enqueue(new Callback<BenhNhan>() {
            @Override
            public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
                if (response.body() != null) postLichHenToApi(response.body().get_id(), name, phone, isoTime);
                else { btnConfirmAppointment.setEnabled(true); Toast.makeText(NewAppointmentActivity.this, "Lỗi tạo hồ sơ", Toast.LENGTH_SHORT).show(); }
            }
            @Override public void onFailure(Call<BenhNhan> call, Throwable t) { btnConfirmAppointment.setEnabled(true); }
        });
    }

    // --- [HÀM ĐÃ SỬA] ĐỂ LƯU ĐÚNG ĐỊNH DẠNG: "Dịch vụ: ... - Note: ..." ---
    private void postLichHenToApi(String patientId, String pName, String pPhone, String isoTime) {
        String note = etNote.getText().toString().trim();
        String finalReason = "";

        // 1. Thêm Dịch vụ
        if (selectedServiceName != null && !selectedServiceName.isEmpty()) {
            finalReason += "Dịch vụ: " + selectedServiceName;
        } else {
            finalReason += "Dịch vụ: Khám tổng quát"; // Mặc định
        }

        // 2. Thêm Ghi chú (Dùng từ khóa " - Note: " để Adapter cắt chuỗi)
        if (!note.isEmpty()) {
            finalReason += " - Note: " + note;
        }

        // 3. Gửi Request
        LichHenRequest request = new LichHenRequest();
        request.setId_benh_nhan(patientId);
        request.setId_bac_si(selectedDoctorId);
        request.setThoi_gian_hen(isoTime);
        request.setLy_do_kham(finalReason); // Đã định dạng chuẩn
        request.setTrang_thai("ChoXacNhan");

        apiService.createLichHen(request).enqueue(new Callback<LichHenResponse>() {
            @Override
            public void onResponse(Call<LichHenResponse> call, Response<LichHenResponse> response) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN TẠO CUỘC HẸN");
                if (response.isSuccessful()) {
                    Toast.makeText(NewAppointmentActivity.this, "✅ Đặt lịch thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
                    Toast.makeText(NewAppointmentActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LichHenResponse> call, Throwable t) {
                btnConfirmAppointment.setEnabled(true);
                btnConfirmAppointment.setText("XÁC NHẬN");
                String msg = t.getMessage();
                if (msg != null && (msg.contains("JsonSyntax") || msg.contains("IllegalState"))) {
                    Toast.makeText(NewAppointmentActivity.this, "✅ Đặt lịch thành công!", Toast.LENGTH_LONG).show();
                    finish();
                } else {
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