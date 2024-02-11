package com.pac.imonline.activity.Database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.pac.imonline.activity.Entities.CommentEntity;

import java.util.List;

@Dao
public interface CommentDao {

    @Insert
    void insertComment(CommentEntity comment);

    @Delete
    void deleteComment(CommentEntity comment);

    @Query("SELECT * FROM comments WHERE post_Id = :postId")
    List<CommentEntity> getCommentsForPost(int postId);

    @Insert
    void insert(CommentEntity commentEntity);
}
