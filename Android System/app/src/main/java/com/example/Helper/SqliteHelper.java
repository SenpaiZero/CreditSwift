package com.example.Helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Date;

public class SqliteHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 6;
    public static final String DATABASE_NAME = "LoanManagement.db";
    public static final String LOGIN_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS Account (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "username TEXT NOT NULL UNIQUE," +
            "password TEXT NOT NULL," +
            "email TEXT NOT NULL," +
            "type TEXT NOT NULL," +
            "archive TEXT NOT NULL," +
            "data TEXT NOT NULL);";

    public static final String LENDER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS lenderAccount (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL UNIQUE," +
            "minimum REAL NOT NULL," +
            "maximum REAL NOT NULL," +
            "rate REAL NOT NULL," +
            "frequency TEXT NOT NULL," +
            "picture BLOB NOT NULL," +
            "appliedBorrower TEXT," +
            "currentAppliedBorrower TEXT," +
            "spent REAL," +
            "interest REAL);";

    public static final String BORROWER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS borrowerAccount (" +
            "id INTEGER PRIMARY KEY AUTOINCREMENT," +
            "name TEXT NOT NULL UNIQUE," +
            "birthday TEXT NOT NULL," +
            "picture BLOB," +
            "list TEXT," +
            "doneList TEXT," +
            "totalLend REAL," +
            "currentTotalLend REAL," +
            "totalInterest REAL," +
            "username TEXT NOT NULL);";


    public SqliteHelper(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(LOGIN_TABLE_CREATE);
        db.execSQL(LENDER_TABLE_CREATE);
        db.execSQL(BORROWER_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Account");
        db.execSQL("DROP TABLE IF EXISTS lenderAccount");
        db.execSQL("DROP TABLE IF EXISTS borrowerAccount");
        onCreate(db);
    }

    public static class AccountEntry
    {
        public static final String TABLE_NAME = "Account";

        // All space will be replaced with _
        public static final String USERNAME_COLUMN = "username";
        public static final String PASSWORD_COLUMN = "password";
        public static final String EMAIL_COLUMN = "email";
        public static final String TYPE_COLUMN = "type";
        public static final String ARCHIVE_COLUMN = "archive"; // Yes or No
        public static final String DATA_COLUMN = "data";
    }

    public static class LenderAccount
    {
        public static final String TABLE_NAME= "lenderAccount";
        public static final String NAME_COLUMN = "name";
        public static final String MIN_COLUMN = "minimum";
        public static final String MAX_COLUMN = "maximum";
        public static final String RATE_COLUMN = "rate";
        public static final String FREQ_COLUMN = "frequency";
        public static final String PIC_COLUMN = "picture";


        // List ng mga borrower na kinuha nya (delimiter is '|')
        // (use for unpaid (minus this to done))
        public static final String APPLIED_BORROWER_COLUMN = "appliedBorrower";

        // Same sa applied borrower pero current lng (use for unpaid)
        public static final String CURRENT_APPLIED_BORROWER_COLUMN = "currentAppliedBorrower";
        public static final String TOTAL_SPENT = "spent";
        public static final String TOTAL_INTEREST = "interest";
    }

    public static class BorrowerAccount
    {
        public static final String TABLE_NAME = "borrowerAccount";
        public static final String USERNAME_COLUMN = "username";
        public static final String NAME_COLUMN = "name";
        public static final String BIRTH_COLUMN = "birthday";
        public static final String PIC_COLUMN = "picture";

        //List ng mga lender na kinuha nya (delimiter is '|') (use for unpaid (minus this to done))
        public static final String LIST_COLUMN = "list";

        //Same sa list pero mga fully paid na
        public static final String LIST_DONE_COLUMN = "doneList";

        // Kung magkano total nyang inutang
        public static final String TOTAL_LEND_COLUMN = "totalLend";

        // Same sa total lend pero current
        public static final String TOTAL_CURRENT_LEND_COLUMN = "currentTotalLend";
        public static final String TOTAL_INTEREST_COLUMN = "totalInterest";
    }
}
