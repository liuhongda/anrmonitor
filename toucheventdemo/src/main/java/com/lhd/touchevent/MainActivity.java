package com.lhd.touchevent;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

public class MainActivity extends Activity {

    static final String tag = "MainActivity";

    MyButton myButton;

    MyTextView myTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        myTextView = (MyTextView) findViewById(R.id.my_tv);
        myButton = (MyButton) findViewById(R.id.my_btn);
//        myTextView.setClickable(true);
        myButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(tag, "myButton onTouch:" + event);
                return false;
            }
        });
        myTextView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Log.d(tag, "myTextView onTouch:" + event);
                return false;
            }
        });
        Log.d(tag, "tv:" + myTextView.isClickable());
        Log.d(tag, "btn:" + myButton.isClickable());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.d(tag, "onTouchEvent:" + event);
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        Log.d(tag, "dispatchTouchEvent:" + ev);
        boolean touch = super.dispatchTouchEvent(ev);
        Log.d(tag, "dispatchTouchEvent:" + touch);
        return touch;
    }
}
