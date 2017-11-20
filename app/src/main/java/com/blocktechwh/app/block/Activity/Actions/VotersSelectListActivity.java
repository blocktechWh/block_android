
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
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class VotersSelectListActivity extends TitleActivity {


    private RecyclerView mRecyclerView;
    private PlayerListAdapter mAdapter;
    //private List<Map<String,Object>> mDatas;
    private List<User> mDatas = new ArrayList<User>();
    private ArrayList<Integer>checkdeArray;
    private Integer id;
    private Button playerAddSure;
    private String img;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_join_select);
        initTitle("参与人员");
        initView();
        getData();
        addEvent();

    }

    private void initView(){

//        mDatas = new ArrayList<Map<String,Object>>();
//        for (int i = 'A'; i < 'z'; i++)
//        {
//            Map<String,Object> hm=new HashMap<String, Object>();
//            hm.put("text","" + (char) i);
//            hm.put("id",R.mipmap.ic_launcher);
//            mDatas.add(hm);
//        }
        checkdeArray=new ArrayList<Integer>();
        playerAddSure=(Button) findViewById(R.id.btn_add_sure);
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
        public void onBindViewHolder(final MyViewHolder holder, final int position){
            holder.tv.setText(mDatas.get(position).getName());
            holder.id=mDatas.get(position).getId();
            String url = Urls.HOST + "staticImg" + mDatas.get(position).getImg();
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
                        //checkdeArray.add(holder.id);
                        id=holder.id;
                        img=Urls.HOST + "staticImg" + mDatas.get(position).getImg();
                        //Toast.makeText(VotersSelectListActivity.this,mDatas.get(position).getImg(),Toast.LENGTH_SHORT).show();

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
            RadioButton rb;
            ImageView iv;
            Integer id;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.itemText_player);
                iv=(ImageView) view.findViewById(R.id.itemImg);
                rb=(RadioButton) view.findViewById(R.id.rb_voter);
                id=0;
            }
        }
    }

    private void addEvent(){
        playerAddSure.setOnClickListener(addSure);
    }
    private View.OnClickListener addSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Bundle bundle=new Bundle();
            bundle.putInt("id",id);
            bundle.putString("imgUrl",img);
            Intent intent=new Intent(VotersSelectListActivity.this,AddPlayerActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

    private void getData(){
        //请求查询联系人
        HttpClient.get(this, Urls.Contacts, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                mDatas = data.toJavaList(User.class);
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

