package com.blocktechwh.app.block.Activity;

import android.os.Bundle;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

public class ForgetActivity extends TitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget);

        initTitle("忘记密码");
        initView();
    }

    private void initView() {

    }
}
