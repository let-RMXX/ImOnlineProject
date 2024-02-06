package com.pac.imonline.activity.Models;

import com.pac.imonline.activity.Models.Posts;
import com.pac.imonline.activity.Models.User;

import java.util.List;

public class MyPostResponse {
    private boolean success;
    private List<Posts> posts;
    private User user;

    public boolean isSuccess() {
        return success;
    }

    public List<Posts> getPosts() {
        return posts;
    }

    public User getUser() {
        return user;
    }
}