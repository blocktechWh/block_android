package com.blocktechwh.app.block.Activity.Wallete;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/12/2.
 */

public class ImpotActivity extends TitleActivity {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);

        initTitle("导入");
    }
}
