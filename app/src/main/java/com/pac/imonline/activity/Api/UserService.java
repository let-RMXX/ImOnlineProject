package com.pac.imonline.activity.Api;

import com.pac.imonline.activity.Entities.UserEntity;
import com.pac.imonline.activity.Constant;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import retrofit2.http.Url;

public interface UserService {

    @POST(Constant.REGISTER)
    Call<Void> registerUser(@Body UserEntity userEntity);
}
