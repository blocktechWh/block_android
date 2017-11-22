package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Bean.VoteInfo;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

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
    private TextView tv_reward_amount;
    //private static List<Map<String,Object>> rewardEditList=new ArrayList<>();
    private static Double rewardTotalAmount;
    private int rank_index=1;
    private EditText et_reward;
    private String isReward="false";
    private Button btn_add_reward_sure;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rewards);
        initTitle("奖励资金");

        initData();
        setEvent();
    }

    private void initData(){
        btn_add_reward_sure=(Button) findViewById(R.id.btn_add_reward_sure);
        et_reward=(EditText) findViewById(R.id.et_reward);
        sIfReward=(Switch) findViewById(R.id.sIfReward);
        sIfReward.setChecked(Boolean.parseBoolean(VoteInfo.getIsReward()));
        ll_rewards_container=(LinearLayout) findViewById(R.id.ll_rewards_container);
        tv_reward_add_btn=(TextView) findViewById(R.id.tv_reward_add_btn);
        tv_rank_text=(TextView) findViewById(R.id.tv_rank_text);
        et_reward_percent=(EditText) findViewById(R.id.et_reward_percent);
        tv_reward_amount=(TextView) findViewById(R.id.tv_reward_amount);
    }

    private void setEvent(){

        //点击提交按钮确认添加奖励
        btn_add_reward_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 VoteInfo.setVoteFee(rewardTotalAmount);
                SetRewardActivity.this.finish();

            }
        });
        //点击开关选择是否添加奖励
        sIfReward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    sIfReward.setText("关");
                    isReward="false";
                    Intent intent = new Intent(SetRewardActivity.this,StartActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                }else{
                    sIfReward.setText("开");
                    isReward="true";
                }
                VoteInfo.setIsReward(isReward);

            }
        });
//        et_reward_percent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                rewardTotalAmount=Double.parseDouble(et_reward.getText().toString());
//                if(!hasFocus){
//                    Double amount=Integer.parseInt(et_reward_percent.getText().toString())/100*rewardTotalAmount;
//                    tv_reward_amount.setText(amount.toString());
//                }
//            }
//        });

        tv_reward_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Double>rewardList=new ArrayList<>();
                if(et_reward_percent.getText().toString()=="0"){
                    Toast.makeText(SetRewardActivity.this,"还未填写奖励比例，无法添加",Toast.LENGTH_SHORT).show();
                }else{
                    rewardTotalAmount=Double.parseDouble(et_reward.getText().toString());
                    Double amount=Double.parseDouble(et_reward_percent.getText().toString())/100*rewardTotalAmount;
                    Toast.makeText(SetRewardActivity.this,et_reward_percent.getText().toString(),Toast.LENGTH_LONG).show();
                    //tv_reward_amount.setText(amount.toString());
                    View item_reward_info_layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_reward_info,null);
                    TextView tv_rank_text1=item_reward_info_layout.findViewById(R.id.tv_rank_text);
                    tv_rank_text1.setText("第"+rank_index+"名");
                    EditText et_reward_percent1=item_reward_info_layout.findViewById(R.id.et_reward_percent);
                    et_reward_percent1.setText(et_reward_percent.getText());
                    TextView tv_reward_amount1=item_reward_info_layout.findViewById(R.id.tv_reward_amount);
                    tv_reward_amount1.setText(amount.toString());
                    ll_rewards_container.addView(item_reward_info_layout);
                    tv_rank_text.setText("第"+(rank_index+1)+"名");

                    ++rank_index;
                    et_reward_percent.setText("");
                    tv_reward_amount.setText("0");
                    rewardList.add(Double.parseDouble(tv_reward_amount.getText().toString()));
                    VoteInfo.setVoteRewardRule(rewardList);
                }
            }
        });
    }



}
