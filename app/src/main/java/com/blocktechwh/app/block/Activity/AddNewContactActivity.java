package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by eagune on 2017/11/7.
 */
public class AddNewContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText("添加联系人");
        ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AddNewContactActivity.this.finish();
            }
        });
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
        }

        private void queryUser(){
            String phone = textInput.getText().toString();
            String url = Urls.SearchContact + phone + '/';
            HttpClient.get(this, url, null, new CallBack() {
                @Override
                public void onSuccess(JSONObject result) {
                    setUser(JSONObject.toJavaObject(result,User.class));
                }
            });
        }

        private void setUser(User user){
            userName_tv.setText(user.getName());
            user_layout.setVisibility(View.VISIBLE);
        }

    }

}

