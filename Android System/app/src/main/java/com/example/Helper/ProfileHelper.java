package com.example.Helper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.media.Image;
import android.util.Log;

import com.example.model.usersModel;

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
    public boolean newAccount(String username, String password, String email, String type)
    {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.AccountEntry.USERNAME_COLUMN, username.trim());
        values.put(SqliteHelper.AccountEntry.EMAIL_COLUMN, email.trim());
        values.put(SqliteHelper.AccountEntry.PASSWORD_COLUMN, password.trim());
        values.put(SqliteHelper.AccountEntry.TYPE_COLUMN, type.trim());
        try
        {
            long newRowId = db.insert(SqliteHelper.AccountEntry.TABLE_NAME, null, values);
            if(newRowId != -1)
                return true;
            else
                return false;
        }
         catch (Exception e)
         {
             Log.e("New Account", e.getMessage());
             return false;
         }
    }

    public LinkedList<usersModel> getUsersList(String type, Bitmap img) // temp lng img
    {
        LinkedList<usersModel> list = new LinkedList<>();
        String[] columns = {SqliteHelper.AccountEntry.USERNAME_COLUMN,
                            SqliteHelper.AccountEntry.TYPE_COLUMN};
        Cursor cursor = db.query(SqliteHelper.AccountEntry.TABLE_NAME, columns, null,null,null,null, null);

        while(cursor.moveToNext())
        {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            String type_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.TYPE_COLUMN));

            if(type.equals(type_))
                list.add(new usersModel(name_, type_, img));
        }

        return list;
    }

    public boolean checkLogin(String username, String password)
    {
        String[] columns = {SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.EMAIL_COLUMN,
                SqliteHelper.AccountEntry.PASSWORD_COLUMN};
        Cursor cursor = db.query(SqliteHelper.AccountEntry.TABLE_NAME, columns, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            String username_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            String password_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.PASSWORD_COLUMN));

            if(username_.trim().equals(username) && password_.trim().equals(password))
            {
                return true;
            }
        }
        return false;
    }
}
