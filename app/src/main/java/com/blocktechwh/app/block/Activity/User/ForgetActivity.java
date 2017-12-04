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

public class ForgetActivity extends TitleActivity {

    private EditText et_phone_number;
    private EditText et_yzm;
    private EditText et_pwd;
    private EditText et_pwd1;
    private Button btn_yzm;
    private Button btn_find_commit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initTitle("忘记密码");
        initView();
        addEvent();
    }

    private void initView() {
        et_phone_number=(EditText) findViewById(R.id.et_phone_number);
        et_yzm=(EditText) findViewById(R.id.et_yzm);
        et_pwd=(EditText) findViewById(R.id.et_pwd);
        et_pwd1=(EditText) findViewById(R.id.et_pwd1);
        btn_yzm=(Button) findViewById(R.id.btn_yzm);
        btn_find_commit=(Button) findViewById(R.id.btn_find_commit);
    }

    private void addEvent(){
        //发送验证码
        btn_yzm.setOnClickListener(sendYzm);

        //提交
        btn_find_commit.setOnClickListener(commit);

    }

    private View.OnClickListener sendYzm = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            String phone=et_phone_number.getText().toString();

            if("".equals(phone)){
                Toast.makeText(App.getContext(),"请输入手机号",Toast.LENGTH_SHORT).show();
                return;
            }

            //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
            String telRegex = "[1][34578]\\d{9}" ;
            if (!phone.matches( telRegex )){
                Toast.makeText(App.getContext(),"无效手机号",Toast.LENGTH_SHORT).show();
                return;
            }


            HttpClient.get(this, Urls.ForgetPasswordActiveCode+phone, null, new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    Toast.makeText(App.getContext(),data.toString(),Toast.LENGTH_SHORT).show();

                }
            });

        }
    };
    private View.OnClickListener commit = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            String phone=et_phone_number.getText().toString();
            String code=et_yzm.getText().toString();
            String passWord=et_pwd.getText().toString();
            String passWord1=et_pwd1.getText().toString();

            if(phone.equals("")){
                Toast.makeText(App.getContext(),"请输入手机号",Toast.LENGTH_SHORT).show();
                return;
            }

            //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
            String telRegex = "[1][34578]\\d{9}";
            String tel_regex_pwd = "[a-zA-Z0-9]{8,20}";
            if (!phone.matches( telRegex )){
                Toast.makeText(App.getContext(),"无效手机号",Toast.LENGTH_SHORT).show();
                return;
            }

            if(code.equals("")){
                Toast.makeText(App.getContext(),"请输入验证码",Toast.LENGTH_SHORT).show();
                return;
            }

            if(passWord.equals("")){
                Toast.makeText(App.getContext(),"请输入新密码",Toast.LENGTH_SHORT).show();
                return;
            }
            if(!passWord.matches( tel_regex_pwd )){
                Toast.makeText(App.getContext(),"密码由8-20字母和数字组成",Toast.LENGTH_SHORT).show();
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
            json.put("phone",phone);
            json.put("newPassword",passWord);
            json.put("activeCode",code);

            HttpClient.post(this, Urls.ForgetPassword, json.toString(), new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    Toast.makeText(App.getContext(),"新密码设置成功",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(App.getContext(),LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                }
            });
        }
    };
}
