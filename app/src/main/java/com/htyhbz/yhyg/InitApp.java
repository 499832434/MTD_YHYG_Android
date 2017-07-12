package com.htyhbz.yhyg;

import android.app.Application;
import android.app.Service;
import android.os.Vibrator;
import com.baidu.mapapi.SDKInitializer;
import com.htyhbz.yhyg.service.LocationService;

/**
 * Created by zongshuo on 2017/7/5.
 */
public class InitApp extends Application {
    public static LocationService locationService;
    public Vibrator mVibrator;
    public static InitApp initApp;
    @Override
    public void onCreate() {
        super.onCreate();
        initApp();
    }



    private void initApp() {
        initApp=this;
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new LocationService(getApplicationContext());
        mVibrator =(Vibrator)getApplicationContext().getSystemService(Service.VIBRATOR_SERVICE);
        SDKInitializer.initialize(getApplicationContext());
    }


}
