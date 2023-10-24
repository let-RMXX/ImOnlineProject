package com.pac.imonline.activity;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MessageDao {
    @Query("SELECT * FROM message ORDER BY chatId ASC")
    List<Message> getAllMessages();
    @Query("SELECT * FROM message WHERE chatId = :chatId ORDER BY chatId ASC")
    List<Message> getMessagesByChat(long chatId);

    @Insert
    void insert(Message message);
    @Delete
    void delete(Message message);
    @Insert
    void insert(List<Message> messagesList);
}
