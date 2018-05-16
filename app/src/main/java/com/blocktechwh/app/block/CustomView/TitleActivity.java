package com.blocktechwh.app.block.CustomView;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.R;

/**
 * Created by eagune on 2017/11/8.
 */

public class TitleActivity extends BaseActivity {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState, @Nullable PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
        App.getInstance().addActivity(this);
    }

    public void initTitle(String title){
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText(title);
        ((LinearLayout)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        ((ImageView)findViewById(R.id.iv_titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
