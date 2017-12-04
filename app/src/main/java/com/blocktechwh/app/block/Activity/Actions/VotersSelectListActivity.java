
package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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
    private static List<Map<String,Object>>playerList;
    private List<Integer>votersTargetList;
    private List<Integer>checkedPositionList;
    private ImageButton titlebar_button_back;




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_voters_select);
            initTitle("参与人员");

        initView();
        getData();
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
        Intent intent = new Intent(VotersSelectListActivity.this,StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
        finish();
    }

    private void initView(){
        playerList=new ArrayList<>();
        votersTargetList=new ArrayList<>();
        checkedPositionList=new ArrayList<>();
        mDatas=new JSONArray();
        checkdeArray=new ArrayList<Integer>();
        votersAddSure=(Button) findViewById(R.id.btn_add_sure);
        mRecyclerView = (RecyclerView)findViewById(R.id.id_players_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());
        titlebar_button_back=(ImageButton)findViewById(R.id.titlebar_button_back);

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

            //去掉已被选择的项
            for(int i=0;i<VoteInfo.getCheckedPositionList().size();i++){
                if(VoteInfo.getCheckedPositionList().get(i)==position){
                    holder.rl_radio_contaner.setVisibility(View.GONE);
                }
            }

            holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        checkedPositionList.add(position);
                        votersTargetList.add(Integer.parseInt(mDatas.getJSONObject(position).getString("id")));
                        Map<String,Object> map_player=new HashMap<>();
                        map_player.put("id",Integer.parseInt(mDatas.getJSONObject(position).getString("id")));
                        map_player.put("img",Urls.HOST + "staticImg" + mDatas.getJSONObject(position).getString("img"));
                        playerList.add(map_player);

                        holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                            @Override
                            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                if(!isChecked){
                                    votersTargetList.remove(votersTargetList.size()-1);
                                    checkedPositionList.remove(checkedPositionList.size()-1);
                                    playerList.remove(playerList.size()-1);
                                    //VoteInfo.getVoteTarget().remove(VoteInfo.getVoteTarget().size()-1);
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
            RelativeLayout rl_radio_contaner;
            Integer id;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.itemText_player);
                iv=(ImageView) view.findViewById(R.id.itemImg);
                rb=(CheckBox) view.findViewById(R.id.rb_voter);
                rl_radio_contaner=(RelativeLayout) view.findViewById(R.id.rl_radio_contaner);
                id=0;
            }
        }
    }

    private void addEvent(){
        //返回
        titlebar_button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        //确定添加
        votersAddSure.setOnClickListener(addSure);
    }
    private View.OnClickListener addSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            if(playerList.size()>0){
                //选取投票者渲染列表
                List<Map<String,Object>>pList=VoteInfo.getPlayerList();
                for(int i=0;i<playerList.size();i++){
                    pList.add(playerList.get(i));
                }
                VoteInfo.setPlayerList(pList);

                //更新选取checkBox列表
                List<Integer>cpList=VoteInfo.getCheckedPositionList();
                for(int i=0;i<checkedPositionList.size();i++){
                    cpList.add(checkedPositionList.get(i));
                }
                VoteInfo.setCheckedPositionList(cpList);

                //更新投票者列表
                List<Integer>vtList=VoteInfo.getVoteTarget();
                for(int j=0;j<votersTargetList.size();j++){
                    vtList.add(votersTargetList.get(j));
                }
                VoteInfo.setVoteTarget(vtList);
            }


            Intent intent=new Intent(VotersSelectListActivity.this,StartActivity.class);
            startActivity(intent);
            finish();

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

