package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import org.json.JSONObject;
import org.json.JSONException;

public class RegisterActivity extends AppCompatActivity {

    private EditText editPhone;
    private EditText editCode;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText("用户注册");
        ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                RegisterActivity.this.finish();
            }
        });

        editPhone = (EditText)findViewById(R.id.id_edit_phone);
        editCode = (EditText)findViewById(R.id.id_edit_code);
        editPassword = (EditText)findViewById(R.id.id_edit_password);

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
        String phone = editPhone.getText().toString();
        if("".equals(phone)){
            Toast.makeText(App.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        }else {
            String url = Urls.RegistorActiveCode + phone;
            HttpClient.get(this, url, null, new CallBack() {
                @Override
                public void onSuccess(JSONObject data) {
                    try {
                        String verifyCode = data.getString("data");
                        CharSequence verifyMsg = "您的验证码是：" + verifyCode;
                        Toast.makeText(App.getContext(), verifyMsg, Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void mRegister(){
        final String phone = editPhone.getText().toString();
        String code = editCode.getText().toString();
        String password = editPassword.getText().toString();
        if("".equals(phone)){
            Toast.makeText(App.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        }else if("".equals(code)){
            Toast.makeText(App.getContext(), "验证码不能为空", Toast.LENGTH_SHORT).show();
        }else if("".equals(password)){
            Toast.makeText(App.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject json = new JSONObject();
            try{
                json.put("phone",phone);
                json.put("identifyCode",code);
                json.put("password",password);
                json.put("rePassword",password);
                HttpClient.post(this, Urls.Registor, json.toString(), new CallBack() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        PreferencesUtils.putString(App.getContext(),"Phone",phone);
                        App.phone = phone;
                        try {
                            String token = data.getString("token");
                            Toast.makeText(App.getContext(), "注册成功", Toast.LENGTH_SHORT).show();
                            IntoMainActivity(token);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }catch (JSONException e){
                e.printStackTrace();
            }
        }
    }

    public void IntoMainActivity(String token){
        PreferencesUtils.putString(App.getContext(),"Token",token);
        PreferencesUtils.putBoolean(App.getContext(),"isLogin",true);
        App.token = token;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}

