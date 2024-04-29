package com.example.taskperformance;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Helper.ProfileHelper;
import com.example.Helper.userInterfaceHelper;

public class borrower_info extends AppCompatActivity {

    Spinner daySpinner, monthSpinner, yearSpinner;
    EditText lastName, firstName, middleName;
    Button cancel, save;
    ImageView profile;
    userInterfaceHelper UIHelper;
    ProfileHelper profileHelper;
    private static final int PICK_IMAGE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.borrower_info);

        UIHelper = new userInterfaceHelper(this);
        UIHelper.setContext(this);
        UIHelper.removeActionbar();
        UIHelper.transparentStatusBar();

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
            cancel.setVisibility(View.VISIBLE);
            // Load the data and display it

            return;
        }

        cancel.setVisibility(View.INVISIBLE);
    }

    void addNewInfo(String name, String birthday, Bitmap img) {
        if(profileHelper.newUserInformation(getIntent().getStringExtra("username").toString(),
                name, birthday, img))
            goBack();
    }
    void setupUIVariable() {
        daySpinner = findViewById(R.id.monthSpinner);
        monthSpinner = findViewById(R.id.daySpinner);
        yearSpinner = findViewById(R.id.yearSpinner);

        cancel = findViewById(R.id.cancelLenderInfoBtn);
        save = findViewById(R.id.startBtn);

        profile = findViewById(R.id.picture);

        firstName = findViewById(R.id.firstNameTB);
        lastName = findViewById(R.id.lastNameTB);
        middleName = findViewById(R.id.middleNameTB);

    }

    void setOnclick() {
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
                String name_ = lastName.getText().toString().toUpperCase() + " " +
                        firstName.getText().toString().toUpperCase() + " " +
                        middleName.getText().toString().toUpperCase();

                String birth_ = monthSpinner.getSelectedItem().toString().toUpperCase() + " " +
                        daySpinner.getSelectedItem().toString() + " " +
                        yearSpinner.getSelectedItem().toString();
                addNewInfo(name_, birth_, ((BitmapDrawable) profile.getDrawable()).getBitmap());
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
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

    public void goBack() {
        startActivity(new Intent(borrower_info.this, userHome.class));
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