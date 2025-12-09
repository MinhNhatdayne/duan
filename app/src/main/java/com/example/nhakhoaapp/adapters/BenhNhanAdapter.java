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
import com.example.nhakhoaapp.models.entity.BenhNhan;
import java.util.List;

public class BenhNhanAdapter extends RecyclerView.Adapter<BenhNhanAdapter.ViewHolder> {

    private Context context;
    private List<BenhNhan> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(BenhNhan bn);
        void onDelete(String id, String name);
    }

    public BenhNhanAdapter(Context context, List<BenhNhan> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<BenhNhan> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BenhNhan bn = list.get(position);
        holder.tvName.setText(bn.getHo_ten());

        // Hiển thị: Giới tính - Ngày sinh
        String gioiTinh = (bn.getGioi_tinh() != null) ? bn.getGioi_tinh() : "Chưa rõ";
        String ngaySinh = (bn.getNgay_sinh() != null) ? bn.getNgay_sinh() : "--/--/----";

        holder.tvGenderDob.setText(gioiTinh + " - " + ngaySinh);
        holder.tvPhone.setText(bn.getSo_dien_thoai());

        // Avatar chữ cái đầu
        if (bn.getHo_ten() != null && !bn.getHo_ten().isEmpty()) {
            holder.tvAvatarChar.setText(String.valueOf(bn.getHo_ten().charAt(0)).toUpperCase());
        }

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(bn));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(bn.get_id(), bn.getHo_ten()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGenderDob, tvPhone, tvAvatarChar;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Lưu ý: Đảm bảo file item_patient.xml có id là tvGenderYear hoặc đổi tên biến cho phù hợp
            // Ở đây tôi dùng tạm tvGenderDob khớp với logic
            tvName = itemView.findViewById(R.id.tvName);
            tvGenderDob = itemView.findViewById(R.id.tvGenderYear); // Tận dụng ID cũ của layout item
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAvatarChar = itemView.findViewById(R.id.tvAvatarChar);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}