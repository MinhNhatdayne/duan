//package com.example.nhakhoaapp.adapters;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.nhakhoaapp.R;
//import com.example.nhakhoaapp.models.entity.BenhNhan;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class apdaptertestbenhnhan extends RecyclerView.Adapter<apdaptertestbenhnhan.BenhNhanViewHolder> {
//
//    public interface OnItemClickListener {
//        void onItemClick(BenhNhan benhNhan, int position);
//    }
//
//    private List<BenhNhan> list;
//    private OnItemClickListener listener;
//
//    public apdaptertestbenhnhan(OnItemClickListener listener) {
//        this.list = new ArrayList<>();
//        this.listener = listener;
//    }
//
//    public void setData(List<BenhNhan> data) {
//        this.list = data;
//        notifyDataSetChanged();
//    }
//
//    public BenhNhan getItem(int position) {
//        return list.get(position);
//    }
//
//    public void addItem(BenhNhan bn) {
//        list.add(0, bn); // thêm lên đầu
//        notifyItemInserted(0);
//    }
//
//    public void updateItem(int position, BenhNhan bn) {
//        list.set(position, bn);
//        notifyItemChanged(position);
//    }
//
//    public void removeItem(int position) {
//        list.remove(position);
//        notifyItemRemoved(position);
//    }
//
//    @NonNull
//    @Override
//    public BenhNhanViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext())
//                .inflate(R.layout.item_benhnhantest, parent, false);
//        return new BenhNhanViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull BenhNhanViewHolder holder, int position) {
//        BenhNhan bn = list.get(position);
//        if (bn == null) return;
//
//        holder.tvTen.setText(bn.getTen());
//
//        String info = "Ngày sinh: " + bn.getNgaySinh()
//                + "\nGiới tính: " + bn.getGioiTinh()
//                + "\nSĐT: " + bn.getSoDienThoai();
//
//        holder.tvThongTin.setText(info);
//
//        holder.itemView.setOnClickListener(v -> {
//            if (listener != null) {
//                listener.onItemClick(bn, holder.getAdapterPosition());
//            }
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return list != null ? list.size() : 0;
//    }
//
//    static class BenhNhanViewHolder extends RecyclerView.ViewHolder {
//        TextView tvTen, tvThongTin;
//
//        public BenhNhanViewHolder(@NonNull View itemView) {
//            super(itemView);
//            tvTen = itemView.findViewById(R.id.tvTen);
//            tvThongTin = itemView.findViewById(R.id.tvThongTin);
//        }
//    }
//}
