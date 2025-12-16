package com.example.nhakhoaapp.activities_staff;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu; // Import PopupMenu
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapters.DailyAppointmentAdapter;
import com.example.nhakhoaapp.api.ApiClient;
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.request.LichHenRequest;
import com.example.nhakhoaapp.models.response.LichHenResponse;
import com.google.android.material.bottomnavigation.BottomNavigationView;

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

public class DailyScheduleActivity extends AppCompatActivity {

    private RecyclerView rvAppointments;
    private TextView tvCurrentDate, tvAppointmentCount;
    private BottomNavigationView bottomNavigationView;
    private ApiService apiService;
    private DailyAppointmentAdapter adapter;
    private Calendar tempCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_schedule);

        apiService = ApiClient.getApiService();

        rvAppointments = findViewById(R.id.rv_daily_appointments);
        tvCurrentDate = findViewById(R.id.tv_current_date);
        tvAppointmentCount = findViewById(R.id.tv_appointment_count);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle("Lịch Khám Hôm Nay");
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        setupRecyclerView();
        updateDateDisplay();
        fetchDailySchedule();

        bottomNavigationView = findViewById(R.id.bottom_navigation_staff);
        bottomNavigationView.setOnItemSelectedListener(this::handleStaffNavigation);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (bottomNavigationView != null) bottomNavigationView.setSelectedItemId(R.id.nav_staff_schedule);
        fetchDailySchedule();
    }

    private boolean handleStaffNavigation(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_staff_home) {
            startActivity(new Intent(this, StaffDashboardActivity.class));
            overridePendingTransition(0, 0); finish(); return true;
        } else if (id == R.id.nav_staff_schedule) {
            return true;
        } else if (id == R.id.nav_staff_appointments) {
            startActivity(new Intent(this, AppointmentManagerActivity.class));
            overridePendingTransition(0, 0); return true;
        }
        return false;
    }

    private void setupRecyclerView() {
        rvAppointments.setLayoutManager(new LinearLayoutManager(this));
        adapter = new DailyAppointmentAdapter(this, new ArrayList<>());

        // [CẬP NHẬT LISTENER] Xử lý click và click 3 chấm
        adapter.setOnItemActionClickListener(new DailyAppointmentAdapter.OnItemActionClickListener() {
            @Override
            public void onMoreActionClick(LichHenResponse item, View view) {
                // Khi bấm nút 3 chấm -> Hiện Menu
                showPopupMenu(item, view);
            }

            @Override
            public void onItemClick(LichHenResponse item) {
                // Khi bấm vào item -> Mở nhanh dialog Sửa (hoặc xem chi tiết)
                showEditDialog(item);
            }
        });

        rvAppointments.setAdapter(adapter);
    }

    // ==========================================
    // 1. MENU POPUP (Gom 3 chức năng)
    // ==========================================
    private void showPopupMenu(LichHenResponse item, View view) {
        PopupMenu popup = new PopupMenu(this, view);
        // Thêm các options vào menu
        popup.getMenu().add(0, 0, 0, "Sửa thông tin");
        popup.getMenu().add(0, 1, 1, "Cập nhật trạng thái");
        popup.getMenu().add(0, 2, 2, "Xóa lịch hẹn");

        popup.setOnMenuItemClickListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case 0: // Sửa
                    showEditDialog(item);
                    return true;
                case 1: // Trạng thái
                    showUpdateStatusDialog(item);
                    return true;
                case 2: // Xóa
                    showDeleteConfirmation(item);
                    return true;
                default:
                    return false;
            }
        });
        popup.show();
    }

    // ==========================================
    // 2. CHỨC NĂNG SỬA THÔNG TIN
    // ==========================================
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

        tvName.setText("Tên: " + item.getTen_benh_nhan());
        String sdt = item.getSdt_benh_nhan();
        tvPhone.setText("SĐT: " + ((sdt != null && sdt.startsWith("GUEST_")) ? "Khách vãng lai" : sdt));

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

            updateFullInfo(item, newIsoTime, finalReasonToSend, item.getTrang_thai(), dialog);
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());
        dialog.show();
    }

    private void updateFullInfo(LichHenResponse item, String newIsoTime, String newReason, String status, AlertDialog dialog) {
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
                    Toast.makeText(DailyScheduleActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    if(dialog != null) dialog.dismiss();
                    fetchDailySchedule();
                } else {
                    Toast.makeText(DailyScheduleActivity.this, "Lỗi: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<LichHenResponse> call, Throwable t) { Toast.makeText(DailyScheduleActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show(); }
        });
    }

    // ==========================================
    // 3. CHỨC NĂNG CẬP NHẬT TRẠNG THÁI
    // ==========================================
    private void showUpdateStatusDialog(LichHenResponse item) {
        String[] statuses = {"ChoXacNhan", "DaXacNhan", "DaKham", "Huy"};
        String[] displayStatuses = {"Chờ xác nhận", "Đã xác nhận (Chờ khám)", "Đã khám (Hoàn tất)", "Hủy lịch"};

        int checkedItem = -1;
        for (int i = 0; i < statuses.length; i++) {
            if (statuses[i].equals(item.getTrang_thai())) { checkedItem = i; break; }
        }

        new AlertDialog.Builder(this)
                .setTitle("Cập nhật trạng thái")
                .setSingleChoiceItems(displayStatuses, checkedItem, (dialog, which) -> {
                    // Tái sử dụng hàm updateFullInfo nhưng giữ nguyên thời gian và lý do cũ
                    updateFullInfo(item, item.getThoi_gian_hen(), item.getLy_do_kham(), statuses[which], null);
                    dialog.dismiss();
                })
                .setNegativeButton("Đóng", null)
                .show();
    }

    // ==========================================
    // 4. CHỨC NĂNG XÓA
    // ==========================================
    private void showDeleteConfirmation(LichHenResponse item) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa lịch hẹn của " + item.getTen_benh_nhan() + "?")
                .setPositiveButton("Xóa", (dialog, which) -> deleteAppointment(item.get_id()))
                .setNegativeButton("Hủy", null)
                .show();
    }

    private void deleteAppointment(String id) {
        apiService.deleteLichHen(id).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(DailyScheduleActivity.this, "Đã xóa!", Toast.LENGTH_SHORT).show();
                    fetchDailySchedule(); // Load lại
                } else {
                    Toast.makeText(DailyScheduleActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                }
            }
            @Override public void onFailure(Call<Void> call, Throwable t) { Toast.makeText(DailyScheduleActivity.this, "Lỗi mạng", Toast.LENGTH_SHORT).show(); }
        });
    }

    // --- HELPER TIME ---
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

    // --- API FETCH LIST ---
    private void fetchDailySchedule() {
        tvAppointmentCount.setText("Đang tải lịch hẹn...");
        apiService.getTodayAppointments().enqueue(new Callback<List<LichHenResponse>>() {
            @Override
            public void onResponse(@NonNull Call<List<LichHenResponse>> call, @NonNull Response<List<LichHenResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LichHenResponse> appointments = response.body();
                    if (adapter != null) adapter.setData(appointments);
                    updateSummary(appointments);
                } else {
                    Toast.makeText(DailyScheduleActivity.this, "Hôm nay trống lịch", Toast.LENGTH_SHORT).show();
                    updateSummary(new ArrayList<>());
                    if (adapter != null) adapter.setData(new ArrayList<>());
                }
            }
            @Override public void onFailure(@NonNull Call<List<LichHenResponse>> call, @NonNull Throwable t) {
                Toast.makeText(DailyScheduleActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_LONG).show();
                updateSummary(new ArrayList<>());
            }
        });
    }

    private void updateDateDisplay() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEEE, dd/MM/yyyy", new Locale("vi", "VN"));
        String today = dateFormat.format(Calendar.getInstance().getTime());
        tvCurrentDate.setText(String.format("Hôm nay: %s", today));
    }

    private void updateSummary(List<LichHenResponse> appointments) {
        int total = appointments.size();
        long pending = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            pending = appointments.stream().filter(l -> "ChoXacNhan".equals(l.getTrang_thai())).count();
        } else {
            for (LichHenResponse item : appointments) {
                if ("ChoXacNhan".equals(item.getTrang_thai())) pending++;
            }
        }
        tvAppointmentCount.setText(String.format("Tổng số: %d (Chờ xác nhận: %d)", total, pending));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) { finish(); return true; }
        return super.onOptionsItemSelected(item);
    }
}