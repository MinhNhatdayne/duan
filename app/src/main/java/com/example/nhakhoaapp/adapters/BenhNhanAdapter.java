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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

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

        // 1. Tên bệnh nhân
        holder.tvName.setText(bn.getHo_ten());

        // 2. Xử lý ngày sinh (QUAN TRỌNG)
        String rawDate = bn.getNgay_sinh();
        String displayDate = formatDate(rawDate); // Chuyển từ ISO sang dd/MM/yyyy

        String gioiTinh = (bn.getGioi_tinh() != null) ? bn.getGioi_tinh() : "Chưa rõ";

        // Hiển thị: Nam - 30/10/2005
        holder.tvGenderDob.setText(gioiTinh + " - " + displayDate);

        // 3. Số điện thoại
        holder.tvPhone.setText(bn.getSo_dien_thoai());

        // 4. Avatar chữ cái đầu
        if (bn.getHo_ten() != null && !bn.getHo_ten().isEmpty()) {
            holder.tvAvatarChar.setText(String.valueOf(bn.getHo_ten().charAt(0)).toUpperCase());
        }

        // Sự kiện click
        holder.btnEdit.setOnClickListener(v -> listener.onEdit(bn));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(bn.get_id(), bn.getHo_ten()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    // --- HÀM FORMAT NGÀY THÁNG (Thêm mới) ---
    private String formatDate(String isoDate) {
        if (isoDate == null || isoDate.isEmpty()) return "--/--/----";
        try {
            // Định dạng đầu vào từ Server (ISO 8601)
            // TimeZone UTC là bắt buộc để ngày không bị lệch (vd: 30 thành 29)
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
            inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));

            Date date = inputFormat.parse(isoDate);

            // Định dạng đầu ra mong muốn: Ngày/Tháng/Năm
            SimpleDateFormat outputFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            return outputFormat.format(date);
        } catch (Exception e) {
            // Nếu lỗi parse (do data cũ hoặc sai format), trả về chuỗi gốc cắt ngắn
            if (isoDate.length() >= 10) {
                return isoDate.substring(0, 10); // Lấy tạm yyyy-MM-dd
            }
            return isoDate;
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvGenderDob, tvPhone, tvAvatarChar;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // Ánh xạ theo ID trong layout item_patient.xml
            tvName = itemView.findViewById(R.id.tvName);
            tvGenderDob = itemView.findViewById(R.id.tvGenderYear);
            tvPhone = itemView.findViewById(R.id.tvPhone);
            tvAvatarChar = itemView.findViewById(R.id.tvAvatarChar);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}