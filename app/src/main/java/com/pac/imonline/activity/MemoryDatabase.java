package com.pac.imonline.activity;

import java.util.ArrayList;
import java.util.List;

public class MemoryDatabase {

    private static List<ProfileActivity> profile;

    private static List<ProfileActivity> initializeList() {
        if (profile == null){
            profile = new ArrayList<>();
            profile.add(new ProfileActivity(1, "https://www.w3schools.com/howto/img_avatar.png", "Carlos Álvaro", "Student", "963951139", "carlosa@outlook.pt","Beja"));

        }
        return profile;
    }
    public static List<ProfileActivity> getAllContacts() {
        return initializeList();
    }

    public static ProfileActivity getContactForPosition(int position) {
        return initializeList().get(position);
    }
}
