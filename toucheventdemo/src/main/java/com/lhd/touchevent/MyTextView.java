package com.lhd.touchevent;

import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * Created by liuhongda on 2017/12/3.
 */

public class MyTextView extends android.support.v7.widget.AppCompatTextView {

    static final String tag = "MyTextView";

    public MyTextView(Context context) {
        super(context);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyTextView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        Log.d(tag, "dispatchTouchEvent:" + event);
        return super.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(tag, "onTouchEvent:" + event);
        boolean touch = super.onTouchEvent(event);
        Log.d(tag, "touch:" + touch);
        return touch;
    }

    @Override
    public boolean isClickable() {
        return super.isClickable();
    }
}
