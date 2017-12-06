package com.lhd.anrmonitor;

import android.annotation.TargetApi;
import android.os.Build;
import android.os.Debug;
import android.os.Handler;
import android.os.Looper;
import android.os.Process;
import android.os.SystemClock;
import android.util.Log;

import java.lang.reflect.Method;

/**
 * Created by lhd on 2017/11/23.
 */

public class AnrWatchDog extends Thread {

    private static final String TAG = "ANRWatchDog";

    private int timeout = 5000;

    private boolean ignoreDebugger;

    private Object cpuTrackerObj;

    private Method updateMethod;

    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private class AnrChecker implements Runnable {

        private boolean mCompleted;

        private long mStartTime;

        private long executeTime = SystemClock.uptimeMillis();

        @Override
        public void run() {
            synchronized (AnrWatchDog.this) {
                mCompleted = true;
                executeTime = SystemClock.uptimeMillis();
            }
        }

        void schedule() {
            mCompleted = false;
            mStartTime = SystemClock.uptimeMillis();
            mainHandler.postAtFrontOfQueue(this);
        }

        boolean isBlocked() {
            return !mCompleted || executeTime - mStartTime >= 5000;
        }
    }

    public interface AnrListener {
        void onAnrHappened(String stackTraceInfo);
    }

    private AnrChecker anrChecker = new AnrChecker();

    private AnrListener anrListener;

    private AnrWatchDog(Builder builder) {
        super("ANR-WatchDog-Thread");
        this.timeout = builder.timeout;
        this.ignoreDebugger = builder.ignoreDebugger;
        this.anrListener = builder.anrListener;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void run() {
        Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND); // 后台线程
        while (!isInterrupted()) {
            synchronized (this) {
                anrChecker.schedule();
                long waitTime = timeout;
                long start = SystemClock.uptimeMillis();
                while (waitTime > 0) {
                    try {
                        wait(waitTime);
                    } catch (InterruptedException e) {
                        Log.w(TAG, e.toString());
                    }
                    waitTime = timeout - (SystemClock.uptimeMillis() - start);
                }
                if (!anrChecker.isBlocked()) {
                    continue;
                }
            }
            if (!ignoreDebugger && Debug.isDebuggerConnected()) {
                continue;
            }
//            String cpuInfo = getCpuInfo();
            String stackTraceInfo = getStackTraceInfo();
            if (anrListener != null) {
                anrListener.onAnrHappened(stackTraceInfo);
            }
        }
    }

    private String getCpuInfo() {
        String cpuInfo = "";
        try {
            if (cpuTrackerObj == null) {
                Class<?> cls = Class.forName("com.android.internal.os.ProcessCpuTracker");
                cpuTrackerObj = cls.getConstructor(boolean.class).newInstance(false);
                Method initMethod = cpuTrackerObj.getClass().getMethod("init");
                initMethod.invoke(cpuTrackerObj);
                updateMethod = cpuTrackerObj.getClass().getMethod("update");
            } else {
                updateMethod.invoke(cpuTrackerObj);
            }
            synchronized (cpuTrackerObj) {
                cpuTrackerObj.wait(500);
            }
            updateMethod.invoke(cpuTrackerObj);
            Method printCurrentStateMethod = cpuTrackerObj.getClass().getMethod("printCurrentState", long.class);
            cpuInfo = (String) printCurrentStateMethod.invoke(cpuTrackerObj, SystemClock.uptimeMillis());
            /*Method printCurrentLoadMethod = cpuTrackerObj.getClass().getMethod("printCurrentLoad");
            String currentLoadInfo = (String) printCurrentLoadMethod.invoke(cpuTrackerObj);
            Method getTotalCpuPercentMethod = cpuTrackerObj.getClass().getMethod("getTotalCpuPercent");
            float percent = (float) getTotalCpuPercentMethod.invoke(cpuTrackerObj);
            LogUtil.i("currentLoadInfo:" + currentLoadInfo);
            LogUtil.i("percent:" + percent);*/
        } catch (Exception e) {
            Log.e(TAG, Log.getStackTraceString(e));
        }
        return cpuInfo;
    }

    private String getStackTraceInfo() {
        StringBuilder stringBuilder = new StringBuilder();
        for (StackTraceElement stackTraceElement : Looper.getMainLooper().getThread().getStackTrace()) {
            stringBuilder
                    .append(stackTraceElement.toString())
                    .append("\r\n");
        }
        return stringBuilder.toString();
    }

    public static class Builder {
        private boolean ignoreDebugger;
        private int timeout;
        private AnrListener anrListener;

        public Builder timeout(int timeout) {
            this.timeout = timeout;
            return this;
        }

        public Builder ignoreDebugger(boolean ignoreDebugger) {
            this.ignoreDebugger = ignoreDebugger;
            return this;
        }

        public Builder anrListener(AnrListener anrListener) {
            this.anrListener = anrListener;
            return this;
        }

        public AnrWatchDog build() {
            return new AnrWatchDog(this);
        }
    }
}


