package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import com.alibaba.fastjson.JSONObject;


public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private EditText et_phone;
    private EditText et_password;
    private TextView tv_more;
    private TextView tv_register;
    private TextView tv_forget;
    private ImageView iv_more;
    private TextView tv_text;
    private RelativeLayout rl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.button2);
        tv_register = (TextView) findViewById(R.id.textView5);
        et_phone = (EditText)findViewById(R.id.id_edit_phone);
        et_password = (EditText)findViewById(R.id.id_edit_password);
        tv_more = (TextView) findViewById(R.id.textView62);
        iv_more = (ImageView) findViewById(R.id.imageView19);
        tv_text = (TextView) findViewById(R.id.textView4);
        rl = (RelativeLayout) findViewById(R.id.relativeLayout);

        btn_login = (Button) findViewById(R.id.id_button_login);
        tv_forget = (TextView) findViewById(R.id.id_text_forget);
        tv_register = (TextView) findViewById(R.id.id_text_register);
        et_phone = (EditText)findViewById(R.id.id_edit_phone);
        et_password = (EditText)findViewById(R.id.id_edit_password);

        btn_login.setOnClickListener(mLoginClick);
        tv_forget.setOnClickListener(toForget);
        tv_register.setOnClickListener(toRegister);
        tv_more.setOnClickListener(getMore);
        iv_more.setOnClickListener(getMore);
    }

    private View.OnClickListener mLoginClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            mLocalLogin();
        }
    };
    private View.OnClickListener toRegister = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener toForget = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this,ForgetActivity.class);
            startActivity(intent);
        }
    };

    private void mLocalLogin(){
        final String phone = et_phone.getText().toString();
        String password = et_password.getText().toString();
        if("".equals(phone)){
            Toast.makeText(App.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        }else if("".equals(password)){
            Toast.makeText(App.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject json = new JSONObject();
            json.put("account",phone);
            json.put("password",password);
            HttpClient.post(this, Urls.Login, json.toString(), new CallBack() {
                @Override
                public void onSuccess(JSONObject data) {
                    PreferencesUtils.putString(App.getContext(),"Phone",phone);
                    App.phone = phone;
                    String token = data.getString("token");
                    System.out.println(token);
                    User user = JSONObject.parseObject(data.getString("userInfo"), User.class);
                    System.out.println(user);
                    IntoMainActivity(token);
                }
            });
        }
    }

    public void IntoMainActivity(String token){
        PreferencesUtils.putString(App.getContext(),"Token",token);
        PreferencesUtils.putBoolean(App.getContext(),"isLogin",true);
        App.token = token;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private View.OnClickListener getMore = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            tv_text.setVisibility(View.VISIBLE);
            rl.setVisibility(View.VISIBLE);
            tv_more.setVisibility(View.GONE);
            iv_more.setVisibility(View.GONE);
        }
    };

}
