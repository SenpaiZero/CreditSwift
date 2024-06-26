package com.example.taskperformance;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.Adapter.historyAdapter;
import com.example.Adapter.userLenderAdapter;
import com.example.Adapter.userListAdapter;
import com.example.Helper.ProfileHelper;
import com.example.Helper.SettingHelper;
import com.example.Helper.userInterfaceHelper;
import com.example.model.historyModel;
import com.example.model.listBorrowModel;
import com.example.model.userLenderModel;

import java.util.LinkedList;

public class userHome extends AppCompatActivity {

    Spinner animSpeedSpinner, animSpinner;
    Button tabLender, tabDashboard, tabUser, rate, freq;
    TextView title, titleConfirm, msgConfirm;;
    LinkedList<userLenderModel> usersLenderList;
    LinkedList<listBorrowModel> listBorrowModel;
    LinkedList<historyModel> listHistory;
    SettingHelper settingHelper;
    RecyclerView userCon, dashboardRec;
    LinearLayout adminCon, filterCon;
    CardView logoutBtn, lenderArchiveBtn, dashboardBtn, changePasswordBtn, settingsBtn, profileBtn, historyBtn;
    ConstraintLayout settingCon, confirmationLayout, dashboard, applyInfo;
    userInterfaceHelper UIHelper;
    ProfileHelper profileHelper;
    Button settingApply, settingCancel, confirmLogout, cancelLogout, applyApply, applyCancel;
    EditText applyAmount, applyYear;
    public static userHome user_home;

    TextView unpaid, paid, contract, totalLoans, currentLoans, paidLoans;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_home);

        UIHelper = new userInterfaceHelper(this);
        UIHelper.setContext(this);
        UIHelper.removeActionbar();
        UIHelper.transparentStatusBar();

        settingHelper = new SettingHelper(this);
        profileHelper = new ProfileHelper(this);
        // Confirmation
        UIHelper.setConfirmation("LOGOUT", "DO YOU REALLY WANT TO LOGOUT?", "NO", "YES");
        UIHelper.setNegativeConfirmation("cancel");
        UIHelper.setPositiveConfirmation("logout");

        setUIVariables();
        setOnClick();
        changeTintTab(tabUser);

        if(profileHelper.checkUserFirstTime(getIntent().getStringExtra("name")))
        {
            startActivity(new Intent(userHome.this, borrower_info.class)
                    .putExtra("firstTime", true)
                    .putExtra("username", getIntent().getStringExtra("username")));
        }

        user_home = this;
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

        dashboardRec = findViewById(R.id.listCer);

        unpaid = findViewById(R.id.unpaidTxt);
        paid = findViewById(R.id.fullPaidTxt);
        contract = findViewById(R.id.allContractTxt);

        totalLoans = findViewById(R.id.totalLoansTxt);
        currentLoans = findViewById(R.id.paidLenderTxt);
        paidLoans = findViewById(R.id.profitTxt);

        rate = findViewById(R.id.ratesBtn);
        freq = findViewById(R.id.freqBtn);
        filterCon = findViewById(R.id.filterCon);

        historyBtn = findViewById(R.id.userHistory);
    }

    public void setDashboard() {
        double[] data = profileHelper.getBorrowerDashboard(getIntent().getStringExtra("username"));
        contract.setText("ALL CONTRACTS\n"+String.format("%.0f", data[0]));
        paid.setText("PAID\n"+String.format("%.0f", data[1]));
        unpaid.setText("UNPAID\n"+String.format("%.0f", data[2]));

        totalLoans.setText("TOTAL LOANS\n"+data[3]);
        currentLoans.setText("CURRENT LOANS\n"+data[4]);
        paidLoans.setText("PAID LOANS\n"+data[5]);
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
                UIHelper.setConfirmation("LOGOUT", "DO YOU REALLY WANT TO LOGOUT?", "NO", "YES");
                UIHelper.setNegativeConfirmation("cancel");
                UIHelper.setPositiveConfirmation("logout");
                UIHelper.setConfirmVisibility(true);
            }
        });

        settingsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingCon.setVisibility(View.VISIBLE);
            }
        });

        historyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                title.setText("HISTORY");
                userCon.setVisibility(View.VISIBLE);
                adminCon.setVisibility(View.INVISIBLE);
                filterCon.setVisibility(View.INVISIBLE);

                if(listBorrowModel != null)
                    if(listBorrowModel.size() > 0)
                        listBorrowModel.clear();
                if(usersLenderList != null)
                    if(usersLenderList.size() > 0)
                        usersLenderList.clear();
                if(listHistory != null)
                    if(listHistory.size() > 0)
                        listHistory.clear();

                listHistory = profileHelper.getHistoryList_borrower(getIntent().getStringExtra("username"));
                historyAdapter historyAdapter_ = new historyAdapter(userHome.this,listHistory);

                userCon.setAdapter(historyAdapter_);
                userCon.setLayoutManager(new LinearLayoutManager(userHome.this));
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
                startActivity(new Intent(userHome.this, changePassword.class)
                        .putExtra("username", getIntent().getStringExtra("username").toString())
                        .putExtra("type", "BORROWER"));
            }
        });

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(userHome.this, borrower_info.class)
                        .putExtra("username", getIntent().getStringExtra("username"))
                        .putExtra("firstTime", false)
                        .putExtra("borrower", true));
            }
        });

        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = rate.getText().toString().toUpperCase();
                String val = "";
                if(s.equals("ALL RATES")) {
                    rate.setText("BELOW 10%");
                    val = "10.0";
                }
                else if(s.equals("BELOW 10%")){
                    rate.setText("BELOW 8%");
                    val = "8.0";
                }
                else if(s.equals("BELOW 8%")){
                    rate.setText("BELOW 6%");
                    val = "6.0";
                }
                else if(s.equals("BELOW 6%")){
                    rate.setText("BELOW 4%");
                    val = "4.0";
                }
                else if(s.equals("BELOW 4%")){
                    rate.setText("BELOW 2%");
                    val = "2.0";
                }
                else
                    rate.setText("ALL RATES");

                setLenderList(val, freq.getText().toString());
            }
        });

        freq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = freq.getText().toString().toUpperCase();
                if(s.equals("ALL FREQ")) {
                    freq.setText("ANNUAL");
                }
                else if(s.equals("ANNUAL")) {
                    freq.setText("SEMI-ANNUAL");
                }
                else if(s.equals("SEMI-ANNUAL")) {
                    freq.setText("QUARTERLY");
                } else if(s.equals("QUARTERLY")) {
                    freq.setText("MONTHLY");
                }
                else if(s.equals("MONTHLY")) {
                    freq.setText("WEEKLY");
                }
                else if(s.equals("WEEKLY")) {
                    freq.setText("ALL FREQ");
                }


                String x = rate.getText().toString().toUpperCase();
                String val = x.contains("10") ? "10.0" : x.contains("8") ? "8.0" : x.contains("6") ? "6.0" :
                        x.contains("4") ? "4.0" : x.contains("2") ? "2.0" : "ALL FREQ";

                setLenderList(val, freq.getText().toString());
            }
        });
    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    void changeTintTab(Button button)
    {
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

    void setLenderList(String rate, String freq) {
        userLenderAdapter usersLenderAdapter_;
        title.setText("LENDER");
        changeContainerVisibility(false);
        usersLenderList = profileHelper.getUsersLenderList("LENDER", false, rate, freq);
        usersLenderAdapter_ = new userLenderAdapter(usersLenderList, this, userLenderAdapter.user, profileHelper);

        userCon.setAdapter(usersLenderAdapter_);
        userCon.setLayoutManager(new LinearLayoutManager(this));
    }
    void changeContent(View button)
    {
        filterCon.setVisibility(View.INVISIBLE);
        dashboard.setVisibility(View.INVISIBLE);
        if(usersLenderList != null)
            if(usersLenderList.size() > 0)
                usersLenderList.clear();

        ProfileHelper profileHelper = new ProfileHelper(this);
        if(button.getId() == tabLender.getId())
        {
            if(listBorrowModel != null)
                if(listBorrowModel.size() > 0)
                    listBorrowModel.clear();
            filterCon.setVisibility(View.VISIBLE);
            setLenderList(rate.getText().toString(), freq.getText().toString());
        }
        else if(button.getId() == tabDashboard.getId())
        {
            title.setText("DASHBOARD");
            changeContainerVisibility(false);

            if(usersLenderList != null)
                if(usersLenderList.size() > 0)
                    usersLenderList.clear();

            userCon.setVisibility(View.INVISIBLE);
            dashboard.setVisibility(View.VISIBLE);
            setDashboardList();
        }
        else if(button.getId() == tabUser.getId())
        {
            title.setText("USER");
            changeContainerVisibility(true);
        }
    }

    public void setDashboardList() {
        userListAdapter userListAdapter_;
        listBorrowModel = profileHelper.getCurrentListBorrow(getIntent().getStringExtra("username"));
        userListAdapter_ = new userListAdapter(listBorrowModel, profileHelper, UIHelper, this, userListAdapter.borrower, getIntent().getStringExtra("username"));
        dashboardRec.setAdapter(userListAdapter_);
        dashboardRec.setLayoutManager(new LinearLayoutManager(this));
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

    public void openApplyInfo(String companyName, double min, double max) {
        applyInfo.setVisibility(View.VISIBLE);
        applyAmount.setText(null);
        applyYear.setText(null);
        applyCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyInfo.setVisibility(View.INVISIBLE);
            }
        });
        applyApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double amount = Double.valueOf(applyAmount.getText().toString());
                int year = Integer.valueOf(applyYear.getText().toString());

                if(amount < min || amount > max) {
                    UIHelper.showCustomToast("Please make sure that the amount is within the company's budget.");
                    return;
                }

                if(year > 25) {
                    UIHelper.showCustomToast("Please make sure that the year is lower than 25.");
                    return;
                }
                if(year <= 0) {
                    UIHelper.showCustomToast("Please make sure that the year is bigger than 0.");
                    return;
                }
                String a = profileHelper.addUpdateCurrentLend(getIntent().getStringExtra("username"),
                        companyName, amount, amount, year, true);
                Log.d("apply", "username: " + getIntent().getStringExtra("username"));

                if(a.equalsIgnoreCase("EXIST")) {
                    Log.d("apply", "ADD ALREADY EXIST. STOPPED");
                    UIHelper.showCustomToast("You already applied for this lender.");
                }
                else  if(a.equalsIgnoreCase("ADD")){
                    Log.d("apply", "NEW APPLY ADDED");
                    UIHelper.showCustomToast("You successfully applied to the lender.");
                }
                else if(a.equalsIgnoreCase("UPDATE")){
                    Log.d("apply", "APPLY HAS BEEN UPDATED");
                    UIHelper.showCustomToast("You successfully updated the data");
                }
                else {
                    Log.d("apply", "APPLY ERROR");
                    UIHelper.showCustomToast("An error has occured.");
                }

                applyInfo.setVisibility(View.INVISIBLE);
            }
        });
    }
    @SuppressLint("MissingSuperCall")
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}