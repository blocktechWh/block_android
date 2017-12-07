package com.blocktechwh.app.block.Service;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Contact.ContactRequestActivity;
import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.R;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;


public class NotifyService extends Service {
    private boolean isRun;// 线程是否继续的标志
    private Handler handler1; // 显示当前时间线程消息处理器。
    private Handler handler2;// 推送通知栏消息的线程消息处理器。
    private int notificationCounter;// 一个用于计算通知多少的计数器。
    private static WebSocketClient client;
    private HomeWatcherReceiver mHomeKeyReceiver;
    public static final String SYSTEM_DIALOG_REASON_KEY = "reason";
    public static final String SYSTEM_DIALOG_REASON_HOME_KEY = "homekey";
    private boolean hasPressHome=false;


    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("onCreate");
        mHomeKeyReceiver = new HomeWatcherReceiver();
        final IntentFilter homeFilter = new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS);
        registerReceiver(mHomeKeyReceiver, homeFilter);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mHomeKeyReceiver);
        isRun = false;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        isRun = true;
        final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);// 注册通知管理器
        new Thread(new Runnable() {
            @Override
            // 在Runnable中，如果要让线程自己一直跑下去，必须自己定义while结构
            // 如果这个run()方法读完了，则整个线程自然死亡
            public void run() {
                // 定义一个线程中止标志
//                while (isRun) {
//                    try {
//                        Thread.sleep(5000);// Java中线程的休眠，必须在try-catch结构中
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                    if (!isRun) {
//                        break;
//                    }
//                    Message msg = new Message();
//                    handler2.sendMessage(msg);
//                }
                try {
                    webSocketConnect();
                }
                catch(Exception e) {
                }
            }
        }).start();// 默认线程不启动，必须自己start()

        handler1 = new Handler(new Handler.Callback() {// 这样写，就不弹出什么泄漏的警告了
            @SuppressWarnings("deprecation")
            @Override
            // 这里notification.setLatestEventInfo，
            // 设置通知标题与内容会被eclipse标志过时，
            // 但新的方法，使用builder去设置通知的方法只能应用于android3.0以上的设备，对于android2.2的设备是无法使用的。
            // 在现时国内有部分设备还是在android2.2的情况下，还是用这条几乎兼容所有版本安卓的“过时”方法吧！
            public boolean handleMessage(Message msg) {
                notificationCounter++;// 计数器+1

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                mBuilder.setContentTitle("新消息")//设置通知栏标题
                        .setContentText("您有一个待接收的红包")
                        //	.setNumber(number) //设置通知集合的数量
                        .setTicker("您有一个待接收的红包") //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        //	.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                        .setSmallIcon(R.mipmap.logo);//设置通知小ICON

                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                mBuilder.setContentIntent(pendingIntent);

                notificationManager.notify(notificationCounter,mBuilder.build());
                return false;
            }
        });

        handler2 = new Handler(new Handler.Callback() {// 这样写，就不弹出什么泄漏的警告了
            @SuppressWarnings("deprecation")
            @Override
            // 这里notification.setLatestEventInfo，
            // 设置通知标题与内容会被eclipse标志过时，
            // 但新的方法，使用builder去设置通知的方法只能应用于android3.0以上的设备，对于android2.2的设备是无法使用的。
            // 在现时国内有部分设备还是在android2.2的情况下，还是用这条几乎兼容所有版本安卓的“过时”方法吧！
            public boolean handleMessage(Message msg) {
                notificationCounter++;// 计数器+1

                NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext());
                mBuilder.setContentTitle("新消息")//设置通知栏标题
                        .setContentText("您有一个好友请求")
                        //	.setNumber(number) //设置通知集合的数量
                        .setTicker("您有一个好友请求") //通知首次出现在通知栏，带上升动画效果的
                        .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                        .setPriority(Notification.PRIORITY_DEFAULT) //设置该通知优先级
                        //	.setAutoCancel(true)//设置这个标志当用户单击面板就可以让通知将自动取消
                        .setOngoing(false)//ture，设置他为一个正在进行的通知。他们通常是用来表示一个后台任务,用户积极参与(如播放音乐)或以某种方式正在等待,因此占用设备(如一个文件下载,同步操作,主动网络连接)
                        .setDefaults(Notification.DEFAULT_VIBRATE)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                        //Notification.DEFAULT_ALL  Notification.DEFAULT_SOUND 添加声音 // requires VIBRATE permission
                        .setSmallIcon(R.mipmap.logo);//设置通知小ICON

                Intent intent = new Intent(getApplicationContext(),ContactRequestActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),0,intent,0);
                mBuilder.setContentIntent(pendingIntent);

                notificationManager.notify(notificationCounter,mBuilder.build());
                return false;
            }
        });
        return START_STICKY;// 这个返回值其实并没有什么卵用，除此以外还有START_NOT_STICKY与START_REDELIVER_INTENT
    }



    private void webSocketConnect()throws URISyntaxException, NotYetConnectedException, UnsupportedEncodingException {

        System.out.println("new client.");
        client = new WebSocketClient(new URI("ws://111.231.146.57:20086/ws?token="+ App.token),new Draft_17()) {

            @Override
            public void onOpen(ServerHandshake arg0) {
                System.out.println("打开链接");
            }

            @Override
            public void onMessage(String arg0) {
                System.out.println("收到消息"+arg0);
                JSONObject busiId=JSONObject.parseObject(arg0);
                if(arg0.contains("gift")){

                    if(!isRunningForeground(getApplicationContext())){
                        System.out.println("在后台");
                        Message msg = new Message();
                        handler1.sendMessage(msg);
                    }else{
                        System.out.println("在前台");
                        Intent intent = new Intent();
                        intent.putExtra("id",Integer.parseInt(busiId.getString("busiId")));
                        intent.putExtra("type","gift");
                        //对应BroadcastReceiver中intentFilter的action
                        intent.setAction("BROADCAST_ACTION");
                        //发送广播
                        sendBroadcast(intent);
                    }


                }
                if(arg0.contains("contact")){

                    if(!isRunningForeground(getApplicationContext())){
                        System.out.println("在后台");
                        Message msg = new Message();
                        handler2.sendMessage(msg);
                    }else{
                        System.out.println("在前台");
                        Intent intent = new Intent();
                        intent.putExtra("fromId",Integer.parseInt(busiId.getString("from")));
                        intent.putExtra("busiId",Integer.parseInt(busiId.getString("busiId")));
                        intent.putExtra("type","contact");
                        //对应BroadcastReceiver中intentFilter的action
                        intent.setAction("BROADCAST_ACTION");
                        //发送广播
                        sendBroadcast(intent);
                    }

                }

            }

            @Override
            public void onError(Exception arg0) {
                arg0.printStackTrace();
                System.out.println("发生错误已关闭");
            }

            @Override
            public void onClose(int arg0, String arg1, boolean arg2) {
                System.out.println(client.getURI());
                System.out.println("链接已关闭");
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                try {
                    System.out.println(new String(bytes.array(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }


        };

        client.connect();

//        while(!client.getReadyState().equals(READYSTATE.OPEN)){
//            System.out.println("还没有打开");
//        }
//        System.out.println("打开了");
//        send("hello world".getBytes("utf-8"));
        client.send("hello world");


    }

    class HomeWatcherReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_DIALOG_REASON_KEY);
                if (SYSTEM_DIALOG_REASON_HOME_KEY.equals(reason)) {
                    Toast.makeText(context, "Home按键", Toast.LENGTH_SHORT).show();
                    hasPressHome=true;
                }
            }
        }
    }


    private boolean isRunningForeground (Context context)
    {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        String currentPackageName = cn.getPackageName();
        if(!TextUtils.isEmpty(currentPackageName) && currentPackageName.equals(getPackageName()))
        {
            return true;
        }

        return false;
    }
}

