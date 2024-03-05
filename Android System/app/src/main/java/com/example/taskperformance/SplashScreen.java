package com.example.taskperformance;


import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.example.Helper.SettingHelper;
import com.example.Helper.userInterfaceHelper;

public class SplashScreen extends AppCompatActivity {

    userInterfaceHelper UIHelper;
    SettingHelper settingHelper;
    ImageView bg, top, bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        settingHelper = new SettingHelper(this);

        UIHelper = new userInterfaceHelper(this);
        UIHelper.transparentStatusBar();
        UIHelper.removeActionbar();

        bg = findViewById(R.id.bg);
        top = findViewById(R.id.top2);
        bottom = findViewById(R.id.bottom2);

        if(!settingHelper.getAnimation())
        {
            startActivity(new Intent(SplashScreen.this, MainActivity.class));
            overridePendingTransition(R.anim.fadein, R.anim.fadeout);
            finish();
            return;
        }
        Animation anim = new ScaleAnimation(
                1.0f, // Start scale X
                0.33f, // End scale X
                1.0f, // Start scale Y
                0.33f, // End scale Y
                Animation.RELATIVE_TO_SELF, 0.5f, // Pivot point X
                Animation.RELATIVE_TO_SELF, 0.5f // Pivot point Y
        );

        anim.setDuration(3000);
        anim.setFillAfter(true);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {}

            @Override
            public void onAnimationEnd(Animation animation) {
                startActivity(new Intent(SplashScreen.this, MainActivity.class));
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                finish();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });

        bg.startAnimation(anim);

        ObjectAnimator scaleYTop = ObjectAnimator.ofFloat(top, View.SCALE_Y, 0.1f, 1.0f);
        scaleYTop.setDuration(2500);
        scaleYTop.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator scaleYBottom = ObjectAnimator.ofFloat(bottom, View.SCALE_Y, 0.1f, 1.0f);
        scaleYBottom.setDuration(2500);
        scaleYBottom.setInterpolator(new AccelerateDecelerateInterpolator());

        scaleYTop.start();
        scaleYBottom.start();

    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}