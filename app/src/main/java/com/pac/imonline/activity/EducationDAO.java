package com.pac.imonline.activity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;
@Dao
public interface EducationDAO {
    @Query("SELECT * FROM EducationActivity")
    List<EducationActivity> getAll();

    @Query("SELECT * FROM EducationActivity WHERE id = :educationId")
    EducationActivity getById(long educationId);


    @Insert
    void insert ( EducationActivity educationActivity);

    @Insert
    void insert (List<EducationActivity> educationActivityList);

    @Update
    void update(EducationActivity educationActivity);

    @Delete
    void delete(EducationActivity educationActivity);
}
