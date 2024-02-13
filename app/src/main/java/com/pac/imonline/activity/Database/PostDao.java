package com.pac.imonline.activity.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pac.imonline.activity.Entities.PostEntity;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insert(PostEntity post);

    @Query("SELECT * FROM posts")
    LiveData<List<PostEntity>> getAllPosts();

    @Update
    void update(PostEntity postEntity);

    @Query("SELECT * FROM posts WHERE userId = :userId")
    List<PostEntity> getPostsByUserId(int userId);

}
