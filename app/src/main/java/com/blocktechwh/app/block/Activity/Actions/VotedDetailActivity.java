package com.blocktechwh.app.block.Activity.Actions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.VotedDetail;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.ImageViewPlus;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.Fragment.HomeFragment;
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

public class VotedDetailActivity extends TitleActivity {
    private int voteId;
    private RecyclerView VotedDetailRecyclerView;//投票详情对象
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
    private TextView tv_fill_info;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voted_detail);
        initTitle("投票详情");
        Bundle bundle=this.getIntent().getExtras();
        voteId=bundle.getInt("voteId");
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

    private void getData(){
        //查询投票详情
        HttpClient.get(this, Urls.QueryVotedDetail+voteId, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                System.out.print("投票详情mDatas="+data);
                if(!Boolean.parseBoolean(data.getString("isRaise"))){
                    //设置不可点击
                    tv_to_fill.setAlpha((float)0.3);
                    tv_to_fill.setVisibility(View.VISIBLE);
                    tv_to_fill.setOnClickListener(null);
                }

                VotedDetail.setVoteImg(data.getString("img"));
                VotedDetail.setRewardTotal(Double.parseDouble(data.getString("voteTotalFee")));
                VotedDetail.setVoteTheme(data.getString("theme"));
                VotedDetail.setTotalCount(data.getInteger("totalCount"));

                List<Map<String,Object>>voteOptionsList=new ArrayList<>();
                for(int i=0;i<data.getJSONArray("options").size();i++){
                    Map<String,Object> map_vote_option1=new HashMap();
                    map_vote_option1.put("optionCount",Integer.parseInt(data.getJSONArray("options").getJSONObject(i).getString("optionCount")));
                    map_vote_option1.put("optionContent",data.getJSONArray("options").getJSONObject(i).getString("optionContent"));
                    voteOptionsList.add(map_vote_option1);
                }
                VotedDetail.setVoteOptionsList(voteOptionsList);


                List<Map<String,Object>>voteRewardsList=new ArrayList<>();//rewards
                for(int i=0;i<data.getJSONArray("rewards").size();i++){
                    Map<String,Object> map_vote_rewads=new HashMap();
                    map_vote_rewads.put("amount",data.getJSONArray("rewards").getJSONObject(i).getString("amount"));
                    map_vote_rewads.put("rank",data.getJSONArray("options").getJSONObject(i).getString("rank"));
                    voteRewardsList.add(map_vote_rewads);
                }
                VotedDetail.setVoteRewardsList(voteRewardsList);


                List<String>playersList=new ArrayList<>();//optionSupplier
                for(int i=0;i<data.getJSONArray("optionSupplier").size();i++){
                    playersList.add(data.getJSONArray("optionSupplier").getJSONObject(i).getString("img"));
                }
                VotedDetail.setPlayersList(playersList);
                operateVotedDetailData();
            }
        });
    }

    private void operateVotedDetailData(){
        //渲染主题图片
        String url =Urls.HOST + "staticImg" + VotedDetail.getVoteImg();
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                im_vote_img.setImageBitmap(bmp);
            }
        });

        //渲染投票主题
        tv_vote_theme.setText(VotedDetail.getVoteTheme());

        //渲染奖励总金额
        tv_reward_total.setText(VotedDetail.getRewardTotal().toString()+"元");


        //渲染投票项
        if(VotedDetail.getVoteOptionsList().size()>0){
            for(int i=0;i<VotedDetail.getVoteOptionsList().size();i++){
                checkedIndex=i;
                View item_vote_option_layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_voted_option,null);
                TextView tv_vote_option_text=item_vote_option_layout.findViewById(R.id.tv_vote_option_text);
                tv_vote_option_text.setText(VotedDetail.getVoteOptionsList().get(i).get("optionContent").toString());

                TextView tv_number_percetage=item_vote_option_layout.findViewById(R.id.tv_number_percetage);

                Integer percent=0;
                if(VotedDetail.getTotalCount()==0){
                    percent=0;
                }else{
                    percent=Integer.parseInt(VotedDetail.getVoteOptionsList().get(i).get("optionCount").toString())*100/VotedDetail.getTotalCount();

                }
                tv_number_percetage.setText(percent+"%"+"("+VotedDetail.getVoteOptionsList().get(i).get("optionCount").toString()+"人)");
                ProgressBar pbVoteCountProgress=item_vote_option_layout.findViewById(R.id.pbVoteCountProgress);
                pbVoteCountProgress.setProgress(percent);

                ll_vote_options.addView(item_vote_option_layout);

            }
        }


        //渲染奖励项
        for(int i=0;i<VotedDetail.getVoteRewardsList().size();i++){
            View item_vote_reward = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_vote_reward,null);
            TextView tv_single_vote_reward=item_vote_reward.findViewById(R.id.tv_single_vote_reward);
            tv_single_vote_reward.setText(VotedDetail.getVoteRewardsList().get(i).get("amount").toString()+"元");
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
            //Toast.makeText(VotedDetailActivity.this,"VotedDetail.getPlayersList().size()="+VotedDetail.getPlayersList().size(),Toast.LENGTH_SHORT).show();
            return VotedDetail.getPlayersList().size();
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
                imageView = new ImageViewPlus(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            String url =Urls.HOST + "staticImg" + VotedDetail.getPlayersList().get(position);
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    imageView.setImageBitmap(bmp);
                }
            });
            //imageView.setImageResource(R.mipmap.icon9);
            return imageView;
        }


        // Keep all Images in array
//        public Integer[] mThumbIds = {
//                R.mipmap.icon25,R.mipmap.ic_launcher,R.mipmap.icon24,R.mipmap.icon25,R.mipmap.icon25,R.mipmap.ic_launcher,R.mipmap.icon24,R.mipmap.icon25,R.mipmap.icon25,R.mipmap.ic_launcher,R.mipmap.icon24,R.mipmap.icon25
//        };

    }

    private void addEvent(){
        //点击跳转加注页面
        tv_to_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Bundle bundle=new Bundle();
                bundle.putInt("voteId",2);
                Intent intent = new Intent(VotedDetailActivity.this,VoteFillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtras(bundle);
                startActivity(intent);            }
        });

        //点击确认投票
        bt_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                JSONObject json_vote=new JSONObject();
                json_vote.put("voteId",voteId);//投票id
                json_vote.put("optionId",checkedOptionIds);

                HttpClient.post(this, Urls.MAKEVote, json_vote.toString(), new CallBack<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        System.out.print("投票返回="+data);
                        Toast.makeText(VotedDetailActivity.this,"投票成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(VotedDetailActivity.this,HomeFragment.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
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
                Intent intent = new Intent(VotedDetailActivity.this,FillInfoActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

}
