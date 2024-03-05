package com.example.Helper;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
    public boolean newAccount(String username, String password, String email)
    {
        ContentValues values = new ContentValues();
        values.put(SqliteHelper.AccountEntry.USERNAME_COLUMN, username);
        values.put(SqliteHelper.AccountEntry.EMAIL_COLUMN, email);
        values.put(SqliteHelper.AccountEntry.PASSWORD_COLUMN, password);

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

            if(username_.equals(username) && password_.equals(password))
            {
                return true;
            }
        }
        return false;
    }
}
