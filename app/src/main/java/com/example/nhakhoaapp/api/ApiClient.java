package com.example.nhakhoaapp.api;

import java.util.concurrent.TimeUnit; // 1. Import thư viện này để chỉnh thời gian

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

//    private static final String BASE_URL = "https://test-gh9i.onrender.com/";
    private static final String BASE_URL = "http://10.0.2.2:3000";
    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // Cấu hình Log để xem dữ liệu gửi đi/nhận về
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            // 2. Cấu hình Client với thời gian chờ (Timeout) tăng lên
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .connectTimeout(90, TimeUnit.SECONDS) // Chờ kết nối tối đa 30s
                    .readTimeout(90, TimeUnit.SECONDS)    // Chờ server phản hồi tối đa 30s (QUAN TRỌNG CHO GỬI MAIL)
                    .writeTimeout(90, TimeUnit.SECONDS)   // Chờ gửi dữ liệu lên tối đa 30s
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // Dùng Gson để parse JSON
                    .client(client) // Gắn client đã cấu hình vào Retrofit
                    .build();
        }
        return retrofit;
    }

    // Phương thức tiện ích để lấy ApiService nhanh gọn
    public static ApiService getApiService() {
        return getClient().create(ApiService.class);
    }
}