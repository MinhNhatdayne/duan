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
            View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_header, parent, false);
            return new HeaderViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_staff, parent, false);
            return new ItemViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == TYPE_HEADER) {
            AppointmentHeader header = (AppointmentHeader) listItems.get(position);
            ((HeaderViewHolder) holder).tvHeaderTitle.setText(header.getHeaderText());
        } else {
            LichHenResponse item = (LichHenResponse) listItems.get(position);
            ItemViewHolder itemHolder = (ItemViewHolder) holder;

            // [ĐÃ SỬA] Gọi đúng tên hàm trong Model LichHenResponse

            // 1. Tên Bệnh nhân
            itemHolder.tvPatientName.setText(item.getTen_benh_nhan()); // Cũ: getTenBenhNhanDisplay()

            // 2. Tên Bác sĩ
            itemHolder.tvDoctorName.setText("BS. " + item.getTen_bac_si()); // Cũ: getTenBacSiDisplay()

            // 3. Dịch vụ / Lý do khám
            itemHolder.tvServiceContent.setText(item.getLy_do_kham()); // Cũ: getLyDoKham()

            // 4. Thời gian
            String timeString = formatTimeOnly(item.getThoi_gian_hen()); // Cũ: getThoiGianHen()
            itemHolder.tvTime.setText(timeString);

            // 5. Trạng thái & Màu sắc
            String status = item.getTrang_thai(); // Cũ: getTrangThai()
            updateStatusUI(itemHolder, status);
        }
    }

    @Override
    public int getItemCount() {
        return listItems != null ? listItems.size() : 0;
    }

    // Helper: Định dạng màu sắc trạng thái
    private void updateStatusUI(ItemViewHolder holder, String status) {
        // Kiểm tra null để tránh crash
        if (status == null) status = "";

        String displayStatus = status;
        int textColor = Color.GRAY;
        int bgColor = Color.parseColor("#F5F5F5");

        if ("ChoXacNhan".equals(status)) {
            displayStatus = "Chờ xác nhận";
            textColor = Color.parseColor("#F57C00");
            bgColor = Color.parseColor("#FFF3E0");
        } else if ("DaXacNhan".equals(status) || "ChoKham".equals(status)) {
            displayStatus = "Chờ khám";
            textColor = Color.parseColor("#1976D2");
            bgColor = Color.parseColor("#E3F2FD");
        } else if ("HoanThanh".equals(status)) {
            displayStatus = "Hoàn thành";
            textColor = Color.parseColor("#388E3C");
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

    // Helper: Lấy HH:mm
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

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeaderTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeaderTitle = itemView.findViewById(R.id.tv_group_date_header);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName, tvStatus, tvServiceContent, tvDoctorName;
        CardView cardStatusBadge;
        ImageView btnMoreAction;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            cardStatusBadge = itemView.findViewById(R.id.card_status_badge);
            tvServiceContent = itemView.findViewById(R.id.tv_service_content);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            btnMoreAction = itemView.findViewById(R.id.btn_more_action);
        }
    }
}