package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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
    private LinearLayout titlebar_button_back;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:

                goBack();
                break;

            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void goBack(){
        clearVoteInfo();
        setResult(10);
        finish();
    }

    private void initView(){
        titlebar_button_back=(LinearLayout)findViewById(R.id.titlebar_button_back);

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
        public void onBindViewHolder(final MyViewHolder holder,final int position){
            holder.tv_title.setText(totalVoteList.getJSONObject(position).getString("theme"));
            final int voteId=totalVoteList.getJSONObject(position).getInteger("voteId");
            holder.tv_no1.setText(totalVoteList.getJSONObject(position).getJSONArray("currentSupplier").getJSONObject(0).getString("name").toString());//currentSupplier
            holder.tv_no2.setText(totalVoteList.getJSONObject(position).getJSONArray("currentSupplier").getJSONObject(1).getString("name").toString());


            String url = Urls.HOST + "staticImg" + totalVoteList.getJSONObject(position).getString("img");
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.image_layout.setImageBitmap(bmp);
                }
            });

            if(totalVoteList.getJSONObject(position).getBoolean("isOver")){
                isRaise=false;
                holder.ll_opton_button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        holder.tv_state.setText("已结束");
                        Bundle bundle = new Bundle();
                        bundle.putInt("voteId",voteId);
                        bundle.putBoolean("isOver",true);
                        Intent intent= new Intent(VotesListActivity.this, VotedDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
                return;

            }else if(totalVoteList.getJSONObject(position).getBoolean("isRaise")){
                holder.tv_state.setText("投票中/可加注");
                isRaise=true;
            }else{
                holder.tv_state.setText("投票中");
                isRaise=false;
            }
            holder.ll_opton_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    hasVoted(voteId);
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

    private void hasVoted(final int voteId){
        //查询投票是否已投
        HttpClient.get(this, Urls.QueryHasVoted+voteId, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                System.out.print("是否已投返回值="+data);
                if(data.getString("data")=="false"){
                    Bundle bundle = new Bundle();
                    bundle.putInt("voteId",voteId);
                    Intent intent= new Intent(VotesListActivity.this, VoteDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }else{
                    Bundle bundle = new Bundle();
                    bundle.putInt("voteId",voteId);
                    Intent intent= new Intent(VotesListActivity.this, VotedDetailActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                }


            }
        });
    }
    private void addEvent(){
        //返回
        titlebar_button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        //跳转发起投票页
        createVoteBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                clearVoteInfo();
                Intent intent = new Intent(VotesListActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getData(){
        //查询投票列表
        HttpClient.get(this, Urls.QueryTotalVotesList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                totalVoteList=data;
                System.out.println("totalVoteList="+totalVoteList);
                mAdapter.notifyDataSetChanged();
                //Toast.makeText(VotesListActivity.this,totalVoteList.getJSONObject(1).getJSONArray("options").toArray().length, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void clearVoteInfo(){
        App.voteInfo.setIsAnonymous("true");
        App.voteInfo.setBitmap(null);
        App.voteInfo.getVoteRewardRule().clear();
        App.voteInfo.setIsReward("false");
        App.voteInfo.getPlayerList().clear();
        App.voteInfo.setVoteExpireTime("");
        App.voteInfo.getVoteTarget().clear();
        App.voteInfo.getImgUrls().clear();
        App.voteInfo.setVoteTheme("");
        App.voteInfo.getOptions().clear();
        App.voteInfo.setIsLimited("false");
        App.voteInfo.getVoterTargetList().clear();
        App.voteInfo.setIsRaise("true");
        App.voteInfo.getCheckedRadioButtonList().clear();
        App.voteInfo.setVoteExpireTime("");
        App.voteInfo.getCheckedPositionList().clear();
        App.voteInfo.setIfSetReward(false);
    }

}

