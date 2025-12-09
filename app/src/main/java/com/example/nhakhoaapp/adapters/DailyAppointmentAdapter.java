package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.response.LichHenResponse; // Model mới

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyAppointmentAdapter extends RecyclerView.Adapter<DailyAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<LichHenResponse> list;

    public DailyAppointmentAdapter(Context context, List<LichHenResponse> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<LichHenResponse> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    // Giữ lại hàm updateData cho tương thích nếu code cũ có gọi
    public void updateData(List<LichHenResponse> newList) {
        setData(newList);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Dùng chung layout item_appointment.xml hoặc tạo item_daily_schedule.xml tùy bạn
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichHenResponse item = list.get(position);

        // Hiển thị Tên (Lấy từ Object đã populate)
        holder.tvPatientName.setText(item.getTen_benh_nhan());

        // Hiển thị Giờ (Format lại từ ISO string)
        holder.tvTime.setText(formatTime(item.getThoi_gian_hen()));

        // Hiển thị Dịch vụ/Lý do
        holder.tvReason.setText(item.getLy_do_kham());

        // Xử lý màu trạng thái
        String status = item.getTrang_thai();
        if ("ChoXacNhan".equals(status)) {
            holder.tvStatus.setText("Chờ xác nhận");
            holder.tvStatus.setTextColor(Color.parseColor("#FF9800")); // Cam
        } else if ("DaXacNhan".equals(status)) {
            holder.tvStatus.setText("Đã xác nhận");
            holder.tvStatus.setTextColor(Color.parseColor("#4CAF50")); // Xanh
        } else {
            holder.tvStatus.setText(status);
            holder.tvStatus.setTextColor(Color.GRAY);
        }
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    // Helper format giờ: "2024-12-08T14:00:00Z" -> "14:00"
    private String formatTime(String isoDate) {
        if (isoDate == null) return "--:--";
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            input.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = input.parse(isoDate);

            SimpleDateFormat output = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return output.format(date);
        } catch (Exception e) {
            return "Error";
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName, tvReason, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID theo layout item_appointment.xml
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvReason = itemView.findViewById(R.id.tv_doctor_name); // hoặc tv_doctor_name tùy layout bạn dùng
            tvStatus = itemView.findViewById(R.id.tv_status);
        }
    }
}