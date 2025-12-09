package com.example.nhakhoaapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.entity.HoSoBenhAn;
import java.util.List;

public class RecordAdapter extends RecyclerView.Adapter<RecordAdapter.ViewHolder> {

    private Context context;
    private List<HoSoBenhAn> list;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onEdit(HoSoBenhAn record);
        void onDelete(String id);
    }

    public RecordAdapter(Context context, List<HoSoBenhAn> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void updateList(List<HoSoBenhAn> newList) {
        this.list = newList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_record, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HoSoBenhAn record = list.get(position);

        holder.tvDiagnosis.setText("Chẩn đoán: " + record.getChan_doan());
        holder.tvDate.setText("Ngày: " + record.getNgay_kham());

        // Vì trong Record chỉ lưu ID, nên ta hiển thị tạm ID
        // (Nâng cao: Cần gọi thêm API lấy tên bệnh nhân từ ID để hiển thị đẹp hơn)
        holder.tvPatientId.setText("BN: " + record.getId_benh_nhan());

        holder.btnEdit.setOnClickListener(v -> listener.onEdit(record));
        holder.btnDelete.setOnClickListener(v -> listener.onDelete(record.get_id()));
    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiagnosis, tvDate, tvPatientId;
        ImageView btnEdit, btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiagnosis = itemView.findViewById(R.id.tvDiagnosis);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvPatientId = itemView.findViewById(R.id.tvPatientId);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}