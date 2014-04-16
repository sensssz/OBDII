package cn.edu.nju.software.obdii.ui;

import android.app.Application;

import com.baidu.mapapi.BMapManager;

import cn.jpush.android.api.JPushInterface;

/**
 * Initialize JPush at the creation of this application
 */
public class OBDApplication extends Application {
    private BMapManager mMapManager;

    @Override
    public void onCreate() {
        super.onCreate();

//      JPushInterface.setDebugMode(true);
        JPushInterface.init(this);

        mMapManager = new BMapManager(this);
        mMapManager.init(null);
    }

    public BMapManager getMapManager() {
        return mMapManager;
    }
}
