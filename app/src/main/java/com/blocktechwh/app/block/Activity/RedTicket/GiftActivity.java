package com.blocktechwh.app.block.Activity.RedTicket;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;


/**
 * Created by 跳跳蛙 on 2017/11/15.
 */

public class GiftActivity extends TitleActivity {
    private int id;
    private String tv_pray;
    private TextView tv_gift_amount;
    private TextView tv_sender_name;
    private TextView tv_pray_text;
    private ImageView iv_sender_img;
    private Button btn_recive_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sure);
        initTitle("接受朋友红包");
        initData();
        getData();
        addEvent();
    }

    private void initData(){
        //获得上个页面传的数据
        Bundle bundle = this.getIntent().getExtras();
        id = bundle.getInt("id");
        tv_pray = bundle.getString("tv_pray");

        tv_gift_amount=(TextView) findViewById(R.id.tv_gift_amount);
        tv_sender_name=(TextView) findViewById(R.id.tv_sender_name);
        tv_pray_text=(TextView) findViewById(R.id.tv_pray_text);
        iv_sender_img=(ImageView) findViewById(R.id.iv_sender_img);
        btn_recive_sure=(Button) findViewById(R.id.btn_recive_sure);

    }

    private void getData(){
        HttpClient.get(this, Urls.GiftDetail+id, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {

                System.out.print("待接收的红包详情="+data);
                operateGift(data);

            }
        });
    }

    private void operateGift(JSONObject jo){

        tv_gift_amount.setText("￥ "+jo.get("giftAmount").toString());
        tv_sender_name.setText(jo.getJSONObject("giftSenderInfo").getString("senderName"));
        tv_pray_text.setText(tv_pray);
        String url = Urls.HOST + "staticImg" + jo.getJSONObject("giftSenderInfo").getString("senderImg");
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                iv_sender_img.setImageBitmap(bmp);
            }
        });

    }

    private void addEvent(){
        btn_recive_sure.setOnClickListener(reciveSure);

    }

    private View.OnClickListener reciveSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            JSONObject json = new JSONObject();
            json.put("giftId",id);
            HttpClient.post(this, Urls.RecieveGift+id, "", new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    Intent intent= new Intent(GiftActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            });

        }
    };

}
