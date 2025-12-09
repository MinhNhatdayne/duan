package com.example.nhakhoaapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.models.entity.DanhMucDichVu;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class ServiceAdapter extends RecyclerView.Adapter<ServiceAdapter.ViewHolder> {

    private Context context;
    private List<DanhMucDichVu> listService;
    private OnServiceClickListener listener;

    public interface OnServiceClickListener {
        void onServiceClick(DanhMucDichVu service);
    }

    public ServiceAdapter(Context context, List<DanhMucDichVu> listService, OnServiceClickListener listener) {
        this.context = context;
        this.listService = listService;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Tạo View từ file item_service.xml
        View view = LayoutInflater.from(context).inflate(R.layout.item_service, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DanhMucDichVu service = listService.get(position);
        if (service == null) return;

        holder.tvName.setText(service.getTen_dich_vu());

        // Định dạng tiền tệ
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        String price = currencyFormat.format(service.getGia_co_ban());
        holder.tvPrice.setText("Giá từ: " + price);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onServiceClick(service);
        });
    }

    @Override
    public int getItemCount() {
        return listService != null ? listService.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_service_name);
            tvPrice = itemView.findViewById(R.id.tv_service_price);
        }
    }
}