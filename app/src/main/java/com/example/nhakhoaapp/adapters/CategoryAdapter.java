package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.entity.DanhMucDichVu;

import java.text.DecimalFormat;
import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {

    private Context context;
    private List<DanhMucDichVu> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(DanhMucDichVu item);
        void onDelete(String id, String name);
    }

    public CategoryAdapter(Context context, List<DanhMucDichVu> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<DanhMucDichVu> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMucDichVu item = list.get(position);

        holder.tvName.setText(item.getTen_dich_vu());
        holder.tvType.setText("Phân loại: " + item.getLoai_dich_vu());

        DecimalFormat formatter = new DecimalFormat("#,###");
        String gia = formatter.format(item.getGia_co_ban());
        holder.tvPrice.setText(gia + " VNĐ / " + item.getDon_vi());

        // Sự kiện Click
        holder.itemView.setOnClickListener(v -> listener.onEdit(item));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(item.get_id(), item.getTen_dich_vu()));
    }

    @Override
    public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvType, tvPrice;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvType = itemView.findViewById(R.id.tvType);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}