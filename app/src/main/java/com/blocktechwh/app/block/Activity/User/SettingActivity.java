package com.blocktechwh.app.block.Activity.User;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

public class SettingActivity extends TitleActivity {

    private LinearLayout logoutButton;
    private LinearLayout updateButton;
    private LinearLayout id_show_versions;
    private LinearLayout id_update_pwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initTitle("设置");
        initView();
    }




    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        //SettingActivity.this.finish();
                        logOut();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }


    private void initView(){

        logoutButton = (LinearLayout)findViewById(R.id.id_logout_button);
        id_update_pwd = (LinearLayout)findViewById(R.id.id_update_pwd);
        id_show_versions = (LinearLayout)findViewById(R.id.id_show_versions);

        updateButton = (LinearLayout)findViewById(R.id.id_update_button);
        logoutButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        id_show_versions.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,VersionsActivity.class));
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,UpdateUserInfoActivity.class));
            }
        });

        id_update_pwd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SettingActivity.this,UpdatePasswardActivity.class));
            }
        });

    }

    private void logOut(){
        String url = Urls.Logout;
        HttpClient.get(this, url, null, new CallBack<JSONObject>() {
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
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

}
