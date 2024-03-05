package com.example.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LoanManagement.db";
    public static final String LOGIN_TABLE_CREATE = "CREATE TABLE Account (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL," +
            "password TEXT NOT NULL," +
            "email TEXT NOT NULL);";


    private static final String CREATE_TABLES = LOGIN_TABLE_CREATE;
    private static final String DELETE_TABLES = "DROP TABLE IF EXIST ";
    public SqliteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DELETE_TABLES + AccountEntry.TABLE_NAME);
    }

    public static class AccountEntry
    {
        public static final String TABLE_NAME = "Account";
        public static final String USERNAME_COLUMN = "username";
        public static final String PASSWORD_COLUMN = "password";
        public static final String EMAIL_COLUMN = "email";
    }
}
