package com.example.nhakhoaapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models_adapter.TimeSlot;

import java.util.List;

public class TimeSlotAdapter extends RecyclerView.Adapter<TimeSlotAdapter.ViewHolder> {

    private final List<TimeSlot> timeSlots;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private final OnSlotClickListener listener;

    /** Interface để truyền dữ liệu khi click */
    public interface OnSlotClickListener {
        void onSlotClick(TimeSlot slot, int position);
    }

    /** Constructor */
    public TimeSlotAdapter(List<TimeSlot> timeSlots, OnSlotClickListener listener) {
        this.timeSlots = timeSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_time_slot, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        TimeSlot slot = timeSlots.get(position);

        holder.tvTime.setText(slot.getTime());

        // Nếu slot không khả dụng → mờ, không click được
        holder.itemView.setAlpha(slot.isAvailable() ? 1f : 0.3f);
        holder.itemView.setClickable(slot.isAvailable());

        // Set trạng thái SELECTED
        holder.itemView.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos == RecyclerView.NO_POSITION) return;

            TimeSlot clickedSlot = timeSlots.get(adapterPos);
            if (!clickedSlot.isAvailable()) return;

            int oldPos = selectedPosition;
            selectedPosition = adapterPos;

            notifyItemChanged(oldPos);
            notifyItemChanged(adapterPos);

            if (listener != null) listener.onSlotClick(clickedSlot, adapterPos);
        });
    }

    @Override
    public int getItemCount() {
        return timeSlots.size();
    }

    /** Chọn slot mặc định từ Activity */
    public void selectPosition(int position) {
        if (position < 0 || position >= timeSlots.size()) return;

        int oldPos = selectedPosition;
        selectedPosition = position;

        notifyItemChanged(oldPos);
        notifyItemChanged(selectedPosition);

        if (listener != null) listener.onSlotClick(timeSlots.get(position), position);
    }

    /** ViewHolder */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time_slot);
        }
    }
}
