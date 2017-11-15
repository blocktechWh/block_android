package com.blocktechwh.app.block.Activity.Contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by eagune on 2017/11/7.
 */
public class AddNewContactActivity extends TitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        initTitle("添加联系人");
        initView();
    }

    private void initView() {
        ((LinearLayout)findViewById(R.id.id_add_by_phone)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(AddNewContactActivity.this, AddContactByPhoneActivity.class));
            }
        });
    }


    public static class AddContactByPhoneActivity extends AppCompatActivity {

        private EditText textInput;
        private TextView userName_tv;
        private LinearLayout user_layout;
        private ImageView userPhoto_iv;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_contact_by_phone);
            initView();
        }

        private void initView() {
            ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    AddContactByPhoneActivity.this.finish();
                }
            });

            textInput = (EditText)findViewById(R.id.editText);
            textInput.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        queryUser();
                        return true;
                    }
                    return false;
                }
            });

            user_layout = (LinearLayout)findViewById(R.id.id_user_layout);
            user_layout.setVisibility(View.GONE);

            userName_tv = (TextView)findViewById(R.id.id_user_name);
            userPhoto_iv = (ImageView)findViewById(R.id.id_user_photo);
        }

        private void queryUser(){
            String phone = textInput.getText().toString();
            String url = Urls.SearchContact + phone + '/';
            HttpClient.get(this, url, null, new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    setUser(JSONObject.toJavaObject(result,User.class));
                }
            });
        }

        private void setUser(final User user){
            System.out.println(user);
            userName_tv.setText(user.getName());

            String url = Urls.HOST + "staticImg" + user.getImg();
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            userPhoto_iv.setImageBitmap(bmp);
                        }
                    });
                }
            });

            user_layout.setVisibility(View.VISIBLE);
            user_layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(App.getContext(),ContactDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isFriend",false);
                    bundle.putString("name",user.getName());
                    bundle.putString("email",user.getEmail());
                    bundle.putString("phone",user.getPhone());
                    bundle.putString("address",user.getAddress());
                    bundle.putString("sex",user.getSex());
                    bundle.putString("img",user.getImg());
                    bundle.putString("id",user.getId().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }

}

