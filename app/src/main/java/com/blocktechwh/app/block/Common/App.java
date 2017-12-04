package com.blocktechwh.app.block.Common;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Handler;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Bean.VoteInfo;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;

/**
 * Created by eagune on 2017/11/1.
 */
public class App extends Application{

    public static Context context;
    public static String versionName;
    public static String token = "";
    public static String phone = "";
    public static User userInfo;
    public static VoteInfo voteInfo;
    private static WebSocketClient client;
    private Handler mHandler = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context=getApplicationContext();

        setVersionName();
        setPrepare();
        mHandler = new Handler();
        //System.setProperty("jsse.enableSNIExtension", "false");
        System.out.println("versionName : "+versionName);
        try {
            webSocketConnect();
        }
        catch(Exception e) {
        }
    }

    private void setPrepare() {
        if(PreferencesUtils.getBoolean(this,"isLogin",false)){
            token = PreferencesUtils.getString(this,"Token","");
            phone = PreferencesUtils.getString(this,"Phone","");
            userInfo = JSONObject.parseObject(PreferencesUtils.getString(this,"UserInfo",""), User.class);
        }
    }


    private void webSocketConnect()throws URISyntaxException, NotYetConnectedException, UnsupportedEncodingException {

        System.out.println("new client.");
        client = new WebSocketClient(new URI("ws://111.231.146.57:20086/ws?token="+App.token),new Draft_17()) {

            @Override
            public void onOpen(ServerHandshake arg0) {
                System.out.println("打开链接");
            }

            @Override
            public void onMessage(String arg0) {
                System.out.println("收到消息"+arg0);
                if(arg0.contains("gift")){
//                    mHandler.postDelayed(new Runnable() {
//
//                        @Override
//                        public void run() {
//                            WindowUtils.showPopupWindow(App.this);
//
//                        }
//                    }, 1000 * 3);
                    //showPopupWindow(getContext());
                    //queryGifts();
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
    private void queryGifts(){
        //查询红包
        HttpClient.get(this, Urls.GiftWaitRecieveList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {

                System.out.print("待接收的红包列表="+data);
                if(data.size()>0){
                    //operateGiftList(data);
                }

            }
        });
    }
    private void setVersionName() {
        try {
            PackageInfo pi=context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            versionName=pi.versionName;
            System.out.println("versionName="+versionName);
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
