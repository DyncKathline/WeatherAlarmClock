package com.kaku.weac.nohttp;

import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.view.Gravity;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.kaku.weac.R;

public class LoadingDialog extends Dialog{

    private ProgressBar mProgressBar;
    private TextView mTvMessage;

    public LoadingDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_loading);
        mProgressBar = findViewById(R.id.progress_bar);
        mTvMessage = findViewById(R.id.tv_message);
        Window window = getWindow();
        if(window != null) {
            window.setGravity(Gravity.CENTER);
            window.setBackgroundDrawable(new ColorDrawable(0));
        }
    }

    /**
     * Set the color of the Bar.
     *
     * @param color color.
     */
    public LoadingDialog setColorFilter(@ColorInt int color) {
        Drawable drawable = mProgressBar.getIndeterminateDrawable();
        drawable = drawable.mutate();
        drawable.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        mProgressBar.setIndeterminateDrawable(drawable);
        return this;
    }

    /**
     * Set the message.
     *
     * @param message message resource id.
     */
    public LoadingDialog setMessage(@StringRes int message) {
        mTvMessage.setText(message);
        return this;
    }

    /**
     * Set the message.
     *
     * @param message message.
     */
    public LoadingDialog setMessage(String message) {
        mTvMessage.setText(message);
        return this;
    }
}