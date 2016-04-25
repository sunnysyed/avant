package com.sunnysyed.avant.api;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/*

 */

public class AvantApi {


    private static String mProductionUrl = "";
    private static String mDevelopmentUrl = "http://54.200.34.203/avant/";


    private static Api REST_CLIENT;

    private static Retrofit retrofit;

    static {
        setupRestClient();
    }

    private AvantApi() {}

    public static Api get() {
        return REST_CLIENT;
    }

    public static Retrofit getRetrofit(){
        return retrofit;
    }

    /**
     * Set up retrofit with a new OkHttpClient
     * Add interceptor for logs
     * Set date format for Gson
     * Add error handling
     */
    private static void setupRestClient() {


        mProductionUrl = mDevelopmentUrl;
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(interceptor).build();



        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy-MM-dd'T'HH:mm:ss")
                .create();

        retrofit = new Retrofit.Builder()
                .baseUrl(mProductionUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        REST_CLIENT = retrofit.create(Api.class);
    }
}