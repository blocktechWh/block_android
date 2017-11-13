package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by Administrator on 2017/11/11.
 */

public class AddPlayerActivity extends TitleActivity {
    private Button addSure;
    private TextView addPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_players);

        addSure=(Button) findViewById(R.id.button19);
        addPlayer=(TextView) findViewById(R.id.textView24);

        addEvent();


    }

    private void addEvent(){
        addSure.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPlayerActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });

        addPlayer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(AddPlayerActivity.this,VotersSelectListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
    }
    //button19
}
