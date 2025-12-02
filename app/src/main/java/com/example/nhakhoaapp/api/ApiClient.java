package com.example.nhakhoaapp.api;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // ⚠ Đổi BASE_URL này cho đúng:
    // - Emulator: http://10.0.2.2:3000/
    // - Máy thật: http://192.168.xxx.xxx:3000/
    private static final String BASE_URL = "http://10.0.2.2:3000/";

    private static Retrofit retrofit;

    public static Retrofit getClient() {
        if (retrofit == null) {

            // Log request/response (OkHttp Interceptor)
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create()) // dùng Gson
                    .client(client)
                    .build();
        }
        return retrofit;
    }
}
