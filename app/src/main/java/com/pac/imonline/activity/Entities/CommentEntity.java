package com.pac.imonline.activity.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "comments")
public class CommentEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    @ColumnInfo(name = "post_id")
    private int postId;

    @ColumnInfo(name = "user_name")
    private String userName;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "comment")
    private String comment;

    // Constructor, getters, and setters

    public CommentEntity(int postId, String userName, String date, String comment) {
        this.postId = postId;
        this.userName = userName;
        this.date = date;
        this.comment = comment;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
