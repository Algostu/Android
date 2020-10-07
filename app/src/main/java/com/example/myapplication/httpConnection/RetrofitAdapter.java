package com.example.myapplication.httpConnection;

import android.content.Context;

import com.example.myapplication.Login.Data.UserJson;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class RetrofitAdapter {
    public static Retrofit retrofit = null;


    public static RetrofitService getInstance(String baseUrl, Context context)
    {
        OkHttpClient client = new OkHttpClient();
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.addInterceptor(new AddCookiesInterceptor(context)); // VERY VERY IMPORTANT
        builder.addInterceptor(new ReceivedCookiesInterceptor(context)); // VERY VERY IMPORTANT
        client = builder.build();

        if(retrofit==null)
        {
            retrofit = new Retrofit.Builder().
                    baseUrl(baseUrl).
                    addConverterFactory(GsonConverterFactory.create()).
                    client(client).
                    build();
        }
        RetrofitService service = retrofit.create(RetrofitService.class);
        return service;
    }

}
