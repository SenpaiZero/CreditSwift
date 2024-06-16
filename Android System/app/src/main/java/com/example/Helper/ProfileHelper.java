package com.example.Helper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Log;

import com.example.model.applyModel;
import com.example.model.listBorrowModel;
import com.example.model.userLenderModel;
import com.example.model.usersModel;

import java.io.ByteArrayOutputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

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
                return "duplicate";
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

            if(name_.equalsIgnoreCase(name)) {
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
                    new String[]{username.toUpperCase().toUpperCase()}
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
                new String[]{name.toUpperCase()});
        int rowsAffected2 = db.update(SqliteHelper.AccountEntry.TABLE_NAME,
                contentVal,
                SqliteHelper.AccountEntry.USERNAME_COLUMN + " = ?",
                new String[]{name.toUpperCase()});

        return rowsAffected > 0 && rowsAffected2 > 0;
    }

    public boolean updateBorrowerInfo(String username, String fullname, String email, String birth, Bitmap pic) {
        ContentValues content = new ContentValues();
        content.put(SqliteHelper.BorrowerAccount.NAME_COLUMN, fullname.toUpperCase());
        content.put(SqliteHelper.BorrowerAccount.BIRTH_COLUMN, birth);
        byte[] imageBytes = null;
        if (pic != null) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            pic.compress(Bitmap.CompressFormat.PNG, 0, outputStream);
            imageBytes = outputStream.toByteArray();
        }
        content.put(SqliteHelper.BorrowerAccount.PIC_COLUMN, imageBytes);

        ContentValues contentVal = new ContentValues();
        contentVal.put(SqliteHelper.AccountEntry.EMAIL_COLUMN, email.toUpperCase());

        int rowsAffected = db.update(SqliteHelper.BorrowerAccount.TABLE_NAME,
                content,
                SqliteHelper.BorrowerAccount.USERNAME_COLUMN + " = ?",
                new String[]{username.toUpperCase()});
        int rowsAffected2 = db.update(SqliteHelper.AccountEntry.TABLE_NAME,
                contentVal,
                SqliteHelper.AccountEntry.USERNAME_COLUMN + " = ?",
                new String[]{username.toUpperCase()});

        return rowsAffected > 0 && rowsAffected2 > 0;
    }

    public String userFullnameToUsername(String fullname) {
        String[] columns = {
                SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.BorrowerAccount.TABLE_NAME + "." + SqliteHelper.BorrowerAccount.NAME_COLUMN,
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

        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.NAME_COLUMN));

            if(name_.equalsIgnoreCase(fullname)) {
                return cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN)).toUpperCase();
            }
        }
        return null;
    }

    public boolean acceptDeclineCurrentLend(String lenderName, String borrowName, boolean isAccept) {
        String[] columns = {
                SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.LenderAccount.TABLE_NAME + "." + SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN,
                SqliteHelper.LenderAccount.RATE_COLUMN,
                SqliteHelper.LenderAccount.FREQ_COLUMN
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
        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
            String val_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN));
            double rate_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.RATE_COLUMN));
            String freq = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.FREQ_COLUMN));

            // Found the lender
            if(name_.equalsIgnoreCase(lenderName)) {
                String[] data = val_.split("\\|");
                Log.e("apply", "bef: " + Arrays.toString(data));
                String[] data_;

                for(int i = 0; i < data.length; i++) {
                    data_ = data[i].split(":");
                    Log.d("Apply", data_[0] + " : " + userFullnameToUsername(borrowName));
                    if(data_[0].equalsIgnoreCase(userFullnameToUsername(borrowName))) {
                        if(isAccept) {
                            data[i] = data_[0]+":"+"TRUE"+":"+data_[2]+":"+data_[3]+":"+data_[4]+":"+DateHelper.addMonth(data_[5], freq);
                            double interest_= (rate_ / 100) * Double.valueOf(data_[3]);
                            updateTotalInterestSpent(lenderName, interest_, Double.valueOf(data_[3]));
                            updateCurrentTotalLend(data_[0], Double.valueOf(data_[3]), true);
                        }
                        else
                        {
                            data[i] = "";
                        }
                        Log.e("apply", "update data");
                    }
                }

                String newVal = "";

                for (String datum : data) {
                    if(!datum.isEmpty())
                        newVal += datum + "|";
                }

                Log.d("Apply", newVal);
                ContentValues values = new ContentValues();
                values.put(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN, newVal);
                db.update(SqliteHelper.LenderAccount.TABLE_NAME,  values,
                        SqliteHelper.LenderAccount.NAME_COLUMN+ " = ?",
                        new String[]{lenderName.toUpperCase()});

                if(isAccept) {
                    addRemoveListBorrower(lenderName, borrowName, true);
                }
                return true;
            }
        }
        return false;
    }

    public void addRemoveListBorrower(String lenderName, String borrowName, boolean isAdd) {
        String[] col = {
                SqliteHelper.BorrowerAccount.NAME_COLUMN,
                SqliteHelper.BorrowerAccount.LIST_COLUMN
        };
        Cursor cursor = db.query(
                SqliteHelper.BorrowerAccount.TABLE_NAME,
                col,
                null,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.NAME_COLUMN));
            String val_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.LIST_COLUMN));
            ContentValues values = new ContentValues();
            Log.d("Apply", name_ + " :::: " + borrowName);

            String comp = isAdd ? name_ : userFullnameToUsername(name_);
            Log.d("Apply", comp + "COMP COMOP");
            if(comp.equalsIgnoreCase(borrowName)) {
                String newVal = "";
                if(isAdd) {
                    newVal = val_ + lenderName + "|";
                }
                else {
                    System.out.println("REMOVING!!");
                    String data[] = val_.split("\\|");
                    for(int i = 0; i < data.length; i++) {
                        if(data[i].equalsIgnoreCase(lenderName)) {
                            data[i] = "";
                        }
                    }

                    newVal = "";

                    for (String datum : data) {
                        if(!datum.isEmpty())
                            newVal += datum + "|";
                    }
                }

                System.out.println("Removing list: " + newVal);
                values.put(SqliteHelper.BorrowerAccount.LIST_COLUMN, newVal);
                if(isAdd) {
                    db.update(SqliteHelper.BorrowerAccount.TABLE_NAME,  values,
                            SqliteHelper.BorrowerAccount.NAME_COLUMN+ " = ?",
                            new String[]{borrowName.toUpperCase()});
                }
                else {
                    db.update(SqliteHelper.BorrowerAccount.TABLE_NAME,  values,
                            SqliteHelper.BorrowerAccount.USERNAME_COLUMN+ " = ?",
                            new String[]{borrowName.toUpperCase()});
                }
            }
        }

    }
    public void donePayment(String borrowerName, String lenderName, double total, double year) {
        ContentValues values = new ContentValues();
        String valB = lenderName+":"+total+":"+year+"|";
        String valL = borrowerName+":"+total+":"+year+"|";
        String[] col = {
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.APPLIED_BORROWER_COLUMN
        };
        Cursor cursor = db.query(
                SqliteHelper.LenderAccount.TABLE_NAME,
                col,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
            if(name_.equalsIgnoreCase(lenderName.replaceAll(" ", "_"))) {
                String val_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.APPLIED_BORROWER_COLUMN));
                values.clear();

                if(val_ == null || val_.isEmpty()) val_ = "";

                if(val_.length() < 1)
                    values.put(SqliteHelper.LenderAccount.APPLIED_BORROWER_COLUMN, valL);
                else
                    values.put(SqliteHelper.LenderAccount.APPLIED_BORROWER_COLUMN, val_ + valL);

                db.update(SqliteHelper.LenderAccount.TABLE_NAME,  values,
                        SqliteHelper.LenderAccount.NAME_COLUMN+ " = ?",
                        new String[]{lenderName.replaceAll(" ", "_").toUpperCase()});
                break;
            }
        }

        ContentValues values2 = new ContentValues();
        String[] col2 = {
                SqliteHelper.BorrowerAccount.USERNAME_COLUMN,
                SqliteHelper.BorrowerAccount.LIST_DONE_COLUMN
        };
        Cursor cursor2 = db.query(
                SqliteHelper.BorrowerAccount.TABLE_NAME,
                col2,
                null,
                null,
                null,
                null,
                null
        );
        while (cursor2.moveToNext()) {
            String name_ = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.USERNAME_COLUMN));
            Log.d("Apply", "DONE PAY: Name: " + name_ + " : Comp: " + borrowerName);
            if(name_.equalsIgnoreCase(borrowerName.replaceAll(" ", "_"))) {
                String val_ = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.LIST_DONE_COLUMN));


                if(val_ == null || val_.isEmpty()) val_ = "";

                if(val_.length() < 1)
                    values2.put(SqliteHelper.BorrowerAccount.LIST_DONE_COLUMN, valB);
                else
                    values2.put(SqliteHelper.BorrowerAccount.LIST_DONE_COLUMN, val_ + valB);

                db.update(SqliteHelper.BorrowerAccount.TABLE_NAME,  values2,
                        SqliteHelper.BorrowerAccount.USERNAME_COLUMN+ " = ?",
                        new String[]{borrowerName.replaceAll(" ", "_").toUpperCase()});
                break;
            }
        }
    }
    public String addUpdateCurrentLend(String borrowerName, String lenderName, double remaining, double total, int year, boolean isApply) {
        ContentValues lender = new ContentValues();
        String val = borrowerName + ":"+String.valueOf(!isApply).toUpperCase()+":"+remaining+":"+total+":"+year+":"+DateHelper.getCurrentDate()+"|";
        lender.put(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN, val);
        Log.d("APPLY", "VALUE: " + val + " LENDER NAME: " + lenderName);
        String[] col = {
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN,
                SqliteHelper.LenderAccount.FREQ_COLUMN
        };
        Cursor cursor = db.query(
                SqliteHelper.LenderAccount.TABLE_NAME,
                col,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
            String val_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN));
            String freq = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.FREQ_COLUMN));

            if(val_ == null || val_.isEmpty()) val_ = "";
            if(name_.equalsIgnoreCase(lenderName.replaceAll(" ", "_"))) {
                if(isApply) {
                    if(val_.contains(borrowerName)) return "EXIST";
                }

                boolean isUpdated = false;
                String[] data = val_.split("\\|");
                Log.e("apply", "bef: " + Arrays.toString(data));
                String[] data_;
                String returnType = "ADD";

                for(int i = 0; i < data.length; i++) {
                     data_ = data[i].split(":");
                     if(data_[0].equalsIgnoreCase(borrowerName)) {
                         // Remaining nakukuha sa UIHelper through parameters
                         // This means payment
                         if(year == 1122334455 || total == 1122334455) {
                             val = borrowerName + ":"+String.valueOf(!isApply).toUpperCase()+":"+remaining+":"+data_[3]+":"+data_[4]+":"+DateHelper.addMonth(data_[5], freq)+"|";
                             Log.e("apply", "updated remaining " + remaining);
                         }

                         if(remaining <= 0) {
                             donePayment(borrowerName, lenderName, Double.valueOf(data_[3]) , Double.valueOf(data_[4]));
                             addRemoveListBorrower(lenderName.replaceAll(" ", "_"), borrowerName, false);
                             updateCurrentTotalLend(data_[0], Double.valueOf(data_[3]), false);
                             updateTotalLend(data_[0], Double.valueOf(data_[3]));

                             data[i] = "";
                         } else {
                             data[i] = val;
                         }
                         Log.e("apply", "update data " + remaining);
                         isUpdated = true;
                         returnType = "UPDATE";
                     }
                }


                String newVal = "";

                for (String datum : data) {
                    if(!datum.isEmpty())
                        newVal += datum + "|";
                }
                if(!isUpdated) newVal = val_ + val;

                Log.e("apply", "aft: " + Arrays.toString(data));
                lender.clear();
                Log.e("apply", "newVal: " + newVal);
                lender.put(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN, newVal);
                db.update(SqliteHelper.LenderAccount.TABLE_NAME,  lender,
                        SqliteHelper.LenderAccount.NAME_COLUMN+ " = ?",
                        new String[]{lenderName.replaceAll(" ", "_").toUpperCase()});
                return returnType;
            }
        }
        return "";
    }

    public LinkedList<applyModel> getApplyList(String companyName) {
        LinkedList<applyModel> list = new LinkedList<>();

        String[] col = {
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN
        };
        Cursor cursor = db.query(
                SqliteHelper.LenderAccount.TABLE_NAME,
                col,
                null,
                null,
                null,
                null,
                null
        );

        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
            String val_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN));

            Log.d("apply", "COMPANY: " + companyName + " COMPARE: " + name_ + " VALUE: " + val_);
            if(name_.equalsIgnoreCase(companyName)) {
                if(val_==null || val_.isEmpty()) return list;

                String[] data = val_.split("\\|"); // Escape | character
                String[] data_;

                for(int i = 0; i < data.length; i++) {
                    data_ = data[i].split(":");
                    String[] columns = {
                            SqliteHelper.AccountEntry.EMAIL_COLUMN,
                            SqliteHelper.BorrowerAccount.NAME_COLUMN,
                            SqliteHelper.BorrowerAccount.PIC_COLUMN
                    };

                    Cursor cursor2 = db.query(
                            true,
                            SqliteHelper.BorrowerAccount.TABLE_NAME + " INNER JOIN " + SqliteHelper.AccountEntry.TABLE_NAME +
                                    " ON " + SqliteHelper.BorrowerAccount.TABLE_NAME + "." + SqliteHelper.BorrowerAccount.USERNAME_COLUMN +
                                    " = " + SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                            columns,
                            SqliteHelper.BorrowerAccount.TABLE_NAME + "." + SqliteHelper.BorrowerAccount.USERNAME_COLUMN + "=?",
                            new String[]{data_[0].toUpperCase()}, // Pass username as selection argument
                            null,
                            null,
                            null,
                            null
                    );

                    while (cursor2.moveToNext()) {
                        String email_ = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
                        String fullname_ = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.NAME_COLUMN));

                        byte[] imageBytes = cursor2.getBlob(cursor2.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.PIC_COLUMN));
                        Bitmap img = null; // Default value for the image

                        if (imageBytes != null && imageBytes.length > 0) {
                            img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        }

                        if(data_[1].equalsIgnoreCase("FALSE"))
                            list.add(new applyModel(fullname_, email_, Double.valueOf(data_[3]), img));
                    }
                    cursor2.close();
                }
                cursor.close();
                return list;
            }
        }
        cursor.close();
        return null;
    }
    public boolean changeArchive(String username, boolean isArchive) {
        ContentValues val = new ContentValues();
        val.put(SqliteHelper.AccountEntry.ARCHIVE_COLUMN, isArchive ? "YES" : "NO");

        db.update(
                SqliteHelper.AccountEntry.TABLE_NAME,
                val,
                SqliteHelper.AccountEntry.USERNAME_COLUMN + " = ?",
                new String[]{username.replaceAll(" ", "_").toUpperCase()}
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
        actValues.put(SqliteHelper.AccountEntry.TYPE_COLUMN, "LENDER");
        actValues.put(SqliteHelper.AccountEntry.ARCHIVE_COLUMN, "NO");
        actValues.put(SqliteHelper.AccountEntry.DATA_COLUMN, "YES");

        values.put(SqliteHelper.LenderAccount.NAME_COLUMN, name_.toUpperCase());
        values.put(SqliteHelper.LenderAccount.MIN_COLUMN, minimum);
        values.put(SqliteHelper.LenderAccount.MAX_COLUMN, maximum);
        values.put(SqliteHelper.LenderAccount.RATE_COLUMN, rate);
        values.put(SqliteHelper.LenderAccount.FREQ_COLUMN, frequency);
        values.put(SqliteHelper.LenderAccount.APPLIED_BORROWER_COLUMN, "");
        values.put(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN, "");
        values.put(SqliteHelper.LenderAccount.TOTAL_SPENT, 0);
        values.put(SqliteHelper.LenderAccount.TOTAL_INTEREST, 0);

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
                return "duplicate";
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

    public LinkedList<usersModel> getUsersList(String type, boolean isArchive, String search) {
        LinkedList<usersModel> list = new LinkedList<>();
        LinkedList<usersModel> archlist = new LinkedList<>();
        boolean isSearch = search != null || !search.isEmpty();
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

        String where = SqliteHelper.BorrowerAccount.NAME_COLUMN + " LIKE ?";


        String searchPattern = "%" + search.replaceAll(" ", "_") + "%";
        String[] selectionArgs = new String[]{searchPattern};

        Cursor cursor = db.query(
                SqliteHelper.AccountEntry.TABLE_NAME + " INNER JOIN " + SqliteHelper.BorrowerAccount.TABLE_NAME +
                        " ON " + selection,
                columns,
                isSearch ? where : null,
                isSearch ? selectionArgs : null,
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

            if (type.equalsIgnoreCase(type_)) {
                if (archive_.equalsIgnoreCase("YES"))
                    archlist.add(new usersModel(name_, type_, email_, img, fullname_));
                else
                    list.add(new usersModel(name_, type_, email_, img, fullname_));
            }
        }

        return isArchive ? archlist : list;
    }

    public LinkedList<userLenderModel> getUsersLenderList(String type, boolean isArchive, String search) {
        LinkedList<userLenderModel> list = new LinkedList<>();
        LinkedList<userLenderModel> archlist = new LinkedList<>();
        boolean isSearch = search != null || !search.isEmpty();

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

        String where = SqliteHelper.LenderAccount.NAME_COLUMN + " LIKE ?";


        String searchPattern = "%" + search.replaceAll(" ", "_") + "%";
        String[] selectionArgs = new String[]{searchPattern};

        Cursor cursor = db.query(
                SqliteHelper.AccountEntry.TABLE_NAME + " INNER JOIN " + SqliteHelper.LenderAccount.TABLE_NAME +
                        " ON " + selection,
                columns,
                isSearch ? where : null,
                isSearch ? selectionArgs : null,
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

            if (type.equalsIgnoreCase(type_)) {
                if (archive_.equalsIgnoreCase("YES"))
                    archlist.add(new userLenderModel(name_, min_, max_, rate_, freq_, email_, img));
                else
                    list.add(new userLenderModel(name_, min_, max_, rate_, freq_, email_, img));
            }
        }

        return isArchive ? archlist : list;
    }
    public String checkLogin(String username, String password) {
        String[] columns = {
                SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.PASSWORD_COLUMN,
                SqliteHelper.AccountEntry.ARCHIVE_COLUMN,
                SqliteHelper.AccountEntry.TYPE_COLUMN
        };

        String selection = SqliteHelper.AccountEntry.USERNAME_COLUMN + " = ?";
        String[] selectionArgs = { username.toUpperCase() };

        @SuppressLint("Recycle") Cursor cursor =
                db.query(SqliteHelper.AccountEntry.TABLE_NAME,
                        columns, selection, selectionArgs,
                        null,
                        null,
                        null);

        while(cursor.moveToNext())
        {
            String username_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            String password_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.PASSWORD_COLUMN));
            String archive_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.ARCHIVE_COLUMN));
            String type_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.TYPE_COLUMN));

            if(archive_.equalsIgnoreCase("YES")) return "archived";
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
                lenderUsername = username_;
                lenderEmail = email_;
                lenderName = name_;
                lenderFreq = freq_;
                lenderMin = min_;
                lenderMax = max_;
                lenderRate = rate_;
                lenderPic = img;
                break;
            }
        }
    }

    public String lenderUsername, lenderEmail, lenderName, lenderFreq;
    public double lenderMin, lenderMax, lenderRate;
    public Bitmap lenderPic;

    public void getEditBorrower(String username) {
        String[] columns = {
                SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.TYPE_COLUMN,
                SqliteHelper.AccountEntry.EMAIL_COLUMN,
                SqliteHelper.AccountEntry.ARCHIVE_COLUMN,
                SqliteHelper.BorrowerAccount.TABLE_NAME + "." + SqliteHelper.BorrowerAccount.NAME_COLUMN,
                SqliteHelper.BorrowerAccount.NAME_COLUMN,
                SqliteHelper.BorrowerAccount.PIC_COLUMN,
                SqliteHelper.BorrowerAccount.BIRTH_COLUMN
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
            String username_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            String email_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.NAME_COLUMN));
            String birth_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.BIRTH_COLUMN));

            Log.d("user", username_);
            if(username.equalsIgnoreCase(username_))
            {
                byte[] imageBytes = cursor.getBlob(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.PIC_COLUMN));
                Bitmap img = null;

                if (imageBytes != null && imageBytes.length > 0) {
                    img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                }
                borrowUsername = username_;
                borrowEmail = email_;
                borrowName = name_;
                borrowPic = img;
                borrowBirth = birth_;
                break;
            }
        }
    }

    public String borrowUsername, borrowEmail, borrowName, borrowBirth;
    public Bitmap borrowPic;

    public LinkedList<listBorrowModel> getCurrentListBorrow(String borrowName) {
        LinkedList<listBorrowModel> list = new LinkedList<>();

        Cursor cursor = db.query(
                SqliteHelper.BorrowerAccount.TABLE_NAME,
                new String[] {SqliteHelper.BorrowerAccount.NAME_COLUMN,
                        SqliteHelper.BorrowerAccount.LIST_COLUMN},
                null,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.NAME_COLUMN));
            String val_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.LIST_COLUMN));

            Log.e("APPLY", "NAME: " + name_ + " compa: " + borrowName);
            if(userFullnameToUsername(name_).equalsIgnoreCase(borrowName)) {
                // now kukunin na val = lender
                String[] lenders = val_.split("\\|");

                for(int i = 0; i < lenders.length; i++) {
                    String[] columns = {
                            SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                            SqliteHelper.AccountEntry.EMAIL_COLUMN,
                            SqliteHelper.LenderAccount.TABLE_NAME + "." + SqliteHelper.LenderAccount.NAME_COLUMN,
                            SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN,
                            SqliteHelper.LenderAccount.FREQ_COLUMN,
                            SqliteHelper.LenderAccount.RATE_COLUMN,
                            SqliteHelper.LenderAccount.PIC_COLUMN
                    };

                    String selection = SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN +
                            " = " + SqliteHelper.LenderAccount.TABLE_NAME + "." + SqliteHelper.LenderAccount.NAME_COLUMN;

                    Cursor cursor2 = db.query(
                            SqliteHelper.AccountEntry.TABLE_NAME + " INNER JOIN " + SqliteHelper.LenderAccount.TABLE_NAME +
                                    " ON " + selection,
                            columns,
                            null,
                            null,
                            null,
                            null,
                            null
                    );

                    Log.d("apply", "List is running");
                    while (cursor2.moveToNext()) {
                        String companyName_ = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN)).toUpperCase();

                        Log.e("APPLY", "comapny: " + companyName_ + " :: compare: " + lenders[i]);
                        if(companyName_.equalsIgnoreCase(lenders[i])) {
                            String value_ = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN));
                            String email_ = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
                            String freq_ = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.LenderAccount.FREQ_COLUMN));
                            String lenderName = cursor2.getString(cursor2.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
                            double rate_ = cursor2.getDouble(cursor2.getColumnIndexOrThrow(SqliteHelper.LenderAccount.RATE_COLUMN));

                            if(value_ == null || value_.isEmpty()) return list;
                            String[] data = value_.split("\\|");
                            String[] data_;
                            byte[] imageBytes = cursor2.getBlob(cursor2.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.PIC_COLUMN));
                            Bitmap img = null;

                            if (imageBytes != null && imageBytes.length > 0) {
                                img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                            }

                            for(int j = 0; j < data.length; j++) {
                                data_ = data[j].split(":");
                                if(data_.length <= 1) continue;

                                String name = data_[0];
                                String isAccept = data_[1];
                                double remaining = Double.valueOf(data_[2]);
                                double total = Double.valueOf(data_[3]);
                                int year = Integer.valueOf(data_[4]);
                                String due = data_[5];
                                if(isAccept.equalsIgnoreCase("TRUE") && name.equalsIgnoreCase(borrowName)) {
                                    list.add(new listBorrowModel(lenderName.replaceAll("_", " "),
                                            freq_, email_, total, remaining, year, rate_, img, due));
                                    Log.e("APPLY", "ADDED TO THE LIST");
                                }
                            }
                        }
                    }
                }
                return list;
            }
        }
        return list;
    }

    public LinkedList<listBorrowModel> getBorrowList_Lender(String lenderName, String filterType) {
        LinkedList<listBorrowModel> list = new LinkedList<>();

        String[] columns = {
                SqliteHelper.AccountEntry.TABLE_NAME + "." + SqliteHelper.AccountEntry.USERNAME_COLUMN,
                SqliteHelper.AccountEntry.EMAIL_COLUMN,
                SqliteHelper.LenderAccount.TABLE_NAME + "." + SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN,
                SqliteHelper.LenderAccount.FREQ_COLUMN,
                SqliteHelper.LenderAccount.RATE_COLUMN
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

        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
            if(name_.equalsIgnoreCase(lenderName)) {
                String val_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN));
                String email_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
                String freq_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.FREQ_COLUMN));
                double interest = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.FREQ_COLUMN));

                if(val_ == null) val_ = "";
                if(val_ == null | val_.isEmpty()) return list;
                String[] data = val_.split("\\|");
                String[] data_;

                for(int i = 0; i < data.length; i++) {
                    data_ = data[i].split(":");
                    if(data_.length <= 1) continue;

                    Cursor cursor2 = db.query(
                            SqliteHelper.BorrowerAccount.TABLE_NAME,
                            new String[] {SqliteHelper.BorrowerAccount.USERNAME_COLUMN,
                                    SqliteHelper.BorrowerAccount.PIC_COLUMN},
                            SqliteHelper.BorrowerAccount.USERNAME_COLUMN + "=?",
                            new String[]{data_[0].toUpperCase()},
                            null,
                            null,
                            null
                    );
                    Bitmap img = null;
                    if(cursor2.moveToNext()) {
                        byte[] imageBytes = cursor2.getBlob(cursor2.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.PIC_COLUMN));

                        if (imageBytes != null && imageBytes.length > 0) {
                            img = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                        }
                    }
                    String borrowName = data_[0];
                    String isAccept = data_[1];
                    double remaining = Double.valueOf(data_[2]);
                    double total = Double.valueOf(data_[3]);
                    int year = Integer.valueOf(data_[4]);
                    String due = data_[5];


                    if(isAccept.equalsIgnoreCase("TRUE")){
                        if(filterType.equalsIgnoreCase("PAST DUE")) {
                            if(!DateHelper.isDue(due)) {
                                list.add(new listBorrowModel(borrowName, freq_, getUserEmail(borrowName),
                                        total, remaining, year, interest, img, due));
                            }
                        }
                        else if(filterType.equalsIgnoreCase("TO PAY")) {
                            if(DateHelper.isDue(due)) {
                                list.add(new listBorrowModel(borrowName, freq_, getUserEmail(borrowName),
                                        total, remaining, year, interest, img, due));
                            }
                        }
                        else {
                            list.add(new listBorrowModel(borrowName, freq_, getUserEmail(borrowName),
                                    total, remaining, year, interest, img, due));
                        }
                    }
                }
                return list;
            }
        }
        return list;
    }

    public String getUserEmail(String user)  {
        String[] col = {
            SqliteHelper.AccountEntry.USERNAME_COLUMN,
            SqliteHelper.AccountEntry.EMAIL_COLUMN
        };
        String name = user.contains("\\|") ? userFullnameToUsername(user) : user;
        Cursor cursor = db.query(SqliteHelper.AccountEntry.TABLE_NAME,
                col, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            String s = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            if(s.equalsIgnoreCase(name))
                return cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
        }
        return "";
    }
    public boolean checkEmailExist(String email) {
        String[] col = {
            SqliteHelper.AccountEntry.EMAIL_COLUMN
        };

        Cursor cursor = db.query(SqliteHelper.AccountEntry.TABLE_NAME,
                col, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            String email_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
            if(email_.equalsIgnoreCase(email))
                return true;
        }
        return false;
    }
    public boolean checkEmailExist_update(String email, String defEmail) {
        String[] col = {
                SqliteHelper.AccountEntry.EMAIL_COLUMN
        };

        Cursor cursor = db.query(SqliteHelper.AccountEntry.TABLE_NAME,
                col, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            String email_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
            if(!email_.equalsIgnoreCase(defEmail)) {
                if(email_.equalsIgnoreCase(email))
                    return true;
            }
        }
        return false;
    }
    public String getUsernameFromEmail(String email) {
        String[] col = {
                SqliteHelper.AccountEntry.EMAIL_COLUMN,
                SqliteHelper.AccountEntry.USERNAME_COLUMN
        };

        Cursor cursor = db.query(SqliteHelper.AccountEntry.TABLE_NAME,
                col, null, null, null, null, null);

        while(cursor.moveToNext())
        {
            String email_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.EMAIL_COLUMN));
            String user_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.AccountEntry.USERNAME_COLUMN));
            if(email_.equalsIgnoreCase(email_))
                return user_;
        }
        return "";
    }
    public boolean changePassword(String username, String password) {
        String selection = SqliteHelper.AccountEntry.USERNAME_COLUMN + " = ?";
        String[] selectionArgs = { username };

        ContentValues values = new ContentValues();
        values.put(SqliteHelper.AccountEntry.PASSWORD_COLUMN, password);

        int rowsUpdated = db.update(
                SqliteHelper.AccountEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);

        return rowsUpdated > 0;
    }

    public boolean updateTotalInterestSpent(String user, double interest, double spent) {
        String[] col = {
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.TOTAL_INTEREST,
                SqliteHelper.LenderAccount.TOTAL_SPENT
        };

        Cursor cursor = db.query(SqliteHelper.LenderAccount.TABLE_NAME,
                col, null, null, null, null, null);

        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
            double interest_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.TOTAL_INTEREST));
            double spent_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.TOTAL_SPENT));

            if(name_.equalsIgnoreCase(user)) {
                ContentValues values = new ContentValues();
                double totalInterest = interest_ + interest;
                double totalSpent = spent_ + spent;

                values.put(SqliteHelper.LenderAccount.TOTAL_INTEREST, totalInterest);
                values.put(SqliteHelper.LenderAccount.TOTAL_SPENT, totalSpent);

                String selection = SqliteHelper.LenderAccount.NAME_COLUMN + " = ?";
                String[] selectionArgs = { user };
                int rowsUpdated = db.update(
                        SqliteHelper.LenderAccount.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                return rowsUpdated > 0;
            }
        }
        return false;

    }
    public boolean updateCurrentTotalLend(String user, double lend, boolean isAdd) {
        String[] col = {
                SqliteHelper.BorrowerAccount.USERNAME_COLUMN,
                SqliteHelper.BorrowerAccount.TOTAL_CURRENT_LEND_COLUMN
        };

        Cursor cursor = db.query(SqliteHelper.BorrowerAccount.TABLE_NAME,
                col, null, null, null, null, null);

        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.USERNAME_COLUMN));
            double lend_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.TOTAL_CURRENT_LEND_COLUMN));

            String s = user;
            if(user.contains("\\|")) s = userFullnameToUsername(user);

            if(name_.equalsIgnoreCase(s)) {
                ContentValues values = new ContentValues();
                double totalLend;
                if(isAdd)
                    totalLend = lend_ + lend;
                else
                    totalLend = lend_ - lend;

                if(totalLend <= 0) totalLend = 0;

                values.put(SqliteHelper.BorrowerAccount.TOTAL_CURRENT_LEND_COLUMN, totalLend);

                String selection = SqliteHelper.BorrowerAccount.USERNAME_COLUMN + " = ?";
                String[] selectionArgs = { user };
                int rowsUpdated = db.update(
                        SqliteHelper.BorrowerAccount.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                return rowsUpdated > 0;
            }
        }
        return false;
    }
    public boolean updateTotalLend(String user, double lend) {
        String[] col = {
                SqliteHelper.BorrowerAccount.USERNAME_COLUMN,
                SqliteHelper.BorrowerAccount.TOTAL_LEND_COLUMN
        };

        Cursor cursor = db.query(SqliteHelper.BorrowerAccount.TABLE_NAME,
                col, null, null, null, null, null);

        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.USERNAME_COLUMN));
            double lend_ = cursor.getDouble(cursor.getColumnIndexOrThrow(SqliteHelper.BorrowerAccount.TOTAL_LEND_COLUMN));

            String s = user;
            if(user.contains("\\|")) s = userFullnameToUsername(user);

            if(name_.equalsIgnoreCase(s)) {
                ContentValues values = new ContentValues();
                double totalLend = lend_ + lend;

                values.put(SqliteHelper.BorrowerAccount.TOTAL_LEND_COLUMN, totalLend);

                String selection = SqliteHelper.BorrowerAccount.USERNAME_COLUMN + " = ?";
                String[] selectionArgs = { user };
                int rowsUpdated = db.update(
                        SqliteHelper.BorrowerAccount.TABLE_NAME,
                        values,
                        selection,
                        selectionArgs);

                return rowsUpdated > 0;
            }
        }
        return false;
    }

    public double[] getBorrowerDashboard(String user) {
        String[] columns = {
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN,
                SqliteHelper.LenderAccount.APPLIED_BORROWER_COLUMN
        };
        double totalLoans = 0, currentLoans = 0, paidLoans = 0;

        Cursor cursor = db.query(
                SqliteHelper.LenderAccount.TABLE_NAME,
                columns,
                null,
                null,
                null,
                null,
                null
        );

        try {
            while (cursor.moveToNext()) {
                String lendName_ = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.NAME_COLUMN));
                String currentApplied = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN));
                String applied = cursor.getString(cursor.getColumnIndexOrThrow(SqliteHelper.LenderAccount.APPLIED_BORROWER_COLUMN));

                String s = user.contains("\\|") ? userFullnameToUsername(user) : user;

                if (currentApplied != null && !currentApplied.isEmpty()) {
                    // Current
                    String[] current = currentApplied.split("\\|");
                    for (String value : current) {
                        String[] current_ = value.split(":");
                        if(current_.length <= 1) continue;

                        String name = current_[0];
                        String isAccept = current_[1];
                        double total = Double.parseDouble(current_[3]);

                        if (name.equalsIgnoreCase(s) && isAccept.equalsIgnoreCase("TRUE")) {
                            currentLoans += total;
                        }
                    }
                }

                if (applied != null && !applied.isEmpty()) {
                    // Done
                    String[] done = applied.split("\\|");
                    for (String value : done) {
                        String[] done_ = value.split(":");
                        String name = done_[0];
                        double total = Double.parseDouble(done_[1]);

                        if (name.equalsIgnoreCase(s)) {
                            paidLoans += total;
                        }
                    }
                }
            }
        } finally {
            cursor.close();
        }

        totalLoans = currentLoans + paidLoans;

        int[] counts = getBorrowerCounts(user);
        return new double[]{
                counts[2], counts[1], counts[0], totalLoans, currentLoans, paidLoans
        };
    }


    public int[] getBorrowerCounts(String user) {
        String[] col = {
                SqliteHelper.BorrowerAccount.USERNAME_COLUMN,
                SqliteHelper.BorrowerAccount.LIST_DONE_COLUMN,
                SqliteHelper.BorrowerAccount.LIST_COLUMN
        };

        Cursor cursor = db.query(SqliteHelper.BorrowerAccount.TABLE_NAME,
                col, null, null, null, null, null);

        int fullPaid = 0, unpaid = 0, allContract = 0;
        while(cursor.moveToNext()) {
            String name_ = cursor.getString(cursor.getColumnIndexOrThrow(col[0]));
            String listDone = cursor.getString(cursor.getColumnIndexOrThrow(col[1]));
            String list = cursor.getString(cursor.getColumnIndexOrThrow(col[2]));

            String s = user.contains("\\|") ? userFullnameToUsername(user) : user;
            if(name_.equalsIgnoreCase(s)) {
                if(!list.isEmpty())
                    unpaid = list.split("\\|").length;
                if(!listDone.isEmpty())
                    fullPaid = listDone.split("\\|").length;
                allContract = unpaid + fullPaid;
                return new int[] {
                        unpaid, fullPaid, allContract
                };
            }
        }
        return new int[] {0, 0, 0};
    }

    public double[] getLenderDashboard(String name) {
        String[] col = {
                SqliteHelper.LenderAccount.NAME_COLUMN,
                SqliteHelper.LenderAccount.APPLIED_BORROWER_COLUMN,
                SqliteHelper.LenderAccount.CURRENT_APPLIED_BORROWER_COLUMN,
                SqliteHelper.LenderAccount.RATE_COLUMN,
                SqliteHelper.LenderAccount.TOTAL_INTEREST
        };

        int finish = 0, unfinish = 0, all = 0;
        double unpaid = 0, paid = 0, profit = 0, currentTotal = 0;

        Cursor cursor = db.query(SqliteHelper.LenderAccount.TABLE_NAME, col, null, null, null, null, null);

        try {
            while (cursor.moveToNext()) {
                String name_ = cursor.getString(cursor.getColumnIndexOrThrow(col[0]));

                if (name.equalsIgnoreCase(name_)) {
                    String applied_ = cursor.getString(cursor.getColumnIndexOrThrow(col[1]));
                    String current_ = cursor.getString(cursor.getColumnIndexOrThrow(col[2]));
                    double rate_ = cursor.getDouble(cursor.getColumnIndexOrThrow(col[3]));
                    double inte = cursor.getDouble(cursor.getColumnIndexOrThrow(col[4]));

                    if (current_ != null && !current_.isEmpty()) {
                        // Current
                        String[] currentData = current_.split("\\|");

                        for (String s : currentData) {
                            String[] data = s.split(":");
                            if(data.length <= 1) continue;
                            String user_ = data[0].toUpperCase();
                            String isApply_ = data[1];
                            double remaining_ = Double.parseDouble(data[2]);
                            double total_ = Double.parseDouble(data[3]);
                            int year_ = Integer.parseInt(data[4]);

                            if (isApply_.equalsIgnoreCase("TRUE")) {
                                unpaid += remaining_;
                                currentTotal += total_;
                                unfinish += 1;
                            }
                        }
                    }

                    if (applied_ != null && !applied_.isEmpty()) {
                        // DONE
                        String[] appliedData = applied_.split("\\|");
                        for (String s : appliedData) {
                            String[] data = s.split(":");
                            String user_ = data[0].toUpperCase();
                            double total_ = Double.parseDouble(data[1]);
                            int year_ = Integer.parseInt(String.format("%.0f", Double.parseDouble(data[2])));

                            double n = total_ + (currentTotal - unpaid);
                            paid += Math.max(n, 0);
                            finish += 1;
                        }
                    }

                    profit = rate_ > 0 ? ((rate_ / 100) * paid) : paid;
                    all = finish + unfinish;
                }
            }
        } finally {
            cursor.close();
        }

        return new double[] {
                all, finish, unfinish, paid, unpaid, profit
        };
    }

}
