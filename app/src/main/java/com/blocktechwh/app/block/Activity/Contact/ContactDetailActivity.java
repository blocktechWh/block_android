package com.blocktechwh.app.block.Activity.Contact;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/11/14.
 */

public class ContactDetailActivity extends TitleActivity {
    private Button btn_send;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_sended);
        initTitle("详细资料");
        initData();
        addEvent();
    }

    private void initData(){
        btn_send=(Button) findViewById(R.id.button10);
    }
    private void addEvent(){
        btn_send.setOnClickListener(toSendRedTicket);
    }

    private View.OnClickListener toSendRedTicket = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            startActivity(new Intent(ContactDetailActivity.this, ContactDetailActivity.class));
        }
    };
}
