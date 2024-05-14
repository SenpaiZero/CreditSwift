package com.example.model;

import android.graphics.Bitmap;

import com.example.Helper.SettingHelper;
import com.example.Helper.SqliteHelper;

public class listBorrowModel {
    String name, frequency, email;
    int year;
    double interest, total, remaining;
    Bitmap pic;
    public listBorrowModel(String name, String frequency, String email, double total, double remaining, int year, double interest, Bitmap pic) {
        this.name = name;
        this.frequency = frequency;
        this.email = email;
        this.total = total;
        this.remaining = remaining;
        this.year = year;
        this.pic = pic;
        this.interest = interest;
    }
    public double getPayment() {
        double percent = getInterest();

        int freq = 1;
        if(getFrequency().equalsIgnoreCase("Weekly"))
            freq = year * 48;
        else if(getFrequency().equalsIgnoreCase("Monthly"))
            freq = year * 12;
        else if(getFrequency().equalsIgnoreCase("Quarterly"))
            freq = year * 4;
        else if(getFrequency().equalsIgnoreCase("Semi-Annual"))
            freq = year * 2;
        else freq = year;

        if (freq == 0) {
            System.out.println("Error: Unknown frequency.");
            return 0.0; // or handle this case appropriately
        }

        // Ensure that interest rate is not zero
        if (percent == 0) {
            System.out.println("Error: Interest rate is zero.");
            return 0.0; // or handle this case appropriately
        }
        double payment = total * (percent / freq) / (1 - Math.pow(1 + (percent / freq), -freq * year));
        return payment;
    }


    public double getInterest() {
        return interest;
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

    public double getTotal() {
        return total;
    }

    public double getRemaining() {
        return remaining;
    }

    public int getYear() {
        return year;
    }

    public Bitmap getPic() {
        return pic;
    }

}
