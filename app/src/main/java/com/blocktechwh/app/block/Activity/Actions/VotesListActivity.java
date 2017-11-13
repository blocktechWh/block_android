package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by Administrator on 2017/11/10.
 */

public class VotesListActivity extends TitleActivity {
    private Button createVoteBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_list);
        createVoteBtn=(Button) findViewById(R.id.start_vote);
        addEvent();
    }


    private void addEvent(){

        createVoteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VotesListActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
    }
}

