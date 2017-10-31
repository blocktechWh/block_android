package com.blocktechwh.app.block.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blocktechwh.app.block.R;

public class LoginActivity extends AppCompatActivity {

    private Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btn_login = (Button) findViewById(R.id.button2);
        btn_login.setOnClickListener(mLocalLogin);
    }

    private View.OnClickListener mLocalLogin = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            Intent intent =new Intent(LoginActivity.this,MainActivity.class);
            startActivity(intent);
        }
    };

}
