package com.example.nhakhoaapp.adapters;

import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models_adapter.DateSlot;

import java.util.List;

public class DateSlotAdapter extends RecyclerView.Adapter<DateSlotAdapter.ViewHolder> {

    private List<DateSlot> list;
    private int selectedPosition = -1; // Mặc định chưa chọn ngày nào
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(DateSlot slot, int position);
    }

    public DateSlotAdapter(List<DateSlot> list, OnDateClickListener listener) {
        this.list = list;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item_date_slot (đã sửa ở bước trước)
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date_slot, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateSlot slot = list.get(position);

        // Set nội dung text
        holder.tvDayOfWeek.setText(slot.getDayOfWeek());
        holder.tvDayNumber.setText(String.valueOf(slot.getDay()));

        // --- XỬ LÝ GIAO DIỆN (ĐỔI MÀU KHI CHỌN) ---
        if (position == selectedPosition) {
            // == TRẠNG THÁI ĐANG CHỌN ==
            
            // 1. Đổi nền thành màu XANH (file drawable bạn vừa tạo)
            holder.itemView.setBackgroundResource(R.drawable.bg_date_selected);

            // 2. Đổi màu chữ thành TRẮNG
            holder.tvDayOfWeek.setTextColor(Color.WHITE);
            holder.tvDayNumber.setTextColor(Color.WHITE);

            // 3. Làm đậm chữ để nổi bật
            holder.tvDayNumber.setTypeface(null, Typeface.BOLD);
            holder.tvDayOfWeek.setTypeface(null, Typeface.BOLD);

        } else {
            // == TRẠNG THÁI BÌNH THƯỜNG ==
            
            // 1. Đổi nền về TRẮNG viền xám
            holder.itemView.setBackgroundResource(R.drawable.bg_date_normal);

            // 2. Đổi màu chữ về màu gốc (Xám và Đen)
            holder.tvDayOfWeek.setTextColor(Color.parseColor("#757575")); // Màu xám nhạt
            holder.tvDayNumber.setTextColor(Color.BLACK);

            // 3. Chữ bình thường
            holder.tvDayNumber.setTypeface(null, Typeface.BOLD); // Số ngày vẫn nên đậm
            holder.tvDayOfWeek.setTypeface(null, Typeface.NORMAL);
        }

        // --- XỬ LÝ SỰ KIỆN CLICK ---
        holder.itemView.setOnClickListener(v -> {
            int previousPos = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            // Chỉ cập nhật lại 2 item bị thay đổi để tối ưu hiệu năng
            notifyItemChanged(previousPos);
            notifyItemChanged(selectedPosition);

            // Gửi sự kiện ra ngoài Activity
            if (listener != null) {
                listener.onDateClick(slot, selectedPosition);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDayOfWeek, tvDayNumber;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDayOfWeek = itemView.findViewById(R.id.tv_day_of_week);
            tvDayNumber = itemView.findViewById(R.id.tv_day_number);
        }
    }
}