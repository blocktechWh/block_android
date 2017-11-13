package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.blocktechwh.app.block.R;

/**
 * Created by Administrator on 2017/11/10.
 */

public class StartActivity extends AppCompatActivity {
    private TextView addPlayersButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_vote);
        addPlayersButton=(TextView)findViewById(R.id.textView17);

        addEvent();

//textView17
    }

    private void addEvent(){
        addPlayersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,AddPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
    }
}
