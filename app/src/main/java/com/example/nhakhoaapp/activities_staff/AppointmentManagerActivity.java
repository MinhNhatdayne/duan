package com.example.nhakhoaapp.activities_staff;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.SingleAppointmentAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.request.LichHenRequest;
import com.example.nhakhoaapp.models.response.LichHenResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentManagerActivity extends AppCompatActivity {

    // Views UI
    private RecyclerView rvAppointments;
    private Button btnNewAppointment;
    private BottomNavigationView bottomNavigationView;

    // Filter Views
    private TextView tvFilterDate;
    private EditText etSearchDoctor;
    private ImageView btnClearFilter;

    // Data Logic
    private SingleAppointmentAdapter adapter;
    private List<Object> displayList = new ArrayList<>(); // List dùng để hiển thị (đã lọc)
    private List<LichHenResponse> masterList = new ArrayList<>(); // List gốc chứa toàn bộ dữ liệu
    private ApiService apiService;

    // Filter Variables
    private String selectedDateFilter = ""; // Lưu ngày lọc dạng yyyy-MM-dd
    private Calendar filterCalendar = Calendar.getInstance();

    // Biến dùng cho Dialog chỉnh sửa (Edit)
    private Calendar tempCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_manager);

        apiService = ApiClient.getApiService();

        initViews();
        setupRecyclerView();
        setupListeners();
        setupFilterLogic(); // [MỚI] Cài đặt bộ lọc
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadAppointments();
        if (bottomNavigationView != null)
            bottomNavigationView.setSelectedItemId(R.id.nav_staff_appointments);
    }

    private void initViews() {
        rvAppointments = findViewById(R.id.recycler_appointments);
        btnNewAppointment = findViewById(R.id.btn_new_appointment);
        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);

        // Ánh xạ Filter Views
        tvFilterDate = findViewById(R.id.tv_filter_date);
        etSearchDoctor = findViewById(R.id.et_search_doctor);
        btnClearFilter = findViewById(R.id.btn_clear_filter);
    }

    private void setupListeners() {
        btnNewAppointment.setOnClickListener(v -> {
            Intent intent = new Intent(AppointmentManagerActivity.this, NewAppointmentActivity.class);
            startActivity(intent);
        });

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_staff_home) {
                startActivity(new Intent(this, StaffDashboardActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            } else if (id == R.id.nav_staff_schedule) {
                startActivity(new Intent(this, DailyScheduleActivity.class));
                overridePendingTransition(0, 0); finish(); return true;
            } else if (id == R.id.nav_staff_appointments) {
                return true;
            }
            return false;
        });
    }

    // ==========================================
    // KHU VỰC BỘ LỌC (FILTER LOGIC)
    // ==========================================
    private void setupFilterLogic() {
        // 1. Chọn ngày lọc
        tvFilterDate.setOnClickListener(v -> {
            new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
                filterCalendar.set(year, month, dayOfMonth);

                // Format để hiển thị đẹp
                SimpleDateFormat sdfDisplay = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                tvFilterDate.setText(sdfDisplay.format(filterCalendar.getTime()));
                tvFilterDate.setTextColor(getResources().getColor(R.color.primary_blue));

                // Format để so sánh (yyyy-MM-dd)
                SimpleDateFormat sdfIso = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                selectedDateFilter = sdfIso.format(filterCalendar.getTime());

                applyFilters(); // Kích hoạt lọc
            }, filterCalendar.get(Calendar.YEAR), filterCalendar.get(Calendar.MONTH), filterCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        // 2. Tìm kiếm Bác sĩ (lắng nghe khi gõ)
        etSearchDoctor.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                applyFilters(); // Lọc ngay khi gõ
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // 3. Xóa bộ lọc
        btnClearFilter.setOnClickListener(v -> {
            selectedDateFilter = "";
            tvFilterDate.setText("Lọc theo ngày");
            tvFilterDate.setTextColor(getResources().getColor(android.R.color.darker_gray));
            etSearchDoctor.setText("");
            applyFilters(); // Reset danh sách về gốc
        });
    }

    private void applyFilters() {
        List<LichHenResponse> filteredList = new ArrayList<>();
        String searchDoc = etSearchDoctor.getText().toString().toLowerCase().trim();

        for (LichHenResponse item : masterList) {
            boolean isDateMatch = true;
            boolean isDocMatch = true;

            // Kiểm tra Ngày
            if (!selectedDateFilter.isEmpty()) {
                // Giả sử item.getThoi_gian_hen() là ISO String "2024-12-20T09:00..."
                if (item.getThoi_gian_hen() != null && item.getThoi_gian_hen().length() >= 10) {
                    String itemDate = item.getThoi_gian_hen().substring(0, 10);
                    if (!itemDate.equals(selectedDateFilter)) {
                        isDateMatch = false;
                    }
                } else {
                    isDateMatch = false;
                }
            }

            // Kiểm tra tên Bác sĩ
            if (!searchDoc.isEmpty()) {
                String docName = item.getTen_bac_si();
                if (docName == null || !docName.toLowerCase().contains(searchDoc)) {
                    isDocMatch = false;
                }
            }

            if (isDateMatch && isDocMatch) {
                filteredList.add(item);
            }
        }

        updateRecyclerView(filteredList);
    }

    private void updateRecyclerView(List<LichHenResponse> data) {
        displayList.clear();
        displayList.addAll(data);
        adapter.notifyDataSetChanged();
    }

    // ==========================================
    // KHU VỰC DATA & ADAPTER
    // ==========================================
    private void setupRecyclerView() {
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new SingleAppointmentAdapter(this, displayList, (item, view) -> {
            showPopupMenu(item, view);
        });
        rvAppointments.setAdapter(adapter);
    }

    private void loadAppointments() {
        apiService.getAllLichHen().enqueue(new Callback<List<LichHenResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<LichHenResponse>> call, @NonNull Response<List<LichHenResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    masterList.clear();
                    masterList.addAll(response.body());

                    // Sắp xếp danh sách gốc (Mới nhất lên đầu)
                    sortList(masterList);

                    // Áp dụng bộ lọc hiện tại (nếu có) để hiển thị
                    applyFilters();
                } else {
                    Toast.makeText(AppointmentManagerActivity.this, "Không có lịch hẹn", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<LichHenResponse>> call, @NonNull Throwable t) {
                Toast.makeText(AppointmentManagerActivity.this, "Lỗi kết nối", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sortList(List<LichHenResponse> list) {
        try {
            Collections.sort(list, (o1, o2) -> {
                if (o1.getThoi_gian_hen() == null || o2.getThoi_gian_hen() == null) return 0;
                return o2.getThoi_gian_hen().compareTo(o1.getThoi_gian_hen());
            });
        } catch (Exception e) { e.printStackTrace(); }
    }

    // ==========================================
    // MENU & CÁC CHỨC NĂNG SỬA/XÓA
    // ==========================================
    private void showPopupMenu(LichHenResponse item, View view) {
        PopupMenu popup = new PopupMenu(this, view);
        popup.getMenu().add(0, 0, 0, "Sửa thông tin");
        popup.getMenu().add(0, 1, 1, "Cập nhật trạng thái");
        popup.getMenu().add(0, 2, 2, "Xóa lịch hẹn");

        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case 0: showEditDialog(item); return true;
                case 1: showUpdateStatusDialog(item); return true;
                case 2: showDeleteConfirmation(item); return true;
                default: return false;
            }
        });
        popup.show();
    }

    // --- 1. SỬA THÔNG TIN (DIALOG) ---
    private void showEditDialog(LichHenResponse item) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_edit_appointment, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        TextView tvName = view.findViewById(R.id.tv_patient_name_display);
        TextView tvPhone = view.findViewById(R.id.tv_patient_phone_display);
        TextView tvService = view.findViewById(R.id.tv_service_display);
        TextView tvDate = view.findViewById(R.id.tv_edit_date);
        TextView tvTime = view.findViewById(R.id.tv_edit_time);
        EditText etNote = view.findViewById(R.id.et_edit_reason);
        Button btnSave = view.findViewById(R.id.btn_save_edit);
        Button btnCancel = view.findViewById(R.id.btn_cancel_edit);

        // Đổ dữ liệu cũ
        tvName.setText("Tên: " + item.getTen_benh_nhan());
        String sdt = item.getSdt_benh_nhan();
        tvPhone.setText("SĐT: " + ((sdt != null && sdt.startsWith("GUEST_")) ? "Khách vãng lai" : (sdt != null ? sdt : "Chưa có")));

        // Tách chuỗi Lý do khám
        String fullReason = item.getLy_do_kham();
        String currentService = "Khám tổng quát";
        String currentNote = "";
        if (fullReason != null) {
            if (fullReason.contains(" - Note: ")) {
                String[] parts = fullReason.split(" - Note: ");
                currentService = parts[0];
                if (parts.length > 1) currentNote = parts[1];
            } else if (fullReason.startsWith("Note: ")) {
                currentNote = fullReason;
            } else {
                currentService = fullReason;
            }
        }
        tvService.setText(currentService);
        etNote.setText(currentNote);

        parseIsoToCalendar(item.getThoi_gian_hen(), tempCalendar);
        updateDialogDateTimeDisplay(tvDate, tvTime);

        // Sự kiện chọn ngày giờ
        tvDate.setOnClickListener(v -> new DatePickerDialog(this, (view1, year, month, dayOfMonth) -> {
            tempCalendar.set(Calendar.YEAR, year);
            tempCalendar.set(Calendar.MONTH, month);
            tempCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateDialogDateTimeDisplay(tvDate, tvTime);
        }, tempCalendar.get(Calendar.YEAR), tempCalendar.get(Calendar.MONTH), tempCalendar.get(Calendar.DAY_OF_MONTH)).show());

        tvTime.setOnClickListener(v -> new TimePickerDialog(this, (view1, hourOfDay, minute) -> {
            tempCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
            tempCalendar.set(Calendar.MINUTE, minute);
            tempCalendar.set(Calendar.SECOND, 0);
            updateDialogDateTimeDisplay(tvDate, tvTime);
        }, tempCalendar.get(Calendar.HOUR_OF_DAY), tempCalendar.get(Calendar.MINUTE), true).show());

        String finalCurrentService = currentService;
        btnSave.setOnClickListener(v -> {
            String newNote = etNote.getText().toString().trim();
            String newIsoTime = getIsoStringFromCalendar(tempCalendar);
            String finalReasonToSend = finalCurrentService;
            if (!newNote.isEmpty()) finalReasonToSend += " - Note: " + newNote;

            updateAppointmentInfo(item, newIsoTime, finalReasonToSend, item.getTrang_thai(), dialog);
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void updateAppointmentInfo(LichHenResponse item, String newIsoTime, String newReason, String status, AlertDialog dialog) {
        LichHenRequest request = new LichHenRequest();
        request.setId_benh_nhan(item.getRawBenhNhanId());
        request.setId_bac_si(item.getRawBacSiId());
        request.setThoi_gian_hen(newIsoTime);
        request.setLy_do_kham(newReason);
        request.setTrang_thai(status);

        apiService.updateLichHen(item.get_id(), request).enqueue(new Callback<LichHenResponse>() {
            @Override
            public void onResponse(Call<LichHenResponse> call, Response<LichHenResponse> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AppointmentManagerActivity.this, "Đã sửa thành công!", Toast.LENGTH_SHORT).show();
                    if(dialog != null) dialog.dismiss();
                    loadAppointments(); // Load lại toàn bộ
                } else {
                    Toast.makeText(AppointmentManagerActivity.this, "Lỗi server: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<LichHenResponse> call, Throwable t) {
                Toast.makeText(AppointmentManagerActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- 2. ĐỔI TRẠNG THÁI ---
    private void showUpdateStatusDialog(LichHenResponse item) {
        String[] statuses = {"ChoXacNhan", "DaXacNhan", "DaKham", "Huy"};
        String[] displayStatuses = {"Chờ xác nhận", "Đã xác nhận", "Hoàn thành", "Hủy lịch"};
        int checkedItem = -1;
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(item.getTrang_thai())) { checkedItem = i; break; }
        }

        new AlertDialog.Builder(this)
                .setTitle("Cập nhật trạng thái")
                .setSingleChoiceItems(displayStatuses, checkedItem, (dialog, which) -> {
                    // Tái sử dụng hàm updateAppointmentInfo
                    updateAppointmentInfo(item, item.getThoi_gian_hen(), item.getLy_do_kham(), statuses[which], null);
                    dialog.dismiss();
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    // --- 3. XÓA ---
    private void showDeleteConfirmation(LichHenResponse item) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa lịch hẹn này?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteAppointment(item.get_id()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteAppointment(String id) {
        apiService.deleteLichHen(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(AppointmentManagerActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    loadAppointments();
                } else {
                    Toast.makeText(AppointmentManagerActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(AppointmentManagerActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- HELPER DATE TIME ---
    private void updateDialogDateTimeDisplay(TextView tvDate, TextView tvTime) {
        SimpleDateFormat sdfDate = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm", Locale.getDefault());
        tvDate.setText(sdfDate.format(tempCalendar.getTime()));
        tvTime.setText(sdfTime.format(tempCalendar.getTime()));
    }

    private void parseIsoToCalendar(String isoDate, Calendar cal) {
        if (isoDate == null) return;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = sdf.parse(isoDate);
            if (date != null) cal.setTime(date);
        } catch (Exception e) { e.printStackTrace(); }
    }

    private String getIsoStringFromCalendar(Calendar cal) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
        return sdf.format(cal.getTime());
    }
}