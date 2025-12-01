package com.example.nhakhoaapp.api;

import com.example.nhakhoaapp.models.BenhNhan;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ApiService {
    @GET("User")
    Call<List<BenhNhan>> getUsers();

    @POST("User")
    Call<BenhNhan> createUser(@Body BenhNhan user);
}
