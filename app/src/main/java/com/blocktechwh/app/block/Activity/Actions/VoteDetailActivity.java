package com.blocktechwh.app.block.Activity.Actions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.VoteDetail;
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


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_detail);
        Bundle bundle=this.getIntent().getExtras();
        voteId=bundle.getInt("id");
        Toast.makeText(VoteDetailActivity.this,"voteId="+voteId, Toast.LENGTH_SHORT).show();
        initData();
        operateVoteDetailData();
        addEvent();

    }

    private void initData(){
        ll_vote_options=(LinearLayout) findViewById(R.id.ll_vote_options);
        tv_reward_total=(TextView) findViewById(R.id.tv_reward_total);
        ll_vote_reward_list=(LinearLayout) findViewById(R.id.ll_vote_reward_list);
        tv_vote_theme=(TextView) findViewById(R.id.tv_vote_theme);
        im_vote_img=(ImageView) findViewById(R.id.im_vote_img);
        tv_to_fill=(TextView) findViewById(R.id.tv_to_fill);
        bt_vote=(Button) findViewById(R.id.bt_vote);


        VoteDetail.setVoteImg(R.mipmap.ic_launcher);
        VoteDetail.setRewardTotal(1000.0);
        VoteDetail.setVoteTheme("周末洗厕所比赛初赛");

        List<Map<String,Object>>voteOptionsList=new ArrayList<>();
        Map<String,Object> map_vote_option1=new HashMap();
        map_vote_option1.put("voteId",1);
        map_vote_option1.put("option","王二洗厕所比赛");
        voteOptionsList.add(map_vote_option1);
        Map<String,Object> map_vote_option2=new HashMap();
        map_vote_option2.put("voteId",1);
        map_vote_option2.put("option","王勤洗厕所比赛");
        voteOptionsList.add(map_vote_option2);
        Map<String,Object> map_vote_option3=new HashMap();
        map_vote_option3.put("voteId",1);
        map_vote_option3.put("option","胡艺瑾洗厕所比赛");
        voteOptionsList.add(map_vote_option3);
        VoteDetail.setVoteOptionsList(voteOptionsList);

        List<String>voteRewardsList=new ArrayList<>();
        voteRewardsList.add("50"+"元");
        voteRewardsList.add("30"+"元");
        voteRewardsList.add("20"+"元");
        VoteDetail.setVoteRewardsList(voteRewardsList);

        List<Integer>playersList=new ArrayList<>();
        playersList.add(R.mipmap.ic_launcher);
        playersList.add(R.mipmap.ic_launcher);
        playersList.add(R.mipmap.ic_launcher);
        VoteDetail.setPlayersList(playersList);

        //渲染用户头像
        gridview=(GridView) findViewById(R.id.gridview_players);
        gridview.setAdapter(new ImageAdapter(this));

    }

    private void operateVoteDetailData(){
        //渲染主题图片
        im_vote_img.setImageResource(R.mipmap.ic_launcher);

        //渲染投票主题
        tv_vote_theme.setText(VoteDetail.getVoteTheme());

        //渲染奖励总金额
        tv_reward_total.setText(VoteDetail.getRewardTotal().toString()+"元");

        //渲染投票项
        for(int i=0;i<VoteDetail.getVoteOptionsList().size();i++){
            View item_vote_option_layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_vote_option,null);
            TextView tv_vote_option_text=item_vote_option_layout.findViewById(R.id.tv_vote_option_text);
            tv_vote_option_text.setText(VoteDetail.getVoteOptionsList().get(i).get("option").toString());
            CheckBox cb_vote_option=item_vote_option_layout.findViewById(R.id.cb_vote_option);
            cb_vote_option.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){

                    }
                }
            });
            ll_vote_options.addView(item_vote_option_layout);

        }

        //渲染奖励项
        for(int i=0;i<VoteDetail.getVoteRewardsList().size();i++){
            View item_vote_reward = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_vote_reward,null);
            TextView tv_single_vote_reward=item_vote_reward.findViewById(R.id.tv_single_vote_reward);
            tv_single_vote_reward.setText(VoteDetail.getVoteRewardsList().get(i));
            TextView tv_vote_rand=item_vote_reward.findViewById(R.id.tv_vote_rand);
            tv_vote_rand.setText("第"+i+1+"名获得：");
            ll_vote_reward_list.addView(item_vote_reward);//将这一行加入表格中

        }

    }

    class ImageAdapter extends BaseAdapter {
        private Context mContext;

        // Constructor
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
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
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 8, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(VoteDetail.getPlayersList().get(position));

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
                Intent intent = new Intent(VoteDetailActivity.this,VoteFillActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtras(bundle);
                startActivity(intent);            }
        });

        //点击确认投票
        bt_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            JSONObject json_vote=new JSONObject();
            List<String> optionId=new ArrayList<>();
            optionId.add("9");//活动id
            optionId.add("10");
            json_vote.put("voteId","8");//投票id
            json_vote.put("optionId",optionId);

        HttpClient.post(this, Urls.MAKEVote, json_vote.toString(), new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                System.out.print("投票返回="+data);
            }
        });
            }
        });
    }

}
