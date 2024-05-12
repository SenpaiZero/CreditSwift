package com.example.Helper;

import android.graphics.Bitmap;

import java.nio.ByteBuffer;
import java.util.Arrays;

public class validationHelper {
    public static boolean checkAlpha(String s) {
        return !s.matches("^[A-Za-z]*$");
    }

    public static boolean checkAlphaNumeric(String s) {
        return !s.matches("^[a-zA-Z0-9]*$");
    }

    public static boolean checkMinMaxLen(String s, int min, int max) {
        return !(s.length() >= min && s.length() <= max);
    }
    public static boolean checkAlphaWithSpace(String s) {
        return !s.matches("^[A-Za-z\\s]*$");
    }

    public static boolean checkEmail(String s) {
        return !s.matches("^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]*$");
    }

    public static boolean checkAllCaps(String s) {
        return !s.equals(s.toUpperCase());
    }
    public static boolean checkAllSmall(String s) {
        return !s.equals(s.toLowerCase());
    }
    public static boolean checkImageChange(Bitmap b1, Bitmap b2) {
        if(b2 == null) return true;
        ByteBuffer buffer1 = ByteBuffer.allocate(b1.getHeight() * b1.getRowBytes());
        b1.copyPixelsToBuffer(buffer1);

        ByteBuffer buffer2 = ByteBuffer.allocate(b2.getHeight() * b2.getRowBytes());
        b2.copyPixelsToBuffer(buffer2);

        return Arrays.equals(buffer1.array(), buffer2.array());
    }

}
