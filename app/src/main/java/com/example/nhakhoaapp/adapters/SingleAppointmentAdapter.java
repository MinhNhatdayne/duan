package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.LichHen;
import com.example.nhakhoaapp.models_adapter.AppointmentHeader;

import java.util.List;

public class SingleAppointmentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // Định nghĩa Hằng số ViewType
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_DETAIL = 1;

    // Adapter nhận List<Object>
    private final List<Object> combinedList;
    private final Context context;

    public SingleAppointmentAdapter(Context context, List<Object> combinedList) {
        this.context = context;
        this.combinedList = combinedList;
    }

    /**
     * PHƯƠNG THỨC XÁC ĐỊNH VIEW TYPE
     * Dựa trên loại Class của đối tượng (instanceof)
     */
    @Override
    public int getItemViewType(int position) {
        Object item = combinedList.get(position);
        
        if (item instanceof AppointmentHeader) {
            return TYPE_HEADER;
        } else if (item instanceof LichHen) {
            return TYPE_DETAIL;
        }
        // Trường hợp không xác định (Nên có)
        return super.getItemViewType(position); 
    }
    
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        
        if (viewType == TYPE_HEADER) {
            View headerView = inflater.inflate(R.layout.item_appointment_header, parent, false);
            return new HeaderViewHolder(headerView);
        } else { // TYPE_DETAIL
            View detailView = inflater.inflate(R.layout.item_appointment_detail, parent, false);
            return new DetailViewHolder(detailView);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Object item = combinedList.get(position);

        if (holder.getItemViewType() == TYPE_HEADER) {
            // Ép kiểu (cast) về AppointmentHeader để lấy dữ liệu
            AppointmentHeader header = (AppointmentHeader) item;
            ((HeaderViewHolder) holder).tvHeader.setText(header.getHeaderText()); 
        } else { 
            // Ép kiểu (cast) về LichHen
            LichHen lichHen = (LichHen) item;
            DetailViewHolder detailHolder = (DetailViewHolder) holder;

            detailHolder.tvTime.setText(lichHen.getGio_kham());
            detailHolder.tvPatientName.setText(lichHen.getTen_benh_nhan());
            
            detailHolder.itemView.setOnClickListener(v -> {
                Toast.makeText(context, "Mở chi tiết cuộc hẹn: " + lichHen.getTen_benh_nhan(), Toast.LENGTH_SHORT).show();
            });
        }
    }

    @Override
    public int getItemCount() {
        return combinedList.size();
    }

    // --- VIEW HOLDER CHO HEADER ---
    public static class HeaderViewHolder extends RecyclerView.ViewHolder {
        TextView tvHeader;
        public HeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeader = itemView.findViewById(R.id.tv_group_date_header); 
        }
    }

    // --- VIEW HOLDER CHO DETAIL ---
    public static class DetailViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvPatientName;
        public DetailViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_appointment_time);
            tvPatientName = itemView.findViewById(R.id.tv_patient_name);
        }
    }
}