package com.blocktechwh.app.block.Common;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Bean.VoteInfo;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

/**
 * Created by eagune on 2017/11/1.
 */
public class App extends Application {

    private static Context context;
    private static String versionName;
    public static String token = "";
    public static String phone = "";
    public static User userInfo;
    public static VoteInfo voteInfo;

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
        if(PreferencesUtils.getBoolean(this,"isLogin",false)){
            token = PreferencesUtils.getString(this,"Token","");
            phone = PreferencesUtils.getString(this,"Phone","");
            userInfo = JSONObject.parseObject(PreferencesUtils.getString(this,"UserInfo",""), User.class);
        }
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

    public static void resetVoteInfo(){
        voteInfo=new VoteInfo();
    }
}
