package com.example.taskperformance;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.Helper.ProfileHelper;
import com.example.Helper.SqliteHelper;
import com.example.Helper.userInterfaceHelper;

public class lender_info extends AppCompatActivity {

    userInterfaceHelper UIHelper;
    ProfileHelper profileHelper;
    private static final int PICK_IMAGE = 100;
    EditText name, email, min, max;
    Spinner rate, freq;
    ImageView profile;
    Button cancel, save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lender_info);

        UIHelper = new userInterfaceHelper(this);
        UIHelper.setContext(this);
        UIHelper.removeActionbar();
        UIHelper.transparentStatusBar();

        profileHelper = new ProfileHelper(this);
        setUIVariable();
        setOnClick();
        checkEdit();
    }

    void checkEdit() {
        if(getIntent().getBooleanExtra("create", false))
            return;

        // Load the data and display it
    }
    void setUIVariable()
    {
        name = findViewById(R.id.companyNameTB);
        email = findViewById(R.id.lenderEmailTB);
        min = findViewById(R.id.minTB);
        max = findViewById(R.id.maxTB);

        rate = findViewById(R.id.interestSpinner);
        freq = findViewById(R.id.freqSpinner);

        profile = findViewById(R.id.picture);
        cancel = findViewById(R.id.cancelLenderInfoBtn);
        save = findViewById(R.id.startBtn);
    }

    void setOnClick() {
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goAdmin();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewLender(name.getText().toString(),
                        email.getText().toString(),
                        Double.valueOf(max.getText().toString()),
                        Double.valueOf(min.getText().toString()),
                        Double.valueOf(rate.getSelectedItem().toString().replaceAll("%", "")),
                        freq.getSelectedItem().toString(),
                        ((BitmapDrawable) profile.getDrawable()).getBitmap());
            }
        });

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });
    }
    void createNewLender(String companyName, String email, double max, double min,
                         double interest, String freq, Bitmap img) {
        if(profileHelper.newLender(companyName, email, min, max, interest, freq, img))
        {
           // UIHelper.showCustomToast("You have successfully added a new lender.");
            goAdmin();
        }
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
    void goAdmin() {
        startActivity(new Intent(lender_info.this, adminHome.class)
                .putExtra("normal", false));
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