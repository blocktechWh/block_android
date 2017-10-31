package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blocktechwh.app.block.R;

public class LoginActivity extends AppCompatActivity {
    private TextView tv;
    private Button btn1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        tv = (TextView)findViewById(R.id.textView3);
//        tv.setText(Html.fromHtml("<u>" + "hahaha" + "</u>" ));
        btn1=(Button) findViewById(R.id.button2);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //跳转首页
                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                //启动
               startActivity(intent);
            }
        });
    }
}
