package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.blocktechwh.app.block.Activity.RedTicket.GiftActivity;
import com.blocktechwh.app.block.Bean.RedTicketWait;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment1 extends Fragment {
    private RecyclerView mRecyclerView;
    private RedTIcketWaitAdapter mAdapter;
    private List<RedTicketWait> mDatas = new ArrayList<RedTicketWait>();
    private View view;
    private int id;
    private String tv_pray_text;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home1, container, false);
        getData();
        initData();
        return view;
    }

    private void initData(){

        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_red_ticket_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new RedTIcketWaitAdapter());
    }


    private View.OnClickListener toGiftSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            bundle.putString("tv_pray",tv_pray_text);
            Intent intent= new Intent(getActivity(), GiftActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    class RedTIcketWaitAdapter extends RecyclerView.Adapter<RedTIcketWaitAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_red_ticket_wait, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position){
            holder.tv_pray.setText(mDatas.get(position).getPray_text());
            holder.lo_gift_sure.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = holder.getAdapterPosition();
                    RedTicketWait redTicketWait = mDatas.get(index);
                    Intent intent = new Intent(App.getContext(), GiftActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("id",redTicketWait.getId());
                    bundle.putString("pray_text",redTicketWait.getPray_text());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            String url = Urls.HOST + "staticImg" + mDatas.get(position).getImg_url();
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.id_sender_image.setImageBitmap(bmp);
                }
            });
        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_pray;
            TextView tv_maker_name;
            TextView tv_create_time;
            ImageButton id_sender_image;
            LinearLayout lo_gift_sure;

            public MyViewHolder(View view)
            {
                super(view);
                tv_pray=view.findViewById(R.id.tv_pray);
                tv_maker_name=view.findViewById(R.id.tv_maker_name);
                tv_create_time=view.findViewById(R.id.tv_create_time);
                id_sender_image=view.findViewById(R.id.id_sender_image);
                lo_gift_sure=view.findViewById(R.id.lo_gift_sure);

            }
        }
    }

    private void getData(){
        System.out.print("token="+App.token);
        HttpClient.get(this, Urls.GiftWaitRecieveList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {

                System.out.print("待接收的红包列表mDatas="+data);
                mDatas = data.toJavaList(RedTicketWait.class);
                mAdapter.notifyDataSetChanged();

            }
        });
    }

}
