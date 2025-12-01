package com.example.nhakhoaapp.api;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    // Emulator: dùng 10.0.2.2
    private static final String BASE_URL = "http://10.0.2.2:3000/";
    // Nếu dùng điện thoại thật: đổi thành "http://IP_MAY_TINH:3000/"

    private static Retrofit retrofit;

    public static ApiService getApi() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
