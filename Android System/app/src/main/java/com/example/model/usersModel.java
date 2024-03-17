package com.example.model;

import android.graphics.Bitmap;
import android.media.Image;

public class usersModel {

    String name;
    String type;
    Bitmap picture;
    public usersModel(String name, String type, Bitmap picture) {
        this.name = name;
        this.type = type;
        this.picture = picture;
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
