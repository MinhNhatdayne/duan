package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R; // Đảm bảo đúng package R
import com.example.nhakhoaapp.models_data.DateSlot;
import java.util.List;

public class DateAdapter extends RecyclerView.Adapter<DateAdapter.ViewHolder> {
    
    private final List<DateSlot> dateSlots;
    private final Context context;
    private OnDateClickListener listener;
    private int selectedPosition = 4; // Mặc định chọn ngày 14 (vị trí 4)

    public interface OnDateClickListener {
        void onDateClick(DateSlot slot, int position);
    }

    public DateAdapter(Context context, List<DateSlot> dateSlots, OnDateClickListener listener) {
        this.context = context;
        this.dateSlots = dateSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_date_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateSlot slot = dateSlots.get(position);
        holder.tvDayOfWeek.setText(slot.getDayOfWeek());
        holder.tvDate.setText(slot.getDate());

        boolean isSelected = (selectedPosition == position);
        
        // 1. Áp dụng trạng thái SELECTED: Drawable Selector tự đổi nền (tròn xanh/trong suốt)
        holder.tvDate.setSelected(isSelected);

        // 2. Quản lý màu chữ
        if (isSelected) {
            holder.tvDate.setTextColor(Color.WHITE);
            holder.tvDayOfWeek.setTextColor(ContextCompat.getColor(context, R.color.primary_blue));
        } else {
            holder.tvDate.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tvDayOfWeek.setTextColor(ContextCompat.getColor(context, R.color.black));
        }

        holder.itemView.setOnClickListener(v -> {
            int oldSelectedPosition = selectedPosition;
            selectedPosition = position;
            
            notifyItemChanged(oldSelectedPosition);
            notifyItemChanged(selectedPosition);
            
            if (listener != null) {
                listener.onDateClick(slot, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dateSlots.size();
    }
    
    public void onDateClick(DateSlot slot, int position) {
        if (listener != null) {
            listener.onDateClick(slot, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayOfWeek, tvDate;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayOfWeek = itemView.findViewById(R.id.tv_day_of_week);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}