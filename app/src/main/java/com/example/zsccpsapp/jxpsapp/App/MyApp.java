package com.example.zsccpsapp.jxpsapp.App;

import android.app.Activity;
import android.app.Application;

import com.baidu.mapapi.SDKInitializer;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by 99zan on 2018/2/6.
 */

public class MyApp extends Application {

    private List<Activity> mList = new LinkedList<Activity>();
    String uid;
    private static MyApp instance;

    public MyApp() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(this);
    }

    public synchronized static MyApp getInstance() {
        if (null == instance) {
            instance = new MyApp();
        }
        return instance;
    }
    // add Activity
    public void addActivity(Activity activity) {
        mList.add(activity);
    }

    public void exit() {
        try {
            for (Activity activity : mList) {
                if (activity != null)
                    activity.finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.exit(0);
        }
    }
    public void onLowMemory() {
        super.onLowMemory();
        System.gc();
    }
}
