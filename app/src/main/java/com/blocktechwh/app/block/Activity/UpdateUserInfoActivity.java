package com.blocktechwh.app.block.Activity;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.InputDialog;
import com.blocktechwh.app.block.R;

public class UpdateUserInfoActivity extends AppCompatActivity {

    private Dialog inputDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        initView();
    }

    private void initView(){
        ((TextView)findViewById(R.id.titlebar_title_tv)).setText("更新用户资料");
        ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                UpdateUserInfoActivity.this.finish();
            }
        });

        inputDialog = new InputDialog(this){
            @Override
            public void handle(String string){
                System.out.println(string);
            }
        };

        ((LinearLayout)findViewById(R.id.id_user_name_btn)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                inputDialog.show();
            }
        });
    }

    private void mInputHandler(){

    }

}
