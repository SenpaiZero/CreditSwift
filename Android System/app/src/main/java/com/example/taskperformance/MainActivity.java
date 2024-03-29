package com.example.taskperformance;

import static android.text.InputType.TYPE_CLASS_TEXT;
import static android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Helper.ProfileHelper;
import com.example.Helper.SettingHelper;
import com.example.Helper.userInterfaceHelper;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {

    Spinner animSpeedSpinner, animSpinner;
    ImageButton login, register;
    TextView loginTxt, registerTxt, termsBtn, closeTerm, titleConfirm, msgConfirm;
    ImageView bg, top, bottom, setting, exit;
    ConstraintLayout layout;
    userInterfaceHelper UIHelper;
    SettingHelper settingHelper;
    ProfileHelper profileHelper;
    ConstraintLayout loginLayout, registerLayout, termLayout, confirmationLayout, settingsLayout;
    Button cancelLogin, loginBtn, cancelRegister, registerBtn, cancelExit, confirmExit, settingCancel, settingApply;
    EditText loginUserTB, loginPassTB, registerUserTB, registerEmailTB, registerPassTB, registerRePassTB;
    boolean isOpened, isLoginEye, isRegisterEye;
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UIHelper = new userInterfaceHelper(this);
        UIHelper.removeActionbar();
        UIHelper.transparentStatusBar();

        // Confirmation
        UIHelper.setConfirmation("EXIT", "DO YOU REALLY WANT TO EXIT?", "NO", "YES");
        UIHelper.setNegativeConfirmation("close");
        UIHelper.setPositiveConfirmation("exit");

        profileHelper = new ProfileHelper(this);
        settingHelper = new SettingHelper(this);

        isOpened = false;
        setUIVariables();
        setOnTouchListener();
        setOnClickListener();

        UIHelper.setSpinner(getResources().getStringArray(R.array.yesno), animSpinner);
        UIHelper.setSpinner(getResources().getStringArray(R.array.animation_speed), animSpeedSpinner);
    }

    void openLoginRegister(View view, ConstraintLayout layout, ImageView bg, boolean isReverse)
    {

        float start, end;

        if(isReverse)
        {
            start = 0.8f;
            end = 1.0f;
        }
        else
        {
            start = 1.0f;
            end = 0.8f;
        }
        ValueAnimator anim = ValueAnimator.ofFloat(start, end);
        if(settingHelper.getAnimation())
            anim.setDuration((long) Math.abs(200 / settingHelper.getAnimationSpeed()));
        else
            anim.setDuration(0);
        anim.setInterpolator(new LinearInterpolator());

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isOpened && (view.getId() != R.id.cancelLogin && view.getId() != R.id.cancelRegister)) return;
                if(UIHelper.isConfirmationVisible() || settingsLayout.getVisibility() == View.VISIBLE) return;

                setViewPassword(loginPassTB);
                setViewPassword(registerPassTB);
                setViewPassword(registerRePassTB);
                isOpened = true;
                hideAllElements(isReverse);
                if(isReverse)
                {
                    layout.setVisibility(View.GONE);
                    isOpened = false;
                    resetTextBox();
                }

                anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    @Override
                    public void onAnimationUpdate(@NonNull ValueAnimator animation) {
                        ((LinearLayout.LayoutParams) bg.getLayoutParams()).weight = (float) animation.getAnimatedValue();
                        bg.requestLayout();
                    }
                });
                anim.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        if(!isReverse)
                            layout.setVisibility(View.VISIBLE);
                    }
                });
                anim.start();
            }
        });
    }

    void resetTextBox()
    {
        registerUserTB.setText(null);
        registerEmailTB.setText(null);
        registerPassTB.setText(null);
        registerRePassTB.setText(null);

        loginUserTB.setText(null);
        loginPassTB.setText(null);
    }
    void setOnClickListener()
    {
        // Login
        openLoginRegister(login, loginLayout, top, false);
        openLoginRegister(top, loginLayout, top, false);
        openLoginRegister(loginTxt, loginLayout, top, false);

        // Register
        openLoginRegister(register, registerLayout, bottom, false);
        openLoginRegister(registerTxt, registerLayout, bottom, false);
        openLoginRegister(bottom, registerLayout, bottom, false);

        //Cancel Btn
        openLoginRegister(cancelLogin, loginLayout, top, true);
        openLoginRegister(cancelRegister, registerLayout, bottom, true);

        // Terms and condition
        termsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termLayout.setVisibility(View.VISIBLE);
            }
        });

        closeTerm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                termLayout.setVisibility(View.INVISIBLE);
            }
        });

        settingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsLayout.setVisibility(View.INVISIBLE);
            }
        });

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = loginUserTB.getText().toString();
                String pass = loginPassTB.getText().toString();

                if(user.equals("admin") && pass.equals("admin"))
                    startActivity(new Intent(MainActivity.this, adminHome.class));
                if(user.isEmpty())
                {
                    UIHelper.showCustomToast("Username input is invalid");
                    return;
                }
                else if(pass.isEmpty())
                {
                    UIHelper.showCustomToast("Password input is invalid");
                    return;
                }


                if(profileHelper.checkLogin(user, pass))
                {
                    UIHelper.showCustomToast("Login Success");
                    return;
                }
                else
                {
                    UIHelper.showCustomToast("Incorrect Credentials. Please try again.");
                }
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = registerUserTB.getText().toString();
                String pass = registerPassTB.getText().toString();
                String rePass = registerRePassTB.getText().toString();
                String email = registerEmailTB.getText().toString();

                if(user.isEmpty())
                {
                    UIHelper.showCustomToast("Username input is invalid");
                    return;
                }
                else if(email.isEmpty())
                {
                    UIHelper.showCustomToast("Email input is invalid");
                    return;
                }
                else if(pass.isEmpty())
                {
                    UIHelper.showCustomToast("Password input is invalid");
                    return;
                }

                if(rePass.equals(pass))
                {
                    if(profileHelper.newAccount(user, pass, email, "BORROWER"))
                    {
                        UIHelper.showCustomToast("You have successfully registered.");
                        resetTextBox();
                    }
                    else
                    {
                        // something went wrong
                        UIHelper.showCustomToast("Something went wrong. Please try again later.");
                    }
                }
                else
                {
                    UIHelper.showCustomToast("Passwords must be the same.");
                }
            }
        });

        settingApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isAnim;
                float animSpeed;
                String animString = animSpinner.getSelectedItem().toString();
                String animSpeedString = animSpeedSpinner.getSelectedItem().toString();

                isAnim = (animString.equalsIgnoreCase("Yes")) ? true : false;

                if(animSpeedString.equals("1x")) animSpeed = SettingHelper.Animation.SPEED_1X;
                else if(animSpeedString.equals("1.5x")) animSpeed = SettingHelper.Animation.SPEED_1_5X;
                else if(animSpeedString.equals("2x")) animSpeed = SettingHelper.Animation.SPEED_2X;
                else if(animSpeedString.equals("2.5x")) animSpeed = SettingHelper.Animation.SPEED_2_5X;
                else if(animSpeedString.equals("3x")) animSpeed = SettingHelper.Animation.SPEED_3X;
                else animSpeed = SettingHelper.Animation.SPEED_1X;

                settingHelper.setAnimation(isAnim);
                settingHelper.setAnimationSpeed(animSpeed);

                UIHelper.showCustomToast("Settings Applied. Please restart the application for the settings to take effects", 10000);
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    void setViewPassword(final EditText text) {
        text.setOnTouchListener(new View.OnTouchListener() {
            boolean open = true;

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableRight = text.getRight() - text.getCompoundDrawables()[2].getBounds().width();
                    if (event.getRawX() >= drawableRight) {
                        int w = text.getWidth();
                        if (open) {
                            open = false;
                            text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            text.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MainActivity.this, R.drawable.password_img),
                                    null, ContextCompat.getDrawable(MainActivity.this, R.drawable.password_reveal), null);
                        } else {
                            open = true;
                            text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            text.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(MainActivity.this, R.drawable.password_img),
                                    null, ContextCompat.getDrawable(MainActivity.this, R.drawable.password_hide), null);
                        }
                        text.setWidth(w);
                        return true;
                    }
                }
                return false;
            }
        });
    }

    @SuppressLint("ClickableViewAccessibility")
    void setOnTouchListener()
    {
        // Exit Button
        exit.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                UIHelper.setConfirmVisibility(true);
                return true;
            }
        });

        // setting Button
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DecimalFormat format = new DecimalFormat("0.#");
                animSpinner.setSelection(UIHelper.getSpinnerSelected(animSpinner, settingHelper.getAnimation() ? "Yes" : "No"));
                animSpeedSpinner.setSelection(UIHelper.getSpinnerSelected(animSpeedSpinner, format.format(settingHelper.getAnimationSpeed())  + "x"));
                settingsLayout.setVisibility(View.VISIBLE);
            }
        });

    }
    @SuppressLint("WrongConstant")
    void hideAllElements(boolean isVisible)
    {
        int visibility;
        if(isVisible) visibility = 0;
        else visibility = 8;
        loginTxt.setVisibility(visibility);
        login.setVisibility(visibility);

        register.setVisibility(visibility);
        registerTxt.setVisibility(visibility);

        exit.setVisibility(visibility);
        setting.setVisibility(visibility);

        bg.setVisibility(visibility);
    }
    void setUIVariables() {
        layout = findViewById(R.id.mainLayout);
        bg = findViewById(R.id.bg);
        top = findViewById(R.id.top2);
        bottom = findViewById(R.id.bottom2);

        login = findViewById(R.id.loginBtn);
        register = findViewById(R.id.registerBtn);

        exit = findViewById(R.id.exitBtn);

        loginTxt = findViewById(R.id.loginTxt);
        registerTxt = findViewById(R.id.registerTxt);

        loginLayout = findViewById(R.id.loginInclude);
        cancelLogin = findViewById(R.id.cancelLogin);

        registerLayout = findViewById(R.id.registerInclude);
        cancelRegister = findViewById(R.id.cancelRegister);

        // Terms and conditions
        termsBtn = findViewById(R.id.termTxt);
        closeTerm = findViewById(R.id.cancelBtn);
        termLayout = findViewById(R.id.termInclude);

        // Confirmation
        confirmationLayout = findViewById(R.id.confirmationInclude);
        cancelExit = findViewById(R.id.negativeBtnConfirmation);
        confirmExit = findViewById(R.id.positiveBtnConfirmation);
        titleConfirm = findViewById(R.id.titleTxt);
        msgConfirm = findViewById(R.id.messageTxt);

        // Setting
        setting = findViewById(R.id.settingBtn);
        settingsLayout = findViewById(R.id.settingsInclude);
        settingApply = findViewById(R.id.confirmBtnSetting);
        settingCancel = findViewById(R.id.cancelBtnSetting);

        // Setting - spinner
        animSpinner = findViewById(R.id.animationSpinner);
        animSpeedSpinner = findViewById(R.id.animSpdSpinner);

        // Login
        loginUserTB = findViewById(R.id.loginUserTB);
        loginPassTB = findViewById(R.id.loginPasswordTB);
        loginBtn = findViewById(R.id.loginBtnLogin);

        // Register
        registerUserTB = findViewById(R.id.registerUserTB);
        registerEmailTB = findViewById(R.id.registerEmailTB);
        registerPassTB = findViewById(R.id.registerPasswordTB);
        registerRePassTB = findViewById(R.id.registerConfirmPasswordTB);
        registerBtn = findViewById(R.id.registerBtnRegister);
    }



    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}