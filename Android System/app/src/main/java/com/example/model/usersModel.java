package com.example.model;

import android.graphics.Bitmap;
import android.media.Image;

public class usersModel {

    String name;
    String type;
    String email;
    Bitmap picture;
    String fullname;
    public usersModel(String name, String type, String email, Bitmap picture, String fullname) {
        this.name = name;
        this.type = type;
        this.email = email;
        this.picture = picture;
        this.fullname = fullname;
    }

    public String getFullname() {
        return fullname;
    }
    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }

    public String getType() {
        return type;
    }

    public Bitmap getPicture() {
        return picture;
    }
}
