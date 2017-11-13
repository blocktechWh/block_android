package com.blocktechwh.app.block.CustomView;

import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blocktechwh.app.block.R;

/**
 * Created by eagune on 2017/11/8.
 */

public class TitleActivity extends BaseActivity {

    public void initTitle(String title){
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText(title);
        ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

}
