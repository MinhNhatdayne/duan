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

        // 1. Xử lý hiển thị Disable/Enable
        // Cần setEnabled để đồng bộ trạng thái với View cha
        if (slot.isAvailable()) {
            holder.itemView.setAlpha(1.0f);       // Rõ nét
            holder.itemView.setEnabled(true);     // Cho phép tương tác
            holder.itemView.setClickable(true);
        } else {
            holder.itemView.setAlpha(0.3f);       // Mờ đi
            holder.itemView.setEnabled(false);    // Vô hiệu hóa
            holder.itemView.setClickable(false);
        }

        // 2. Set trạng thái Selected (Quan trọng để XML đổi màu viền/nền)
        holder.itemView.setSelected(selectedPosition == position);

        // 3. Sự kiện Click
        holder.itemView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos == RecyclerView.NO_POSITION) return;

            TimeSlot clickedSlot = timeSlots.get(adapterPos);

            // Kiểm tra an toàn: Nếu không available thì thoát luôn
            if (!clickedSlot.isAvailable()) return;

            // Logic cập nhật vị trí chọn
            int oldPos = selectedPosition;
            selectedPosition = adapterPos;

            // Cập nhật giao diện: Reset cái cũ, highlight cái mới
            notifyItemChanged(oldPos);
            notifyItemChanged(selectedPosition);

            // Gửi sự kiện ra ngoài Activity
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

        // Kiểm tra nếu vị trí mặc định không available thì không chọn
        if (!timeSlots.get(position).isAvailable()) return;

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
            // Ánh xạ ID từ file item_time_slot.xml
            tvTime = itemView.findViewById(R.id.tv_time_slot);
        }
    }
}