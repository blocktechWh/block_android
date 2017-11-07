package com.blocktechwh.app.block.Activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.blocktechwh.app.block.R;

/**
 * Created by eagune on 2017/11/7.
 */
public class AddNewContactActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        initView();
    }

    private void initView() {
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText("添加联系人");
        ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                AddNewContactActivity.this.finish();
            }
        });
    }

}
