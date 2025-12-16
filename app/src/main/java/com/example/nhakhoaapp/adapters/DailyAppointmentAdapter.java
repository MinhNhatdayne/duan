package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.response.LichHenResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class DailyAppointmentAdapter extends RecyclerView.Adapter<DailyAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<LichHenResponse> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LichHenResponse item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public DailyAppointmentAdapter(Context context, List<LichHenResponse> list) {
        this.context = context;
        this.list = list;
    }

    public void setData(List<LichHenResponse> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Sử dụng chung layout với bên AppointmentManager
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichHenResponse item = list.get(position);

        // 1. Tên Bệnh Nhân
        String pName = item.getTen_benh_nhan();
        holder.tvPatientName.setText(pName != null ? pName : "Không xác định");

        // 2. [THÊM MỚI] Số điện thoại (Logic giống bên SingleAppointmentAdapter)
        String sdt = item.getSdt_benh_nhan();
        if (sdt != null && sdt.startsWith("GUEST_")) {
            holder.tvPatientPhone.setText("Khách vãng lai (Không SĐT)");
        } else {
            holder.tvPatientPhone.setText(sdt != null ? sdt : "Chưa cập nhật");
        }

        // 3. Tách Dịch vụ và Ghi chú
        String fullReason = item.getLy_do_kham();
        if (fullReason != null && !fullReason.isEmpty()) {
            if (fullReason.contains(" - Note: ")) {
                String[] parts = fullReason.split(" - Note: ");
                holder.tvServiceContent.setText(parts[0]);
                holder.tvNote.setText("Note: " + parts[1]);
                holder.layoutNote.setVisibility(View.VISIBLE);
            } else if (fullReason.startsWith("Note: ")) {
                holder.tvServiceContent.setText("Dịch vụ: Không có");
                holder.tvNote.setText(fullReason);
                holder.layoutNote.setVisibility(View.VISIBLE);
            } else {
                holder.tvServiceContent.setText(fullReason);
                holder.layoutNote.setVisibility(View.GONE);
            }
        } else {
            holder.tvServiceContent.setText("Khám tổng quát");
            holder.layoutNote.setVisibility(View.GONE);
        }

        // 4. Tên Bác Sĩ
        String dName = item.getTen_bac_si();
        holder.tvDoctorName.setText(dName != null ? "BS. " + dName : "Chưa phân công");

        // 5. Thời gian
        if (item.getThoi_gian_hen() != null) {
            String[] timeParts = formatTimeParts(item.getThoi_gian_hen());
            holder.tvTime.setText(timeParts[0]);
        } else {
            holder.tvTime.setText("--:--");
        }

        // 6. Trạng thái
        String status = item.getTrang_thai();
        if (status == null) status = "Unknown";

        switch (status) {
            case "ChoXacNhan":
                holder.tvStatus.setText("Chờ duyệt");
                holder.tvStatus.setTextColor(Color.parseColor("#E65100"));
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#FFE0B2"));
                break;
            case "DaXacNhan":
                holder.tvStatus.setText("Đã duyệt");
                holder.tvStatus.setTextColor(Color.parseColor("#1B5E20"));
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
                break;
            case "DaKham":
                holder.tvStatus.setText("Hoàn tất");
                holder.tvStatus.setTextColor(Color.parseColor("#0D47A1"));
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
                break;
            case "Huy":
                holder.tvStatus.setText("Đã hủy");
                holder.tvStatus.setTextColor(Color.parseColor("#B71C1C"));
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
                break;
            default:
                holder.tvStatus.setText(status);
                holder.tvStatus.setTextColor(Color.GRAY);
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                break;
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });

        holder.btnMoreAction.setOnClickListener(v -> {
            Toast.makeText(context, "Thao tác: " + pName, Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    private String[] formatTimeParts(String isoDate) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            input.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = input.parse(isoDate);
            SimpleDateFormat output = new SimpleDateFormat("HH:mm", Locale.getDefault());
            String fullTime = output.format(date);
            return fullTime.split(" ");
        } catch (Exception e) {
            return new String[]{"--:--", ""};
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName, tvPatientPhone, tvServiceContent, tvDoctorName, tvNote, tvStatus;
        View layoutNote;
        CardView cardStatusBadge;
        ImageView btnMoreAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ đầy đủ ID mới
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvPatientPhone = itemView.findViewById(R.id.tv_patient_phone); // [THÊM MỚI]

            tvServiceContent = itemView.findViewById(R.id.tv_service_content);
            layoutNote = itemView.findViewById(R.id.layout_note);
            tvNote = itemView.findViewById(R.id.tv_appointment_note);

            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            cardStatusBadge = itemView.findViewById(R.id.card_status_badge);
            btnMoreAction = itemView.findViewById(R.id.btn_more_action);
        }
    }
}