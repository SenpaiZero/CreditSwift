package com.example.model;

import android.graphics.Bitmap;

public class historyModel {
    String name;
    double total;
    double year;
    Bitmap pic;
    public historyModel(String name, double total, double year, Bitmap pic) {
        this.name = name;
        this.total = total;
        this.year = year;
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public double getTotal() {
        return total;
    }

    public double getYear() {
        return year;
    }

    public Bitmap getPic() {
        return pic;
    }


}
