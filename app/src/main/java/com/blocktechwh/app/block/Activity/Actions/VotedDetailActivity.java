package com.blocktechwh.app.block.Activity.Actions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Bean.VotedDetail;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.ImageViewPlus;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

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
    private TextView bt_start_vote;
    private Button bt_vote;
    private List<String>checkedOptionIds=new ArrayList<>();
    private int checkedIndex;
    private String creater;
    private TextView tv_vote_total_data;
    private TextView tv_fill_info;
    private TextView tv_vote_creater;
    private TextView tv_is_anonymous;
    private TextView tv_is_mysele;
    private LinearLayout ll_reward_amount_info;
    private LinearLayout ll_reward_container;
    private TextView tv_reward_title;
    private ImageView iv_end;
    private ImageView iv_array;
    private FrameLayout fl_fill_click_container;
    private boolean isOver;
    private RelativeLayout rl_reward_title;
    private LinearLayout ll_voters;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voted_detail);
        initTitle("投票详情");

        App.getInstance().addActivity(this);

        Bundle bundle=this.getIntent().getExtras();
        voteId=bundle.getInt("voteId");
        isOver=bundle.getBoolean("isOver");
        creater=bundle.getString("creater");
        System.out.println("creater="+creater);

        System.out.println("isOver="+isOver);
        initData();
        getData();
        addEvent();

    }

    private void initData(){
        tv_fill_info= (TextView) findViewById(R.id.tv_fill_info);

        //已结束隐藏
        fl_fill_click_container=(FrameLayout) findViewById(R.id.fl_fill_click_container);
        iv_end=(ImageView) findViewById(R.id.iv_end);
        if(isOver){
            tv_fill_info.setVisibility(View.GONE);
            fl_fill_click_container.setVisibility(View.GONE);
            iv_end.setVisibility(View.VISIBLE);
        }else{
            tv_fill_info.setVisibility(View.VISIBLE);
            fl_fill_click_container.setVisibility(View.VISIBLE);
            iv_end.setVisibility(View.GONE);
        }
        bt_start_vote=(TextView) findViewById(R.id.bt_start_vote);
        bt_start_vote.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        bt_start_vote.getPaint().setAntiAlias(true);//抗锯齿
        tv_reward_title= (TextView) findViewById(R.id.tv_reward_title);
        tv_is_anonymous= (TextView) findViewById(R.id.tv_is_anonymous);
        tv_vote_creater= (TextView) findViewById(R.id.tv_vote_creater);
        tv_vote_total_data= (TextView) findViewById(R.id.tv_vote_total_data);
        tv_is_mysele= (TextView) findViewById(R.id.tv_is_mysele);
        rl_reward_title= (RelativeLayout) findViewById(R.id.rl_reward_title);
        ll_reward_amount_info=(LinearLayout) findViewById(R.id.ll_reward_amount_info);
        ll_voters=(LinearLayout) findViewById(R.id.ll_voters);
        ll_vote_options=(LinearLayout) findViewById(R.id.ll_vote_options);
        ll_reward_container=(LinearLayout) findViewById(R.id.ll_reward_container);
        tv_reward_total=(TextView) findViewById(R.id.tv_reward_total);
        ll_vote_reward_list=(LinearLayout) findViewById(R.id.ll_vote_reward_list);
        tv_vote_theme=(TextView) findViewById(R.id.tv_vote_theme);
        im_vote_img=(ImageView) findViewById(R.id.im_vote_img);
        iv_array=(ImageView) findViewById(R.id.iv_array);
        tv_to_fill=(TextView) findViewById(R.id.tv_to_fill);
//        bt_vote=(Button) findViewById(R.id.bt_vote);

        ll_voters.setVisibility(View.GONE);

    }

    private void getData(){
        //查询投票详情
        HttpClient.get(this, Urls.QueryVotedDetail+voteId, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                System.out.println("投票详情="+data);
                if(!Boolean.parseBoolean(data.getString("isRaise"))){
                    //设置不可点击
                    fl_fill_click_container.setVisibility(View.GONE);
                    tv_to_fill.setOnClickListener(null);
                    tv_fill_info.setVisibility(View.GONE);
                    tv_fill_info.setOnClickListener(null);
                }else{
                    fl_fill_click_container.setVisibility(View.VISIBLE);
                    tv_fill_info.setVisibility(View.VISIBLE);

                }

                VotedDetail.setVoteImg(data.getString("img"));
                VotedDetail.setRewardTotal(Double.parseDouble(data.getString("voteTotalFee")));
                VotedDetail.setVoteTheme("主题："+data.getString("theme"));
                VotedDetail.setTotalCount(data.getInteger("totalCount"));
                VotedDetail.setVoteTotalData("已投人数："+data.getString("voteNum")+"/"+data.getString("totalVote"));
                VotedDetail.setIsAnonymous(data.getBoolean("isAnonymous"));
                VotedDetail.setCreater(creater);

                List<Map<String,Object>>voteOptionsList=new ArrayList<>();
                for(int i=0;i<data.getJSONArray("options").size();i++){
                    Map<String,Object> map_vote_option1=new HashMap();
                    map_vote_option1.put("optionCount",Integer.parseInt(data.getJSONArray("options").getJSONObject(i).getString("optionCount")));
                    map_vote_option1.put("optionContent",data.getJSONArray("options").getJSONObject(i).getString("optionContent"));

                    //存储voteList
                    int size=JSONArray.parseArray(data.getJSONArray("options").getJSONObject(i).getString("votedList").toString()).size();

                    List<JSONObject>votedList=new ArrayList<>();
                    for(int j=0;j<size;j++){
                        JSONObject json = new JSONObject();
                        json.put("voterImg",data.getJSONArray("options").getJSONObject(i).getJSONArray("votedList").getJSONObject(j).getString("img"));
                        json.put("voterName",data.getJSONArray("options").getJSONObject(i).getJSONArray("votedList").getJSONObject(j).getString("name"));
                        json.put("voterCreateTime",data.getJSONArray("options").getJSONObject(i).getJSONArray("votedList").getJSONObject(j).getString("createTime"));
                        votedList.add(json);

                    }
                    map_vote_option1.put("votedList",votedList);


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

        //渲染总投票数据
        //System.out.println("VotedDetail.getVoteTotalData()="+VotedDetail.getVoteTotalData().toString());
        tv_vote_total_data.setText(VotedDetail.getVoteTotalData().toString());

        //渲染投票主题
        tv_vote_theme.setText(VotedDetail.getVoteTheme());

        //渲染奖励总金额
        tv_reward_total.setText(VotedDetail.getRewardTotal().toString());

        //是否匿名
        tv_is_anonymous.setText("是否匿名： "+ (VotedDetail.isIsAnonymous()?"是":"否"));

        //发起人
        tv_vote_creater.setText("发起人： "+ (VotedDetail.getCreater()));

        System.out.println("VoteOptionsList="+VotedDetail.getVoteOptionsList());

        //渲染投票项
        if(VotedDetail.getVoteOptionsList().size()>0){
            for(int i=0;i<VotedDetail.getVoteOptionsList().size();i++){
                checkedIndex=i;
                View item_vote_option_layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_voted_option,null);
                TextView tv_vote_option_text=item_vote_option_layout.findViewById(R.id.tv_vote_option_text);
                final FrameLayout fl_option=item_vote_option_layout.findViewById(R.id.fl_option);
                final ImageView iv_array=item_vote_option_layout.findViewById(R.id.iv_array);
                final LinearLayout ll_voters_info=item_vote_option_layout.findViewById(R.id.ll_voters_info);
                final LinearLayout ll_no_option=item_vote_option_layout.findViewById(R.id.ll_no_option);
                final TextView tv_is_mysele=item_vote_option_layout.findViewById(R.id.tv_is_mysele);
                ll_voters_info.setVisibility(View.GONE);
                ll_no_option.setVisibility(View.GONE);
                tv_vote_option_text.setText(VotedDetail.getVoteOptionsList().get(i).get("optionContent").toString());

                final int size= JSONArray.parseArray(VotedDetail.getVoteOptionsList().get(i).get("votedList").toString()).size();
                if(!VotedDetail.isIsAnonymous()){
                    fl_option.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(size>0){
                                if(ll_voters_info.getVisibility()==View.GONE){
                                    ll_voters_info.setVisibility(View.VISIBLE);
                                    iv_array.setBackground(getResources().getDrawable(R.mipmap.icon_array_up));
                                }else{
                                    ll_voters_info.setVisibility(View.GONE);
                                    iv_array.setBackground(getResources().getDrawable(R.mipmap.icon5));
                                }
                            }else{
                                if(ll_no_option.getVisibility()==View.GONE){
                                    ll_no_option.setVisibility(View.VISIBLE);
                                    iv_array.setBackground(getResources().getDrawable(R.mipmap.icon_array_up));
                                }else{
                                    ll_no_option.setVisibility(View.GONE);
                                    iv_array.setBackground(getResources().getDrawable(R.mipmap.icon5));
                                }
                            }

                        }
                    });
                }else{
                    iv_array.setVisibility(View.GONE);
                }


                TextView tv_number_percetage=item_vote_option_layout.findViewById(R.id.tv_number_percetage);
                LinearLayout ll_voters_container=item_vote_option_layout.findViewById(R.id.ll_voters_container);
                Integer percent=0;
                if(VotedDetail.getTotalCount()==0){
                    percent=0;
                }else{
                    percent=Integer.parseInt(VotedDetail.getVoteOptionsList().get(i).get("optionCount").toString())*100/VotedDetail.getTotalCount();

                }
                tv_number_percetage.setText(percent+"%"+"("+VotedDetail.getVoteOptionsList().get(i).get("optionCount").toString()+"人)");
                ProgressBar pbVoteCountProgress=item_vote_option_layout.findViewById(R.id.pbVoteCountProgress);
                pbVoteCountProgress.setProgress(percent);

                LinearLayout ll_vote_option_layout=(LinearLayout)item_vote_option_layout;


                System.out.println("size="+size);

                for(int j=0;j< size;j++){
                    View item_voters_info = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_voters_info,null);
                    JSONObject jb=JSONArray.parseArray(VotedDetail.getVoteOptionsList().get(i).get("votedList").toString()).getJSONObject(j);
                    TextView tv_voter_name=item_voters_info.findViewById(R.id.tv_voter_name);

                    String myName=JSONObject.parseObject(PreferencesUtils.getString(VotedDetailActivity.this,"UserInfo",""), User.class).getName();
                    if(jb.getString("voterName").equals(myName)){
                        tv_is_mysele.setVisibility(View.VISIBLE);
                    }
                    tv_voter_name.setText(jb.getString("voterName"));
                    TextView tv_vote_time=item_voters_info.findViewById(R.id.tv_vote_time);
                    tv_vote_time.setText(jb.getString("voterCreateTime").substring(5,16));

                    final ImageView iv_voter_img=item_voters_info.findViewById(R.id.iv_voter_img);
                    String voterImgUrl =Urls.HOST + "staticImg" + jb.getString("voterImg");
                    HttpClient.getImage(this, voterImgUrl, new CallBack<Bitmap>() {
                        @Override
                        public void onSuccess(final Bitmap bmp) {
                            iv_voter_img.setImageBitmap(bmp);
                        }
                    });

                    ll_voters_container.addView(item_voters_info);
                }

                ll_vote_options.addView(item_vote_option_layout);

            }
        }

        if(VotedDetail.getVoteRewardsList().size()<=0){
            tv_reward_title.setVisibility(View.GONE);
            ll_reward_amount_info.setVisibility(View.GONE);
        }
        //渲染奖励项
        if(VotedDetail.getVoteRewardsList().size()<=0){
            rl_reward_title.setVisibility(View.GONE);
        }else{
            for(int i=0;i<VotedDetail.getVoteRewardsList().size();i++){
                View item_vote_reward = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_vote_reward,null);
                TextView tv_single_vote_reward=item_vote_reward.findViewById(R.id.tv_single_vote_reward);
                tv_single_vote_reward.setText(VotedDetail.getVoteRewardsList().get(i).get("amount").toString());
                TextView tv_vote_rand=item_vote_reward.findViewById(R.id.tv_vote_rand);
                tv_vote_rand.setText("第"+(i+1)+"名获得：");
                ll_vote_reward_list.addView(item_vote_reward);//将这一行加入表格中

            }
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


        //点击显示奖励信息
        rl_reward_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ll_reward_container.getVisibility()==View.GONE){
                    ll_reward_container.setVisibility(View.VISIBLE);
                    iv_array.setBackground(getResources().getDrawable(R.mipmap.icon_array_up));
                }else{
                    ll_reward_container.setVisibility(View.GONE);
                    iv_array.setBackground(getResources().getDrawable(R.mipmap.icon5));
                }
            }
        });

        //跳转发起投票
        bt_start_vote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearVoteInfo();
                Intent intent = new Intent(VotedDetailActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });

        //点击确认投票
/*        bt_vote.setOnClickListener(new View.OnClickListener() {
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
        });*/

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

    private void clearVoteInfo(){
        App.voteInfo.setIsAnonymous("true");
        App.voteInfo.setBitmap(null);
        App.voteInfo.getVoteRewardRule().clear();
        App.voteInfo.setIsReward("false");
        App.voteInfo.getPlayerList().clear();
        App.voteInfo.setVoteExpireTime("");
        App.voteInfo.getVoteTarget().clear();
        App.voteInfo.getImgUrls().clear();
        App.voteInfo.setVoteTheme("");
        App.voteInfo.getOptions().clear();
        App.voteInfo.setIsLimited("false");
        App.voteInfo.getVoterTargetList().clear();
        App.voteInfo.setIsRaise("true");
        App.voteInfo.getCheckedRadioButtonList().clear();
        App.voteInfo.setVoteExpireTime("");
        App.voteInfo.getCheckedPositionList().clear();
        App.voteInfo.setIfSetReward(false);
        App.voteInfo.setFirstStepFinished(false);
        App.voteInfo.setSecondStepFinished(false);
        App.voteInfo.setThirdStepFinished(false);
        App.voteInfo.setFouthStepFinished(false);
        PreferencesUtils.putString(App.getContext(),"optionName","");
    }

}
