package com.pac.imonline.activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ImOnlineApi {
    @GET("usersinfo")
    Call<List<Post>> getAllUsers();

    @FormUrlEncoded
    @POST("register")
    Call<Void> registerUser(
            @Field("username") String username,
            @Field("email") String email,
            @Field("password") String password
    );
}
