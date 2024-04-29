package com.example.model;

import android.graphics.Bitmap;
import android.media.Image;

public class usersModel {

    String name;
    String type;
    String email;
    Bitmap picture;
    public usersModel(String name, String type, String email, Bitmap picture) {
        this.name = name;
        this.type = type;
        this.email = email;
        this.picture = picture;
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
