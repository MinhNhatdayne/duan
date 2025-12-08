package com.example.nhakhoaapp.api;

import com.example.nhakhoaapp.models.BenhNhan;
import com.example.nhakhoaapp.models.NhanVien;
import com.example.nhakhoaapp.models.Img;
import com.example.nhakhoaapp.models.ChiTietToaThuoc;
import com.example.nhakhoaapp.models.HoSoBenhAn;
import com.example.nhakhoaapp.models.LichHen;
import com.example.nhakhoaapp.models.HoaDon;
import com.example.nhakhoaapp.models.ChiTietHoaDon;
import com.example.nhakhoaapp.models.BaoHiem;
import com.example.nhakhoaapp.models.DanhMucDichVu;
import com.example.nhakhoaapp.models.ChiTietDichVu;
import com.example.nhakhoaapp.models.dto.AppointmentStats;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {

    // ===================== BENH NHÂN =====================

    @GET("BenhNhan")
    Call<List<BenhNhan>> getAllBenhNhan();

    @GET("BenhNhan/{id}")
    Call<BenhNhan> getBenhNhanById(@Path("id") String id);

    @POST("BenhNhan")
    Call<BenhNhan> createBenhNhan(@Body BenhNhan benhNhan);

    @PUT("BenhNhan/{id}")
    Call<BenhNhan> updateBenhNhan(
            @Path("id") String id,
            @Body BenhNhan benhNhan
    );

    @DELETE("BenhNhan/{id}")
    Call<Void> deleteBenhNhan(@Path("id") String id);


    // ===================== NHÂN VIÊN =====================

    @GET("NhanVien")
    Call<List<NhanVien>> getAllNhanVien();

    @GET("NhanVien/{id}")
    Call<NhanVien> getNhanVienById(@Path("id") String id);

    @POST("NhanVien")
    Call<NhanVien> createNhanVien(@Body NhanVien nv);

    @PUT("NhanVien/{id}")
    Call<NhanVien> updateNhanVien(
            @Path("id") String id,
            @Body NhanVien nv
    );

    @DELETE("NhanVien/{id}")
    Call<Void> deleteNhanVien(@Path("id") String id);


    // ===================== IMG =====================

    @GET("Img")
    Call<List<Img>> getAllImg();

    @GET("Img/{id}")
    Call<Img> getImgById(@Path("id") String id);

    @POST("Img")
    Call<Img> createImg(@Body Img img);

    @PUT("Img/{id}")
    Call<Img> updateImg(
            @Path("id") String id,
            @Body Img img
    );

    @DELETE("Img/{id}")
    Call<Void> deleteImg(@Path("id") String id);


    // ===================== CHI TIẾT TOA THUỐC =====================

    @GET("ChiTietToaThuoc")
    Call<List<ChiTietToaThuoc>> getAllChiTietToaThuoc();

    @GET("ChiTietToaThuoc/{id}")
    Call<ChiTietToaThuoc> getChiTietToaThuocById(@Path("id") String id);

    @POST("ChiTietToaThuoc")
    Call<ChiTietToaThuoc> createChiTietToaThuoc(@Body ChiTietToaThuoc cttt);

    @PUT("ChiTietToaThuoc/{id}")
    Call<ChiTietToaThuoc> updateChiTietToaThuoc(
            @Path("id") String id,
            @Body ChiTietToaThuoc cttt
    );

    @DELETE("ChiTietToaThuoc/{id}")
    Call<Void> deleteChiTietToaThuoc(@Path("id") String id);


    // ===================== HỒ SƠ BỆNH ÁN =====================

    @GET("HoSoBenhAn")
    Call<List<HoSoBenhAn>> getAllHoSoBenhAn();

    @GET("HoSoBenhAn/{id}")
    Call<HoSoBenhAn> getHoSoBenhAnById(@Path("id") String id);

    @POST("HoSoBenhAn")
    Call<HoSoBenhAn> createHoSoBenhAn(@Body HoSoBenhAn hsba);

    @PUT("HoSoBenhAn/{id}")
    Call<HoSoBenhAn> updateHoSoBenhAn(
            @Path("id") String id,
            @Body HoSoBenhAn hsba
    );

    @DELETE("HoSoBenhAn/{id}")
    Call<Void> deleteHoSoBenhAn(@Path("id") String id);


    // ===================== LỊCH HẸN =====================

    @GET("LichHen")
    Call<List<LichHen>> getAllLichHen();

    @GET("LichHen/{id}")
    Call<LichHen> getLichHenById(@Path("id") String id);

    @POST("LichHen")
    Call<LichHen> createLichHen(@Body LichHen lichHen);

    @PUT("LichHen/{id}")
    Call<LichHen> updateLichHen(
            @Path("id") String id,
            @Body LichHen lichHen
    );

    @DELETE("LichHen/{id}")
    Call<Void> deleteLichHen(@Path("id") String id);

    // ⬇️ API BỔ SUNG CHO LỊCH HẸN HÔM NAY ⬇️
    @GET("LichHen/today/list") // <<< ĐÃ THAY ĐỔI TẠI ĐÂY
    Call<List<LichHen>> getTodayAppointments();

    // ⬇️ API BỔ SUNG CHO THỐNG KÊ DASHBOARD ⬇️
    @GET("LichHen/stats/today")
    Call<AppointmentStats> getTodayAppointmentStats();
    // ⬆️ API BỔ SUNG CHO THỐNG KÊ DASHBOARD ⬆️


    // ===================== HÓA ĐƠN =====================

    @GET("HoaDon")
    Call<List<HoaDon>> getAllHoaDon();

    @GET("HoaDon/{id}")
    Call<HoaDon> getHoaDonById(@Path("id") String id);

    @POST("HoaDon")
    Call<HoaDon> createHoaDon(@Body HoaDon hoaDon);

    @PUT("HoaDon/{id}")
    Call<HoaDon> updateHoaDon(
            @Path("id") String id,
            @Body HoaDon hoaDon
    );

    @DELETE("HoaDon/{id}")
    Call<Void> deleteHoaDon(@Path("id") String id);


    // ===================== CHI TIẾT HÓA ĐƠN =====================

    @GET("ChiTietHoaDon")
    Call<List<ChiTietHoaDon>> getAllChiTietHoaDon();

    @GET("ChiTietHoaDon/{id}")
    Call<ChiTietHoaDon> getChiTietHoaDonById(@Path("id") String id);

    @POST("ChiTietHoaDon")
    Call<ChiTietHoaDon> createChiTietHoaDon(@Body ChiTietHoaDon cthd);

    @PUT("ChiTietHoaDon/{id}")
    Call<ChiTietHoaDon> updateChiTietHoaDon(
            @Path("id") String id,
            @Body ChiTietHoaDon cthd
    );

    @DELETE("ChiTietHoaDon/{id}")
    Call<Void> deleteChiTietHoaDon(@Path("id") String id);


    // ===================== BẢO HIỂM =====================

    @GET("BaoHiem")
    Call<List<BaoHiem>> getAllBaoHiem();

    @GET("BaoHiem/{id}")
    Call<BaoHiem> getBaoHiemById(@Path("id") String id);

    @POST("BaoHiem")
    Call<BaoHiem> createBaoHiem(@Body BaoHiem baoHiem);

    @PUT("BaoHiem/{id}")
    Call<BaoHiem> updateBaoHiem(
            @Path("id") String id,
            @Body BaoHiem baoHiem
    );

    @DELETE("BaoHiem/{id}")
    Call<Void> deleteBaoHiem(@Path("id") String id);


    // ===================== DANH MỤC DỊCH VỤ =====================

    @GET("DanhMucDichVu")
    Call<List<DanhMucDichVu>> getAllDanhMucDichVu();

    @GET("DanhMucDichVu/{id}")
    Call<DanhMucDichVu> getDanhMucDichVuById(@Path("id") String id);

    @POST("DanhMucDichVu")
    Call<DanhMucDichVu> createDanhMucDichVu(@Body DanhMucDichVu dmdv);

    @PUT("DanhMucDichVu/{id}")
    Call<DanhMucDichVu> updateDanhMucDichVu(
            @Path("id") String id,
            @Body DanhMucDichVu dmdv
    );

    @DELETE("DanhMucDichVu/{id}")
    Call<Void> deleteDanhMucDichVu(@Path("id") String id);


    // ===================== CHI TIẾT DỊCH VỤ =====================

    @GET("ChiTietDichVu")
    Call<List<ChiTietDichVu>> getAllChiTietDichVu();

    @GET("ChiTietDichVu/{id}")
    Call<ChiTietDichVu> getChiTietDichVuById(@Path("id") String id);

    @POST("ChiTietDichVu")
    Call<ChiTietDichVu> createChiTietDichVu(@Body ChiTietDichVu ctdv);

    @PUT("ChiTietDichVu/{id}")
    Call<ChiTietDichVu> updateChiTietDichVu(
            @Path("id") String id,
            @Body ChiTietDichVu ctdv
    );

    @DELETE("ChiTietDichVu/{id}")
    Call<Void> deleteChiTietDichVu(@Path("id") String id);
}