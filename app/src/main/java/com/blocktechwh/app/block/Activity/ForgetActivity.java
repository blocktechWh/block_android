package com.blocktechwh.app.block.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blocktechwh.app.block.R;

public class ForgetActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText("忘记密码");
        ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                ForgetActivity.this.finish();
            }
        });
    }
}
