package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/11/18.
 */

public class VotedDetailActivity extends TitleActivity {
    private TextView tvFillInfo;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voted_detail);
        initTitle("投票列表");

        initData();
        setEvent();
    }

    private void initData() {
        tvFillInfo = (TextView) findViewById(R.id.tvFillInfo);

    }

    private void setEvent(){
        tvFillInfo.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VotedDetailActivity.this,FillInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
    }
}
