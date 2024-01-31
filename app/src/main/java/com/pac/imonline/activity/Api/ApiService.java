package com.pac.imonline.activity.Api;

import com.pac.imonline.activity.Constant;
import com.pac.imonline.activity.UserInfoActivity;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    @Multipart
    @POST(Constant.SAVE_USER_INFO)
    Call<UserInfoActivity> saveUserInfo(
            @Header("Authorization") String authorization,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part photo
    );
}