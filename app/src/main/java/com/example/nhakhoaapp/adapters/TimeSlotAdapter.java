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

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models_data.TimeSlot;
import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private final List<TimeSlot> timeSlots;
    private final Context context;
    private OnSlotClickListener listener;
    private int selectedPosition = RecyclerView.NO_POSITION;
    
    public interface OnSlotClickListener {
        void onSlotClick(TimeSlot slot, int position);
    }

    public TimeSlotAdapter(Context context, List<TimeSlot> timeSlots, OnSlotClickListener listener) {
        this.context = context;
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_time_slot, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot slot = timeSlots.get(position);
        holder.tvTimeSlot.setText(slot.getTime());

        if (!slot.isAvailable()) {
            // 1. Trạng thái KÍN (Unavailable): Ghi đè màu nền và màu chữ
            holder.tvTimeSlot.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
            holder.tvTimeSlot.setTextColor(ContextCompat.getColor(context, R.color.black));
            holder.tvTimeSlot.setSelected(false); 
            holder.itemView.setEnabled(false); 
            
        } else {
            // Khả dụng: Sử dụng Selector cho nền
            holder.itemView.setEnabled(true);
            boolean isSelected = (selectedPosition == position);
            
            // 2. Thiết lập trạng thái SELECTED: Drawable Selector tự đổi nền (xanh đậm/trắng)
            holder.tvTimeSlot.setSelected(isSelected); 
            
            if (isSelected) {
                // ĐANG CHỌN: Chữ trắng (trên nền xanh)
                holder.tvTimeSlot.setTextColor(Color.WHITE);
            } else {
                // CÒN TRỐNG: Chữ đen (trên nền trắng)
                holder.tvTimeSlot.setTextColor(ContextCompat.getColor(context, R.color.black));
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (slot.isAvailable()) { 
                int oldSelectedPosition = selectedPosition;
                selectedPosition = position;
                
                notifyItemChanged(oldSelectedPosition);
                notifyItemChanged(selectedPosition);
                
                if (listener != null) {
                    listener.onSlotClick(slot, position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }
    
    public void onSlotClick(TimeSlot slot, int position) {
        if (listener != null) {
            listener.onSlotClick(slot, position);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTimeSlot;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTimeSlot = itemView.findViewById(R.id.tv_time_slot);
        }
    }
}