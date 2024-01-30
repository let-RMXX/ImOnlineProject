package com.pac.imonline.activity.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.pac.imonline.activity.Entities.UserEntity;

@Dao
public interface UserDao {

    @Insert
    void registerUser(UserEntity userEntity);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity loginUser(String email, String password);

    @Query("SELECT * FROM users WHERE username = :username")
    UserEntity getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email")
    UserEntity getUserByEmail(String email);
}
