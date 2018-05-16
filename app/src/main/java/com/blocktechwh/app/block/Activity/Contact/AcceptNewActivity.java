package com.blocktechwh.app.block.Activity.Contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/12/22.
 */

public class AcceptNewActivity extends TitleActivity {
    private String name;
    private String email;
    private String phone;
    private String address;
    private String sex;
    private String img;
    private String id;
    private String userId;
    private Boolean isFriend;
    private Button acceptBtn;
    private int size;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_accept);
        initTitle("待添加好友详情");

        App.getInstance().addActivity(this);

        Bundle bundle = this.getIntent().getExtras();

        id = bundle.getString("id");
        userId = bundle.getString("userId");
        img = bundle.getString("img");
        size = bundle.getInt("size");

        initTitle("详细资料");
        initView();
        getDta();
        setEvent();
    }

    private void initView(){
        acceptBtn = (Button)findViewById(R.id.id_button_accept_contact);


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
            }
        });
    }


    private void setEvent(){
        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAcceptRequest();
            }
        });
    }
    private void getDta(){
        //获取联系人详情
        HttpClient.get(this, Urls.GetUserInfoById+userId, null, new CallBack<JSONObject>() {
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

    private void mAcceptRequest(){
        String url = Urls.AgreeContactRequest + id;
        HttpClient.put(this, url, new JSONObject().toString(), new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(AcceptNewActivity.this,"已同意好友请求",Toast.LENGTH_SHORT).show();
                if(size>0){
                    Intent intent=new Intent(AcceptNewActivity.this,ContactRequestActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                    finish();
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putString("from","AcceptNewActivity");
                    Intent intent= new Intent(AcceptNewActivity.this, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            }
        });
    }
}
