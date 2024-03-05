package com.example.Helper;

import android.app.Activity;
import android.content.SharedPreferences;
import android.content.res.Resources;

import androidx.appcompat.app.AppCompatActivity;

public class SettingHelper extends AppCompatActivity {
    Activity activity;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;
    public SettingHelper(Activity act)
    {
        activity = act;
        sharedPref = activity.getPreferences(Activity.MODE_PRIVATE);
        editor = sharedPref.edit();
    }

    public void setAnimation(boolean isAnim)
    {
        editor.putBoolean("animation", isAnim);
        editor.apply();
    }

    public boolean getAnimation()
    {
        return sharedPref.getBoolean("animation", true);
    }

    public void setAnimationSpeed(float speed)
    {
        editor.putFloat("animationSpeed", speed);
        editor.apply();
    }

    public float getAnimationSpeed()
    {
        return sharedPref.getFloat("animationSpeed", Animation.SPEED_1X);
    }

    public void resetSharedPref()
    {
        editor.clear();
        editor.apply();
    }

    public static class Animation
    {
        public static final float SPEED_1X = 1f;
        public static final float SPEED_1_5X = 1.5f;
        public static final float SPEED_2X = 2f;
        public static final float SPEED_2_5X = 2.5f;
        public static final float SPEED_3X = 3f;
    }
}
