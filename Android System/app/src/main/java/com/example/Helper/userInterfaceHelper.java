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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.taskperformance.MainActivity;
import com.example.taskperformance.R;

public class userInterfaceHelper extends AppCompatActivity {
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
        else
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
                        context.startActivity(new Intent(context, MainActivity.class));
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
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(compareValue)){
                return i;
            }
        }

        return 0;
    }
}

