package com.blocktechwh.app.block.Activity.Actions;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

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
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_rewards);
        initTitle("奖励资金");

        initData();
        setEvent();
    }

    private void initData(){
        sIfReward=(Switch) findViewById(R.id.sIfReward);
        ll_rewards_container=(LinearLayout) findViewById(R.id.ll_rewards_container);
        tv_reward_add_btn=(TextView) findViewById(R.id.tv_reward_add_btn);
        tv_rank_text=(TextView) findViewById(R.id.tv_rank_text);
        et_reward_percent=(EditText) findViewById(R.id.et_reward_percent);
        tv_reward_amount=(TextView) findViewById(R.id.tv_reward_amount);
    }

    private void setEvent(){
        et_reward_percent.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus==false){
                    Double amount=Integer.parseInt(et_reward_percent.getText().toString())/100*rewardTotalAmount;
                    tv_reward_amount.setText(amount.toString());
                }
            }
        });

        tv_reward_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(et_reward_percent.getText().toString()!=""){
                    Toast.makeText(SetRewardActivity.this,"還未填寫獎勵比例，無法添加",Toast.LENGTH_SHORT).show();
                }else{
                    View item_reward_info_layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_reward_info,null);
                    TextView tv_rank_text1=item_reward_info_layout.findViewById(R.id.tv_rank_text);
                    tv_rank_text1.setText("第"+rank_index+"名");
                    EditText et_reward_percent1=item_reward_info_layout.findViewById(R.id.et_reward_percent);
                    et_reward_percent1.setText(et_reward_percent.getText());
                    TextView tv_reward_amount1=item_reward_info_layout.findViewById(R.id.tv_reward_amount);
                    tv_reward_amount1.setText(tv_reward_amount.getText());
                    ll_rewards_container.addView(item_reward_info_layout);

                    tv_rank_text.setText("第"+(rank_index+1)+"名");
                    et_reward_percent.setText("");
                    tv_reward_amount.setText("0");

                }
            }
        });
    }



}
