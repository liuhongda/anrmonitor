package diyview.lhd.com.diyview;

import android.app.Application;
import android.os.Looper;

import com.github.moduth.blockcanary.BlockCanary;
import com.github.moduth.blockcanary.BlockCanaryContext;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by liuhongda on 2017/9/8.
 */

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
//        BlockCanary.install(this, new BlockCanaryContext()).start();

    }
}
