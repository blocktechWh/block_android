package com.blocktechwh.app.block.Activity.User;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/11/25.
 */

public class VersionsActivity extends TitleActivity {
    private TextView tv_version_name;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_versions);
        initTitle("版本信息");
        initView();

        setEvent();
    }


    private void initView(){
        tv_version_name=(TextView) findViewById(R.id.tv_version_name);
        tv_version_name.setText("部落客"+App.versionName);
    }

    private void setEvent(){
        tv_version_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }
}
