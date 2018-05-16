package com.blocktechwh.app.block.Activity.RedTicket;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.StatusBarUtils;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/12/5.
 */

public class RedTicketResultActivity extends TitleActivity {
    private String senderImg;
    private String senderName;
    private String msg;
    private String giftAmount;
    private int id;

    private TextView tv_gift_amount;
    private TextView tv_sender_name;
    private TextView tv_pray_text;
    private ImageView iv_sender_img;
    private Button btn_recive_sure;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redticket_result);

        App.getInstance().addActivity(this);

        //获得上个页面传的数据
        Bundle bundle = this.getIntent().getExtras();
        id=bundle.getInt("id");

        initView();

        getData();

    }

    private void initView(){
        tv_gift_amount=(TextView) findViewById(R.id.tv_gift_amount);
        tv_sender_name=(TextView) findViewById(R.id.tv_sender_name);
        tv_pray_text=(TextView) findViewById(R.id.tv_pray_text);
        iv_sender_img=(ImageView) findViewById(R.id.iv_sender_img);
        StatusBarUtils.setWindowStatusBarColor(this,R.color.redTicketBackgroundColor);
//        //透明状态栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//        //透明导航栏
//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getData(){
        HttpClient.get(this, Urls.GiftDetail+id, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {

                System.out.println("待接收的红包详情="+data);
                tv_gift_amount.setText(data.getString("giftAmount"));
                tv_sender_name.setText(data.getJSONObject("giftSenderInfo").getString("senderName"));
                tv_pray_text.setText(data.getJSONObject("giftSenderInfo").getString("msg"));

                String url = Urls.HOST + "staticImg" + data.getJSONObject("giftSenderInfo").getString("senderImg");
                HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                    @Override
                    public void onSuccess(final Bitmap bmp) {
                        iv_sender_img.setImageBitmap(bmp);
                    }
                });

            }
        });
    }


}
