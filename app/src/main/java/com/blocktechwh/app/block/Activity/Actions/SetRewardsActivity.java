package com.blocktechwh.app.block.Activity.Actions;

import android.os.Bundle;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/11/17.
 */

public class SetRewardsActivity extends TitleActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rewards);
        initTitle("奖励资金");

        initData();
    }

    private void initData(){

    }
}
