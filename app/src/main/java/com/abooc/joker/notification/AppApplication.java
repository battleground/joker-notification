package com.abooc.joker.notification;

import android.app.Application;

import com.abooc.util.Debug;
import com.avos.avoscloud.AVOSCloud;

/**
 * Created by dayu on 2017/4/24.
 */

public class AppApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Debug.debugOn();


//        AVOSCloud.initialize(this, "{{appid}}", "{{appkey}}");
//        AVOSCloud.setDebugLogEnabled(BuildConfig.DEBUG);
//        LeanCloud.subscribePush(this);


    }
}
