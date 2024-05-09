package com.example.model;

import android.graphics.Bitmap;

public class applyModel {

    String name, email;
    int amount;
    Bitmap img;
    public applyModel(String name, String email, int amount, Bitmap img) {
        this.name = name;
        this.email = email;
        this.amount = amount;
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public int getAmount() {
        return amount;
    }

    public Bitmap getImg() {
        return img;
    }
    public String getEmail() {return email; }

}
