package com.example.model;

import android.graphics.Bitmap;

public class listBorrowModel {
    String name, frequency, email;
    int total, remaining, year;
    Bitmap pic;
    public listBorrowModel(String name, String frequency, String email, int total, int remaining, int year, Bitmap pic) {
        this.name = name;
        this.frequency = frequency;
        this.email = email;
        this.total = total;
        this.remaining = remaining;
        this.year = year;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public String getFrequency() {
        return frequency;
    }

    public String getEmail() {
        return email;
    }

    public int getTotal() {
        return total;
    }

    public int getRemaining() {
        return remaining;
    }

    public int getYear() {
        return year;
    }

    public Bitmap getPic() {
        return pic;
    }

}
