package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 跳跳蛙 on 2017/11/18.
 */

public class SetRewardActivity extends TitleActivity {
    private Switch sIfReward;
    private LinearLayout ll_rewards_container;
    private TextView tv_reward_add_btn;
    private TextView tv_rank_text;
    private EditText et_reward_percent;
    private static int rewardTotalAmount;
    private int rank_index=1;
    private EditText et_reward;
    private String isReward="false";
    private Button btn_add_reward_sure;
    private List<Integer>rewardList=new ArrayList<>();
    private RelativeLayout reward_input_container;
    private VotesListAdapter mAdapter;
    private ImageView tv_icon_add;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_start_vote_4);
        initTitle("奖励资金");
        App.getInstance().addActivity(this);
        initData();
        setEvent();
    }

    private void initData(){

        tv_icon_add=(ImageView) findViewById(R.id.tv_icon_add);
        reward_input_container=(RelativeLayout) findViewById(R.id.reward_input_container);
        btn_add_reward_sure=(Button) findViewById(R.id.btn_add_reward_sure);
        et_reward=(EditText) findViewById(R.id.et_reward);
        sIfReward=(Switch) findViewById(R.id.sIfReward);
        sIfReward.setChecked(true);
        ll_rewards_container=(LinearLayout) findViewById(R.id.ll_rewards_container);
        tv_reward_add_btn=(TextView) findViewById(R.id.tv_reward_add_btn);
        tv_rank_text=(TextView) findViewById(R.id.tv_rank_text);
        et_reward_percent=(EditText) findViewById(R.id.et_reward_percent);


    }

    private void setEvent(){

        btn_add_reward_sure.setOnClickListener(addRewardOptions);//点击提交按钮确认添加奖励

        //点击开关选择是否添加奖励
        sIfReward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    sIfReward.setText("关");
                    App.voteInfo.setIfSetReward(false);
                    btn_add_reward_sure.setOnClickListener(null);
                    btn_add_reward_sure.setBackgroundColor(Color.argb(255,120,120,120));
//                    Intent intent = new Intent(SetRewardActivity.this,StartActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                    startActivity(intent);
                }else{
                    sIfReward.setText("开");
                    if(App.voteInfo.getVoteRewardRule().size()>0){
                        App.voteInfo.setIfSetReward(true);
                    }
                    btn_add_reward_sure.setBackgroundColor(Color.argb(255,23,144,225));
                    btn_add_reward_sure.setOnClickListener(addRewardOptions);//点击提交按钮确认添加奖励
                }
                App.voteInfo.setIsReward(isReward);

            }
        });


        //点击添加奖励项
        tv_reward_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rewardTotalAmount=Integer.parseInt(et_reward.getText().toString());
                int totalAmount=0;
                int hasRewardAmountPercent=0;
                //计算已添加奖励总比例
                for(int i=0;i<rewardList.size();i++){
                    hasRewardAmountPercent+=rewardList.get(i)*100/rewardTotalAmount;
                }

                if(et_reward_percent.getText().toString().trim().equals(null)){
                    Toast.makeText(SetRewardActivity.this,"请输入有效金额",Toast.LENGTH_SHORT).show();
                    return;
                }

                //计算已添加奖励总比例并判断
                hasRewardAmountPercent+=Integer.parseInt(et_reward_percent.getText().toString().trim());
                if(rewardList.size()+1<App.voteInfo.getOptions().size()&&100-hasRewardAmountPercent==0){
                    Toast.makeText(SetRewardActivity.this,"奖励金额配置数小于投票项目数",Toast.LENGTH_SHORT).show();

                }else if(rewardList.size()+1==App.voteInfo.getOptions().size()&&100-hasRewardAmountPercent>0){
                    Toast.makeText(SetRewardActivity.this,"奖励金额配置未达到100%",Toast.LENGTH_SHORT).show();

                }else if(rewardList.size()+1==App.voteInfo.getOptions().size()&&100-hasRewardAmountPercent<0){
                    Toast.makeText(SetRewardActivity.this,"奖励金额配置超过100%",Toast.LENGTH_SHORT).show();

                }else if(100-hasRewardAmountPercent<0){
                    Toast.makeText(SetRewardActivity.this,"奖励金额配置超过100%",Toast.LENGTH_SHORT).show();
                }else if(100-hasRewardAmountPercent>0){
                    //添加新建奖励到列表
                    int amount=Integer.parseInt(et_reward_percent.getText().toString().trim())*rewardTotalAmount/100;
                    //tv_reward_amount.setText(amount.toString());
                    View item_reward_info_layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_reward_info,null);
                    TextView tv_rank_text1=item_reward_info_layout.findViewById(R.id.tv_rank_text);
                    tv_rank_text1.setText("第"+rank_index+"名");
                    EditText et_reward_percent1=item_reward_info_layout.findViewById(R.id.et_reward_percent);
                    et_reward_percent1.setText(et_reward_percent.getText().toString().trim());
                    et_reward_percent1.setFocusable(false);

                    ll_rewards_container.addView(item_reward_info_layout);

                    //添加数据到list
                    rewardList.add(Integer.parseInt(et_reward_percent.getText().toString().trim())*rewardTotalAmount/100);

                    //重置输入行数据
                    tv_rank_text.setText("第"+(rank_index+1)+"名");
                    ++rank_index;
                    et_reward_percent.setText((100-hasRewardAmountPercent)+"");
                    App.voteInfo.setVoteRewardRule(rewardList);
                    //Toast.makeText(SetRewardActivity.this,App.voteInfo.getVoteRewardRule().toString(),Toast.LENGTH_SHORT).show();

                }else if(100-hasRewardAmountPercent==0){
                    //Toast.makeText(SetRewardActivity.this,"奖励金额已配置完",Toast.LENGTH_SHORT).show();

                    //添加新建奖励到列表
                    int amount=Integer.parseInt(et_reward_percent.getText().toString().trim())/100*rewardTotalAmount;
                    //tv_reward_amount.setText(amount.toString());
                    View item_reward_info_layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_reward_info,null);
                    TextView tv_rank_text1=item_reward_info_layout.findViewById(R.id.tv_rank_text);
                    tv_rank_text1.setText("第"+rank_index+"名");
                    EditText et_reward_percent1=item_reward_info_layout.findViewById(R.id.et_reward_percent);
                    et_reward_percent1.setFocusable(false);
                    et_reward_percent1.setText(et_reward_percent.getText());
                    ll_rewards_container.addView(item_reward_info_layout);

                    //添加数据到list
                    rewardList.add(Integer.parseInt(et_reward_percent.getText().toString().trim())*rewardTotalAmount/100);

                    App.voteInfo.setVoteRewardRule(rewardList);

                    //隐藏
                    reward_input_container.setVisibility(View.GONE);
                    tv_reward_add_btn.setText("奖励金额已配置完");
                    tv_icon_add.setVisibility(View.GONE);
                    tv_reward_add_btn.setOnClickListener(null);

                }
            }
        });
    }

    class VotesListAdapter extends RecyclerView.Adapter<VotesListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_vote_active, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position){
            holder.tv_option_text.setText((position+1)+"."+App.voteInfo.getOptions().get(position).get("item").toString());
            String url = App.voteInfo.getImgUrls().get(position);
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.iv_option_img.setImageBitmap(bmp);
                }
            });

        }

        @Override
        public int getItemCount(){
            return App.voteInfo.getOptions().size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_option_text;
            ImageView iv_option_img;

            public MyViewHolder(View view)
            {
                super(view);
                tv_option_text = (TextView) view.findViewById(R.id.tv_option_text);
                iv_option_img=(ImageView) view.findViewById(R.id.iv_option_img);

            }
        }
    }

    //点击提交按钮确认添加奖励
    private View.OnClickListener addRewardOptions = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            rewardTotalAmount=Integer.parseInt(et_reward.getText().toString());
            App.voteInfo.setVoteFee(rewardTotalAmount);

            int totalAmount=0;
            int hasRewardAmountPercent=0;
            //计算已添加奖励总比例
            for(int i=0;i<rewardList.size();i++){
                hasRewardAmountPercent+=rewardList.get(i)*100/rewardTotalAmount;
            }

            //计算已添加奖励总比例并判断
            if(reward_input_container.isShown()){
                hasRewardAmountPercent+=Integer.parseInt(et_reward_percent.getText().toString().trim());
            }

            if(rewardList.size()+1!=App.voteInfo.getOptions().size()&&reward_input_container.isShown()){
                Toast.makeText(SetRewardActivity.this,"奖励金额配置数与投票项数量不一致",Toast.LENGTH_SHORT).show();

            } else if(rewardList.size()+1==App.voteInfo.getOptions().size()&&100-hasRewardAmountPercent>0){
                Toast.makeText(SetRewardActivity.this,"奖励金额配置未达到100%",Toast.LENGTH_SHORT).show();

            }else if(rewardList.size()+1==App.voteInfo.getOptions().size()&&100-hasRewardAmountPercent<0){
                Toast.makeText(SetRewardActivity.this,"奖励金额配置超过100%",Toast.LENGTH_SHORT).show();

            }else if(100-hasRewardAmountPercent<0){
                Toast.makeText(SetRewardActivity.this,"奖励金额已超出总额，请重新配置",Toast.LENGTH_SHORT).show();
                return;
            }else if(100-hasRewardAmountPercent>0){

                //添加数据到list
                Toast.makeText(SetRewardActivity.this,"奖励金额配置还未完成，请继续配置",Toast.LENGTH_SHORT).show();
                App.voteInfo.setVoteRewardRule(rewardList);
                return;

            }else if(100-hasRewardAmountPercent==0){
                //添加数据到list
                if(reward_input_container.isShown()){
                    rewardList.add(Integer.parseInt(et_reward_percent.getText().toString().trim())*rewardTotalAmount/100);
                }

                App.voteInfo.setVoteRewardRule(rewardList);
                App.voteInfo.setIfSetReward(true);
                Intent intent = new Intent(SetRewardActivity.this,StartActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        }
    };



}
