package com.blocktechwh.app.block.Fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.R;

import java.util.ArrayList;
import java.util.List;

public class StartVoteFouthStepFragment extends Fragment {

    private View view;
    private Switch switchifAdd;
    private LinearLayout ll_rewards_container;
    private RelativeLayout rl_reward_add_btn;
    private TextView tv_rank_text;
    private TextView tv_option_amount_info;
    private TextView tv_reward_add_btn;
    private EditText et_reward_percent;
    private TextView tv_reward_amount;
    private RelativeLayout tv_reward_inptut;
    //private static List<Map<String,Object>> rewardEditList=new ArrayList<>();
    private static int rewardTotalAmount;
    private int rank_index=1;
    private EditText et_reward;
    private String isReward="false";
    private Button btn_add_reward_sure;
    private List<Integer> rewardList=new ArrayList<>();
    private RelativeLayout reward_input_container;
    private RecyclerView mRecyclerView;
    private boolean ifSetReward=true;
    private ImageView tv_icon_add;
    private RelativeLayout rl_fill;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_start_vote_4, container, false);
        initData();
        setEvent();

        return view;
    }

    private void initData(){

        tv_icon_add=(ImageView) view.findViewById(R.id.tv_icon_add);
        reward_input_container=(RelativeLayout) view.findViewById(R.id.reward_input_container);
        btn_add_reward_sure=(Button) view.findViewById(R.id.btn_add_reward_sure);
        et_reward=(EditText) view.findViewById(R.id.et_reward);
        switchifAdd=(Switch) view.findViewById(R.id.switchifAdd);
        switchifAdd.setChecked(true);
        switchifAdd.setText("开");
        App.voteInfo.setIsRaise("true");
        ll_rewards_container=(LinearLayout)view.findViewById(R.id.ll_rewards_container);
        rl_reward_add_btn=(RelativeLayout) view.findViewById(R.id.rl_reward_add_btn);
        tv_rank_text=(TextView) view.findViewById(R.id.tv_rank_text);
        tv_reward_inptut=(RelativeLayout) view.findViewById(R.id.tv_reward_inptut);
        et_reward_percent=(EditText) view.findViewById(R.id.et_reward_percent);
        tv_option_amount_info=(TextView) view.findViewById(R.id.tv_option_amount_info);
        tv_reward_add_btn=(TextView) view.findViewById(R.id.tv_reward_add_btn);
        rl_fill=(RelativeLayout) view.findViewById(R.id.rl_fill);

        tv_option_amount_info.setText("共有"+App.voteInfo.getOptions().size()+"个候选项");
    }

    private void setEvent(){

        btn_add_reward_sure.setOnClickListener(addRewardOptions);//点击提交按钮确认添加奖励

        //点击开关选择是否加注
        switchifAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    switchifAdd.setText("关");
                    App.voteInfo.setIsRaise("false");
                }else{
                    switchifAdd.setText("开");
                    App.voteInfo.setIsRaise("true");

                }

            }
        });
        //点击开关外层控件选择是否匿名
        rl_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchifAdd.isChecked()){
                    switchifAdd.setChecked(false);
                    switchifAdd.setText("关");
                    App.voteInfo.setIsRaise("false");
                }else{
                    switchifAdd.setChecked(true);
                    switchifAdd.setText("开");
                    App.voteInfo.setIsRaise("true");
                }
            }
        });


        //点击添加奖励项
        rl_reward_add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amountRegex = "^\\+?[1-9][0-9]*$" ;
                if(!et_reward.getText().toString().trim().matches(amountRegex)||et_reward.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(),"请输入有效总奖励金额",Toast.LENGTH_SHORT).show();
                    return;
                }

                rewardTotalAmount=Integer.parseInt(et_reward.getText().toString().trim());
                int hasRewardAmountPercent=0;
                //计算已添加奖励总比例
                for(int i=0;i<rewardList.size();i++){
                    hasRewardAmountPercent+=rewardList.get(i);
                }


                if(!et_reward_percent.getText().toString().trim().matches(amountRegex)||et_reward_percent.getText().toString().trim().equals("")){
                    Toast.makeText(getActivity(),"请输入有效名次奖励金额",Toast.LENGTH_SHORT).show();
                    return;
                }
                //计算已添加奖励总比例并判断
                hasRewardAmountPercent+=Integer.parseInt(et_reward_percent.getText().toString());
                if(rewardList.size()+2<App.voteInfo.getOptions().size()&&rewardTotalAmount-hasRewardAmountPercent==0){
                    Toast.makeText(getActivity(),"奖励金额配置数小于投票项目数",Toast.LENGTH_SHORT).show();

                }else if(rewardList.size()+1==App.voteInfo.getOptions().size()&&rewardTotalAmount-hasRewardAmountPercent>0){
                    Toast.makeText(getActivity(),"奖励金额配置未达到总金额",Toast.LENGTH_SHORT).show();

                }else if(rewardList.size()+1==App.voteInfo.getOptions().size()&&rewardTotalAmount-hasRewardAmountPercent<0){
                    Toast.makeText(getActivity(),"奖励金额配置超过总金额",Toast.LENGTH_SHORT).show();

                }else if(rewardTotalAmount-hasRewardAmountPercent<0){
                    Toast.makeText(getActivity(),"奖励金额配置超过总金额",Toast.LENGTH_SHORT).show();
                }else if(rewardList.size()+1<App.voteInfo.getOptions().size()&&rewardTotalAmount-hasRewardAmountPercent>=0){
                    //添加新建奖励到列表
                    View item_reward_info_layout = LayoutInflater.from(getActivity()).inflate(R.layout.item_reward_info,null);
                    TextView tv_rank_text1=item_reward_info_layout.findViewById(R.id.tv_rank_text);
                    tv_rank_text1.setText("第"+rank_index+"名");
                    TextView et_reward_percent1=item_reward_info_layout.findViewById(R.id.et_reward_percent);
                    et_reward_percent1.setText(et_reward_percent.getText());
                    et_reward_percent1.setFocusable(false);
                    ll_rewards_container.addView(item_reward_info_layout);

                    //添加数据到list
                    rewardList.add(Integer.parseInt(et_reward_percent.getText().toString()));

                    //重置输入行数据
                    tv_rank_text.setText("第"+(rank_index+1)+"名");
                    ++rank_index;
                    et_reward_percent.setText((rewardTotalAmount-hasRewardAmountPercent)+"");
                    App.voteInfo.setVoteRewardRule(rewardList);
                    //Toast.makeText(SetRewardActivity.this,App.voteInfo.getVoteRewardRule().toString(),Toast.LENGTH_SHORT).show();

                }else if(rewardList.size()+1==App.voteInfo.getOptions().size()&&rewardTotalAmount-hasRewardAmountPercent==0){

                    //添加新建奖励到列表
                    View item_reward_info_layout = LayoutInflater.from(getActivity()).inflate(R.layout.item_reward_info,null);
                    TextView tv_rank_text1=item_reward_info_layout.findViewById(R.id.tv_rank_text);
                    tv_rank_text1.setText("第"+rank_index+"名");
                    TextView et_reward_percent1=item_reward_info_layout.findViewById(R.id.et_reward_percent);
                    et_reward_percent1.setText(et_reward_percent.getText());
                    ll_rewards_container.addView(item_reward_info_layout);

                    //添加数据到list
                    rewardList.add(Integer.parseInt(et_reward_percent.getText().toString()));

                    App.voteInfo.setVoteRewardRule(rewardList);

                    //隐藏
                    reward_input_container.setVisibility(View.GONE);
                    tv_reward_add_btn.setText("奖励金额已配置完");
                    rl_reward_add_btn.setBackgroundColor(Color.WHITE);
                    tv_reward_inptut.setVisibility(View.GONE);
                    tv_icon_add.setVisibility(View.GONE);
                    tv_reward_add_btn.setOnClickListener(null);

                }
            }
        });
    }

    //点击提交按钮确认添加奖励
    private View.OnClickListener addRewardOptions = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            String amountRegex = "^\\+?[1-9][0-9]*$" ;
            if(!et_reward.getText().toString().trim().matches(amountRegex)||et_reward.getText().toString().trim().equals("")){
                Toast.makeText(getActivity(),"请输入有效总奖励金额",Toast.LENGTH_SHORT).show();
                return;
            }

            rewardTotalAmount=Integer.parseInt(et_reward.getText().toString().trim());
            App.voteInfo.setVoteFee(rewardTotalAmount);

            int totalAmount=0;
            int hasRewardAmountPercent=0;
            //计算已添加奖励总比例
            for(int i=0;i<rewardList.size();i++){
                hasRewardAmountPercent+=rewardList.get(i);
            }

            if(!et_reward_percent.getText().toString().trim().matches(amountRegex)||et_reward_percent.getText().toString().trim().equals("")){
                Toast.makeText(getActivity(),"请输入有效名次奖励金额",Toast.LENGTH_SHORT).show();
                return;
            }
            //计算已添加奖励总比例并判断
            if(reward_input_container.isShown()){
                hasRewardAmountPercent+=Integer.parseInt(et_reward_percent.getText().toString());
            }

            if(rewardList.size()+1!=App.voteInfo.getOptions().size()&&reward_input_container.isShown()){
                Toast.makeText(getActivity(),"奖励金额配置数与投票项数量不一致",Toast.LENGTH_SHORT).show();

            } else if(rewardList.size()+1== App.voteInfo.getOptions().size()&&rewardTotalAmount-hasRewardAmountPercent>0){
                Toast.makeText(getActivity(),"奖励金额配置未达到总金额",Toast.LENGTH_SHORT).show();

            }else if(rewardList.size()+1==App.voteInfo.getOptions().size()&&rewardTotalAmount-hasRewardAmountPercent<0){
                Toast.makeText(getActivity(),"奖励金额配置超过总金额",Toast.LENGTH_SHORT).show();

            }else if(rewardTotalAmount-hasRewardAmountPercent<0){
                Toast.makeText(getActivity(),"奖励金额已超出总额，请重新配置",Toast.LENGTH_SHORT).show();
                return;
            }else if(rewardTotalAmount-hasRewardAmountPercent>0){

                //添加数据到list
                Toast.makeText(getActivity(),"奖励金额配置还未完成，请继续配置",Toast.LENGTH_SHORT).show();
                App.voteInfo.setVoteRewardRule(rewardList);
                return;

            }else if(rewardTotalAmount-hasRewardAmountPercent==0){
                //添加数据到list
                if(reward_input_container.isShown()){
                    rewardList.add(Integer.parseInt(et_reward_percent.getText().toString()));
                }
                App.voteInfo.setVoteRewardRule(rewardList);
                App.voteInfo.setIfSetReward(true);

                //提交投票
                StartVoteSubmit();
            }
        }
    };

    private void StartVoteSubmit(){
        final JSONObject json = new JSONObject();

        json.put("voteImg",App.voteInfo.getVoteImg());
        json.put("voteTheme",App.voteInfo.getVoteTheme());
        json.put("isLimited",App.voteInfo.getIsLimited());
        json.put("isRaise",App.voteInfo.getIsRaise());
        json.put("isAnonymous",App.voteInfo.getIsAnonymous());
        json.put("voteExpireTime",App.voteInfo.getVoteExpireTime());
        json.put("options",App.voteInfo.getOptions());
        json.put("voteTarget",App.voteInfo.getVoteTarget());

        json.put("voteFee",App.voteInfo.getVoteFee());
        json.put("voteRewardRule",App.voteInfo.getVoteRewardRule());
        System.out.println("json="+json);

        //提交投票
//        HttpClient.post(this, Urls.MakeVote, json.toString(), new CallBack<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject data) {
//                Toast.makeText(getContext(),"发起投票成功",Toast.LENGTH_SHORT).show();
//                App.voteInfo.setFouthStepFinished(true);
//                Intent intent = new Intent(getContext(),VotesListActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                startActivity(intent);
//
//            }
//        });
    }


}
