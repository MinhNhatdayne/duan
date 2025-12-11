package com.example.nhakhoaapp.activities;

import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nhakhoaapp.R;
import com.example.nhakhoaapp.adapter.HoaDonAdapter;
import com.example.nhakhoaapp.api.ApiClient; // Import Class ApiClient của bạn
import com.example.nhakhoaapp.api.ApiService;
import com.example.nhakhoaapp.models.entity.HoaDon;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class QuanLyHoaDonActivity extends AppCompatActivity {

    private RecyclerView rcvHoaDon;
    private FloatingActionButton fabAdd;
    private HoaDonAdapter adapter;
    private List<HoaDon> listHoaDon = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan_ly_hoa_don);

        // --- KHỞI TẠO API TỪ CLASS CỦA BẠN ---
        apiService = ApiClient.getApiService();
        // --------------------------------------

        initView();
        loadData(); // Gọi API lấy danh sách

        fabAdd.setOnClickListener(v -> showDialog(null));
    }

    private void initView() {
        rcvHoaDon = findViewById(R.id.rcvHoaDon);
        fabAdd = findViewById(R.id.fabAddHoaDon);

        rcvHoaDon.setLayoutManager(new LinearLayoutManager(this));

        adapter = new HoaDonAdapter(this, listHoaDon, new HoaDonAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(HoaDon hoaDon) {
                showDialog(hoaDon);
            }

            @Override
            public void onDeleteClick(String id) {
                confirmDelete(id);
            }
        });

        rcvHoaDon.setAdapter(adapter);
    }

    // --- 1. GET: LẤY DANH SÁCH ---
    private void loadData() {
        apiService.getAllHoaDon().enqueue(new Callback<List<HoaDon>>() {
            @Override
            public void onResponse(Call<List<HoaDon>> call, Response<List<HoaDon>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    listHoaDon.clear();
                    listHoaDon.addAll(response.body());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(QuanLyHoaDonActivity.this, "Không lấy được dữ liệu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<HoaDon>> call, Throwable t) {
                Toast.makeText(QuanLyHoaDonActivity.this, "Lỗi kết nối: " + t.getMessage(), Toast.LENGTH_LONG).show();
                Log.e("API_ERROR", t.getMessage());
            }
        });
    }

    // --- HIỂN THỊ DIALOG THÊM/SỬA ---
    private void showDialog(HoaDon hoaDon) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_edit_hoa_don, null);
        builder.setView(view);

        EditText etIdBenhNhan = view.findViewById(R.id.etIdBenhNhan);
        EditText etIdNhanVien = view.findViewById(R.id.etIdNhanVien);
        EditText etTongTien = view.findViewById(R.id.etTongTien);
        EditText etTrangThai = view.findViewById(R.id.etTrangThai);
        EditText etPhuongThuc = view.findViewById(R.id.etPhuongThuc);

        // Nếu là SỬA, điền dữ liệu cũ vào
        if (hoaDon != null) {
            etIdBenhNhan.setText(hoaDon.getId_benh_nhan());
            etIdNhanVien.setText(hoaDon.getId_nhan_vien_lap());
            etTongTien.setText(String.valueOf(hoaDon.getTong_tien()));
            etTrangThai.setText(hoaDon.getTrang_thai_thanh_toan());
            etPhuongThuc.setText(hoaDon.getPhuong_thuc_thanh_toan());
            builder.setTitle("Cập nhật Hóa Đơn");
        } else {
            builder.setTitle("Thêm Hóa Đơn Mới");
        }

        builder.setPositiveButton("Lưu", (dialog, which) -> {
            String idBN = etIdBenhNhan.getText().toString().trim();
            String idNV = etIdNhanVien.getText().toString().trim();
            String strTien = etTongTien.getText().toString().trim();
            String trangThai = etTrangThai.getText().toString().trim();
            String phuongThuc = etPhuongThuc.getText().toString().trim();

            if (idBN.isEmpty() || idNV.isEmpty() || strTien.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập đủ thông tin bắt buộc", Toast.LENGTH_SHORT).show();
                return;
            }

            double tongTien = Double.parseDouble(strTien);
            // Tạo object mới (Ngày lập để null để backend tự tạo)
            HoaDon newHoaDon = new HoaDon(idBN, idNV, null, tongTien, trangThai, phuongThuc);

            if (hoaDon == null) {
                createHoaDon(newHoaDon); // Gọi API Thêm
            } else {
                updateHoaDon(hoaDon.get_id(), newHoaDon); // Gọi API Sửa
            }
        });

        builder.setNegativeButton("Hủy", (dialog, which) -> dialog.dismiss());
        builder.show();
    }

    // --- 2. POST: THÊM MỚI ---
    private void createHoaDon(HoaDon hoaDon) {
        apiService.createHoaDon(hoaDon).enqueue(new Callback<HoaDon>() {
            @Override
            public void onResponse(Call<HoaDon> call, Response<HoaDon> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuanLyHoaDonActivity.this, "Thêm thành công!", Toast.LENGTH_SHORT).show();
                    loadData(); // Tải lại danh sách
                } else {
                    Toast.makeText(QuanLyHoaDonActivity.this, "Thêm thất bại: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HoaDon> call, Throwable t) {
                Toast.makeText(QuanLyHoaDonActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- 3. PUT: CẬP NHẬT ---
    private void updateHoaDon(String id, HoaDon hoaDon) {
        apiService.updateHoaDon(id, hoaDon).enqueue(new Callback<HoaDon>() {
            @Override
            public void onResponse(Call<HoaDon> call, Response<HoaDon> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(QuanLyHoaDonActivity.this, "Cập nhật thành công!", Toast.LENGTH_SHORT).show();
                    loadData();
                } else {
                    Toast.makeText(QuanLyHoaDonActivity.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<HoaDon> call, Throwable t) {
                Toast.makeText(QuanLyHoaDonActivity.this, "Lỗi API: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    // --- 4. DELETE: XÓA ---
    private void confirmDelete(String id) {
        new AlertDialog.Builder(this)
                .setTitle("Xác nhận xóa")
                .setMessage("Bạn có chắc muốn xóa hóa đơn này?")
                .setPositiveButton("Xóa", (dialog, which) -> {
                    apiService.deleteHoaDon(id).enqueue(new Callback<Void>() {
                        @Override
                        public void onResponse(Call<Void> call, Response<Void> response) {
                            if (response.isSuccessful()) {
                                Toast.makeText(QuanLyHoaDonActivity.this, "Đã xóa", Toast.LENGTH_SHORT).show();
                                loadData();
                            } else {
                                Toast.makeText(QuanLyHoaDonActivity.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
                            }
                        }
                        @Override
                        public void onFailure(Call<Void> call, Throwable t) {
                            Toast.makeText(QuanLyHoaDonActivity.this, "Lỗi API", Toast.LENGTH_SHORT).show();
                        }
                    });
                })
                .setNegativeButton("Hủy", null)
                .show();
    }
}