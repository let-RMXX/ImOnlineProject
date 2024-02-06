package com.pac.imonline.activity.Api;

import com.pac.imonline.activity.Constant;
import com.pac.imonline.activity.Models.MyPostResponse;

import java.util.List;

import com.pac.imonline.activity.Models.Comment;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @Multipart
    @POST(Constant.SAVE_USER_INFO)
    Call<Void> saveUserInfo(
            @Header("Authorization") String authorization,
            @Part("name") RequestBody name,
            @Part MultipartBody.Part photo
    );

    @DELETE(Constant.DELETE_COMMENT + "/{id}")
    Call<Void> deleteComment(
            @Header("Authorization") String authorization,
            @Path("id") int commentId
    );

    @POST(Constant.LIKE_POST + "/{postId}")
    Call<Void> likePost(
            @Header("Authorization") String authorization,
            @Path("postId") int postId
    );

    @DELETE(Constant.DELETE_POST + "/{postId}")
    Call<Void> deletePost(
            @Header("Authorization") String authorization,
            @Path("postId") int postId
    );

    @GET(Constant.COMMENTS + "/{postId}")
    Call<List<Comment>> getComments(
            @Header("Authorization") String authorization,
            @Path("postId") int postId
    );

    @POST(Constant.CREATE_COMMENT)
    Call<Comment> addComment(
            @Header("Authorization") String authorization,
            @Path("id") int postId,
            @Path("comment") String commentText
    );

    @FormUrlEncoded
    @POST(Constant.UPDATE_POST)
    Call<Void> updatePost(
            @Header("Authorization") String authorization,
            @Field("id") int postId,
            @Field("desc") String description
    );

    @GET(Constant.MY_POST)
    Call<MyPostResponse> getMyPosts(@Header("Authorization") String authorization);

    @POST("logout")
    Call<Void> logout(@Header("Authorization") String authorization);

    @Multipart
    @POST(Constant.ADD_POST)
    Call<Void> addPost(
            @Header("Authorization") String authorization,
            @Part("desc") String description,
            @Part String photo
    );
}