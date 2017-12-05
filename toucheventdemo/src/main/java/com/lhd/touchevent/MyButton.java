package com.lhd.touchevent;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Button;

/**
 * Created by liuhongda on 2017/12/3.
 */

public class MyButton extends android.support.v7.widget.AppCompatButton {

    static final String tag = "MyButton";

    public MyButton(Context context) {
        super(context);
    }

    public MyButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyButton(Context context, AttributeSet attrs, int defStyleAttr) {
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
