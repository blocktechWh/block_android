package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.MainActivity;
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
    private LinearLayout ll_no_got;
    private LinearLayout ll_no_send;
    private TextView tv_send_gift;
    private RecyclerView mRecyclerView;
    private RedTiketSentFragment.Adapter mAdapter;
    private List<RedTicketSendData> mDatas = new ArrayList<RedTicketSendData>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view=inflater.inflate(R.layout.view_sended, container, false);
        initView();
        getData();
        addEvent();
        return view;
    }

    private void initView(){
        ll_no_got = (LinearLayout) view.findViewById(R.id.ll_no_got);
        ll_no_send = (LinearLayout) view.findViewById(R.id.ll_no_send);

        tv_send_gift = (TextView) view.findViewById(R.id.tv_send_gift);
        tv_send_gift.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_send_gift.getPaint().setAntiAlias(true);//抗锯齿

        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_send_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new RedTiketSentFragment.Adapter());
    }

    private void getData(){
        HttpClient.get(this, Urls.GiftSendList, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                mDatas = data.getJSONArray("list").toJavaList(RedTicketSendData.class);
                if(mDatas.size()<=0){
                    mRecyclerView.setVisibility(View.GONE);
                    ll_no_got.setVisibility(View.GONE);
                    ll_no_send.setVisibility(View.VISIBLE);
                }else{
                    mRecyclerView.setVisibility(View.VISIBLE);
                    ll_no_got.setVisibility(View.GONE);
                    ll_no_send.setVisibility(View.GONE);
                    System.out.println("发出红包="+data);
                    mAdapter.notifyDataSetChanged();
                }

            }
        });
    }

    private void addEvent(){
        tv_send_gift.setOnClickListener(sendGift);
    }

    private View.OnClickListener sendGift = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Bundle bundle = new Bundle();
            bundle.putString("from","RedTiketSentFragment");
            Intent intent= new Intent(getActivity(), MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };

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
            holder.amount.setText(mDatas.get(position).getAmount().toString());
            holder.tv_get_percent.setText(mDatas.get(position).getReceivedCount().toString()+"/"+mDatas.get(position).getTotalCount().toString());
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

            TextView geter_name,get_time,amount,tv_get_percent;
            ImageView image_layout;

            public MyViewHolder(View view)
            {
                super(view);
                geter_name = (TextView) view.findViewById(R.id.textView_getter);
                get_time = (TextView) view.findViewById(R.id.textView_time);
                amount = (TextView) view.findViewById(R.id.textView_amount);
                tv_get_percent = (TextView) view.findViewById(R.id.tv_get_percent);

                image_layout=(ImageView) view.findViewById(R.id.imageView_getter);
            }
        }
    }
}
