package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.LichHen;

import java.util.List;

public class DailyAppointmentAdapter extends RecyclerView.Adapter<DailyAppointmentAdapter.ViewHolder> {

    private final List<LichHen> appointmentList;
    private final Context context;

    public DailyAppointmentAdapter(Context context, List<LichHen> appointmentList) {
        this.context = context;
        this.appointmentList = appointmentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LichHen lichHen = appointmentList.get(position);

        // 1. Gán dữ liệu cơ bản
        holder.tvTime.setText(lichHen.getGio_kham());
        holder.tvPatientName.setText(lichHen.getTen_benh_nhan());
        holder.tvServiceContent.setText(lichHen.getLy_do_kham()); // Hoặc getTen_dich_vu()
        holder.tvStatus.setText(lichHen.getTrang_thai());

        // Giả lập tên bác sĩ (Nếu model có thì dùng lichHen.getTen_bac_si())
        holder.tvDoctorName.setText("BS. Phụ Trách");

        // 2. Xử lý màu sắc trạng thái (Badge Style)
        applyStatusStyle(holder, lichHen.getTrang_thai());

        // 3. Xử lý sự kiện bấm vào nút "3 chấm" (More Action)
        holder.btnMoreAction.setOnClickListener(v -> showPopupMenu(v, lichHen, position));
    }

    /**
     * Hàm hiển thị Menu con khi bấm vào dấu 3 chấm
     */
    private void showPopupMenu(View view, LichHen lichHen, int position) {
        PopupMenu popup = new PopupMenu(context, view);
        // Tạo menu bằng code (hoặc inflate từ xml menu nếu có)
        popup.getMenu().add("Xác nhận Đã khám");
        popup.getMenu().add("Dời lịch hẹn");
        popup.getMenu().add("Hủy hẹn");

        popup.setOnMenuItemClickListener(item -> {
            String title = item.getTitle().toString();
            switch (title) {
                case "Xác nhận Đã khám":
                    lichHen.setTrang_thai("Đã khám");
                    notifyItemChanged(position);
                    Toast.makeText(context, "Đã cập nhật trạng thái: Đã khám", Toast.LENGTH_SHORT).show();
                    return true;
                case "Dời lịch hẹn":
                    Toast.makeText(context, "Chức năng dời lịch cho: " + lichHen.getTen_benh_nhan(), Toast.LENGTH_SHORT).show();
                    return true;
                case "Hủy hẹn":
                    lichHen.setTrang_thai("Hủy");
                    notifyItemChanged(position);
                    return true;
            }
            return false;
        });
        popup.show();
    }

    /**
     * Hàm set màu nền và màu chữ cho Badge trạng thái
     */
    private void applyStatusStyle(ViewHolder holder, String status) {
        switch (status) {
            case "Đã khám":
            case "Hoàn thành":
                // Nền Xanh lá nhạt - Chữ Xanh lá đậm
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#E8F5E9"));
                holder.tvStatus.setTextColor(Color.parseColor("#2E7D32"));
                break;

            case "Đang chờ":
            case "Sắp tới":
                // Nền Cam nhạt - Chữ Cam đậm
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#FFF3E0"));
                holder.tvStatus.setTextColor(Color.parseColor("#EF6C00"));
                break;

            case "Chưa khám":
            case "Hủy":
                // Nền Đỏ nhạt - Chữ Đỏ đậm
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#FFEBEE"));
                holder.tvStatus.setTextColor(Color.parseColor("#C62828"));
                break;

            case "Dời lịch":
                // Nền Xanh dương nhạt - Chữ Xanh dương đậm
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#E3F2FD"));
                holder.tvStatus.setTextColor(Color.parseColor("#1565C0"));
                break;

            default:
                // Mặc định màu xám
                holder.cardStatusBadge.setCardBackgroundColor(Color.parseColor("#F5F5F5"));
                holder.tvStatus.setTextColor(Color.parseColor("#757575"));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName, tvServiceContent, tvDoctorName, tvStatus;
        CardView cardStatusBadge;
        ImageView btnMoreAction;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ đúng với ID trong item_appointment_staff.xml mới
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvServiceContent = itemView.findViewById(R.id.tv_service_content);
            tvDoctorName = itemView.findViewById(R.id.tv_doctor_name);
            tvStatus = itemView.findViewById(R.id.tv_status);
            cardStatusBadge = itemView.findViewById(R.id.card_status_badge);
            btnMoreAction = itemView.findViewById(R.id.btn_more_action);
        }
    }
}