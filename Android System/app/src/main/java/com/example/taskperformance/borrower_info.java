package com.example.taskperformance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Helper.ProfileHelper;
import com.example.Helper.userInterfaceHelper;

public class borrower_info extends AppCompatActivity {

    Spinner daySpinner, monthSpinner, yearSpinner;
    EditText lastName, firstName, middleName, email;
    TextView emailTxt;
    Button cancel, save;
    ImageView profile;
    userInterfaceHelper UIHelper;
    ProfileHelper profileHelper;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrower_info);

        UIHelper = new userInterfaceHelper(borrower_info.this);
        UIHelper.removeActionbar();
        UIHelper.transparentStatusBar();
        UIHelper.setContext(this);

        setupUIVariable();
        setOnclick();
        profileHelper = new ProfileHelper(this);

        UIHelper.setupDateSpinner(daySpinner, monthSpinner, yearSpinner);
        checkEdit();
    }

    void checkEdit() {
        //if it's edit
        if(!getIntent().getBooleanExtra("firstTime", false))
        {
            emailTxt.setVisibility(View.VISIBLE);
            email.setVisibility(View.VISIBLE);
            cancel.setVisibility(View.VISIBLE);
            // Load the data and display it
            profileHelper.getEditBorrower(getIntent().getStringExtra("username"));
            String username_ = profileHelper.borrowUsername;
            String name_ = profileHelper.borrowName;
            String birth_ = profileHelper.borrowBirth;
            String email_ = profileHelper.borrowEmail;
            Bitmap pic_ = profileHelper.borrowPic;

            lastName.setText(name_.split("\\|")[0]);
            firstName.setText(name_.split("\\|")[1]);
            middleName.setText(name_.split("\\|")[2]);

            monthSpinner.setSelection(UIHelper.getSpinnerSelected(monthSpinner, birth_.split(" ")[0]));
            yearSpinner.setSelection(UIHelper.getSpinnerSelected(yearSpinner, birth_.split(" ")[2]));
            daySpinner.setSelection(UIHelper.getSpinnerSelected(daySpinner, birth_.split(" ")[1]));

            email.setText(email_);
            profile.setImageBitmap(pic_);
            return;
        }
        cancel.setVisibility(View.INVISIBLE);
        emailTxt.setVisibility(View.INVISIBLE);
        email.setVisibility(View.INVISIBLE);

    }

    void addNewInfo(String name, String birthday, Bitmap img) {
        if(profileHelper.newUserInformation(getIntent().getStringExtra("username").toString(),
                name, birthday, img))
            goBack(true);
    }
    void setupUIVariable() {
        daySpinner = findViewById(R.id.daySpinner);
        monthSpinner = findViewById(R.id.monthSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);

        cancel = findViewById(R.id.cancelLenderInfoBtn);
        save = findViewById(R.id.startBtn);

        profile = findViewById(R.id.picture);

        firstName = findViewById(R.id.firstNameTB);
        lastName = findViewById(R.id.lastNameTB);
        middleName = findViewById(R.id.middleNameTB);
        email = findViewById(R.id.emailBorrowerTB);
        emailTxt = findViewById(R.id.textView44);
    }

    void setOnclick() {
        boolean isFirstTime = getIntent().getBooleanExtra("firstTime", false);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name_ = lastName.getText().toString().toUpperCase() + "|" +
                        firstName.getText().toString().toUpperCase() + "|" +
                        middleName.getText().toString().toUpperCase();

                String birth_ = monthSpinner.getSelectedItem().toString().toUpperCase() + " " +
                        daySpinner.getSelectedItem().toString() + " " +
                        yearSpinner.getSelectedItem().toString();

                if(isFirstTime)
                    addNewInfo(name_, birth_, ((BitmapDrawable) profile.getDrawable()).getBitmap());
                else
                    profileHelper.updateBorrowerInfo(getIntent().getStringExtra("username").toString(),
                            name_,
                            email.getText().toString().toUpperCase(),
                            birth_, ((BitmapDrawable) profile.getDrawable()).getBitmap()
                            );

                goBack(false);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack(false);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PICK_IMAGE) {
            Uri imageUri = data.getData();
            Bitmap resized = resizeImage(imageUri);
            profile.setImageBitmap(resized);
        }
    }

    public void goBack(boolean isCreate) {
        if(isCreate)
        {
            startActivity(new Intent(borrower_info.this, userHome.class));
            return;
        }
        startActivity(new Intent(borrower_info.this, adminHome.class));
    }
    private Bitmap resizeImage(Uri imageUri) {
        try {
            // Load the image from the Uri
            Bitmap originalBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);

            // Define maximum dimensions for the resized image
            int maxWidth = 512; // You can adjust this value as needed
            int maxHeight = 512; // You can adjust this value as needed

            // Get the original dimensions of the image
            int originalWidth = originalBitmap.getWidth();
            int originalHeight = originalBitmap.getHeight();

            // Calculate the scaling factors to maintain aspect ratio
            float scaleWidth = ((float) maxWidth) / originalWidth;
            float scaleHeight = ((float) maxHeight) / originalHeight;

            // Use the smaller scaling factor to ensure that the image fits within the specified dimensions
            float scaleFactor = Math.min(scaleWidth, scaleHeight);

            // Calculate the new dimensions for the resized image
            int newWidth = Math.round(originalWidth * scaleFactor);
            int newHeight = Math.round(originalHeight * scaleFactor);

            // Resize the original bitmap to the new dimensions
            Bitmap resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, newWidth, newHeight, true);

            // Return the resized bitmap
            return resizedBitmap;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}