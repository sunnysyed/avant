package com.sunnysyed.avant.api;

import com.sunnysyed.avant.api.model.UserModel;

import java.util.HashMap;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * Created by SuNnY on 3/29/16.
 */
public interface Api {
    @FormUrlEncoded
    @POST("user/register")
    Call<UserModel> register(@Field("email") String email, @Field("password") String password, @Field("first_name") String first_name, @Field("last_name") String last_name);

    @FormUrlEncoded
    @POST("user/login")
    Call<UserModel> login(@Field("email") String email, @Field("password") String password);

    @GET("user/profile")
    Call<UserModel> getProfile(@Header("Authorization") String access_token);

    @POST("loan_application/create")
    Call<UserModel> createLoanApplication(@Header("Authorization") String access_token);

    @POST("loan_application/upload_attachment")
    Call<UserModel> addImageToLoanApplication(@Header("Authorization") String authorization, @Body RequestBody body);
}
