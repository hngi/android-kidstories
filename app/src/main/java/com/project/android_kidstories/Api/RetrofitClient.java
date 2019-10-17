package com.project.android_kidstories.Api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //private static final String BASE_URL = "https://api-kidstories.herokuapp.com/api/v1/";
   // private static final String BASE_URL = "http://54.144.121.183:8000/api/v1/";
    private static final String BASE_URL = "https://kidstories.app/api/v1/";
    private static Retrofit INSTANCE;


    public static synchronized Retrofit getInstance(){
        if(INSTANCE==null) {
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.level(HttpLoggingInterceptor.Level.BODY);
            OkHttpClient client = new OkHttpClient.Builder()
                    .connectTimeout(2, TimeUnit.MINUTES)
                    .readTimeout(2, TimeUnit.MINUTES)
                    .writeTimeout(2, TimeUnit.MINUTES)
                    .addInterceptor(interceptor)
                    .build();


            return new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        } else {
            return INSTANCE;
        }
    }

}
