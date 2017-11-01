package com.blocktechwh.app.block.Common;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by eagune on 2017/11/1.
 */
public class App extends Application {

    private static Context context;
    private static String versionName;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        setVersionName();
        setPrepare();
        //System.setProperty("jsse.enableSNIExtension", "false");
        System.out.println("versionName : "+versionName);
    }

    private void setPrepare() {
//        if(PreferencesUtils.getBoolean(this,"isLogin",false)){
//            token=PreferencesUtils.getString(this,"Token","");
//            System.out.println("token = " + token);
//        }
    }

    private void setVersionName() {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName=pi.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            versionName="";
        }
    }

    public static Context getContext() {
        return context;
    }
}
