package com.blocktechwh.app.block.Activity.Actions;

import android.os.Bundle;
import android.widget.Switch;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/11/18.
 */

public class SetRewardActivity extends TitleActivity {
    private Switch sIfReward;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rewards);
        initTitle("奖励资金");

        initData();
    }

    private void initData(){
        sIfReward=(Switch) findViewById(R.id.sIfReward);
    }
}
