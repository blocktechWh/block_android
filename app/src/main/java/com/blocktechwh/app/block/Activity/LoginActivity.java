package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;
    private TextView tv_register;
    private EditText editPhone;
    private EditText editPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.button2);
        tv_register = (TextView) findViewById(R.id.textView5);
        editPhone = (EditText)findViewById(R.id.id_edit_phone);
        editPassword = (EditText)findViewById(R.id.id_edit_password);

        btn_login.setOnClickListener(mLoginClick);
        tv_register.setOnClickListener(toRegister);
    }

    private View.OnClickListener mLoginClick = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            mLocalLogin();
        }
    };

    private void mLocalLogin(){
        final String phone = editPhone.getText().toString();
        String password = editPassword.getText().toString();
        if("".equals(phone)){
            Toast.makeText(App.getContext(), "手机号不能为空", Toast.LENGTH_SHORT).show();
        }else if("".equals(password)){
            Toast.makeText(App.getContext(), "密码不能为空", Toast.LENGTH_SHORT).show();
        }else {
            try{
                JSONObject json = new JSONObject();
                json.put("account",phone);
                json.put("password",password);
                HttpClient.post(this, Urls.Login, json.toString(), new CallBack() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        PreferencesUtils.putString(App.getContext(),"Phone",phone);
                        App.phone = phone;
                        try {
                            String token = data.getString("token");
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

    private View.OnClickListener toRegister = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
            startActivity(intent);
        }
    };

}
