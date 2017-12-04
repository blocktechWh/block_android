package com.blocktechwh.app.block.Fragment;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.blocktechwh.app.block.Bean.RedTicketSendData;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 跳跳蛙 on 2017/11/14.
 */

public class RedTiketSentFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private RedTiketSentFragment.Adapter mAdapter;
    private List<RedTicketSendData> mDatas = new ArrayList<RedTicketSendData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.view_sended, container, false);
        initView();
        getData();

        return view;
    }

    private void initView(){
        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_send_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new RedTiketSentFragment.Adapter());
    }

    private void getData(){
        HttpClient.get(this, Urls.GiftSendList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                mDatas = data.toJavaList(RedTicketSendData.class);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    class Adapter extends RecyclerView.Adapter<Adapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.view_sended_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position){
            holder.geter_name.setText(mDatas.get(position).getName());
            holder.get_time.setText(mDatas.get(position).getCreateTimeString());
            holder.amount.setText("￥ "+mDatas.get(position).getAmount().toString());
            String url = Urls.HOST + "staticImg" + mDatas.get(position).getImg();
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.image_layout.setImageBitmap(bmp);
                }
            });
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
