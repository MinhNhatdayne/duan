package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.response.LichHenResponse;
import com.example.nhakhoaapp.models_adapter.AppointmentHeader;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class SingleAppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private Context context;
    private List<Object> listItems;

    public SingleAppointmentAdapter(Context context, List<Object> listItems) {
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public int getItemViewType(int position) {
        if (listItems.get(position) instanceof AppointmentHeader) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            // [ĐÚNG] Layout Header
            View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            // [ĐÃ SỬA] Dùng đúng tên layout item_appointment_staff
            View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_staff, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            AppointmentHeader header = (AppointmentHeader) listItems.get(position);
            ((HeaderViewHolder) holder).tvHeaderTitle.setText(header.getHeaderText()); // [SỬA] getHeaderText()
        } else {
            LichHenResponse item = (LichHenResponse) listItems.get(position);
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            // 1. Tên Bệnh nhân
            itemHolder.tvPatientName.setText(item.getTenBenhNhanDisplay());

            // 2. Tên Bác sĩ
            itemHolder.tvDoctorName.setText("BS. " + item.getTenBacSiDisplay());

            // 3. Lý do khám (Dịch vụ)
            itemHolder.tvReason.setText(item.getLyDoKham());

            // 4. Thời gian (Giờ & AM/PM)
            // Cần tách giờ từ chuỗi ISO
            String timeString = formatTimeOnly(item.getThoiGianHen());
            itemHolder.tvTime.setText(timeString);
            
            // Xử lý AM/PM (nếu muốn chuẩn thì parse Date rồi format 'a', ở đây tạm fix cứng hoặc lấy từ timeString)
            // itemHolder.tvAmPm.setText("AM"); 

            // 5. Trạng thái & Màu sắc
            String status = item.getTrangThai();
            updateStatusUI(itemHolder, status);
        }
    }

    @Override
    public int getItemCount() {
        return listItems != null ? listItems.size() : 0;
    }

    // Helper: Định dạng màu sắc trạng thái
    private void updateStatusUI(ItemViewHolder holder, String status) {
        String displayStatus = status;
        int textColor = Color.GRAY;
        int bgColor = Color.parseColor("#F5F5F5"); // Xám nhạt mặc định

        if ("ChoXacNhan".equals(status)) {
            displayStatus = "Chờ xác nhận";
            textColor = Color.parseColor("#F57C00"); // Cam đậm
            bgColor = Color.parseColor("#FFF3E0");   // Cam nhạt
        } else if ("DaXacNhan".equals(status) || "ChoKham".equals(status)) {
            displayStatus = "Chờ khám";
            textColor = Color.parseColor("#1976D2"); // Xanh dương
            bgColor = Color.parseColor("#E3F2FD");
        } else if ("HoanThanh".equals(status)) {
            displayStatus = "Hoàn thành";
            textColor = Color.parseColor("#388E3C"); // Xanh lá
            bgColor = Color.parseColor("#E8F5E9");
        } else if ("Huy".equals(status)) {
            displayStatus = "Đã hủy";
            textColor = Color.RED;
            bgColor = Color.parseColor("#FFEBEE");
        }

        holder.tvStatus.setText(displayStatus);
        holder.tvStatus.setTextColor(textColor);
        holder.cardStatusBadge.setCardBackgroundColor(bgColor);
    }

    // Helper: Chỉ lấy giờ phút (HH:mm)
    private String formatTimeOnly(String isoDate) {
        if (isoDate == null) return "--:--";
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            input.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = input.parse(isoDate);
            
            SimpleDateFormat output = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return output.format(date);
        } catch (ParseException e) {
            return "00:00";
        }
    }

    // ==========================================
    // VIEW HOLDERS (ĐÃ SỬA ID CHO KHỚP XML)
    // ==========================================

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeaderTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            // [SỬA ID] tv_group_date_header (theo item_appointment_header.xml)
            tvHeaderTitle = itemView.findViewById(R.id.tv_group_date_header);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvAmPm, tvPatientName, tvStatus, tvReason, tvDoctorName;
        CardView cardStatusBadge;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // [SỬA ID] Theo item_appointment_staff.xml
            tvTime = itemView.findViewById(R.id.tv_time);
            // tvAmPm = itemView.findViewById(R.id.tv_am_pm); // Nếu XML có ID này thì mở ra
            
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            cardStatusBadge = itemView.findViewById(R.id.card_status_badge);
            
            // [SỬA ID] tv_service_content -> tvReason
            tvReason = itemView.findViewById(R.id.tv_service_content); 
            
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
        }
    }
}