package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView tv_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.button2);
        tv_register = (TextView) findViewById(R.id.textView5);
        btn_login.setOnClickListener(mLoginClick);
        tv_register.setOnClickListener(toRegister);
    }

    private View.OnClickListener mLoginClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            mLocalLogin();
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };

    private void mLocalLogin(){

    }

    public void IntoMainActivity(String token){
        PreferencesUtils.putString(App.getContext(),"Token",token);
        PreferencesUtils.putBoolean(App.getContext(),"isLogin",true);
        App.token = token;
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }

    private View.OnClickListener toRegister = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent =new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    };

}
