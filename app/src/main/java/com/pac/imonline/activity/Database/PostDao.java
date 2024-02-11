package com.pac.imonline.activity.Database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import com.pac.imonline.activity.Entities.PostEntity;

import java.util.List;

@Dao
public interface PostDao {

    @Insert
    void insert(PostEntity post);

    @Query("SELECT * FROM posts")
    LiveData<List<PostEntity>> getAllPosts();

}
