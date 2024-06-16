package com.example.taskperformance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Helper.JavaMailAPI;
import com.example.Helper.PasswordHelper;
import com.example.Helper.ProfileHelper;
import com.example.Helper.SettingHelper;
import com.example.Helper.StayLoginHelper;
import com.example.Helper.userInterfaceHelper;
import com.example.Helper.validationHelper;

import java.text.DecimalFormat;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Spinner animSpeedSpinner, animSpinner;
    ImageButton login, register;
    TextView loginTxt, registerTxt, termsBtn, closeTerm, titleConfirm, msgConfirm, forgotBtn;
    EditText forgotEmail, forgotCode, forgotPassword;
    Button forgotCancel, forgotUpdate, forgotSend;
    ConstraintLayout forgotLayout;
    String code;
    ImageView bg, top, bottom, setting, exit;
    ConstraintLayout layout;
    userInterfaceHelper UIHelper;
    SettingHelper settingHelper;
    ProfileHelper profileHelper;
    CheckBox termCB, stayLogin;
    StayLoginHelper stayLoginHelper;
    ConstraintLayout loginLayout, registerLayout, termLayout, confirmationLayout, settingsLayout;
    Button cancelLogin, loginBtn, cancelRegister, registerBtn, cancelExit, confirmExit, settingCancel, settingApply;
    EditText loginUserTB, loginPassTB, registerUserTB, registerEmailTB, registerPassTB, registerRePassTB;
    boolean isOpened;
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
        stayLoginHelper = new StayLoginHelper(this);

        profileHelper.newAccount("ADMIN", "admin", "NONE", "ADMIN");
        if(stayLoginHelper.getStayLogin()) {
           String va = profileHelper.checkLogin(stayLoginHelper.getUserPass()[0], stayLoginHelper.getUserPass()[1]);
            if(va.equalsIgnoreCase("LENDER"))
            {
                startActivity(new Intent(MainActivity.this, lenderHome.class)
                        .putExtra("name", stayLoginHelper.getUserPass()[0])
                        .putExtra("username", stayLoginHelper.getUserPass()[0]));
            }
            else if(va.equalsIgnoreCase("BORROWER"))
            {
                startActivity(new Intent(MainActivity.this, userHome.class)
                        .putExtra("name", stayLoginHelper.getUserPass()[0])
                        .putExtra("username", stayLoginHelper.getUserPass()[0]));
            }
            else if(va.equalsIgnoreCase("ADMIN")) {
                startActivity(new Intent(MainActivity.this, adminHome.class)
                        .putExtra("name", stayLoginHelper.getUserPass()[0])
                        .putExtra("username", stayLoginHelper.getUserPass()[0]));
            }
        }
    }
    void openLoginRegister(View view, ConstraintLayout layout, ImageView bg, boolean isReverse)
    {

       // startActivity(new Intent(MainActivity.this, borrower_info.class));

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

                PasswordHelper.setViewPassword(loginPassTB, MainActivity.this);
                PasswordHelper.setViewPassword(registerPassTB, MainActivity.this);
                PasswordHelper.setViewPassword(registerRePassTB, MainActivity.this);
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
        PasswordHelper.hidePassword(new EditText[]{loginPassTB, registerPassTB, registerRePassTB}, this);
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


                String va = profileHelper.checkLogin(user, pass);
                if(va.equalsIgnoreCase("FALSE"))
                {
                    UIHelper.showCustomToast("Incorrect Credentials. Please try again.");
                    return;
                }

                if(va.equalsIgnoreCase("archived")) {
                    UIHelper.showCustomToast("Your account is archived.");
                    return;
                }

                stayLoginHelper.setStayLogin(stayLogin.isChecked());
                stayLoginHelper.setUserPass(user, pass);

                if(va.equalsIgnoreCase("ADMIN")) {
                    startActivity(new Intent(MainActivity.this, adminHome.class)
                            .putExtra("username", user));
                }

                if(va.equalsIgnoreCase("LENDER"))
                {
                    startActivity(new Intent(MainActivity.this, lenderHome.class)
                            .putExtra("name", user)
                            .putExtra("username", user));
                }
                else if(va.equalsIgnoreCase("BORROWER"))
                {
                    startActivity(new Intent(MainActivity.this, userHome.class)
                            .putExtra("name", user)
                            .putExtra("username", user));
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

                if(validationHelper.checkAlphaNumeric(user) || validationHelper.checkMinMaxLen(user, 3, 100))
                {
                    UIHelper.showCustomToast("The username you entered is not valid.");
                    return;
                }
                else if(user.equalsIgnoreCase("ADMIN")) {
                    UIHelper.showCustomToast("The username you entered is not valid.");
                    return;
                }
                else if(validationHelper.checkEmail(email))
                {
                    UIHelper.showCustomToast("The email you entered is not valid.");
                    return;
                }
                else if(profileHelper.checkEmailExist(email)) {
                    UIHelper.showCustomToast("The email you entered already exist.");
                    return;
                }
                else if(pass.isEmpty())
                {
                    UIHelper.showCustomToast("Password input is invalid");
                    return;
                }
                else if(!termCB.isChecked()) {
                    UIHelper.showCustomToast("Please read the terms and conditions.");
                    return;
                }

                if(rePass.equals(pass))
                {
                    String va = profileHelper.newAccount(user, pass, email, "BORROWER");
                    if(va.equalsIgnoreCase("true"))
                    {
                        UIHelper.showCustomToast("You have successfully registered.");
                        resetTextBox();
                    }
                    else if(va.equalsIgnoreCase("duplicate")) {
                        UIHelper.showCustomToast("The username already exist.");
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

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotLayout.setVisibility(View.VISIBLE);
            }
        });

        forgotCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotLayout.setVisibility(View.INVISIBLE);
            }
        });

        forgotSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null) {
                    boolean connected = (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                            connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);

                    if(!connected) {
                        UIHelper.showCustomToast("No Internet Connection");
                        return;
                    }
                }

                if(validationHelper.checkEmail(forgotEmail.getText().toString())) {
                    UIHelper.showCustomToast("Invalid email address.");
                    return;
                }
                if(!profileHelper.checkEmailExist(forgotEmail.getText().toString())) {
                    UIHelper.showCustomToast("Email Address does not exist.");
                    return;
                }

                Random rand = new Random();
                int min = 10000;
                int max = 99999;
                int random = rand.nextInt((max - min) + 1) + min;
                code = String.valueOf(random);

                JavaMailAPI javaMailAPI = new JavaMailAPI(MainActivity.this,
                        forgotEmail.getText().toString(),
                        "FORGOT PASSWORD",
                        "Username: "+profileHelper.getUsernameFromEmail(forgotEmail.getText().toString())
                                +"\nYour code is: " + code
                                +"\n\nMake sure that you do not share this code to anyone!");

                javaMailAPI.execute();
            }
        });

        forgotUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!code.equals(forgotCode.getText().toString())) {
                    UIHelper.showCustomToast("Incorrect Code.");
                    return;
                }
                if(forgotPassword.getText().toString().isEmpty()) {
                    UIHelper.showCustomToast("The password is empty.");
                    return;
                }

                profileHelper.changePassword(
                        profileHelper.getUsernameFromEmail(
                            forgotEmail.getText().toString()),
                        forgotPassword.getText().toString()
                );
                UIHelper.showCustomToast("Password has been updated");
                forgotLayout.setVisibility(View.INVISIBLE);
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

        ((TextView)findViewById(R.id.cbTxt)).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                termCB.setChecked(!termCB.isChecked());
                return true;
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
        termCB = findViewById(R.id.termCB);

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
        stayLogin = findViewById(R.id.stayLoginCB);

        // Forgot
        forgotBtn = findViewById(R.id.forgotPass);
        forgotCancel = findViewById(R.id.forgotCancel);
        forgotUpdate = findViewById(R.id.forgotChange);
        forgotSend = findViewById(R.id.sendForgotBtn);
        forgotEmail = findViewById(R.id.forgotEmailTB);
        forgotCode = findViewById(R.id.codeTB);
        forgotPassword = findViewById(R.id.newPassForgotTB);
        forgotLayout = findViewById(R.id.forgotLayout);

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