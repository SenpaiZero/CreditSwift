package com.example.Helper;

import android.app.Activity;
import android.content.SharedPreferences;

public class AdminAccountHelper {

    static Activity activity;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public AdminAccountHelper(Activity act) {
        if(activity == null)
            activity = act;
        sharedPref = activity.getPreferences(Activity.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setPassword(String password) {
        editor.putString("password", password);
        editor.apply();
    }

    public String getPassword() {
        return sharedPref.getString("password", "admin");
    }
}
