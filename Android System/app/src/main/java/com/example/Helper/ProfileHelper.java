package com.example.Helper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import com.example.model.userLenderModel;
import com.example.model.usersModel;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

public class ProfileHelper {
    Activity activity;
    SQLiteDatabase db;
    SqliteHelper sqliteHelper;

    public ProfileHelper(Activity activity)
    {
        this.activity = activity;
        sqliteHelper = new SqliteHelper(activity);
        db = sqliteHelper.getWritableDatabase();
    }
    public String newAccount(String username, String password, String email, String type)
    {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.AccountEntry.USERNAME_COLUMN, username.toUpperCase().trim().replaceAll(" ", "_"));
        values.put(SqliteHelper.AccountEntry.EMAIL_COLUMN, email.trim());
        values.put(SqliteHelper.AccountEntry.PASSWORD_COLUMN, password.trim());
        values.put(SqliteHelper.AccountEntry.TYPE_COLUMN, type.trim());
        values.put(SqliteHelper.AccountEntry.ARCHIVE_COLUMN, "NO");
        values.put(SqliteHelper.AccountEntry.DATA_COLUMN, "NO");
        try
        {
            long newRowId = db.insert(SqliteHelper.AccountEntry.TABLE_NAME, null, values);
            if(newRowId != -1)
                return "true";
            else
                return "false";
        }
        catch (SQLiteConstraintException unique) {
            return "duplicate";
        }
        catch (Exception e)
        {
             Log.e("New Account", e.getMessage());
             return "error";
        }
    }
    public boolean checkUserFirstTime(String name) {
        String[] col = {
                SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.DATA_COLUMN
        };
        Cursor cursor = db.query(
                SqliteHelper.AccountEntry.TABLE_NAME,
                col,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            String data = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.DATA_COLUMN));

            if(name_.equals(name)) {
                if(data.equalsIgnoreCase("NO"))
                    return true;
            }
        }

        return false;
    }

    public boolean newUserInformation(String username, String name, String birth, Bitmap imageBitmap) {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.BorrowerAccount.BIRTH_COLUMN, birth);
        values.put(SqliteHelper.BorrowerAccount.USERNAME_COLUMN, username.toUpperCase());
        values.put(SqliteHelper.BorrowerAccount.NAME_COLUMN, name);
        values.put(SqliteHelper.BorrowerAccount.LIST_COLUMN, "");
        values.put(SqliteHelper.BorrowerAccount.LIST_DONE_COLUMN, "");
        values.put(SqliteHelper.BorrowerAccount.TOTAL_CURRENT_LEND_COLUMN, 0.0);
        values.put(SqliteHelper.BorrowerAccount.TOTAL_LEND_COLUMN, 0.0);
        values.put(SqliteHelper.BorrowerAccount.TOTAL_INTEREST_COLUMN, 0.0);

        ContentValues val = new ContentValues();
        val.put(SqliteHelper.AccountEntry.DATA_COLUMN, "YES");

        byte[] imageBytes = null;
        if (imageBitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            imageBytes = outputStream.toByteArray();
        }

        values.put(SqliteHelper.LenderAccount.PIC_COLUMN, imageBytes);

        try {
            long newRowId = db.insert(SqliteHelper.BorrowerAccount.TABLE_NAME, null, values);
            db.update(
                    SqliteHelper.AccountEntry.TABLE_NAME,
                    val,
                    SqliteHelper.AccountEntry.USERNAME_COLUMN + " = ?",
                    new String[]{username}
            );
            return newRowId != -1;
        } catch (Exception ex) {
            Log.e("New Borrower Info", "Error inserting lender: " + ex.getMessage());
            return false;
        }
    }

    public boolean updateLenderInfo(String name, String email, double min, double max, double rate, String freq, Bitmap pic) {
        ContentValues content = new ContentValues();
        content.put(SqliteHelper.LenderAccount.MIN_COLUMN, min);
        content.put(SqliteHelper.LenderAccount.MAX_COLUMN, max);
        content.put(SqliteHelper.LenderAccount.RATE_COLUMN, rate);
        content.put(SqliteHelper.LenderAccount.FREQ_COLUMN, freq);
        byte[] imageBytes = null;
        if (pic != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            pic.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            imageBytes = outputStream.toByteArray();
        }
        content.put(SqliteHelper.LenderAccount.PIC_COLUMN, imageBytes);

        ContentValues contentVal = new ContentValues();
        contentVal.put(SqliteHelper.AccountEntry.EMAIL_COLUMN, email);

        int rowsAffected = db.update(SqliteHelper.LenderAccount.TABLE_NAME,
                content,
                SqliteHelper.LenderAccount.NAME_COLUMN + " = ?",
                new String[]{name});
        int rowsAffected2 = db.update(SqliteHelper.AccountEntry.TABLE_NAME,
                contentVal,
                SqliteHelper.AccountEntry.USERNAME_COLUMN + " = ?",
                new String[]{name});

        return rowsAffected > 0 && rowsAffected2 > 0;
    }

    public boolean changeArchive(String username, boolean isArchive) {
        ContentValues val = new ContentValues();
        val.put(SqliteHelper.AccountEntry.ARCHIVE_COLUMN, isArchive ? "YES" : "NO");

        db.update(
                SqliteHelper.AccountEntry.TABLE_NAME,
                val,
                SqliteHelper.AccountEntry.USERNAME_COLUMN + " = ?",
                new String[]{username}
        );
        return false;
    }
    public String newLender(String name, String email, double minimum,
                             double maximum, double rate, String frequency,
                             Bitmap imageBitmap) {

        ContentValues values = new ContentValues();
        ContentValues actValues = new ContentValues();

        String name_ = name.trim().replaceAll(" ", "_");
        actValues.put(SqliteHelper.AccountEntry.USERNAME_COLUMN, name_.toUpperCase());
        actValues.put(SqliteHelper.AccountEntry.PASSWORD_COLUMN, name_.toUpperCase());
        actValues.put(SqliteHelper.AccountEntry.EMAIL_COLUMN, email);
        actValues.put(SqliteHelper.AccountEntry.PASSWORD_COLUMN, name_);
        actValues.put(SqliteHelper.AccountEntry.TYPE_COLUMN, "LENDER");
        actValues.put(SqliteHelper.AccountEntry.ARCHIVE_COLUMN, "NO");
        actValues.put(SqliteHelper.AccountEntry.DATA_COLUMN, "YES");

        values.put(SqliteHelper.LenderAccount.NAME_COLUMN, name_);
        values.put(SqliteHelper.LenderAccount.MIN_COLUMN, minimum);
        values.put(SqliteHelper.LenderAccount.MAX_COLUMN, maximum);
        values.put(SqliteHelper.LenderAccount.RATE_COLUMN, rate);
        values.put(SqliteHelper.LenderAccount.FREQ_COLUMN, frequency);

        // Convert the Bitmap image to a byte array
        byte[] imageBytes = null;
        if (imageBitmap != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            imageBitmap.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            imageBytes = outputStream.toByteArray();
        }
        // Add the image byte array to the ContentValues
        values.put(SqliteHelper.LenderAccount.PIC_COLUMN, imageBytes);

        try {
            // Insert the data into the database
            long newRowId2 = db.insert(SqliteHelper.AccountEntry.TABLE_NAME, null, actValues);
            if (newRowId2 == -1) {
                return "false";
            }

            long newRowId = db.insert(SqliteHelper.LenderAccount.TABLE_NAME, null, values);
            return newRowId != -1 ? "true" : "false";
        }
        catch (SQLiteConstraintException unique) {
            return "duplicate";
        }
        catch (Exception ex) {
            Log.e("New Lender", "Error inserting lender: " + ex.getMessage());
            return "error";
        }
    }

    public LinkedList<usersModel> getUsersList(String type, boolean isArchive) {
        LinkedList<usersModel> list = new LinkedList<>();
        LinkedList<usersModel> archlist = new LinkedList<>();

        String[] columns = {
                SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.TYPE_COLUMN,
                SqliteHelper.AccountEntry.EMAIL_COLUMN,
                SqliteHelper.AccountEntry.ARCHIVE_COLUMN,
                SqliteHelper.BorrowerAccount.TABLE_NAME + "." + SqliteHelper.BorrowerAccount.NAME_COLUMN,
                SqliteHelper.BorrowerAccount.NAME_COLUMN,
                SqliteHelper.BorrowerAccount.PIC_COLUMN
        };

        String selection = SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN +
                " = " + SqliteHelper.BorrowerAccount.TABLE_NAME + "." + SqliteHelper.BorrowerAccount.USERNAME_COLUMN;

        Cursor cursor = db.query(
                SqliteHelper.AccountEntry.TABLE_NAME + " INNER JOIN " + SqliteHelper.BorrowerAccount.TABLE_NAME +
                        " ON " + selection,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            String type_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.TYPE_COLUMN));
            String email_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
            String archive_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.ARCHIVE_COLUMN));
            String fullname_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.NAME_COLUMN));

            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.PIC_COLUMN));
            Bitmap img = null; // Default value for the image

            if (imageBytes != null && imageBytes.length > 0) {
                img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }

            if (type.equals(type_)) {
                if (archive_.equalsIgnoreCase("YES"))
                    archlist.add(new usersModel(name_, type_, email_, img, fullname_));
                else
                    list.add(new usersModel(name_, type_, email_, img, fullname_));
            }
        }

        Log.d("", Arrays.toString(list.toArray()));
        return isArchive ? archlist : list;
    }

    public LinkedList<userLenderModel> getUsersLenderList(String type, boolean isArchive)
    {
        LinkedList<userLenderModel> list = new LinkedList<>();
        LinkedList<userLenderModel> archlist = new LinkedList<>();
        String[] columns = {
                SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.ARCHIVE_COLUMN,
                SqliteHelper.AccountEntry.EMAIL_COLUMN,
                SqliteHelper.AccountEntry.TYPE_COLUMN,
                SqliteHelper.LenderAccount.TABLE_NAME + "." + SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.PIC_COLUMN,
                SqliteHelper.LenderAccount.MIN_COLUMN,
                SqliteHelper.LenderAccount.MAX_COLUMN,
                SqliteHelper.LenderAccount.FREQ_COLUMN,
                SqliteHelper.LenderAccount.RATE_COLUMN,
        };

        String selection = SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN +
                " = " + SqliteHelper.LenderAccount.TABLE_NAME + "." + SqliteHelper.LenderAccount.NAME_COLUMN;

        Cursor cursor = db.query(
                SqliteHelper.AccountEntry.TABLE_NAME + " INNER JOIN " + SqliteHelper.LenderAccount.TABLE_NAME +
                        " ON " + selection,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext())
        {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
            double max_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.MAX_COLUMN));
            double min_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.MIN_COLUMN));
            String freq_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.FREQ_COLUMN));
            double rate_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.RATE_COLUMN));
            String email_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
            String archive_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.ARCHIVE_COLUMN));
            String type_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.TYPE_COLUMN));
            byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.PIC_COLUMN));

            Bitmap img = null; // Default value for the image

            if (imageBytes != null && imageBytes.length > 0) {
                img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
            }

            if (type.equals(type_)) {
                if (archive_.equalsIgnoreCase("YES"))
                    archlist.add(new userLenderModel(name_, min_, max_, rate_, freq_, email_, img));
                else
                    list.add(new userLenderModel(name_, min_, max_, rate_, freq_, email_, img));
            }
        }

        return isArchive ? archlist : list;
    }
    public String checkLogin(String username, String password)
    {
        String[] columns = {SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.EMAIL_COLUMN,
                SqliteHelper.AccountEntry.PASSWORD_COLUMN,
                SqliteHelper.AccountEntry.ARCHIVE_COLUMN,
                SqliteHelper.AccountEntry.TYPE_COLUMN};
        Cursor cursor = db.query(SqliteHelper.AccountEntry.TABLE_NAME, columns, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            String username_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            String password_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.PASSWORD_COLUMN));
            String archive_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.ARCHIVE_COLUMN));
            String type_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.TYPE_COLUMN));

            if(archive_.equalsIgnoreCase("YES")) return "false";
            if(username_.trim().equalsIgnoreCase(username) && password_.trim().equals(password))
            {
                return type_;
            }
        }
        return "false";
    }

    public void getEditLender(String username) {
        String[] columns = {
                SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.ARCHIVE_COLUMN,
                SqliteHelper.AccountEntry.EMAIL_COLUMN,
                SqliteHelper.AccountEntry.TYPE_COLUMN,
                SqliteHelper.LenderAccount.TABLE_NAME + "." + SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.PIC_COLUMN,
                SqliteHelper.LenderAccount.MIN_COLUMN,
                SqliteHelper.LenderAccount.MAX_COLUMN,
                SqliteHelper.LenderAccount.FREQ_COLUMN,
                SqliteHelper.LenderAccount.RATE_COLUMN,
        };

        String selection = SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN +
                " = " + SqliteHelper.LenderAccount.TABLE_NAME + "." + SqliteHelper.LenderAccount.NAME_COLUMN;

        Cursor cursor = db.query(
                SqliteHelper.AccountEntry.TABLE_NAME + " INNER JOIN " + SqliteHelper.LenderAccount.TABLE_NAME +
                        " ON " + selection,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String username_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            String email_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
            String freq_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.FREQ_COLUMN));
            double min_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.MIN_COLUMN));
            double max_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.MAX_COLUMN));
            double rate_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.RATE_COLUMN));

            if(username.equalsIgnoreCase(username_))
            {
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.PIC_COLUMN));
                Bitmap img = null; // Default value for the image

                if (imageBytes != null && imageBytes.length > 0) {
                    img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
                Username = username_;
                Email = email_;
                Name = name_;
                Freq = freq_;
                Min = min_;
                Max = max_;
                Rate = rate_;
                pic = img;
                break;
            }
        }
    }

    public String Username, Email, Name, Freq;
    public double Min, Max, Rate;
    public Bitmap pic;
}
