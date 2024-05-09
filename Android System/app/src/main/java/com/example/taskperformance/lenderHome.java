package com.example.taskperformance;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Adapter.applyAdapter;
import com.example.Adapter.userLenderAdapter;
import com.example.Helper.ProfileHelper;
import com.example.Helper.SettingHelper;
import com.example.Helper.userInterfaceHelper;
import com.example.model.applyModel;
import com.example.model.userLenderModel;

import java.util.LinkedList;

public class lenderHome extends AppCompatActivity {

    Spinner animSpeedSpinner, animSpinner;
    Button tabLender, tabDashboard, tabUser;
    TextView title, titleConfirm, msgConfirm;;
    LinkedList<applyModel> applyList;
    SettingHelper settingHelper;
    RecyclerView userCon;
    LinearLayout adminCon;
    CardView logoutBtn, lenderArchiveBtn, dashboardBtn, changePasswordBtn, settingsBtn;
    ConstraintLayout settingCon, confirmationLayout, dashboard, applyInfo;
    userInterfaceHelper UIHelper;
    ProfileHelper profileHelper;
    Button settingApply, settingCancel, confirmLogout, cancelLogout, applyApply, applyCancel;
    EditText applyAmount, applyYear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lender_home);

        UIHelper = new userInterfaceHelper(this);
        UIHelper.setContext(this);
        UIHelper.removeActionbar();
        UIHelper.transparentStatusBar();

        settingHelper = new SettingHelper(this);

        // Confirmation
        UIHelper.setConfirmation("LOGOUT", "DO YOU REALLY WANT TO LOGOUT?", "NO", "YES");
        UIHelper.setNegativeConfirmation("cancel");
        UIHelper.setPositiveConfirmation("logout");

        profileHelper = new ProfileHelper(this);

        setUIVariables();
        setOnClick();
        changeTintTab(tabUser);
    }
    void setUIVariables()
    {
        tabLender = findViewById(R.id.tabLenderBtn);
        // dashboardBtn = findViewById(R.id.tabDashboardBtn);
        tabUser = findViewById(R.id.tabUserBtn);
        title = findViewById(R.id.userTitleTxt);
        dashboard = findViewById(R.id.dashboardInclude);

        userCon = findViewById(R.id.recyclerCon);
        adminCon = findViewById(R.id.userCon);

        //Buttons for user
        logoutBtn = findViewById(R.id.userLogout);
        tabDashboard = findViewById(R.id.tabDashboardBtn);
        lenderArchiveBtn = findViewById(R.id.viewLenderArchive);
        changePasswordBtn = findViewById(R.id.userChangePassword);

        // Settings
        settingCon = findViewById(R.id.userSettingCon);
        settingsBtn = findViewById(R.id.userSettings);
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

        applyInfo = findViewById(R.id.applyInclude);
        applyAmount = findViewById(R.id.amountTB);
        applyYear = findViewById(R.id.yearTB);
        applyApply = findViewById(R.id.applyApplyBtn);
        applyCancel = findViewById(R.id.cancelApplyBtn);
    }
    void setOnClick() {
        // Tab onclick
        tabLender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTintTab(tabLender);
            }
        });

        tabDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTintTab(tabDashboard);
            }
        });

        tabUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeTintTab(tabUser);
            }
        });

        // Admin Buttons
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }
    void changeTintTab(Button button){
        // Setting the text color to default
        tabLender.setTextColor(getColor(R.color.lightLogin));
        tabDashboard.setTextColor(getColor(R.color.lightLogin));
        tabUser.setTextColor(getColor(R.color.lightLogin));

        // Setting the drawable color to default
        tabUser.setCompoundDrawableTintList(getColorStateList(R.color.lightLogin));
        tabDashboard.setCompoundDrawableTintList(getColorStateList(R.color.lightLogin));
        tabLender.setCompoundDrawableTintList(getColorStateList(R.color.lightLogin));

        // Setting the selected button to highlighted color
        button.setTextColor(getColor(R.color.lightTxt));
        button.setCompoundDrawableTintList(getColorStateList(R.color.lightTxt));

        changeContent(button);
    }

    void changeContent(Button button)
    {
        applyAdapter applyAdapter_;
        dashboard.setVisibility(View.INVISIBLE);
        if(applyList != null)
            if(applyList.size() > 0)
                applyList.clear();

        ProfileHelper profileHelper = new ProfileHelper(this);
        if(button.getId() == tabLender.getId())
        {
            title.setText("APPLY LIST");
            changeContainerVisibility(false);
            applyList = profileHelper.getApplyList(getIntent().getStringExtra("name"));
            applyAdapter_ = new applyAdapter(applyList, profileHelper, UIHelper, this);
           // usersLenderAdapter_.setUser_home(userHome.this);
            userCon.setAdapter(applyAdapter_);
            userCon.setLayoutManager(new LinearLayoutManager(this));
        }
        else if(button.getId() == tabDashboard.getId())
        {
            title.setText("DASHBOARD");
            changeContainerVisibility(false);

            dashboard.setVisibility(View.VISIBLE);
        }
        else if(button.getId() == tabUser.getId())
        {
            title.setText("USER");
            changeContainerVisibility(true);
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
}