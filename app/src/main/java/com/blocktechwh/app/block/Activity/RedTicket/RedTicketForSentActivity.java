package com.blocktechwh.app.block.Activity.RedTicket;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 跳跳蛙 on 2017/11/14.
 */

public class RedTicketForSentActivity extends TitleActivity {
    private RecyclerView mRecyclerView;
    private reciveListAdapter mAdapter;
    private List<Map<String,Object>> mDatas;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_sended);
        App.getInstance().addActivity(this);

        Toast.makeText(RedTicketForSentActivity.this,"该页面被加载了",Toast.LENGTH_SHORT).show();
        initView();
    }

        private void initView(){

            mDatas = new ArrayList<Map<String,Object>>();
            for (int i = 'A'; i < 'z'; i++)
            {
                Map<String,Object> hm=new HashMap<String, Object>();
                hm.put("geter_name","胡艺瑾");
                hm.put("get_time","10-11");
                hm.put("amount","250.00");

                hm.put("id_img",R.mipmap.image_test);
                mDatas.add(hm);
            }
            mRecyclerView = (RecyclerView)findViewById(R.id.id_send_recycler);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
            mRecyclerView.setAdapter(mAdapter = new reciveListAdapter());

        }

        class reciveListAdapter extends RecyclerView.Adapter<reciveListAdapter.MyViewHolder>{

            @Override
            public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
                MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                        App.getContext()).inflate(R.layout.view_sended_item, parent,
                        false));
                return holder;
            }

            @Override
            public void onBindViewHolder(MyViewHolder holder, int position){
                holder.geter_name.setText(mDatas.get(position).get("geter_name").toString());
                holder.get_time.setText(mDatas.get(position).get("get_time").toString());
                holder.amount.setText(mDatas.get(position).get("amount").toString());

                holder.image_layout.setBackgroundResource(Integer.parseInt(mDatas.get(position).get("id_img").toString()));

            }

            @Override
            public int getItemCount(){
                return mDatas.size();
            }

            class MyViewHolder extends RecyclerView.ViewHolder{


                TextView geter_name,get_time,amount;

                ImageView image_layout;

                public MyViewHolder(View view)
                {
                    super(view);
                    geter_name = (TextView) view.findViewById(R.id.textView_getter);
                    get_time = (TextView) view.findViewById(R.id.textView_time);
                    amount = (TextView) view.findViewById(R.id.textView_amount);

                    image_layout=(ImageView) view.findViewById(R.id.imageView_getter);

                }
            }
        }



}
