package com.blocktechwh.app.block.Activity.Contact;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.blocktechwh.app.block.Activity.RedTicket.SendRedTicket;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/11/14.
 */

public class ContactDetailForSendActivity extends TitleActivity {
    private Button btn_send;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_sended);
        Toast.makeText(App.getContext(),"被加载了",Toast.LENGTH_SHORT).show();

        initTitle("详细资料");
        initData();
        addEvent();
    }

    private void initData() {
        btn_send = (Button) findViewById(R.id.btn_tosend);
    }

    private void addEvent() {
        btn_send.setOnClickListener(toSendRedTicket);
        Toast.makeText(App.getContext(),"点击事件监听",Toast.LENGTH_SHORT).show();

    }

    private View.OnClickListener toSendRedTicket = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(App.getContext(),"被点击了",Toast.LENGTH_SHORT).show();
            startActivity(new Intent(ContactDetailForSendActivity.this, SendRedTicket.class));
            finish();
        }
    };

}