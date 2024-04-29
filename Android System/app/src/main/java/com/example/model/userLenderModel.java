package com.example.model;

import android.graphics.Bitmap;

public class userLenderModel {

    public userLenderModel(String companyName, double minPrice, double maxPrice, double interest, String frequency, String email, Bitmap pic) {
        this.companyName = companyName;
        this.minPrice = minPrice;
        this.maxPrice = maxPrice;
        this.interest = interest;
        this.frequency = frequency;
        this.email = email;
        this.pic = pic;
    }

    String companyName;
    double interest, minPrice, maxPrice;
    String frequency;
    String email;
    Bitmap pic;

    public String getEmail() {
        return email;
    }

    public Bitmap getPic() {
        return pic;
    }

    public String getCompanyName() {
        return companyName;
    }

    public double getMinPrice() {
        return minPrice;
    }

    public double getMaxPrice() {
        return maxPrice;
    }

    public double getInterest() {
        return interest;
    }

    public String getFrequency() {
        return frequency;
    }



    public static class freq {
        public final static String WEEKLY = "Weekly", MONTHLY = "Monthly", ANNUAL = "Annual";
    }
}
