package com.example.Helper;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.InputType;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;

import androidx.core.content.ContextCompat;

import com.example.taskperformance.MainActivity;
import com.example.taskperformance.R;

public class PasswordHelper {
    public static void hidePassword(EditText[] text, Context context)
    {
        for (int i = 0; i < text.length; i++)
        {
            EditText text_temp = text[i];
            int w = text_temp.getWidth();
            text_temp.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            text_temp.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.password_img),
                    null, ContextCompat.getDrawable(context, R.drawable.password_hide), null);
            text_temp.setWidth(w);
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public static void setViewPassword(final EditText text, Context context) {
        text.setOnTouchListener(new View.OnTouchListener() {
            boolean open = false;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    int drawableRight = text.getRight() - (text.getCompoundDrawables()[2].getBounds().width());
                    if (event.getRawX() >= drawableRight) {
                        int w = text.getWidth();
                        if (open) {
                            open = false;
                            text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            text.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.password_img),
                                    null, ContextCompat.getDrawable(context, R.drawable.password_hide), null);
                        } else {
                            open = true;
                            text.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            text.setCompoundDrawablesWithIntrinsicBounds(ContextCompat.getDrawable(context, R.drawable.password_img),
                                    null, ContextCompat.getDrawable(context, R.drawable.password_reveal), null);
                        }
                        text.setSelection(text.getText().length());
                        text.setWidth(w);
                        text.requestFocus();
                        return true;
                    }
                }
                return false;
            }
        });
    }
}
