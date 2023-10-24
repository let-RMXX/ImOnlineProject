package com.pac.imonline.activity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ProfileDAO {

    @Query("SELECT * FROM ProfileActivity")
    List<ProfileActivity> getAll();

    @Query("SELECT * FROM ProfileActivity WHERE id = :profileId")
    ProfileActivity getById(long profileId);

    @Insert
    void insert ( ProfileActivity profileActivity);

    @Insert
    void insert (List<ProfileActivity> profileActivityList);

    @Update
    void updateProfile(ProfileActivity profileActivity);

    @Delete
    void delete(ProfileActivity profileActivity);


}
