package com.example.Helper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateHelper {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    public static String getCurrentDate() {
        // get current date
        Date currentDate = new Date();
        return DATE_FORMAT.format(currentDate);
    }

    public static boolean isDue(String dueDate) {
        // Compare dueDate to current date. Return true if dueDate is later
        try {
            Date due = DATE_FORMAT.parse(dueDate);
            Date currentDate = new Date();
            return due.after(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static String addMonth(String date, String frequency) {
        // add 1 month to the date and make the day always be 15
        try {
            Date givenDate = DATE_FORMAT.parse(date);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(givenDate);

            switch (frequency.toLowerCase()) {
                case "weekly":
                    calendar.add(Calendar.WEEK_OF_YEAR, 1);
                    break;
                case "monthly":
                    calendar.add(Calendar.MONTH, 1);
                    break;
                case "quarterly":
                    calendar.add(Calendar.MONTH, 3);
                    break;
                case "semi-annual":
                    calendar.add(Calendar.MONTH, 6);
                    break;
                case "annual":
                    calendar.add(Calendar.YEAR, 1);
                    break;
                default:
                    throw new IllegalArgumentException("Invalid frequency: " + frequency);
            }

            // Make the day always be 15 if the frequency is not weekly
            if (!frequency.equalsIgnoreCase("weekly")) {
                calendar.set(Calendar.DAY_OF_MONTH, 15);
            }

            return DATE_FORMAT.format(calendar.getTime());
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

}
