package com.example.nhakhoaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.entity.HoaDon;

import java.util.List;

public class HoaDonAdapter extends RecyclerView.Adapter<HoaDonAdapter.HoaDonViewHolder> {

    private Context context;
    private List<HoaDon> hoaDonList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEditClick(HoaDon hoaDon);
        void onDeleteClick(String id);
    }

    public HoaDonAdapter(Context context, List<HoaDon> hoaDonList, OnItemClickListener listener) {
        this.context = context;
        this.hoaDonList = hoaDonList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public HoaDonViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_hoa_don, parent, false);
        return new HoaDonViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HoaDonViewHolder holder, int position) {
        HoaDon hoaDon = hoaDonList.get(position);
        holder.tvMaHoaDon.setText("Mã HĐ: " + hoaDon.get_id());
        // Định dạng tiền tệ đơn giản
        holder.tvTongTien.setText(String.format("Tổng: %,.0f VND", hoaDon.getTong_tien()));
        holder.tvTrangThai.setText("Trạng thái: " + hoaDon.getTrang_thai_thanh_toan());

        holder.btnEdit.setOnClickListener(v -> listener.onEditClick(hoaDon));
        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(hoaDon.get_id()));
    }

    @Override
    public int getItemCount() {
        return hoaDonList.size();
    }

    public static class HoaDonViewHolder extends RecyclerView.ViewHolder {
        TextView tvMaHoaDon, tvTongTien, tvTrangThai;
        ImageButton btnEdit, btnDelete;

        public HoaDonViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHoaDon = itemView.findViewById(R.id.tvMaHoaDon);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            tvTrangThai = itemView.findViewById(R.id.tvTrangThai);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}