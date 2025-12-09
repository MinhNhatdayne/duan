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
import com.example.nhakhoaapp.models.entity.NhanVien;
import java.util.List;

public class NhanVienAdapter extends RecyclerView.Adapter<NhanVienAdapter.ViewHolder> {

    private Context context;
    private List<NhanVien> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(NhanVien nv);
        void onDelete(String id, String name);
    }

    public NhanVienAdapter(Context context, List<NhanVien> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    // === THÊM HÀM NÀY ĐỂ CẬP NHẬT DANH SÁCH KHI TÌM KIẾM ===
    public void updateList(List<NhanVien> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }
    // ========================================================

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_staff, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVien nv = list.get(position);
        holder.tvName.setText(nv.getHo_ten());
        holder.tvPosition.setText(nv.getChuc_vu());
        holder.tvPhone.setText(nv.getSo_dien_thoai());

        // Avatar chữ cái đầu
        if (nv.getHo_ten() != null && !nv.getHo_ten().isEmpty()) {
            holder.tvAvatarChar.setText(String.valueOf(nv.getHo_ten().charAt(0)).toUpperCase());
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(nv));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(nv.get_id(), nv.getHo_ten()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPosition, tvPhone, tvAvatarChar;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPosition = itemView.findViewById(R.id.tvPosition);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAvatarChar = itemView.findViewById(R.id.tvAvatarChar);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}