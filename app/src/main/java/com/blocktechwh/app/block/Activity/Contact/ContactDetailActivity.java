package com.blocktechwh.app.block.Activity.Contact;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_info);

        Bundle bundle = this.getIntent().getExtras();

        name = bundle.getString("name");
        email = bundle.getString("email");
        phone = bundle.getString("phone");
        address = bundle.getString("address");
        sex = bundle.getString("sex");
        img = bundle.getString("img");

        initTitle("详细资料");
        initView();
    }

    private void initView(){
        ((TextView)findViewById(R.id.id_user_name)).setText(name);
        ((TextView)findViewById(R.id.id_text_email)).setText(email);
        ((TextView)findViewById(R.id.id_text_phone)).setText(phone);
        ((TextView)findViewById(R.id.id_user_address)).setText(address);
        ((TextView)findViewById(R.id.id_text_sex)).setText(sex);

        String url = Urls.HOST + "staticImg" + img;
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ((ImageView)findViewById(R.id.id_user_photo)).setImageBitmap(bmp);
                    }
                });
            }
        });
    }

}
