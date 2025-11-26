package com.example.nhakhoaapp.adapters;

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

    private final List<DateSlot> dateSlots;
    private int selectedPosition = RecyclerView.NO_POSITION;
    private final OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(DateSlot slot, int position);
    }

    public DateSlotAdapter(List<DateSlot> dateSlots, OnDateClickListener listener) {
        this.dateSlots = dateSlots;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_date_slot, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateSlot slot = dateSlots.get(position);

        holder.tvDay.setText(slot.getDayOfWeek());
        holder.tvDate.setText(String.valueOf(slot.getDate()));

        // Áp dụng trạng thái chọn
        holder.itemView.setSelected(selectedPosition == position);

        holder.itemView.setOnClickListener(v -> {
            int adapterPos = holder.getAdapterPosition();
            if (adapterPos == RecyclerView.NO_POSITION) return;

            int oldPos = selectedPosition;
            selectedPosition = adapterPos;

            notifyItemChanged(oldPos);
            notifyItemChanged(selectedPosition);

            listener.onDateClick(dateSlots.get(adapterPos), adapterPos);
        });
    }

    @Override
    public int getItemCount() {
        return dateSlots.size();
    }

    /** Chọn ngày mặc định từ Activity */
    public void selectPosition(int position) {
        if (position < 0 || position >= dateSlots.size()) return;
        int oldPos = selectedPosition;
        selectedPosition = position;
        notifyItemChanged(oldPos);
        notifyItemChanged(selectedPosition);

        if (listener != null) listener.onDateClick(dateSlots.get(position), position);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDay, tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDay = itemView.findViewById(R.id.tv_day_of_week);
            tvDate = itemView.findViewById(R.id.tv_date);
        }
    }
}
