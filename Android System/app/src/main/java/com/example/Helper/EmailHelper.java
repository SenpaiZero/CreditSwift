package com.example.Helper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.example.taskperformance.MainActivity;

public class EmailHelper {
    protected void sendEmail(Activity activity, Context context, String user, String password, String email) {
        Log.i("Send email", "");

        String[] TO = {email};
        String[] CC = {"ygisantos@gmail.com"};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");


        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Your subject");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Username: " + user +
                                                        "\nPassword: " + password);

        try {
            activity.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            activity.finish();
            Log.i("Finished sending email...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(context,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
        }
    }
}
