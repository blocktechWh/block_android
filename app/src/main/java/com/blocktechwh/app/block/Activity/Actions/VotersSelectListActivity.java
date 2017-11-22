
package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.blocktechwh.app.block.Bean.VoteInfo;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotersSelectListActivity extends TitleActivity {


    private RecyclerView mRecyclerView;
    private PlayerListAdapter mAdapter;
    private JSONArray mDatas;
    private ArrayList<Integer>checkdeArray;
    private Button votersAddSure;
    private static List<Map<String,Object>>playerList=new ArrayList<>();
    List<Integer>votersTargetList=new ArrayList<>();


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_voters_select);
            initTitle("参与人员");

        initView();
        getData();
        addEvent();

    }

    private void initView(){
        mDatas=new JSONArray();
        checkdeArray=new ArrayList<Integer>();
        votersAddSure=(Button) findViewById(R.id.btn_add_sure);
        mRecyclerView = (RecyclerView)findViewById(R.id.id_players_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());

    }

    class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_voters_contact, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position){
            holder.tv.setText(mDatas.getJSONObject(position).getString("name").toString());
            holder.id=Integer.parseInt(mDatas.getJSONObject(position).getString("id"));
            String url = Urls.HOST + "staticImg" + mDatas.getJSONObject(position).getString("img");
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.iv.setImageBitmap(bmp);
                }
            });
            holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                        votersTargetList.add(Integer.parseInt(mDatas.getJSONObject(position).getString("id")));
                        VoteInfo.setVoteTarget(votersTargetList);

                        Map<String,Object> map_player=new HashMap<>();
                        map_player.put("id",Integer.parseInt(mDatas.getJSONObject(position).getString("id")));
                        map_player.put("img",Urls.HOST + "staticImg" + mDatas.getJSONObject(position).getString("img"));
                        playerList.add(map_player);
                        VoteInfo.setPlayerList(playerList);
                        //Toast.makeText(VotersSelectListActivity.this,mDatas.getJSONObject(position).getString("img").toString(), Toast.LENGTH_SHORT).show();

                        holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(!isChecked){
                                    playerList.remove(position);
                                    votersTargetList.remove(position);
                                    VoteInfo.setPlayerList(playerList);
                                    VoteInfo.setVoteTarget(votersTargetList);
                                }
                            }
                        });
                    }
                }
            });

        }

        @Override
        public int getItemCount(){
            return mDatas.size();

        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            CheckBox rb;
            ImageView iv;
            Integer id;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.itemText_player);
                iv=(ImageView) view.findViewById(R.id.itemImg);
                rb=(CheckBox) view.findViewById(R.id.rb_voter);
                id=0;
            }
        }
    }

    private void addEvent(){
        votersAddSure.setOnClickListener(addSure);
    }
    private View.OnClickListener addSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Intent intent=new Intent(VotersSelectListActivity.this,StartActivity.class);
            startActivity(intent);
        }
    };

    private void getData(){
        //请求查询联系人
        HttpClient.get(this, Urls.Contacts, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                mDatas = data;
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}

