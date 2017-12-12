package com.blocktechwh.app.block.Common;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.RedTicket.RedTicketResultActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/12/5.
 */

public class mBroadcastReceiver extends BroadcastReceiver {
    private PopupWindow mPopupWindow;
    private String name="";

    //接收到广播后自动调用该方法
    @Override
    public void onReceive(final Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if(bundle.getString("type").equals("gift")){

            int id = bundle.getInt("id");
            queryGiftDetail(context,id);
        }

        if(bundle.getString("type").equals("contact")){
            int busiId = bundle.getInt("busiId");
            int fromId= bundle.getInt("fromId");
            popupContactRequest(context,busiId,fromId);
        }

    }

    private void queryGiftDetail(final Context context,final int id){
        HttpClient.get(this, Urls.GiftDetail+id, null, new CallBack<JSONObject>() {

            @Override
                public void onSuccess(final JSONObject data) {

                System.out.println("已接收的红包详情="+data);

                //写入接收广播后的操作
                // 弹出对话框
                AlertDialog.Builder alert = new AlertDialog.Builder(context,R.style.AlertDialog);
                View view=View.inflate(context, R.layout.view_redticket_popup,null);
                final AlertDialog alertDialog = alert.create();
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

                FrameLayout fl=new FrameLayout(context);

                ImageView img=new ImageView(context);
                ImageView img1=new ImageView(context);
                TextView tv=new TextView(context);
                tv.setText("来自"+data.getJSONObject("giftSenderInfo").getString("senderName"));
                tv.setGravity(Gravity.CENTER_VERTICAL);
                TextView tv1=new TextView(context);
                tv1.setText(data.getJSONObject("giftSenderInfo").getString("msg"));
//        tv1.setGravity(Gravity.CENTER_VERTICAL);
                tv1.setTextSize(18);


                fl.addView(img);
                fl.addView(img1);
                fl.addView(tv);
                fl.addView(tv1);
                img.setImageResource(R.mipmap.img_gift_poup);
                img1.setImageResource(R.mipmap.icon_open);
                img.setBackgroundColor(Color.TRANSPARENT);
                img1.setX(240);
                img1.setY(250);
                tv.setY(290);
                tv1.setY(330);

                //设定img1尺寸
                ViewGroup.LayoutParams lp = img1.getLayoutParams();
                lp.width=120;
                lp.height=400;

                img1.setLayoutParams(lp);

                alertDialog.setView(fl);
                alertDialog.show();

                WindowManager.LayoutParams params = alertDialog.getWindow().getAttributes();
                params.width = 600;
                params.height = 600 ;
                alertDialog.getWindow().setAttributes(params);

                img.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        HttpClient.post(this, Urls.RecieveGift+id, "", new CallBack<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject data) {
                                //Toast.makeText(context,"您收到红包",Toast.LENGTH_SHORT).show();
                                Bundle bundle=new Bundle();
                                bundle.putInt("id",id);
                                Intent intent= new Intent(context, RedTicketResultActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.putExtras(bundle);
                                context.startActivity(intent );
                                alertDialog.dismiss();
                            }
                        });



                    }
                });
            }
        });
    }

    private void popupContactRequest(final Context context,final int busiId,int fromId){

        //根据id查询详细信息
        HttpClient.get(this, Urls.GetUserInfoById+fromId, null, new CallBack<JSONObject>() {

            @Override
            public void onSuccess(JSONObject data) {
                System.out.println("好友详细信息="+data);
                name=data.getString("name").toString();

                //    通过AlertDialog.Builder这个类来实例化我们的一个AlertDialog的对象
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                final AlertDialog alertDialog = builder.create();
                alertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
                //设置Title的图标
                // builder.setIcon(R.drawable.ic_launcher);

                //设置Title的内容
                alertDialog.setTitle("新消息");

                //设置点击其他区域不可取消
                alertDialog.setCancelable(false);

                //设置Content来显示一个信息
                alertDialog.setMessage(name+"请求加您为好友");

                //设置一个PositiveButton
                alertDialog.setButton("接受", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String url = Urls.AgreeContactRequest + busiId;
                        HttpClient.put(this, url, new JSONObject().toString(), new CallBack() {
                            @Override
                            public void onSuccess(Object result) {
                                Toast.makeText(context, "已添加", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

                //设置一个NegativeButton
                alertDialog.setButton2("忽略", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        dialog.dismiss();
                    }
                });


                alertDialog.show();
            }
        });



    }
}
