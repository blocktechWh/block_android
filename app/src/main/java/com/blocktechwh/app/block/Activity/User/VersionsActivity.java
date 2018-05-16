package com.blocktechwh.app.block.Activity.User;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.File;

import okhttp3.Call;

/**
 * Created by 跳跳蛙 on 2017/11/25.
 */

public class VersionsActivity extends TitleActivity {
    private TextView tv_version_name;
    private TextView tv_versions_info;
    private Button btn_download;
    private Notification mNotification;
    private NotificationManager mNotificationManager;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versions);
        initTitle("版本信息");

        App.getInstance().addActivity(this);

        initView();
        setEvent();
    }


    private void initView(){
        mNotificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
        tv_version_name=(TextView) findViewById(R.id.tv_version_name);
        tv_versions_info=(TextView) findViewById(R.id.tv_versions_info);
        btn_download=(Button) findViewById(R.id.btn_download);
        tv_version_name.setText("部落客"+App.versionName);

        if(App.versionName.equals(App.newVersionName)){
            tv_versions_info.setText("该版本已经是最新版本");

        }else{
            tv_versions_info.setText("系统检测到一个最新版本("+App.newVersionName+")");
            btn_download.setVisibility(View.VISIBLE);
        }
    }

    private void setEvent(){
        btn_download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(VersionsActivity.this,"在手机通知栏查看下载",Toast.LENGTH_SHORT).show();
                downloadFile("http://blocktechwh.com/bk.apk");
            }
        });
    }


    /**
     * 下载apk文件
     *
     * @param url
     */
    private void downloadFile(String url) {

        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "block_android") {
            @Override
            public void onError(Call call, Exception e, int id) {
                notifyMsg("温馨提醒", "文件下载失败，请尝试重新下载", 0);

            }

            @Override
            public void onResponse(File response, int id) {
                //当文件下载完成后回调
                notifyMsg("温馨提醒", "文件下载已完成，点击安装", 100);

            }

            @Override
            public void inProgress(float progress, long total, int id) {
                //progress*100为当前文件下载进度，total为文件大小
                if ((int) (progress * 100) % 10 == 0) {
                    //避免频繁刷新View，这里设置每下载10%提醒更新一次进度
                    notifyMsg("温馨提醒", "文件正在下载..", (int) (progress * 100));
                }
            }
        });
    }

    private void notifyMsg(String title, String content, int progress) {

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);//为了向下兼容，这里采用了v7包下的NotificationCompat来构造
        builder.setSmallIcon(R.mipmap.logo).setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.logo)).setContentTitle(title);
        if (progress > 0 && progress < 100) {
            //下载进行中
            builder.setProgress(100, progress, false);
        } else {
            builder.setProgress(0, 0, false);
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        builder.setContentText(content);
        if (progress >= 100) {
            //下载完成
            builder.setContentIntent(getInstallIntent());
        }
        mNotification = builder.build();
        mNotificationManager.notify(0, mNotification);

    }

    /**
     * 安装apk文件
     *
     * @return
     */
    private PendingIntent getInstallIntent() {
        File file = new File("//sdcard//"+"block_android");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setDataAndType(Uri.parse("file://" + file.getAbsolutePath()), "application/vnd.android.package-archive");
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        App.versionName=App.newVersionName;
        return pendingIntent;
    }
}
