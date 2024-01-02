package com.pac.imonline.activity;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ImOnlineApi {
    @GET("usersinfo")
    Call<List<Post>> getAllUsers();
}
