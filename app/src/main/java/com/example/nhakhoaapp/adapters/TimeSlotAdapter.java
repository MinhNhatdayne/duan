package com.example.nhakhoaapp.adapters;

import android.graphics.Color;
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

    // [QUAN TRỌNG] Đổi từ String sang TimeSlot model
    private List<TimeSlot> list;
    private int selectedPosition = -1;
    private OnTimeClickListener listener;

    public interface OnTimeClickListener {
        void onTimeClick(TimeSlot timeSlot); // Trả về cả object
    }

    public TimeSlotAdapter(List<TimeSlot> list, OnTimeClickListener listener) {
        this.list = list;
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
        TimeSlot slot = list.get(position);
        holder.tvTime.setText(slot.getTime());

        // --- LOGIC HIỂN THỊ MÀU SẮC (Sửa holder.itemView thành holder.tvTime) ---

        if (!slot.isAvailable()) {
            // 1. Nếu đã có người đặt -> Màu xám, KHÔNG click được
            holder.tvTime.setBackgroundResource(R.drawable.bg_time_slot_disabled);
            holder.tvTime.setTextColor(Color.GRAY);

            holder.tvTime.setEnabled(false);
            holder.tvTime.setOnClickListener(null); // Xóa sự kiện click
        } else {
            // 2. Nếu còn trống -> Cho phép click
            holder.tvTime.setEnabled(true);

            if (position == selectedPosition) {
                // Đang chọn -> Màu xanh
                holder.tvTime.setBackgroundResource(R.drawable.bg_time_slot_selected);
                holder.tvTime.setTextColor(Color.WHITE);
            } else {
                // Bình thường -> Màu trắng
                holder.tvTime.setBackgroundResource(R.drawable.bg_time_slot_normal);
                holder.tvTime.setTextColor(Color.BLACK);
            }

            // Gán sự kiện click vào tvTime (để người dùng bấm trúng nút mới ăn)
            holder.tvTime.setOnClickListener(v -> {
                int previousPos = selectedPosition;
                selectedPosition = holder.getAdapterPosition();

                notifyItemChanged(previousPos);
                notifyItemChanged(selectedPosition);

                if (listener != null) {
                    listener.onTimeClick(slot);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    // Hàm hỗ trợ reset khi người dùng đổi ngày
    public void clearSelection() {
        selectedPosition = -1;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Đảm bảo trong item_time_slot.xml có TextView id là tv_time
            tvTime = itemView.findViewById(R.id.tv_time);
        }
    }
}