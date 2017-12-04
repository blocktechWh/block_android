package com.blocktechwh.app.block.Activity.RedTicket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/11/14.
 */

public class SendRedTicket extends TitleActivity {
    private Button btn_send;
    private EditText et_amount;
    private EditText et_text_pray;
    private String id;
    private String name;
    private String img;
    private TextView btv_send_name;
    private ImageView id_user_photo;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redticket_send);
        initTitle("发送红包给朋友");
        //获得上个页面传的数据
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getString("id");
        img = bundle.getString("img");
        name = bundle.getString("name");//tv_send_name


        initData();
        addEvent();
    }

    private void initData(){
        btn_send=(Button) findViewById(R.id.button_send);
        et_amount=(EditText) findViewById(R.id.et_amount);
        et_text_pray=(EditText) findViewById(R.id.et_text_pray);
        btv_send_name=(TextView)findViewById(R.id.tv_send_name);
        id_user_photo=(ImageView)findViewById(R.id.id_user_photo);

        btv_send_name.setText(name);
        String url = Urls.HOST + "staticImg" + img;
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                id_user_photo.setImageBitmap(bmp);
            }
        });
    }

    private void addEvent(){
        btn_send.setOnClickListener(sendRedTicket);
    }

    private View.OnClickListener sendRedTicket = new View.OnClickListener(){


        @Override
        public void onClick(View view){
            String s_amount=et_amount.getText().toString();
            String s_pray=et_text_pray.getText().toString();

            JSONObject json = new JSONObject();
            json.put("receiveId",id);
            json.put("amount",s_amount);
            json.put("giftMsg",s_pray);

            System.out.print(json);

            HttpClient.post(this, Urls.SendGift, json.toString(), new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    Toast.makeText(SendRedTicket.this,"发送成功",Toast.LENGTH_SHORT).show();
                }
            });
            startActivity(new Intent(SendRedTicket.this,MainActivity.class));
        }
    };

}
