package com.pac.imonline.activity.Database;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.pac.imonline.activity.Entities.UserEntity;

@Dao
public interface UserDao {

    @Insert
    void registerUser(UserEntity userEntity);

    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    UserEntity loginUser(String email, String password);

    @Query("SELECT * FROM users WHERE name = :username")
    UserEntity getUserByUsername(String username);

    @Query("SELECT * FROM users WHERE email = :email")
    UserEntity getUserByEmail(String email);

    @Query("SELECT * FROM users WHERE id = :userId")
    UserEntity getUserById(int userId);

    @Update
    void updateUser(UserEntity userEntity);
}
