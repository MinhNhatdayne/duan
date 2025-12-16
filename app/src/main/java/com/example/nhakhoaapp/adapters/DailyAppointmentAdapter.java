package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    
    // [CẬP NHẬT INTERFACE] Thêm View vào callback để hiển thị PopupMenu ngay tại nút đó
    private OnItemActionClickListener listener;

    public interface OnItemActionClickListener {
        void onMoreActionClick(LichHenResponse item, View view); // Click 3 chấm
        void onItemClick(LichHenResponse item); // Click vào cả dòng (để xem chi tiết hoặc sửa nhanh)
    }

    public void setOnItemActionClickListener(OnItemActionClickListener listener) {
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichHenResponse item = list.get(position);

        // ... (Giữ nguyên các đoạn setText Tên, Giờ, Lý do, Bác sĩ như cũ) ...
        // Tôi rút gọn đoạn này để tập trung vào phần Click Event bên dưới
        holder.tvPatientName.setText(item.getTen_benh_nhan());
        String sdt = item.getSdt_benh_nhan();
        holder.tvPatientPhone.setText((sdt != null && sdt.startsWith("GUEST_")) ? "Khách vãng lai" : (sdt != null ? sdt : ""));
        
        // Hiển thị lý do/dịch vụ
        String fullReason = item.getLy_do_kham();
        if (fullReason != null && fullReason.contains(" - Note: ")) {
            String[] parts = fullReason.split(" - Note: ");
            holder.tvServiceContent.setText(parts[0]);
            holder.tvNote.setText("Note: " + parts[1]);
            holder.layoutNote.setVisibility(View.VISIBLE);
        } else {
            holder.tvServiceContent.setText(fullReason != null ? fullReason : "Khám tổng quát");
            holder.layoutNote.setVisibility(View.GONE);
        }
        
        holder.tvDoctorName.setText("BS. " + item.getTen_bac_si());
        // ... (Giữ nguyên phần format Time và Color Status) ...
        updateStatusColor(holder, item.getTrang_thai());
        holder.tvTime.setText(formatTimeParts(item.getThoi_gian_hen())[0]);

        // [QUAN TRỌNG] SỰ KIỆN CLICK
        
        // 1. Click vào item -> Mở dialog sửa (hoặc làm gì tùy bạn)
        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onItemClick(item);
        });

        // 2. Click vào nút 3 chấm -> Mở Menu (Xóa, Trạng thái)
        holder.btnMoreAction.setOnClickListener(v -> {
            if (listener != null) listener.onMoreActionClick(item, holder.btnMoreAction);
        });
    }

    @Override
    public int getItemCount() { return list != null ? list.size() : 0; }

    // Các hàm helper cũ giữ nguyên
    private void updateStatusColor(ViewHolder holder, String status) {
        if (status == null) status = "";
        // Copy lại switch case màu sắc từ code cũ của bạn vào đây
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
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#FFCDD2")); 
                break;
            default:
                holder.tvStatus.setText(status);
                holder.tvStatus.setTextColor(Color.GRAY);
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                break;
        }
    }

    private String[] formatTimeParts(String isoDate) {
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            input.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = input.parse(isoDate);
            SimpleDateFormat output = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return output.format(date).split(" "); 
        } catch (Exception e) { return new String[]{"--:--", ""}; }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName, tvPatientPhone, tvServiceContent, tvDoctorName, tvNote, tvStatus;
        View layoutNote; 
        CardView cardStatusBadge;
        ImageView btnMoreAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvPatientPhone = itemView.findViewById(R.id.tv_patient_phone);
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