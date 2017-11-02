package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import org.json.JSONObject;
import org.json.JSONException;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("用户注册");
    }

    public void setTitle(CharSequence title) {
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText(title);
        ((Button)findViewById(R.id.id_button_send)).setOnClickListener(mSendVerifyCode);
    }

    private View.OnClickListener mSendVerifyCode = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            mSendVerifyCode();
//            Intent intent =new Intent(RegisterActivity.this,MainActivity.class);
//            startActivity(intent);
        }
    };

    private void mSendVerifyCode(){
        String url = Urls.ActiveCode+"18565609835";
        HttpClient.get(this, url, null, new CallBack(){
            @Override
            public void onSuccess(JSONObject result){
                try {
                    String code = result.getString("code");
                    System.out.println(code);
                    if(code.equals("000")){
                        System.out.println(result);
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }


}

