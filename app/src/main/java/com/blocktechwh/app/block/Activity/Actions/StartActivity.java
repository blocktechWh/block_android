package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blocktechwh.app.block.R;

/**
 * Created by Administrator on 2017/11/10.
 */

public class StartActivity extends AppCompatActivity {

    private String theme;//主题
    private String themeImg;//主题图片
    private boolean isLimit=false;//是否单选
    private boolean isAnonymous;//是否匿名
    private String voteFee;//奖金总额
    private String content;//投票活动描述
    private String expireTime;//投票到期时间

    private TextView tv_to_add;

    private TextView addPlayersButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_vote);
        //addPlayersButton=(TextView)findViewById(R.id.textView17);

        initData();
        addEvent();


    }
    private void initData(){
        LinearLayout ll=(LinearLayout) findViewById(R.id.ll_active_box);
        LayoutInflater inflater = getLayoutInflater();
        View view = inflater.inflate(R.layout.item_vote_active, null);
        ll.addView(view);
        addPlayersButton=(TextView) findViewById(R.id.tv_to_add);
        tv_to_add=(TextView) findViewById(R.id.tv_to_add);

    }

    private void addEvent(){
        addPlayersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,AddPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
        tv_to_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,AddPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });

    }
}
