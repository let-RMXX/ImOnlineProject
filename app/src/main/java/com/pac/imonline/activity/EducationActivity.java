package com.pac.imonline.activity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
@Entity
public class EducationActivity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String university_name;
    private String date;
    private String graduation;
    private String description;

    public EducationActivity(long id, String university_name, String date, String graduation, String description) {
        this.id = id;
        this.university_name = university_name;
        this.date = date;
        this.graduation = graduation;
        this.description = description;
    }

    public long getId(){return id;}
    public String getUniversity_name() {
        return university_name;
    }

    public void setUniversity_name(String university_name) {
        this.university_name = university_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGraduation() {
        return graduation;
    }

    public void setGraduation(String graduation) {
        this.graduation = graduation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
