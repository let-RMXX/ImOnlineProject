package com.pac.imonline.activity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ContactDao {
    @Query("SELECT * FROM contact")
    List<Contact> getAll();

    @Query("SELECT * FROM contact WHERE id = :contactId")
    Contact getById(long contactId);

    @Insert
    void insert(Contact contact);

    @Delete
    void delete(Contact contact);

}
