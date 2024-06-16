package com.example.taskperformance;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Helper.PasswordHelper;
import com.example.Helper.ProfileHelper;
import com.example.Helper.userInterfaceHelper;

public class changePassword extends AppCompatActivity {

    EditText currentPass, newPass, reNewPass;
    Button cancelBtn, confirmBtn;

    userInterfaceHelper UIHelper;
    ProfileHelper profileHelper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setUIVariables();

        UIHelper = new userInterfaceHelper(this);
        UIHelper.removeActionbar();
        UIHelper.transparentStatusBar();
        UIHelper.setContext(this);

        profileHelper = new ProfileHelper(this);

        PasswordHelper.setViewPassword(currentPass, this);
        PasswordHelper.setViewPassword(newPass, this);
        PasswordHelper.setViewPassword(reNewPass, this);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordButton();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(getIntent().getStringExtra("type").equalsIgnoreCase("LENDER")) {
                    startActivity(new Intent(changePassword.this, lenderHome.class)
                            .putExtra("username", getIntent().getStringExtra("username")));
                }
                else if(getIntent().getStringExtra("type").equalsIgnoreCase("BORROWER")) {
                    startActivity(new Intent(changePassword.this, userHome.class)
                            .putExtra("username", getIntent().getStringExtra("username")));
                }
                else if(getIntent().getStringExtra("type").equalsIgnoreCase("ADMIN")) {
                    startActivity(new Intent(changePassword.this, adminHome.class)
                            .putExtra("username", "ADMIN_" + getIntent().getStringExtra("username")));
                }
            }
        });
    }

    void changePasswordButton() {
        String current = currentPass.getText().toString();
        String newP = newPass.getText().toString();
        String reP = reNewPass.getText().toString();

        if(current.isEmpty()) {
            UIHelper.showCustomToast("Current Password is empty.");
            return;
        }
        else if(newP.isEmpty()) {
            UIHelper.showCustomToast("New Password is empty.");
            return;
        }
        else if(reP.isEmpty()) {
            UIHelper.showCustomToast("Confirm Password is empty.");
            return;
        }

        if(newP.equals(reP)) {
                // Confirmation
                UIHelper.setConfirmation("CHANGE PASSWORD", "DO YOU REALLY WANT TO CHANGE YOUR PASSWORD?", "NO", "YES");
                UIHelper.setNegativeConfirmation("close");
                UIHelper.showCustomToast(UIHelper.setPositiveConfirmation_changePassword(profileHelper, getIntent().getStringExtra("username"),
                        current, newP).toString());

                if(getIntent().getStringExtra("type").equalsIgnoreCase("LENDER")) {
                    startActivity(new Intent(changePassword.this, lenderHome.class)
                            .putExtra("username", getIntent().getStringExtra("username")));
                }
                else if(getIntent().getStringExtra("type").equalsIgnoreCase("BORROWER")) {
                    startActivity(new Intent(changePassword.this, userHome.class)
                            .putExtra("username", getIntent().getStringExtra("username")));
                }
                else if(getIntent().getStringExtra("type").equalsIgnoreCase("ADMIN")) {
                    startActivity(new Intent(changePassword.this, adminHome.class)
                            .putExtra("username", getIntent().getStringExtra("username")));

                }
        }
        else {
            UIHelper.showCustomToast("Both passwords must be the same.");
        }
    }
    void setUIVariables() {
        currentPass = findViewById(R.id.currentPasswordTB);
        newPass = findViewById(R.id.newPassTB);
        reNewPass = findViewById(R.id.newRePassTB);

        cancelBtn = findViewById(R.id.changePassCancel);
        confirmBtn = findViewById(R.id.changePassConfirm);
    }
    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }
}