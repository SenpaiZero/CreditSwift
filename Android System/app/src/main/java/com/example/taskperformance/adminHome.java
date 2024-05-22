package com.example.taskperformance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.service.controls.Control;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Adapter.userLenderAdapter;
import com.example.Adapter.usersAdapter;
import com.example.Helper.ProfileHelper;
import com.example.Helper.SettingHelper;
import com.example.Helper.SqliteHelper;
import com.example.Helper.userInterfaceHelper;
import com.example.model.userLenderModel;
import com.example.model.usersModel;

import java.util.LinkedList;

public class adminHome extends AppCompatActivity {

    Spinner animSpeedSpinner, animSpinner;
    ImageButton createLender;
    Button tabLender, tabBorrower, tabAdmin;
    TextView title, titleConfirm, msgConfirm;;
    LinkedList<usersModel> usersList;
    LinkedList<userLenderModel> usersLenderList;
    SettingHelper settingHelper;
    RecyclerView userCon;
    LinearLayout adminCon;
    CardView logoutBtn, lenderArchiveBtn, borrowerArchiveBtn, changePasswordBtn, settingsBtn;
    ConstraintLayout settingCon, confirmationLayout;
    userInterfaceHelper UIHelper;
    Button settingApply, settingCancel, confirmLogout, cancelLogout;
    String isNormal;
    ProfileHelper profileHelper;
    public static adminHome admin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        isNormal = getIntent().getStringExtra("normal");
        if(isNormal == null || isNormal.isEmpty()) isNormal = "ADMIN";

        UIHelper = new userInterfaceHelper(this);
        UIHelper.setContext(this);
        UIHelper.removeActionbar();
        UIHelper.transparentStatusBar();

        settingHelper = new SettingHelper(this);
        profileHelper = new ProfileHelper(this);

        setUIVariables();
        setOnClick();
        if(isNormal.equalsIgnoreCase("ADMIN"))
            changeTintTab(tabAdmin);
        else if(isNormal.equalsIgnoreCase("LENDER"))
            changeTintTab(tabLender);
        else if(isNormal.equalsIgnoreCase("BORROWER"))
            changeTintTab(tabBorrower);

        admin = this;
    }

    void setLogoutPopup() {
        // Confirmation
        UIHelper.setConfirmation("LOGOUT", "DO YOU REALLY WANT TO LOGOUT?", "NO", "YES");
        UIHelper.setNegativeConfirmation("cancel");
        UIHelper.setPositiveConfirmation("logout");
    }

    void setUIVariables()
    {
        tabLender = findViewById(R.id.tabLenderBtn);
        tabBorrower = findViewById(R.id.tabBorrowerBtn);
        tabAdmin = findViewById(R.id.tabAdminBtn);
        title = findViewById(R.id.adminTitleTxt);

        userCon = findViewById(R.id.recyclerCon);
        adminCon = findViewById(R.id.adminCon);

        //Buttons for admin
        logoutBtn = findViewById(R.id.adminLogout);
        borrowerArchiveBtn = findViewById(R.id.viewBorrowArchive);
        lenderArchiveBtn = findViewById(R.id.viewLenderArchive);
        changePasswordBtn = findViewById(R.id.adminChangePassword);
        createLender = findViewById(R.id.newLenderBtn);

        // Settings
        settingCon = findViewById(R.id.adminSettingCon);
        settingsBtn = findViewById(R.id.adminSettings);
        settingApply = findViewById(R.id.confirmBtnSetting);
        settingCancel = findViewById(R.id.cancelBtnSetting);

        // Setting - spinner
        animSpinner = findViewById(R.id.animationSpinner);
        animSpeedSpinner = findViewById(R.id.animSpdSpinner);

        // Confirmation
        confirmationLayout = findViewById(R.id.confirmationInclude);
        cancelLogout = findViewById(R.id.negativeBtnConfirmation);
        confirmLogout = findViewById(R.id.positiveBtnConfirmation);
        titleConfirm = findViewById(R.id.titleTxt);
        msgConfirm = findViewById(R.id.messageTxt);
    }

    void setOnClick()
    {
        // Tab onclick
        tabLender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTintTab(tabLender);
            }
        });

        tabBorrower.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTintTab(tabBorrower);
            }
        });

        tabAdmin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTintTab(tabAdmin);
            }
        });

        borrowerArchiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContent(borrowerArchiveBtn);
                createLender.setVisibility(View.INVISIBLE);
            }
        });

        lenderArchiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeContent(lenderArchiveBtn);
                createLender.setVisibility(View.INVISIBLE);
            }
        });
        // Admin Buttons
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLogoutPopup();
                UIHelper.setConfirmVisibility(true);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingCon.setVisibility(View.VISIBLE);
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
        settingCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingCon.setVisibility(View.INVISIBLE);
            }
        });

        createLender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminHome.this, lender_info.class)
                        .putExtra("create", true)
                        .putExtra("username", "ADMIN")
                        .putExtra("admin", true));
            }
        });

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(adminHome.this, changePassword.class)
                        .putExtra("username", "ADMIN")
                        .putExtra("type", "ADMIN"));
            }
        });
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    void changeTintTab(Button button)
    {
        // Setting the text color to default
        tabLender.setTextColor(getColor(R.color.lightLogin));
        tabBorrower.setTextColor(getColor(R.color.lightLogin));
        tabAdmin.setTextColor(getColor(R.color.lightLogin));

        // Setting the drawable color to default
        tabAdmin.setCompoundDrawableTintList(getColorStateList(R.color.lightLogin));
        tabBorrower.setCompoundDrawableTintList(getColorStateList(R.color.lightLogin));
        tabLender.setCompoundDrawableTintList(getColorStateList(R.color.lightLogin));

        // Setting the selected button to highlighted color
        button.setTextColor(getColor(R.color.lightTxt));
        button.setCompoundDrawableTintList(getColorStateList(R.color.lightTxt));

        changeContent(button);
    }

    void changeContent(View button)
    {
        if(usersList != null)
            if(usersList.size() > 0)
                usersList.clear();
        if(usersLenderList != null)
            if(usersLenderList.size() > 0)
                usersLenderList.clear();

        if(button.getId() == tabLender.getId())
        {
            title.setText("LENDER");
            setLenderList(false);
        }
        else if(button.getId() == tabBorrower.getId())
        {
            title.setText("BORROWER");
            setBorrowerList(false);
        }
        else if(button.getId() == tabAdmin.getId())
        {
            title.setText("ADMIN");
            createLender.setVisibility(View.INVISIBLE);
            changeContainerVisibility(true);
        }
        else if(button.getId() == borrowerArchiveBtn.getId())
        {
            title.setText("BORROWER ARCHIVES");
            setBorrowerList(true);
        }
        else if(button.getId() == lenderArchiveBtn.getId())
        {
            title.setText("LENDER ARCHIVES");
            setLenderList(true);
        }

    }

    void changeContainerVisibility(boolean isAdmin)
    {
        if(isAdmin)
        {
            userCon.setVisibility(View.INVISIBLE);
            adminCon.setVisibility(View.VISIBLE);
            return;
        }

        userCon.setVisibility(View.VISIBLE);
        adminCon.setVisibility(View.INVISIBLE);
    }

    public void editLender(String username) {
        startActivity(new Intent(adminHome.this, lender_info.class)
                .putExtra("create", false)
                .putExtra("username", username)
                .putExtra("admin", true));
    }
    public void editBorrower(String username) {
        startActivity(new Intent(adminHome.this, borrower_info.class)
                .putExtra("firstTime", false)
                .putExtra("username", username)
                .putExtra("admin", true));
    }
    public void setBorrowerList(boolean isArchive) {
        usersAdapter usersAdapter_;
        createLender.setVisibility(View.INVISIBLE);
        changeContainerVisibility(false);
        usersList = profileHelper.getUsersList("BORROWER", isArchive);
        usersAdapter_ = new usersAdapter(usersList, this,
                isArchive ? usersAdapter.archive : usersAdapter.admin,
                profileHelper, UIHelper);
        userCon.setAdapter(usersAdapter_);
        userCon.setLayoutManager(new LinearLayoutManager(this));
    }

    public void setLenderList(boolean isArchive) {
        userLenderAdapter usersLenderAdapter_;
        changeContainerVisibility(false);
        if(!isArchive)
            createLender.setVisibility(View.VISIBLE);

        usersLenderList = profileHelper.getUsersLenderList("LENDER", isArchive);
        usersLenderAdapter_ = new userLenderAdapter(usersLenderList, this,
                isArchive ? userLenderAdapter.archive : userLenderAdapter.admin,
                profileHelper, UIHelper);
        userCon.setAdapter(usersLenderAdapter_);
        userCon.setLayoutManager(new LinearLayoutManager(this));
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}