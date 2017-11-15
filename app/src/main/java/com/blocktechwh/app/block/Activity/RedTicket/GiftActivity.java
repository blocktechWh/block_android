package com.blocktechwh.app.block.Activity.RedTicket;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/11/15.
 */

public class GiftActivity extends TitleActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_sure);
        initTitle("");

    }
}
