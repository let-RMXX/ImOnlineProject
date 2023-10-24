package com.pac.imonline.activity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CommunityDao {
    @Insert
    void insertCommunity(Community community);

    @Query("SELECT * FROM communities")
    LiveData<List<Community>> getAllCommunities();

    @Query("DELETE FROM communities")
    void deleteAllCommunities();
}
