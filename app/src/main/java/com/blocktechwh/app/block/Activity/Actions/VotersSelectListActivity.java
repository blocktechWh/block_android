package com.blocktechwh.app.block.Activity.Actions;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class VotersSelectListActivity extends TitleActivity {


    private RecyclerView mRecyclerView;
    private PlayerListAdapter mAdapter;
    private List<Map<String,Object>> mDatas;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join_select);
        initTitle("参与人员");
        initView();
        getData();


    }


    private void initView(){

        mDatas = new ArrayList<Map<String,Object>>();
        for (int i = 'A'; i < 'z'; i++)
        {
            Map<String,Object> hm=new HashMap<String, Object>();
            hm.put("text","" + (char) i);
            hm.put("id",R.mipmap.ic_launcher);
            mDatas.add(hm);
        }
        mRecyclerView = (RecyclerView)findViewById(R.id.id_players_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());

    }

    class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.voters_listitem, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            holder.tv.setText(mDatas.get(position).get("text").toString());
            holder.iv.setImageResource(Integer.parseInt(mDatas.get(position).get("id").toString()));

        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            CheckBox tv;
            ImageView iv;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (CheckBox) view.findViewById(R.id.itemText);
                iv=(ImageView) view.findViewById(R.id.itemImg);
            }
        }
    }



    private void getData(){
//        HttpClient.get(this, Urls.ContactRequestsCount, null, new CallBack() {
//            @Override
//            public void onSuccess(JSONObject data) {
////                int requestCount = data.getInteger("data");
////                if(requestCount != 0){
////                    requestCount_tv.setText(data.getString("data"));
////                    request_view.setVisibility(View.VISIBLE);
////                }else{
////                    Toast.makeText(getContext(),"4444", Toast.LENGTH_SHORT).show();
////                }
//            }
//        });

    }

}
