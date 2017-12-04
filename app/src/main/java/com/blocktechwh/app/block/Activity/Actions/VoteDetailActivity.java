package com.blocktechwh.app.block.Activity.Actions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.VoteDetail;
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

/**
 * Created by 跳跳蛙 on 2017/11/18.
 */

public class VoteDetailActivity extends TitleActivity {
    private int voteId;
    private RecyclerView voteDetailRecyclerView;//投票详情对象
    private ImageView im_vote_img;
    private TextView tv_vote_theme;
    private TextView tv_reward_total;
    private LinearLayout ll_vote_options;
    private LinearLayout ll_vote_reward_list;
    private GridView gridview;
    private TextView tv_to_fill;
    private Button bt_vote;
    private List<String>checkedOptionIds=new ArrayList<>();
    private int checkedIndex;
    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private String limited;
    private ContactAdapter.MyViewHolder myViewHolder;
    private int checkedPosition;
    private TextView tv_fill_info;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail);
        initTitle("投票详情");
        Bundle bundle=this.getIntent().getExtras();
        voteId=bundle.getInt("voteId");
        VoteDetail.setVoteId(voteId);
        initData();
        getData();
        addEvent();

    }

    private void initData(){
        tv_fill_info= (TextView) findViewById(R.id.tv_fill_info);
        ll_vote_options=(LinearLayout) findViewById(R.id.ll_vote_options);
        tv_reward_total=(TextView) findViewById(R.id.tv_reward_total);
        ll_vote_reward_list=(LinearLayout) findViewById(R.id.ll_vote_reward_list);
        tv_vote_theme=(TextView) findViewById(R.id.tv_vote_theme);
        im_vote_img=(ImageView) findViewById(R.id.im_vote_img);
        tv_to_fill=(TextView) findViewById(R.id.tv_to_fill);
        bt_vote=(Button) findViewById(R.id.bt_vote);



    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            if(limited=="false"){
                myViewHolder = new MyViewHolder(LayoutInflater.from(App.getContext()).inflate(R.layout.item_vote_option, parent, false));
            }
            if(limited=="true"){
                myViewHolder = new MyViewHolder(LayoutInflater.from(App.getContext()).inflate(R.layout.item_vote_option_single, parent, false));
            }
            return myViewHolder;

        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position){
            if(limited=="false"){
                holder.tv_vote_option_text.setText(VoteDetail.getVoteOptionsList().get(position).get("option").toString());
                holder.cb_vote_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            checkedOptionIds.add(VoteDetail.getVoteOptionsList().get(position).get("optionId").toString());
                            VoteDetail.setCheckedOptionIds(checkedOptionIds);
                            holder.cb_vote_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                                @Override
                                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                                    if(!isChecked){
                                        VoteDetail.getCheckedOptionIds().remove(checkedIndex);
                                    }
                                }
                            });
                        }
                    }
                });
            }

            if(limited=="true"){
                if(position==0){
                    checkedPosition=0;
                    holder.cb_vote_option1.setChecked(true);
                }
                holder.tv_vote_option_text.setText(VoteDetail.getVoteOptionsList().get(position).get("option").toString());
                holder.cb_vote_option1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            //先前被选中的RadioButton取消选中
                            RecyclerView.ViewHolder viewHolder=mRecyclerView.findViewHolderForAdapterPosition(checkedPosition);
                            RadioButton rbChecked=viewHolder.itemView.findViewById(R.id.cb_vote_option);
                            rbChecked.setChecked(false);

                            checkedPosition=position;//将新的被选中的RadioButton位置赋值给checkedPosition
                            checkedOptionIds.clear();
                            checkedOptionIds.add(VoteDetail.getVoteOptionsList().get(position).get("optionId").toString());
                            VoteDetail.setCheckedOptionIds(checkedOptionIds);

                        }
                    }
                });
            }


        }

        @Override
        public int getItemCount(){
            return VoteDetail.getVoteOptionsList().size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_vote_option_text;
            FrameLayout fl_vote_option;
            CheckBox cb_vote_option;
            RadioButton cb_vote_option1;

            public MyViewHolder(View view)
            {
                super(view);
                fl_vote_option = (FrameLayout) view.findViewById(R.id.fl_vote_option);
                tv_vote_option_text = (TextView) view.findViewById(R.id.tv_vote_option_text);
                if(limited=="false"){
                    cb_vote_option = (CheckBox) view.findViewById(R.id.cb_vote_option);
                }

                if(limited=="true"){
                    cb_vote_option1 = (RadioButton) view.findViewById(R.id.cb_vote_option);
                }
            }
        }
    }


    private void getData(){
        //查询投票详情
        HttpClient.get(this, Urls.QueryVoteDetail+voteId, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                System.out.println("投票详情data="+data);
                if(!Boolean.parseBoolean(data.getString("raise"))){
                    //设置不可点击
                    tv_to_fill.setAlpha((float)0.3);
                    tv_to_fill.setVisibility(View.VISIBLE);
                    tv_to_fill.setOnClickListener(null);
                }


                limited=data.getString("limited");

                VoteDetail.setVoteImg(data.getString("img"));
                VoteDetail.setRewardTotal(Double.parseDouble(data.getString("voteFee")));
                VoteDetail.setVoteTheme(data.getString("content"));

                List<Map<String,Object>>voteOptionsList=new ArrayList<>();
                for(int i=0;i<data.getJSONArray("options").size();i++){
                    Map<String,Object> map_vote_option1=new HashMap();
                    map_vote_option1.put("optionId",data.getJSONArray("options").getJSONObject(i).getString("optionId"));
                    map_vote_option1.put("option",data.getJSONArray("options").getJSONObject(i).getString("optionContent"));
                    voteOptionsList.add(map_vote_option1);
                }
                VoteDetail.setVoteOptionsList(voteOptionsList);
                mRecyclerView=(RecyclerView) findViewById(R.id.id_option_recycler);
                mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
                mRecyclerView.setAdapter(mAdapter = new ContactAdapter());
                mAdapter.notifyDataSetChanged();



                List<Map<String,Object>>voteRewardsList=new ArrayList<>();//rewards
                for(int i=0;i<data.getJSONArray("rewards").size();i++){
                    Map<String,Object> map_vote_rewads=new HashMap();
                    map_vote_rewads.put("amount",data.getJSONArray("rewards").getJSONObject(i).getString("amount"));
                    map_vote_rewads.put("rank",data.getJSONArray("rewards").getJSONObject(i).getString("rank"));
                    voteRewardsList.add(map_vote_rewads);
                }
                VoteDetail.setVoteRewardsList(voteRewardsList);


                List<String>playersList=new ArrayList<>();//optionSupplier
                for(int i=0;i<data.getJSONArray("optionSupplier").size();i++){
                    playersList.add(data.getJSONArray("optionSupplier").getJSONObject(i).getString("img"));
                }
                VoteDetail.setPlayersList(playersList);
                operateVoteDetailData();
            }
        });
    }

    private void operateVoteDetailData(){
        //渲染主题图片
        String url =Urls.HOST + "staticImg" + VoteDetail.getVoteImg();
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                im_vote_img.setImageBitmap(bmp);
            }
        });

        //渲染投票主题
        tv_vote_theme.setText(VoteDetail.getVoteTheme());

        //渲染奖励总金额
        tv_reward_total.setText(VoteDetail.getRewardTotal().toString()+"元");


        //渲染奖励项
        System.out.println("VoteDetail.getVoteRewardsList().size()="+VoteDetail.getVoteRewardsList().size());
        for(int i=0;i<VoteDetail.getVoteRewardsList().size();i++){
            View item_vote_reward = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_vote_reward,null);
            TextView tv_single_vote_reward=item_vote_reward.findViewById(R.id.tv_single_vote_reward);
            tv_single_vote_reward.setText(VoteDetail.getVoteRewardsList().get(i).get("amount").toString()+"元");
            TextView tv_vote_rand=item_vote_reward.findViewById(R.id.tv_vote_rand);
            tv_vote_rand.setText("第"+(i+1)+"名获得：");
            ll_vote_reward_list.addView(item_vote_reward);//将这一行加入表格中
        }


        //渲染用户头像
        gridview=(GridView) findViewById(R.id.gridview_players);
        gridview.setAdapter(new ImageAdapter(this));

    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;

        // Constructor
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            //Toast.makeText(VoteDetailActivity.this,"VoteDetail.getPlayersList().size()="+VoteDetail.getPlayersList().size(),Toast.LENGTH_SHORT).show();
            return VoteDetail.getPlayersList().size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            String url =Urls.HOST + "staticImg" + VoteDetail.getPlayersList().get(position);
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    imageView.setImageBitmap(bmp);
                }
            });
            //imageView.setImageResource(R.mipmap.icon9);
            return imageView;
        }
    }

    private void addEvent(){
        //点击跳转加注页面
        tv_to_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putInt("voteId",voteId);
                Intent intent = new Intent(VoteDetailActivity.this,VoteFillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //点击确认投票
        bt_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            JSONObject json_vote=new JSONObject();
            json_vote.put("voteId",voteId);//投票id
            json_vote.put("optionId",checkedOptionIds);
            System.out.println("提交投票json="+json_vote);

            if(checkedOptionIds.size()<=0){
                Toast.makeText(VoteDetailActivity.this,"还未选择受益人",Toast.LENGTH_SHORT).show();
                return;
            }
                HttpClient.post(this, Urls.MAKEVote, json_vote.toString(), new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                Toast.makeText(VoteDetailActivity.this,"投票成功",Toast.LENGTH_SHORT).show();
                VoteDetailActivity.this.finish();

            }
        });

            }
        });

        //点击查看加注信息
        tv_fill_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle=new Bundle();
                bundle.putInt("voteId",voteId);
                Intent intent = new Intent(VoteDetailActivity.this,FillInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }



}
