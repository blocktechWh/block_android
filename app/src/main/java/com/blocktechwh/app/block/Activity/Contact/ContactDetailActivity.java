package com.blocktechwh.app.block.Activity.Contact;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Activity.RedTicket.SendRedTicketActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;


/**
 * Created by eagune on 2017/11/13.
 */

public class ContactDetailActivity extends TitleActivity {

    private String name;
    private String email;
    private String phone;
    private String address;
    private String sex;
    private String img;
    private String id;
    private Boolean isFriend;

    private Button addBtn;
    private Button sendBtn;
    private LinearLayout deleteBtn;
    private Button btn_tosend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        Bundle bundle = this.getIntent().getExtras();

        id = bundle.getString("id");
        isFriend = bundle.getBoolean("isFriend");
        img = bundle.getString("img");

        initTitle("详细资料");

        App.getInstance().addActivity(this);

        initView();
        getDta();
    }

    private void initView(){
        addBtn = (Button)findViewById(R.id.id_button_add_contact);
        deleteBtn = (LinearLayout)findViewById(R.id.id_button_delete_contact);
        sendBtn = (Button)findViewById(R.id.id_button_send_red_ticket);
        if(isFriend){
            addBtn.setVisibility(View.GONE);
            deleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mDeleteFriend();
                }
            });
            sendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    bundle.putString("id",id);
                    bundle.putString("name",name);
                    bundle.putString("img",img);

                    Intent intent= new Intent(ContactDetailActivity.this, SendRedTicketActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }else{
            deleteBtn.setVisibility(View.GONE);
            sendBtn.setVisibility(View.GONE);
            addBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mRequestFriend();
                }
            });
        }


        String url = Urls.HOST + "staticImg" + img;
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                ((ImageView)findViewById(R.id.id_user_photo)).setImageBitmap(bmp);
            }
        });
    }

    private void mRequestFriend(){
        String urls = Urls.RequestContact + id;
        HttpClient.post(this, urls, new JSONObject().toString(), new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(App.getContext(),"已发送",Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("from","ContactDetailActivity");
                Intent intent= new Intent(ContactDetailActivity.this, MainActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private void mDeleteFriend(){

        new AlertDialog.Builder(this).setTitle("确定删除好友吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        //SettingActivity.this.finish();
                        String urls = Urls.DeleteContact + id;
                        System.out.println(urls);

                        HttpClient.delete(this, urls, new JSONObject().toString(), new CallBack() {
                            @Override
                            public void onSuccess(Object result) {
                                Toast.makeText(App.getContext(),"已解除好友关系",Toast.LENGTH_SHORT).show();
                                Bundle bundle = new Bundle();
                                bundle.putString("from","ContactDetailActivity");
                                Intent intent= new Intent(ContactDetailActivity.this, MainActivity.class);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        });

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();

    }

    private void getDta(){
        //获取联系人详情
        HttpClient.get(this, Urls.GetUserInfoById+id, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                System.out.println("联系人详情="+data);
                operateContactDetail(data);
            }
        });
    }

    private void operateContactDetail(JSONObject data){
        ((TextView)findViewById(R.id.id_user_name)).setText(data.getString("name"));
        ((TextView)findViewById(R.id.id_text_email)).setText(data.getString("email"));
        ((TextView)findViewById(R.id.id_text_phone)).setText(data.getString("phone"));
        ((TextView)findViewById(R.id.id_user_address)).setText(data.getString("address"));
        ((TextView)findViewById(R.id.id_text_sex)).setText(data.getInteger("sex")==1?"男":"女");

    }

}
