package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/10.
 */

public class VotesListActivity extends TitleActivity {
    private Button createVoteBtn;
    private RecyclerView mRecyclerView;
    private VotesListAdapter mAdapter;
    private List<Map<String,Object>> mDatas=new ArrayList<>();
    private JSONArray totalVoteList=new JSONArray();
    private ListView ll_option_view;
    private int voteId=2;
    private boolean isRaise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_list);
        initTitle("投票列表");
        getData();
        initView();
        createVoteBtn=(Button) findViewById(R.id.start_vote);
        addEvent();

    }



    private void initView(){

//          mDatas = new ArrayList<Map<String,Object>>();
//        for (int i = 0; i < totalVoteList.size(); i++)
//        {
//            Map<String,Object> hm=new HashMap<String, Object>();
//            hm.put("text_action_title",totalVoteList.getJSONObject(i).getString("theme"));
//            hm.put("text_action_description1",totalVoteList.getJSONObject(i).getString("theme"));
//            List<Map<String,Object>>optionList=new ArrayList<>();
//            for(int j=0;j<totalVoteList.getJSONObject(i).getString("options").length();j++){
//                Map<String,Object>map_option=new HashMap<>();
//                List list= Arrays.asList(totalVoteList.getJSONObject(i).getString("options"));
//                map_option.put("text_action_description",list.get(j).toString().substring(8,13));
//                optionList.add(map_option);
//            }
//            hm.put("text_action_description2","王勤洗厕所");
//            hm.put("text_action_description3","胡义晋洗厕所");
//            hm.put("text_action_state","投票中/可加注");
//            hm.put("text_no1_name","王勤");
//            hm.put("text_no2_name","王二");
//            hm.put("id_img",R.mipmap.image_test);
//            mDatas.add(hm);
//        }
        //        for (int i = 0; i < totalVoteList.size(); i++)
//        for (int i = 0; i < 5; i++)
//        {
//            Map<String,Object> hm=new HashMap<String, Object>();
//            hm.put("text_action_title","周末洗厕所比赛");
//            hm.put("text_action_description1","王二洗厕所");
//            hm.put("text_action_description2","王勤洗厕所");
//            hm.put("text_action_description3","胡义晋洗厕所");
//            hm.put("text_action_state","投票中/可加注");
//            hm.put("text_no1_name","王勤");
//            hm.put("text_no2_name","王二");
//            hm.put("id_img",R.mipmap.image_test);
//            mDatas.add(hm);
//        }
       // ll_option_view=(ListView) findViewById(R.id.ll_option_view);
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
        public void onBindViewHolder(final MyViewHolder holder, int position){
            holder.tv_title.setText(totalVoteList.getJSONObject(position).getString("theme"));


            if(totalVoteList.getJSONObject(position).getBoolean("isOver")){
                holder.tv_state.setText("已结束");
                holder.ll_opton_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("voteId",voteId);
                        Intent intent= new Intent(VotesListActivity.this, VotedDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }else if(totalVoteList.getJSONObject(position).getBoolean("isRaise")){
                holder.tv_state.setText("投票中/可加注");
                isRaise=true;
                holder.ll_opton_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("voteId",voteId);
                        Intent intent= new Intent(VotesListActivity.this, VoteDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }else{
                holder.tv_state.setText("投票中");
                isRaise=false;
                holder.ll_opton_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Bundle bundle = new Bundle();
                        bundle.putInt("voteId",voteId);
                        Intent intent= new Intent(VotesListActivity.this, VoteDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });

            }

            holder.tv_no1.setText(totalVoteList.getJSONObject(position).getJSONArray("currentSupplier").getJSONObject(0).getString("name").toString());//currentSupplier
            holder.tv_no2.setText(totalVoteList.getJSONObject(position).getJSONArray("currentSupplier").getJSONObject(1).getString("name").toString());


            String url = Urls.HOST + "staticImg" + totalVoteList.getJSONObject(position).getString("img");
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.image_layout.setImageBitmap(bmp);
                }
            });

        }

        @Override
        public int getItemCount(){
            return totalVoteList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_title,tv_state,tv_no1,tv_no2;
            ImageView image_layout;
            LinearLayout ll_opton_button;

            public MyViewHolder(View view)
            {
                super(view);
                tv_title = (TextView) view.findViewById(R.id.tv_title);

                tv_no1 = (TextView) view.findViewById(R.id.textView89);
                tv_no2 = (TextView) view.findViewById(R.id.textView88);
                tv_state = (TextView) view.findViewById(R.id.textView80);
                ll_opton_button= (LinearLayout) view.findViewById(R.id.ll_opton_button);
                image_layout=(ImageView) view.findViewById(R.id.image_layout);

            }
        }
    }
    private void addEvent(){

        createVoteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //App.resetVoteInfo();
                Intent intent = new Intent(VotesListActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
    }

    private void getData(){
        //查询投票列表
        HttpClient.get(this, Urls.QueryTotalVotesList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                totalVoteList=data;
                mAdapter.notifyDataSetChanged();
                //Toast.makeText(VotesListActivity.this,totalVoteList.getJSONObject(1).getJSONArray("options").toArray().length, Toast.LENGTH_LONG).show();

                //Toast.makeText(VotesListActivity.this,totalVoteList.toJSONString(),Toast.LENGTH_LONG).show();
            }
        });
    }

}

