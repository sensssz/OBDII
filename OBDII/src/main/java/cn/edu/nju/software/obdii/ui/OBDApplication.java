package cn.edu.nju.software.obdii.ui;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Initialize JPush at the creation of this application
 */
public class OBDApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

//      JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
