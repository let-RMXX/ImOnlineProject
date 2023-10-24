package com.pac.imonline.activity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class WorkActivity {
    @PrimaryKey(autoGenerate = true)
    private long id;
    private String company_name;
    private String date;
    private String role;
    private String description;

    public WorkActivity(long id,String company_name, String date, String role, String description) {
        this.id = id;
        this.company_name = company_name;
        this.date = date;
        this.role = role;
        this.description = description;
    }
    public long getId(){return id;}

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
