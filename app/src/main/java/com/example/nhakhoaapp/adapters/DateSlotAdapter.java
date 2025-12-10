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

    private List<DateSlot> list;
    private int selectedPosition = -1;
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
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_date_slot, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DateSlot slot = list.get(position);

        holder.tvDayOfWeek.setText(slot.getDayOfWeek());
        holder.tvDayNumber.setText(String.valueOf(slot.getDay()));

        holder.itemView.setSelected(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = holder.getAdapterPosition();
            notifyDataSetChanged();
            listener.onDateClick(slot, selectedPosition);
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
