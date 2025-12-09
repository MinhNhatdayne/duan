package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.entity.NhanVien;

import java.util.List;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.ViewHolder> {

    private Context context;
    private List<NhanVien> listDoctors;
    private OnDoctorClickListener listener;

    public interface OnDoctorClickListener {
        void onBookClick(NhanVien doctor);
    }

    public DoctorAdapter(Context context, List<NhanVien> listDoctors, OnDoctorClickListener listener) {
        this.context = context;
        this.listDoctors = listDoctors;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Nạp file item_doctor_list (đã sửa thành LinearLayout)
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NhanVien doctor = listDoctors.get(position);
        if (doctor == null) return;

        // Hiển thị tên
        holder.tvName.setText(doctor.getHo_ten());

        // Hiển thị chuyên khoa (xử lý null)
        if (doctor.getChuc_vu() != null && !doctor.getChuc_vu().isEmpty()) {
            holder.tvSpecialty.setText("Chuyên khoa: " + doctor.getChuc_vu());
        } else {
            holder.tvSpecialty.setText("Nha sĩ");
        }

        // Sự kiện click
        View.OnClickListener clickListener = v -> {
            if (listener != null) {
                listener.onBookClick(doctor);
            }
        };

        // Bấm vào nút hoặc bấm vào cả dòng đều được
        holder.btnBook.setOnClickListener(clickListener);
        holder.itemView.setOnClickListener(clickListener);
    }

    @Override
    public int getItemCount() {
        return listDoctors != null ? listDoctors.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvSpecialty, btnBook;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ ID chính xác từ XML mới
            tvName = itemView.findViewById(R.id.tv_doctor_name);
            tvSpecialty = itemView.findViewById(R.id.tv_doctor_specialty);
            btnBook = itemView.findViewById(R.id.btn_book_doctor);
        }
    }
}