package com.lhd.anrmonitor;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    AnrWatchDog anrWatchDog;

    Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new Handler();
        anrWatchDog = new AnrWatchDog.Builder().timeout(30000).ignoreDebugger(true).anrListener(new AnrWatchDog.AnrListener() {
            @Override
            public void onAnrHappened(String stackTraceInfo) {
                Log.w(TAG, "anr warning:" + stackTraceInfo);
            }
        }).build();
        anrWatchDog.start();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.postDelayed(this, 3000);
            }
        }, 3000);
    }
}
