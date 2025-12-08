//package com.example.nhakhoaapp;
//
//import android.os.Bundle;
//import android.widget.Button;
//import android.widget.EditText;
//import android.widget.Toast;
//
//import androidx.annotation.Nullable;
//import androidx.appcompat.app.AppCompatActivity;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.example.nhakhoaapp.adapters.apdaptertestbenhnhan;
//import com.example.nhakhoaapp.api.ApiClient;
//import com.example.nhakhoaapp.api.ApiService;
//import com.example.nhakhoaapp.models.BenhNhan;
//
//import java.util.List;
//
//import retrofit2.Call;
//import retrofit2.Callback;
//import retrofit2.Response;
//
//public class testbenhnhan extends AppCompatActivity
//        implements apdaptertestbenhnhan.OnItemClickListener {
//
//    // View
//    private EditText edTen, edNgaySinh, edGioiTinh, edDiaChi,
//            edSoDienThoai, edEmail, edPass;
//    private Button btnThem, btnXoa, btnCapNhat;
//    private RecyclerView rvBenhNhan;
//
//    // Retrofit
//    private ApiService apiService;
//
//    // RecyclerView
//    private apdaptertestbenhnhan adapter;
//
//    // Lưu bệnh nhân đang được chọn khi click
//    private BenhNhan selectedBenhNhan = null;
//    private int selectedPosition = -1;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_test); // đúng tên file xml của bạn
//
//        // 1. Ánh xạ view
//        mapViews();
//
//        // 2. Khởi tạo Retrofit ApiService
//        apiService = ApiClient.getClient().create(ApiService.class);
//
//        // 3. Thiết lập RecyclerView + Adapter
//        adapter = new apdaptertestbenhnhan(this);
//        rvBenhNhan.setLayoutManager(new LinearLayoutManager(this));
//        rvBenhNhan.setAdapter(adapter);
//
//        // 4. Load dữ liệu ban đầu
//        loadBenhNhan();
//
//        // 5. Xử lý sự kiện nút
//        btnThem.setOnClickListener(v -> themBenhNhan());
//        btnCapNhat.setOnClickListener(v -> capNhatBenhNhan());
//        btnXoa.setOnClickListener(v -> xoaBenhNhan());
//    }
//
//    private void mapViews() {
//        edTen = findViewById(R.id.ten);
//        edNgaySinh = findViewById(R.id.ngaysinh);
//        edGioiTinh = findViewById(R.id.gioitinh);
//        edDiaChi = findViewById(R.id.diachi);
//        edSoDienThoai = findViewById(R.id.sodienthoai);
//        edEmail = findViewById(R.id.email);
//        edPass = findViewById(R.id.pass);
//
//        btnThem = findViewById(R.id.btnthem);
//        btnXoa = findViewById(R.id.btnxoa);
//        btnCapNhat = findViewById(R.id.btnCapNhat);
//
//        rvBenhNhan = findViewById(R.id.dsbenhnhan);
//    }
//
//    // ================== GỌI API ==================
//
//    private void loadBenhNhan() {
//        apiService.getAllBenhNhan().enqueue(new Callback<List<BenhNhan>>() {
//            @Override
//            public void onResponse(Call<List<BenhNhan>> call, Response<List<BenhNhan>> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    adapter.setData(response.body());
//                } else {
//                    Toast.makeText(testbenhnhan.this, "Không load được danh sách", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<List<BenhNhan>> call, Throwable t) {
//                Toast.makeText(testbenhnhan.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private BenhNhan getBenhNhanFromForm() {
//        String ten = edTen.getText().toString().trim();
//        String ngaySinh = edNgaySinh.getText().toString().trim();
//        String gioiTinh = edGioiTinh.getText().toString().trim();
//        String diaChi = edDiaChi.getText().toString().trim();
//        String sdt = edSoDienThoai.getText().toString().trim();
//        String email = edEmail.getText().toString().trim();
//        String pass = edPass.getText().toString().trim();
//
//        if (ten.isEmpty()) {
//            Toast.makeText(this, "Vui lòng nhập họ tên", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//        if (email.isEmpty()) {
//            Toast.makeText(this, "Vui lòng nhập email", Toast.LENGTH_SHORT).show();
//            return null;
//        }
//
//        // Tạo object BenhNhan (constructor của bạn chỉ cần map cho đúng)
//        return new BenhNhan(ten, ngaySinh, gioiTinh, diaChi, sdt, email, pass);
//    }
//
//    private void themBenhNhan() {
//        BenhNhan bn = getBenhNhanFromForm();
//        if (bn == null) return;
//
//        apiService.createBenhNhan(bn).enqueue(new Callback<BenhNhan>() {
//            @Override
//            public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    // Thêm vào adapter
//                    adapter.addItem(response.body());
//                    rvBenhNhan.scrollToPosition(0);
//                    Toast.makeText(testbenhnhan.this, "Thêm thành công", Toast.LENGTH_SHORT).show();
//                    clearForm();
//                } else {
//                    Toast.makeText(testbenhnhan.this, "Thêm thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BenhNhan> call, Throwable t) {
//                Toast.makeText(testbenhnhan.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void capNhatBenhNhan() {
//        if (selectedBenhNhan == null || selectedPosition == -1) {
//            Toast.makeText(this, "Hãy chọn bệnh nhân cần cập nhật", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        BenhNhan bnNew = getBenhNhanFromForm();
//        if (bnNew == null) return;
//
//        // Dùng id của bệnh nhân đang chọn
//        String id = selectedBenhNhan.getId(); // đảm bảo BenhNhan có getId()
//
//        apiService.updateBenhNhan(id, bnNew).enqueue(new Callback<BenhNhan>() {
//            @Override
//            public void onResponse(Call<BenhNhan> call, Response<BenhNhan> response) {
//                if (response.isSuccessful() && response.body() != null) {
//                    adapter.updateItem(selectedPosition, response.body());
//                    Toast.makeText(testbenhnhan.this, "Cập nhật thành công", Toast.LENGTH_SHORT).show();
//                    clearForm();
//                    selectedBenhNhan = null;
//                    selectedPosition = -1;
//                } else {
//                    Toast.makeText(testbenhnhan.this, "Cập nhật thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BenhNhan> call, Throwable t) {
//                Toast.makeText(testbenhnhan.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void xoaBenhNhan() {
//        if (selectedBenhNhan == null || selectedPosition == -1) {
//            Toast.makeText(this, "Hãy chọn bệnh nhân cần xóa", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        String id = selectedBenhNhan.getId();
//
//        apiService.deleteBenhNhan(id).enqueue(new Callback<Void>() {
//            @Override
//            public void onResponse(Call<Void> call, Response<Void> response) {
//                if (response.isSuccessful()) {
//                    adapter.removeItem(selectedPosition);
//                    Toast.makeText(testbenhnhan.this, "Xóa thành công", Toast.LENGTH_SHORT).show();
//                    clearForm();
//                    selectedBenhNhan = null;
//                    selectedPosition = -1;
//                } else {
//                    Toast.makeText(testbenhnhan.this, "Xóa thất bại", Toast.LENGTH_SHORT).show();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Void> call, Throwable t) {
//                Toast.makeText(testbenhnhan.this, "Lỗi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//    }
//
//    private void clearForm() {
//        edTen.setText("");
//        edNgaySinh.setText("");
//        edGioiTinh.setText("");
//        edDiaChi.setText("");
//        edSoDienThoai.setText("");
//        edEmail.setText("");
//        edPass.setText("");
//    }
//
//    // ================== CLICK ITEM TRONG RECYCLER ==================
//
//    @Override
//    public void onItemClick(BenhNhan benhNhan, int position) {
//        selectedBenhNhan = benhNhan;
//        selectedPosition = position;
//
//        // Đổ dữ liệu lên form
//        edTen.setText(benhNhan.getTen());
//        edNgaySinh.setText(benhNhan.getNgaySinh());
//        edGioiTinh.setText(benhNhan.getGioiTinh());
//        edDiaChi.setText(benhNhan.getDiaChi());
//        edSoDienThoai.setText(benhNhan.getSoDienThoai());
//        edEmail.setText(benhNhan.getEmail());
//        edPass.setText(benhNhan.getPassword());
//    }
//}
