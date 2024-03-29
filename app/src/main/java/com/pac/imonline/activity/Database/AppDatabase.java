package com.pac.imonline.activity.Database;

import android.content.Context;


import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pac.imonline.activity.Community;
import com.pac.imonline.activity.CommunityDao;
import com.pac.imonline.activity.Entities.CommentEntity;
import com.pac.imonline.activity.Entities.PostEntity;
import com.pac.imonline.activity.Entities.UserEntity;

@TypeConverters(Converters.class)
@Database(entities = {Community.class, UserEntity.class, PostEntity.class, CommentEntity.class}, version = 5, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final String dbName = "imonlinedb";
    private static AppDatabase appDatabase;
    private static AppDatabase INSTANCE;

    public static synchronized AppDatabase getAppDatabase(Context context){
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return appDatabase;
    }

    public abstract UserDao userDao();

    public abstract CommunityDao getCommunityDao();

    public abstract PostDao postDao();

    public abstract CommentDao commentDao();

}
