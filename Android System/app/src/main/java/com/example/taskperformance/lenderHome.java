package com.example.taskperformance;

import android.content.Intent;
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
import com.example.Adapter.userListAdapter;
import com.example.Helper.ProfileHelper;
import com.example.Helper.SettingHelper;
import com.example.Helper.userInterfaceHelper;
import com.example.model.applyModel;
import com.example.model.listBorrowModel;
import com.example.model.userLenderModel;

import java.util.LinkedList;

public class lenderHome extends AppCompatActivity {

    Spinner animSpeedSpinner, animSpinner;
    Button tabLender, tabDashboard, tabUser;
    TextView title, titleConfirm, msgConfirm;;
    LinkedList<applyModel> applyList;
    LinkedList<listBorrowModel> borrowerList;
    SettingHelper settingHelper;
    RecyclerView userCon, borrowerListCer;
    LinearLayout adminCon;
    CardView logoutBtn, lenderArchiveBtn, dashboardBtn, changePasswordBtn, settingsBtn, profileBtn;
    ConstraintLayout settingCon, confirmationLayout, dashboard, applyInfo;
    userInterfaceHelper UIHelper;
    ProfileHelper profileHelper;
    Button settingApply, settingCancel, confirmLogout, cancelLogout, applyApply, applyCancel;
    EditText applyAmount, applyYear;
    public static lenderHome lender_home;
    TextView finished, unfinished, allContract, unpaid, paid, profit;
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

        lender_home = this;
        setDashboard();
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
        profileBtn = findViewById(R.id.profileBtn);

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

        borrowerListCer = findViewById(R.id.listCer);

        unpaid = findViewById(R.id.unpaidLenderTxt);
        paid = findViewById(R.id.paidLenderTxt);
        profit = findViewById(R.id.profitTxt);
        finished = findViewById(R.id.finishedContractTxt);
        unfinished = findViewById(R.id.unfinishedContractTxt);
        allContract = findViewById(R.id.allLenderContractTxt);

    }
    public void setDashboard() {
        double[] data = profileHelper.getLenderDashboard(getIntent().getStringExtra("username"));

        allContract.setText("ALL CONTRACTS\n"+(int)data[0]);
        finished.setText("FINISHED CONTRACTS\n"+(int)data[1]);
        unfinished.setText("UNFINISHED CONTRACTS\n"+(int)data[2]);
        paid.setText("PAID\n"+ String.format("%.2f", data[3]));
        unpaid.setText("UNPAID\n"+String.format("%.2f", data[4]));
        profit.setText("PROFIT\n"+String.format("%.2f", data[5]));
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

        changePasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lenderHome.this, changePassword.class)
                        .putExtra("username", getIntent().getStringExtra("username").toString())
                        .putExtra("type", "LENDER"));
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(lenderHome.this, lender_info.class)
                        .putExtra("create", false)
                        .putExtra("username", getIntent().getStringExtra("username".toString())));
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
        userListAdapter userListAdapter_;

        dashboard.setVisibility(View.INVISIBLE);
        if(applyList != null)
            if(applyList.size() > 0)
                applyList.clear();

        ProfileHelper profileHelper = new ProfileHelper(this);
        if(button.getId() == tabLender.getId())
        {
            updateBorrowerList();
        }
        else if(button.getId() == tabDashboard.getId())
        {
            userCon.setVisibility(View.INVISIBLE);
            title.setText("DASHBOARD");
            changeContainerVisibility(false);

            dashboard.setVisibility(View.VISIBLE);

            borrowerList = profileHelper.getBorrowList_Lender(getIntent().getStringExtra("name"));
            userListAdapter_ = new userListAdapter(borrowerList, profileHelper, UIHelper, this, userListAdapter.lender, "");
            borrowerListCer.setAdapter(userListAdapter_);
            borrowerListCer.setLayoutManager(new LinearLayoutManager(this));
        }
        else if(button.getId() == tabUser.getId())
        {
            title.setText("USER");
            changeContainerVisibility(true);
        }

    }

    public void updateBorrowerList() {

        applyAdapter applyAdapter_;
        userCon.setVisibility(View.VISIBLE);
        title.setText("APPLY LIST");
        changeContainerVisibility(false);
        applyList = profileHelper.getApplyList(getIntent().getStringExtra("name"));
        applyAdapter_ = new applyAdapter(applyList, profileHelper, UIHelper, this, getIntent().getStringExtra("username"));

        userCon.setAdapter(applyAdapter_);
        userCon.setLayoutManager(new LinearLayoutManager(this));
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