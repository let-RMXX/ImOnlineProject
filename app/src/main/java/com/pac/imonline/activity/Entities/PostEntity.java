package com.pac.imonline.activity.Entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "posts")
public class PostEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    @ColumnInfo(name = "description")
    public String description;

    @ColumnInfo(name = "imageUri")
    public String imageUri;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUri() {
        return imageUri;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}
