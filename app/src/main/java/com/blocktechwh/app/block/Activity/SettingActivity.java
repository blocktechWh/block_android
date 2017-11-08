package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import com.alibaba.fastjson.JSONObject;

public class SettingActivity extends TitleActivity {

    private LinearLayout logoutButton;
    private LinearLayout updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initTitle("设置");
        initView();
    }

    private void initView(){

        logoutButton = (LinearLayout)findViewById(R.id.id_logout_button);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                logOut();
            }
        });

        updateButton = (LinearLayout)findViewById(R.id.id_update_button);
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,UpdateUserInfoActivity.class));
            }
        });
    }

    private void logOut(){
        String url = Urls.Logout;
        HttpClient.get(this, url, null, new CallBack() {
            @Override
            public void onSuccess(JSONObject data) {
            }
            @Override
            public void onFailure(int errorType, String message){
            }
        });
        App.token = "";
        PreferencesUtils.putString(App.getContext(),"Token","");
        Intent intent = new Intent(SettingActivity.this,LoginActivity.class);
        startActivity(intent);
    }

}
