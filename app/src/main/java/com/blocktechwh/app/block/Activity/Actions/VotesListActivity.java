package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/10.
 */

public class VotesListActivity extends TitleActivity {
    private Button createVoteBtn;
    private RecyclerView mRecyclerView;
    private VotesListAdapter mAdapter;
    private List<Map<String,Object>> mDatas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_list);
        initTitle("参与人员");
        initView();
        createVoteBtn=(Button) findViewById(R.id.start_vote);
        addEvent();
    }



    private void initView(){

        mDatas = new ArrayList<Map<String,Object>>();
        for (int i = 'A'; i < 'G'; i++)
        {
            Map<String,Object> hm=new HashMap<String, Object>();
            hm.put("text_action_title","周末洗厕所比赛");
            hm.put("text_action_description1","王二洗厕所");
            hm.put("text_action_description2","王勤洗厕所");
            hm.put("text_action_description3","胡义晋洗厕所");
            hm.put("text_action_state","投票中/可加注");
            hm.put("text_no1_name","王勤");
            hm.put("text_no2_name","王二");
            hm.put("id_img",R.mipmap.image_test);
            mDatas.add(hm);
        }
        mRecyclerView = (RecyclerView)findViewById(R.id.id_votes_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new VotesListAdapter());

    }

    class VotesListAdapter extends RecyclerView.Adapter<VotesListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.votes_listitem, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            holder.tv_title.setText(mDatas.get(position).get("text_action_title").toString());
            holder.tv_des1.setText(mDatas.get(position).get("text_action_description1").toString());
            holder.tv_des2.setText(mDatas.get(position).get("text_action_description2").toString());
            holder.tv_des3.setText(mDatas.get(position).get("text_action_description3").toString());
            holder.tv_state.setText(mDatas.get(position).get("text_action_state").toString());
            holder.tv_no1.setText(mDatas.get(position).get("text_no1_name").toString());
            holder.tv_no2.setText(mDatas.get(position).get("text_no2_name").toString());

            holder.image_layout.setBackgroundResource(Integer.parseInt(mDatas.get(position).get("id_img").toString()));

        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_title,tv_des1,tv_des2,tv_des3,tv_state,tv_no1,tv_no2;

            LinearLayout image_layout;

            public MyViewHolder(View view)
            {
                super(view);
                tv_title = (TextView) view.findViewById(R.id.textView70);
                tv_des1 = (TextView) view.findViewById(R.id.textView53);
                tv_des2 = (TextView) view.findViewById(R.id.textView52);
                tv_des3 = (TextView) view.findViewById(R.id.textView50);
                tv_no1 = (TextView) view.findViewById(R.id.textView89);
                tv_no2 = (TextView) view.findViewById(R.id.textView88);
                tv_state = (TextView) view.findViewById(R.id.textView80);

                image_layout=(LinearLayout) view.findViewById(R.id.image_layout);

            }
        }
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

