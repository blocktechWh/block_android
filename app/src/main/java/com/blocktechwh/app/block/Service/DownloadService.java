package com.blocktechwh.app.block.Service;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;
import android.view.WindowManager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.R;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.FileCallBack;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import okhttp3.Call;

/**
 * 自动下载更新apk服务
 * Create by: zheng.zhang
 * Date: 2017-12-07
 * time: 09:50
 * Email: lichenwei.me@foxmail.com
 */
public class DownloadService extends Service {

    private String mDownloadUrl;//APK的下载路径
    private String jsonDownloadUrl;//版本信息的下载路径
    private NotificationManager mNotificationManager;
    private Notification mNotification;
    private String versionName;

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("打开下载更新服务");
        mNotificationManager = (NotificationManager) getSystemService(Service.NOTIFICATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (intent == null) {
//            notifyMsg("温馨提醒", "文件下载失败", 0);
//            stopSelf();
//        }
        mDownloadUrl = intent.getStringExtra("apkUrl");//获取下载APK的链接
        jsonDownloadUrl = intent.getStringExtra("jsonUrl");//获取下载APK的链接
        System.out.println("mDownloadUrl="+mDownloadUrl);

        downloadJsonFile(jsonDownloadUrl);//下载json
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
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
        App.versionName=versionName;
        return pendingIntent;
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

                stopSelf();
            }

            @Override
            public void onResponse(File response, int id) {
                //当文件下载完成后回调
                notifyMsg("温馨提醒", "文件下载已完成，点击安装", 100);

                stopSelf();
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
    private void downloadJsonFile(String url) {

        OkHttpUtils.get().url(url).build().execute(new FileCallBack(Environment.getExternalStorageDirectory().getAbsolutePath(), "block_android") {
            @Override
            public void onError(Call call, Exception e, int id) {
                stopSelf();
            }

            @Override
            public void onResponse(File response, int id) {
                //当文件下载完成后回调
                BufferedReader br;
                try {
                    br = new BufferedReader(new FileReader(response));
                    String line = br.readLine();
                    StringBuffer sb = new StringBuffer();
                    while (null != line) {
                        sb.append(line);
                        line = br.readLine();
                    }

                    sb.toString();

                    //版本检查
                    JSONObject jsonObject=JSON.parseObject(sb.toString());

                    versionName=jsonObject.getString("version");
                    App.newVersionName=versionName;

                    if(!versionName.equals(App.versionName)){
                        //
                        //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
                        final AlertDialog alertDialog = builder.create();
                        alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                        //设置Title的图标
                        // builder.setIcon(R.drawable.ic_launcher);

                        //设置Title的内容
                        alertDialog.setTitle("新消息");

                        //设置点击其他区域不可取消
                        alertDialog.setCancelable(false);

                        //设置Content来显示一个信息
                        alertDialog.setMessage("系统检测到一个最新版本("+versionName+")，是否更新?"+"\n\n"+"若更新，请在通知栏查看下载情况");

                        //设置一个PositiveButton
                        alertDialog.setButton("现在更新", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                downloadFile(mDownloadUrl);//下载APK
                            }
                        });

                        //设置一个NegativeButton
                        alertDialog.setButton2("下次再说", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                dialog.dismiss();
                            }
                        });


                        alertDialog.show();

                    }
                } catch (Exception e) {

                }finally {

                }

//                JSONObject jsonObject = new JSONObject(response);
//                String id=response.getS

                stopSelf();

            }

            @Override
            public void inProgress(float progress, long total, int id) {

            }
        });
    }
}
