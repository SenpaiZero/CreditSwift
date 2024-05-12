package com.example.Helper;

import android.app.Activity;
import android.content.SharedPreferences;

public class StayLoginHelper {

    static Activity activity;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public StayLoginHelper(Activity act) {
        if(activity == null)
            activity = act;
        sharedPref = activity.getPreferences(Activity.MODE_PRIVATE);
        editor = sharedPref.edit();
    }
    public void setStayLogin(boolean log) {
        editor.putBoolean("stayLogin", log);
        editor.apply();
    }

    public void setUserPass(String user, String pass) {
        editor.putString("user", user);
        editor.putString("pass", pass);
        editor.apply();
    }

    public String[] getUserPass() {
        return new String[] {
                sharedPref.getString("user", ""),
                sharedPref.getString("pass", "")
        };
    }
    public boolean getStayLogin() {
        return sharedPref.getBoolean("stayLogin", false);
    }

    public void resetSharedPref()
    {
        editor.clear();
        editor.apply();
    }
}
