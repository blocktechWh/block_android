package com.blocktechwh.app.block.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/12/1.
 */

public class UpdatePasswardActivity extends TitleActivity {
    private EditText et_new_pwd;
    private EditText et_new_pwd_sure;
    private EditText et_old_pwd;
    private Button btn_update_sure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_password);

        initTitle("修改密码");
        initView();
        addEvent();
    }

    private void initView(){
        et_new_pwd = (EditText)findViewById(R.id.et_new_pwd);
        et_new_pwd_sure = (EditText)findViewById(R.id.et_new_pwd_sure);
        et_old_pwd = (EditText)findViewById(R.id.et_old_pwd);
        btn_update_sure = (Button)findViewById(R.id.btn_update_sure);

    }

    private void addEvent(){

        //提交
        btn_update_sure.setOnClickListener(commit);

    }

    private View.OnClickListener commit = new View.OnClickListener(){
        @Override
        public void onClick(View view){

            String passWord=et_new_pwd.getText().toString();
            String passWord1=et_new_pwd_sure.getText().toString();
            String oldPwd=et_old_pwd.getText().toString();


            if(passWord.equals("")){
                Toast.makeText(App.getContext(),"请输入新密码",Toast.LENGTH_SHORT).show();
                return;
            }
            if(passWord.length()<6||passWord.length()>20){
                Toast.makeText(App.getContext(),"密码由6-20字母数字组成",Toast.LENGTH_SHORT).show();
                return;
            }

            if(passWord1.equals("")){
                Toast.makeText(App.getContext(),"请输入确认密码",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!passWord1.equals(passWord)){
                Toast.makeText(App.getContext(),"两次输入密码不一致",Toast.LENGTH_SHORT).show();
                return;
            }


            JSONObject json = new JSONObject();
            json.put("oldPwd",oldPwd);
            json.put("newPwd",passWord1);
            System.out.println("修改密码提交："+json);
            HttpClient.put(this, Urls.UpdatePassword, json.toString(), new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    Toast.makeText(App.getContext(),"修改成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(UpdatePasswardActivity.this,SettingActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                }
            });
        }
    };
}
