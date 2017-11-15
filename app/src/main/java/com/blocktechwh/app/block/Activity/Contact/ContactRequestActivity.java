package com.blocktechwh.app.block.Activity.Contact;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class ContactRequestActivity extends TitleActivity {

    private RecyclerView mRecyclerView;
    private List<User> mDatas = new ArrayList<User>();
    private Adapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_new);

        initTitle("新的朋友");
        initView();
        getData();
    }

    private void initView(){
        mRecyclerView = (RecyclerView)findViewById(R.id.id_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new ContactRequestActivity.Adapter());
    }

    class Adapter extends RecyclerView.Adapter<ContactRequestActivity.Adapter.MyViewHolder>{

        @Override
        public ContactRequestActivity.Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            ContactRequestActivity.Adapter.MyViewHolder holder = new ContactRequestActivity.Adapter.MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_friend_new, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final ContactRequestActivity.Adapter.MyViewHolder holder, int position){
            holder.userName_tv.setText(mDatas.get(position).getName());
            holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = holder.getAdapterPosition();
                    mAcceptRequest(index, mDatas.get(index).getId());
                }
            });
            String url = Urls.HOST + "staticImg" + mDatas.get(position).getImg();
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.userPhoto_iv.setImageBitmap(bmp);
                }
            });
        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView userName_tv;
            ImageView userPhoto_iv;
            Button accept_btn;
            public MyViewHolder(View view)
            {
                super(view);
                userName_tv = (TextView) view.findViewById(R.id.id_user_name);
                userPhoto_iv = (ImageView) view.findViewById(R.id.id_user_photo);
                accept_btn = (Button) view.findViewById(R.id.id_accept_btn);
            }
        }
    }

    private void getData(){
        HttpClient.get(this, Urls.ContactRequestsList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                mDatas = data.toJavaList(User.class);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    private void mAcceptRequest(final int position, int id){
        String url = Urls.AgreeContactRequest + id;
        HttpClient.put(this, url, new JSONObject().toString(), new CallBack() {
            @Override
            public void onSuccess(Object result) {
                mDatas.remove(position);
                mAdapter.notifyItemRemoved(position);
            }
        });
    }

}
