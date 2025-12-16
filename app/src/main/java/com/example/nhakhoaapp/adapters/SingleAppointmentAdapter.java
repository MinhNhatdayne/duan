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

    // Interface để bắn sự kiện click nút "3 chấm" ra Activity
    private OnActionClickListener listener;

    public interface OnActionClickListener {
        void onMoreActionClick(LichHenResponse item, View view);
    }

    public SingleAppointmentAdapter(Context context, List<Object> listItems, OnActionClickListener listener) {
        this.context = context;
        this.listItems = listItems;
        this.listener = listener;
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
            // Sử dụng layout item_appointment_staff (đã cập nhật XML)
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

            // 1. Tên Bệnh nhân
            itemHolder.tvPatientName.setText(item.getTen_benh_nhan());

            // 2. [MỚI] Số điện thoại (Xử lý GUEST_)
            String sdt = item.getSdt_benh_nhan();
            if (sdt != null && sdt.startsWith("GUEST_")) {
                itemHolder.tvPatientPhone.setText("Khách vãng lai (Không SĐT)");
            } else {
                itemHolder.tvPatientPhone.setText(sdt != null ? sdt : "Chưa cập nhật");
            }

            // 3. Tên Bác sĩ
            String tenBacSi = item.getTen_bac_si();
            itemHolder.tvDoctorName.setText(tenBacSi != null ? "BS. " + tenBacSi : "Chưa phân công");

            // 4. [MỚI] Xử lý tách Dịch vụ và Ghi chú
            String fullReason = item.getLy_do_kham();
            if (fullReason != null && !fullReason.isEmpty()) {
                if (fullReason.contains(" - Note: ")) {
                    // Trường hợp có cả Dịch vụ và Note
                    String[] parts = fullReason.split(" - Note: ");
                    itemHolder.tvServiceContent.setText(parts[0]); // Phần trước là Dịch vụ

                    itemHolder.tvNote.setText("Note: " + parts[1]); // Phần sau là Ghi chú
                    itemHolder.layoutNote.setVisibility(View.VISIBLE); // Hiện hàng ghi chú
                } else if (fullReason.startsWith("Note: ")) {
                    // Trường hợp chỉ có Note
                    itemHolder.tvServiceContent.setText("Dịch vụ: Không có");
                    itemHolder.tvNote.setText(fullReason);
                    itemHolder.layoutNote.setVisibility(View.VISIBLE);
                } else {
                    // Trường hợp chỉ có Dịch vụ (không có note)
                    itemHolder.tvServiceContent.setText(fullReason);
                    itemHolder.layoutNote.setVisibility(View.GONE); // Ẩn hàng ghi chú đi
                }
            } else {
                itemHolder.tvServiceContent.setText("Khám tổng quát");
                itemHolder.layoutNote.setVisibility(View.GONE);
            }

            // 5. Thời gian
            itemHolder.tvTime.setText(formatTimeOnly(item.getThoi_gian_hen()));

            // 6. Trạng thái & Màu sắc
            updateStatusUI(itemHolder, item.getTrang_thai());

            // 7. Xử lý sự kiện click vào nút 3 chấm
            itemHolder.btnMoreAction.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onMoreActionClick(item, itemHolder.btnMoreAction);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listItems != null ? listItems.size() : 0;
    }

    // --- CÁC HÀM HELPER ---

    private void updateStatusUI(ItemViewHolder holder, String status) {
        if (status == null) status = "";

        String displayStatus = status;
        int textColor = Color.GRAY;
        int bgColor = Color.parseColor("#F5F5F5");

        switch (status) {
            case "ChoXacNhan":
                displayStatus = "Chờ xác nhận";
                textColor = Color.parseColor("#E65100"); // Cam đậm
                bgColor = Color.parseColor("#FFF3E0");   // Cam nhạt
                break;
            case "DaXacNhan":
                displayStatus = "Đã xác nhận";
                textColor = Color.parseColor("#1B5E20"); // Xanh lá đậm
                bgColor = Color.parseColor("#E8F5E9");   // Xanh lá nhạt
                break;
            case "DaKham":
                displayStatus = "Đã khám";
                textColor = Color.parseColor("#0D47A1"); // Xanh dương đậm
                bgColor = Color.parseColor("#E3F2FD");   // Xanh dương nhạt
                break;
            case "Huy":
                displayStatus = "Đã hủy";
                textColor = Color.parseColor("#B71C1C"); // Đỏ đậm
                bgColor = Color.parseColor("#FFEBEE");   // Đỏ nhạt
                break;
        }

        holder.tvStatus.setText(displayStatus);
        holder.tvStatus.setTextColor(textColor);
        holder.cardStatusBadge.setCardBackgroundColor(bgColor);
    }

    private String formatTimeOnly(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "--:--";
        try {
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US);
            input.setTimeZone(TimeZone.getTimeZone("UTC"));
            Date date = input.parse(isoDate);

            SimpleDateFormat output = new SimpleDateFormat("HH:mm", Locale.getDefault());
            return output.format(date);
        } catch (ParseException e) {
            return "00:00";
        }
    }

    // --- VIEWHOLDERS ---

    static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeaderTitle;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeaderTitle = itemView.findViewById(R.id.tv_group_date_header);
        }
    }

    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName, tvPatientPhone, tvStatus, tvServiceContent, tvDoctorName, tvNote;
        View layoutNote; // Để ẩn hiện dòng ghi chú
        CardView cardStatusBadge;
        ImageView btnMoreAction;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ View theo ID trong XML item_appointment_staff
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvPatientPhone = itemView.findViewById(R.id.tv_patient_phone); // [MỚI]

            tvStatus = itemView.findViewById(R.id.tv_status);
            cardStatusBadge = itemView.findViewById(R.id.card_status_badge);

            tvServiceContent = itemView.findViewById(R.id.tv_service_content);

            layoutNote = itemView.findViewById(R.id.layout_note);          // [MỚI]
            tvNote = itemView.findViewById(R.id.tv_appointment_note);      // [MỚI]

            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            btnMoreAction = itemView.findViewById(R.id.btn_more_action);
        }
    }
}