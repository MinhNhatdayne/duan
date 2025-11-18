package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
        
        holder.tvTime.setText(lichHen.getGio_kham());
        holder.tvPatientName.setText(lichHen.getTen_benh_nhan());
        holder.tvReason.setText(lichHen.getLy_do_kham());
        holder.tvStatus.setText(lichHen.getTrang_thai());

        // Xử lý màu sắc trạng thái
        switch (lichHen.getTrang_thai()) {
            case "Đã khám":
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_green_light));
                break;
            case "Đang chờ":
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_orange_light));
                break;
            case "Chưa khám":
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_red_light));
                break;
            case "Dời lịch":
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.holo_blue_light));
                break;
            default:
                holder.tvStatus.setBackgroundTintList(ContextCompat.getColorStateList(context, android.R.color.darker_gray));
                break;
        }

        // Xử lý sự kiện nút Đã khám
        holder.btnExamined.setOnClickListener(v -> {
            // Cập nhật trạng thái trong model (Nếu cần kết nối DB, logic sẽ phức tạp hơn)
            lichHen.setTrang_thai("Đã khám");
            notifyItemChanged(position); 
            Toast.makeText(context, "Đã cập nhật: " + lichHen.getTen_benh_nhan() + " -> Đã khám", Toast.LENGTH_SHORT).show();
        });

        // Xử lý sự kiện nút Dời lịch
        holder.btnReschedule.setOnClickListener(v -> {
            Toast.makeText(context, "Mở màn hình chọn lại thời gian cho: " + lichHen.getTen_benh_nhan(), Toast.LENGTH_SHORT).show();
            // TODO: Mở Intent đến màn hình chọn lại lịch
        });
    }

    @Override
    public int getItemCount() {
        return appointmentList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName, tvReason, tvStatus;
        Button btnExamined, btnReschedule;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_appointment_time);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
            tvReason = itemView.findViewById(R.id.tv_reason);
            tvStatus = itemView.findViewById(R.id.tv_status);
            btnExamined = itemView.findViewById(R.id.btn_action_examined);
            btnReschedule = itemView.findViewById(R.id.btn_action_reschedule);
        }
    }
}