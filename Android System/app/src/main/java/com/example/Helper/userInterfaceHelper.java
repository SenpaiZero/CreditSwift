package com.example.Helper;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.taskperformance.MainActivity;
import com.example.taskperformance.R;
import com.example.taskperformance.adminHome;
import com.example.taskperformance.lenderHome;
import com.example.taskperformance.userHome;

import java.util.Arrays;

public class userInterfaceHelper {
    Activity activity;
    Context context;
    ConstraintLayout confirmation, customCardView;
    TextView titleTxt, messageTxt;
    Button negativeBtn, positiveBtn;

    public userInterfaceHelper(Activity activity)
    {
        this.activity = activity;
        confirmation = activity.findViewById(R.id.confirmationInclude);
        customCardView = activity.findViewById(R.id.card_view);
        if(customCardView != null) customCardView.setVisibility(View.GONE);
    }
    public void setContext(Context context)
    {
        this.context = context;
    }
    public void showCustomToast(String message) {
        showCustomToast_main(message, 0);
    }
    public void showCustomToast(String message, long durationMili) {
        showCustomToast_main(message, durationMili);
    }

    private void showCustomToast_main(String message, long duration)
    {
        final TextView toastText = customCardView.findViewById(R.id.toast_text);
        toastText.setText(message);

        customCardView.setVisibility(View.VISIBLE);
        customCardView.bringToFront();
        customCardView.setZ(999);
        customCardView.setElevation(999);

        // Not sure pa if this will work (don't have testing device)
        customCardView.requestLayout();
        customCardView.invalidate();

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(customCardView, "alpha", 1f, 0f);
        if(duration == 0)
            fadeOut.setDuration(5000);
        else
            fadeOut.setDuration(duration);

        fadeOut.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                customCardView.setVisibility(View.GONE);
            }
        });

        fadeOut.start();
    }
    public void setConfirmVisibility(boolean isVisible)
    {
        if(isVisible)
            confirmation.setVisibility(View.VISIBLE);
        else
            confirmation.setVisibility(View.INVISIBLE);
    }

    public boolean isConfirmationVisible()
    {
        if(confirmation.getVisibility() == View.VISIBLE)
            return true;

        return false;
    }
    public void setConfirmation(String title, String message, String negativeTxt, String positiveTxt)
    {
        titleTxt = activity.findViewById(R.id.titleTxt);
        messageTxt = activity.findViewById(R.id.messageTxt);
        negativeBtn = activity.findViewById(R.id.negativeBtnConfirmation);
        positiveBtn = activity.findViewById(R.id.positiveBtnConfirmation);

        titleTxt.setText(title);
        messageTxt.setText(message);
        negativeBtn.setText(negativeTxt);
        positiveBtn.setText(positiveTxt);
    }
    public void setPositiveConfirmation(String type)
    {
            positiveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(type.equalsIgnoreCase("exit"))
                    {
                        activity.finish();
                        System.exit(0);
                    }
                    else if(type.equalsIgnoreCase("logout"))
                    {
                        StayLoginHelper stayLoginHelper = new StayLoginHelper(activity);
                        stayLoginHelper.setStayLogin(false);

                        activity.finish();
                        Log.d("Logout", stayLoginHelper.getStayLogin() + "");
                        context.startActivity(new Intent(context, MainActivity.class));
                    }
                }
            });
    }
    public String setPositiveConfirmation_changePassword(ProfileHelper profileHelper, String username, String currentPass, String newPass) {
        String va = profileHelper.checkLogin(username, currentPass);

        if(va.equalsIgnoreCase("LENDER")) {
            profileHelper.changePassword(username, newPass);
            Log.d("Change Password", "LENDER");
            return "Password has been updated";
        }
        else if(va.equalsIgnoreCase("BORROWER")) {
            profileHelper.changePassword(username, newPass);
            Log.d("Change Password", "BORROWER");
            return "Password has been updated";
        }
        else if(va.equalsIgnoreCase("ADMIN")) {
            profileHelper.changePassword(username, newPass);
            Log.d("Change Password", "ADMIN");
            return "Password has been updated";
        }
        else {
            return "Incorrect current password.";
        }
    }

    public void setPositiveConfirmation_pay(ProfileHelper profileHelper, String borrowerName, String lenderName, double remaining) {
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileHelper.addUpdateCurrentLend(borrowerName, lenderName, remaining,1122334455, 1122334455, false);
                userHome.user_home.setDashboard();
                userHome.user_home.setDashboardList();
                setConfirmVisibility(false);
            }
        });
    }
    public void setPositiveConfirmation(String type, ProfileHelper profileHelper, String name, boolean isLender) {
        positiveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (type.equalsIgnoreCase("unarchive")) {
                    profileHelper.changeArchive(name, false);
                    setConfirmVisibility(false);

                    if(isLender)
                        adminHome.admin.setLenderList(true);
                    else
                        adminHome.admin.setBorrowerList(true);
                }
                else if(type.equalsIgnoreCase("archive")) {
                    profileHelper.changeArchive(name, true);
                    setConfirmVisibility(false);

                    if(isLender)
                        adminHome.admin.setLenderList(false);
                    else
                        adminHome.admin.setBorrowerList(false);
                }
            }
        });
    }

    public void setNegativeConfirmation(String type)
    {
        if(type.equalsIgnoreCase("close") || type.equalsIgnoreCase("cancel"))
        {
            negativeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    setConfirmVisibility(false);
                }
            });
        }
    }
    public void removeActionbar()
    {
        try
        {
            ((AppCompatActivity)activity).getSupportActionBar().hide();
        } catch (Exception ex)
        {
            try {
                activity.getActionBar().hide();
            } catch (Exception exx)
            {
                Log.e("Action Bar", exx.getMessage());
            }
        }
    }
    public void transparentStatusBar()
    {
        try {
            Window window = activity.getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            window.setStatusBarColor(Color.TRANSPARENT);
        } catch (Exception ex)
        {
            Log.e("Transparent Status Bar", ex.getMessage());
        }

    }

    public void setSpinner(String[] arr, Spinner spin)
    {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_style, arr);
        adapter.setDropDownViewResource(R.layout.spinner_style);
        spin.setAdapter(adapter);
    }

    public int getSpinnerSelected(Spinner spinner, String compareValue)
    {
        for (int i=0;i<spinner.getCount();i++){
            if (spinner.getItemAtPosition(i).toString().trim().equalsIgnoreCase(compareValue.trim())){
                return i;
            }
        }

        return 0;
    }

    public void setupDateSpinner(Spinner day, Spinner month, Spinner year) {

        String[] months = new String[] {
                "January", "February", "March", "April", "May", "June", "July",
                "August", "September", "October", "November", "December"
        };
        String[] years = new String[50];
        for(int i = years.length; i > 0; i--) years[i-1] = String.valueOf(2024-i+1);

        ArrayAdapter<String> adapter_day;
        ArrayAdapter<String> adapter_month;
        ArrayAdapter<String> adapter_year;

        if (day.getAdapter() == null || month.getAdapter() == null || year.getAdapter() == null) {
            adapter_year = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_item, years);
            adapter_month = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, months);

            // Set initial selection
            adapter_day = new ArrayAdapter<>(context,
                    android.R.layout.simple_spinner_dropdown_item, setDay((month.getSelectedItem() != null) ? month.getSelectedItem().toString() : "", (year.getSelectedItem() != null) ? Integer.valueOf(year.getSelectedItem().toString()) : 0));

            adapter_day.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter_month.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            adapter_year.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

            day.setAdapter(adapter_day); // Set the day spinner adapter directly here
            year.setAdapter(adapter_year);
            month.setAdapter(adapter_month);

            // Add OnItemSelectedListener to month spinner
            month.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Update day spinner based on selected month
                    String selectedMonth = parent.getSelectedItem().toString();
                    int selectedYear = Integer.valueOf(year.getSelectedItem().toString());
                    String[] dayArray = setDay(selectedMonth, selectedYear);
                    ArrayAdapter<String> newAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, dayArray);

                    int pos = day.getSelectedItemPosition();
                    day.setAdapter(newAdapter);
                    if(setDay(selectedMonth, selectedYear).length > pos)
                        day.setSelection(pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });

// Add OnItemSelectedListener to year spinner
            year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    // Update day spinner based on selected year
                    String selectedMonth = month.getSelectedItem().toString();
                    int selectedYear = Integer.valueOf(parent.getSelectedItem().toString());
                    String[] dayArray = setDay(selectedMonth, selectedYear);
                    ArrayAdapter<String> newAdapter = new ArrayAdapter<>(context,
                            android.R.layout.simple_spinner_dropdown_item, dayArray);
                    int pos = day.getSelectedItemPosition();
                    day.setAdapter(newAdapter);
                    if(setDay(selectedMonth, selectedYear).length > pos)
                        day.setSelection(pos);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Do nothing
                }
            });

        }
    }



    String[] setDay(String month, int year) {
        String[] days_31 = new String[] {
                "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30", "31"
        };
        String[] days_30 = new String[] {
                "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29","30"
        };
        String[] days_29 = new String[] {
                "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28","29"
        };
        String[] days_28 = new String[] {
                "1","2","3","4","5","6","7","8","9","10","11","12","13","14","15",
                "16","17","18","19","20","21","22","23","24","25","26","27","28"
        };

        String m = month.toLowerCase();
        String[] dayCountArray;
        if (Arrays.asList("january", "march", "may", "july", "august", "october", "december").contains(m))
            dayCountArray = days_31;
        else if (!m.equals("february"))
            dayCountArray = days_30;
        else
            dayCountArray = (year % 4 == 0) ? days_29 : days_28; // Check if it's a leap year

        return dayCountArray;
    }

}

