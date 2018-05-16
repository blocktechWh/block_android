package com.blocktechwh.app.block.Activity.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;


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
        App.getInstance().addActivity(this);

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
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                App.getInstance().exit();
                break;

            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
        }
        return super.onKeyDown(keyCode,event);
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
        final String phone = et_phone.getText().toString().trim();
        String password = et_password.getText().toString().trim();
        if("".equals(phone)){
            Toast.makeText(App.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        }else if("".equals(password)){
            Toast.makeText(App.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            JSONObject json = new JSONObject();
            json.put("account",phone);
            json.put("password",password);
            HttpClient.post(this, Urls.Login, json.toString(), new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    PreferencesUtils.putString(App.getContext(),"Phone",phone);
                    System.out.println("loginCallBack="+data);
                    //PreferencesUtils.putString(App.getContext(),"userId",data.getJSONObject("userInfo").getString("id"));
                    App.phone = phone;
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
