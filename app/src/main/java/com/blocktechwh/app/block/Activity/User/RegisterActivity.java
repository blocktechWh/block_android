package com.blocktechwh.app.block.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import com.alibaba.fastjson.JSONObject;

public class RegisterActivity extends TitleActivity {

    private EditText editPhone;
    private EditText editCode;
    private EditText id_edit_password;
    private EditText id_edit_password2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initTitle("用户注册");

        App.getInstance().addActivity(this);

        initView();
    }

    private void initView() {
        editPhone = (EditText)findViewById(R.id.id_edit_phone);
        editCode = (EditText)findViewById(R.id.id_edit_code);
        id_edit_password = (EditText)findViewById(R.id.id_edit_password);
        id_edit_password2 = (EditText)findViewById(R.id.id_edit_password2);

        ((Button)findViewById(R.id.id_button_send)).setOnClickListener(mSendVerifyCodeClick);
        ((Button)findViewById(R.id.id_button_submit)).setOnClickListener(mRegisterClick);
    }

    private View.OnClickListener mSendVerifyCodeClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            mSendVerifyCode();
        }
    };

    private View.OnClickListener mRegisterClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            mRegister();
        }
    };

    private void mSendVerifyCode(){
        String phone = editPhone.getText().toString().trim();
        String telRegex = "[1][34578]\\d{9}" ;

        if("".equals(phone)){
            Toast.makeText(App.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        }else if(phone.length()!=11){
            Toast.makeText(App.getContext(),"无效手机号",Toast.LENGTH_SHORT).show();
        }else {
            String url = Urls.RegistorActiveCode + phone;
            HttpClient.get(this, url, null, new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    String verifyCode = data.getString("data");
                    CharSequence verifyMsg = "您的验证码是：" + verifyCode;
                    Toast.makeText(App.getContext(), verifyMsg, Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void mRegister(){
        final String phone = editPhone.getText().toString().trim();
        String code = editCode.getText().toString().trim();
        String password = id_edit_password.getText().toString().trim();
        String password1 = id_edit_password2.getText().toString().trim();

        String telRegex = "[1][34578]\\d{9}" ;
        String tel_regex_pwd = "[a-zA-Z0-9]{8,20}" ;
       //[a-zA-Z0-9]{8,15}

        if("".equals(phone)){
            Toast.makeText(App.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        }else if(phone.length()!=11){
            Toast.makeText(App.getContext(),"无效手机号",Toast.LENGTH_SHORT).show();
        }else if("".equals(code)){
            Toast.makeText(App.getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
        }else if("".equals(password)){
            Toast.makeText(App.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }else if(password.length()<6||password.length()>20){
            Toast.makeText(App.getContext(), "密码长度为6-20位", Toast.LENGTH_SHORT).show();

        }else if(password1.equals("")){
            Toast.makeText(App.getContext(), "请输入确认密码", Toast.LENGTH_SHORT).show();

        }else if(!password1.equals(password)){
            Toast.makeText(App.getContext(), "两次密码输入不一致", Toast.LENGTH_SHORT).show();

        }else {
            JSONObject json = new JSONObject();
            json.put("phone",phone);
            json.put("identifyCode",code);
            json.put("password",password);
            json.put("rePassword",password);
            HttpClient.post(this, Urls.Registor, json.toString(), new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    PreferencesUtils.putString(App.getContext(),"Phone",phone);
                    App.phone = phone;
                    Toast.makeText(App.getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                    IntoMainActivity(data.getString("token"), data.getString("userInfo"));
                }
            });
        }
    }

    public void IntoMainActivity(String token,String user){
        PreferencesUtils.putString(App.getContext(),"Token",token);
        PreferencesUtils.putString(App.getContext(),"UserInfo",user);
        PreferencesUtils.putBoolean(App.getContext(),"isLogin",true);
        App.token = token;
        App.userInfo = JSONObject.parseObject(user, User.class);

        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

