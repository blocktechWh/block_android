package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.Fragment.StartVoteFirstStepFragment;
import com.blocktechwh.app.block.Fragment.StartVoteFouthStepFragment;
import com.blocktechwh.app.block.Fragment.StartVoteSecondStepFragment;
import com.blocktechwh.app.block.Fragment.StartVoteThirdStepFragment;
import com.blocktechwh.app.block.R;

/**
 * Created by Administrator on 2017/11/10.
 */

public class StartActivity extends TitleActivity {

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private TextView btn_create;
    private TextView btn_add_option;
    private TextView btn_select_voters;
    private TextView btn_set_reward;

    private StartVoteFirstStepFragment svfsf;
    private StartVoteSecondStepFragment svssf;
    private StartVoteThirdStepFragment svtsf;
    private StartVoteFouthStepFragment svfosf;

    private LinearLayout ll_container;
    private LinearLayout ll_interval3;
    private LinearLayout titlebar_button_back;
    private ImageView lladdImage;
    private ImageView iv_titlebar_button_back;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private Bundle bundle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_vote);
        initTitle("发起投票");
        App.getInstance().addActivity(this);
        initView();
        addEvent();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                goBack();
                //System.exit(0);
                break;

            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
        }
        return super.onKeyDown(keyCode,event);
    }
    private void goBack(){

        Intent intent=new Intent(StartActivity.this,VotesListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
        this.finish();
    }

    private void initView(){

        btn_create=(TextView) findViewById(R.id.btn_create);
        btn_create.setBackgroundColor(Color.WHITE);
        btn_add_option=(TextView) findViewById(R.id.btn_add_option);
        btn_select_voters=(TextView) findViewById(R.id.btn_select_voters);
        btn_set_reward=(TextView) findViewById(R.id.btn_set_reward);
        ll_container=(LinearLayout) findViewById(R.id.ll_container);
        ll_interval3=(LinearLayout) findViewById(R.id.ll_interval3);
        titlebar_button_back=(LinearLayout) findViewById(R.id.titlebar_button_back);
        iv_titlebar_button_back=(ImageView) findViewById(R.id.iv_titlebar_button_back);

        fragmentManager = getSupportFragmentManager();
        bundle=this.getIntent().getExtras();
        if(bundle!=null){
            int index=bundle.getInt("index");
            SelectFragment(index);
            btn_create.setBackground(getResources().getDrawable(R.drawable.lines_bg6));
        }else{
            SelectFragment(0);
        }

        //判断是否显示第四部的tab
        if(App.voteInfo.getIfSetReward()){
            showTab4();
        }else{
            hideTab4();
        }


    }

    public void hideTab4(){
        btn_set_reward.setVisibility(View.GONE);
        ll_interval3.setVisibility(View.GONE);
    }
    public void showTab4(){

        btn_set_reward.setVisibility(View.VISIBLE);
        ll_interval3.setVisibility(View.VISIBLE);
    }
    private void addEvent(){
        btn_create.setOnClickListener(new VoteOnClickListener());
        btn_add_option.setOnClickListener(new VoteOnClickListener());
        btn_select_voters.setOnClickListener(new VoteOnClickListener());
        btn_set_reward.setOnClickListener(new VoteOnClickListener());
        iv_titlebar_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
        titlebar_button_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
            }
        });
    }

    class VoteOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            // TODO Auto-generated method stub
            //ClearImage();
            switch (v.getId()) {
                case R.id.btn_create:
                    SelectFragment(0);
                    tabColorClear();
                    btn_create.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
                    btn_create.setTextColor(Color.argb(255,66,143,219));
                    break;
                case R.id.btn_add_option:
                    if(!App.voteInfo.isFirstStepFinished()){
                        Toast.makeText(StartActivity.this,"请先完成第一步",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SelectFragment(1);
                    tabColorClear();
                    btn_add_option.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
                    btn_add_option.setTextColor(Color.argb(255,66,143,219));
                    break;
                case R.id.btn_select_voters:
                    if(!App.voteInfo.isSecondStepFinished()){
                        Toast.makeText(StartActivity.this,"请先完成第二步",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SelectFragment(2);
                    tabColorClear();
                    btn_select_voters.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
                    btn_select_voters.setTextColor(Color.argb(255,66,143,219));
                    break;
                case R.id.btn_set_reward:
                    if(!App.voteInfo.getIfSetReward()){
                        Toast.makeText(StartActivity.this,"还未开启设置奖励，请在第一步中开启",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(!App.voteInfo.isThirdStepFinished()){
                        Toast.makeText(StartActivity.this,"请先完成第三步",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    SelectFragment(3);
                    tabColorClear();
                    btn_set_reward.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
                    btn_set_reward.setTextColor(Color.argb(255,66,143,219));
                    break;
            }
        }
    }

    private void tabColorClear(){
        btn_create.setBackground(getResources().getDrawable(R.drawable.lines_bg6));
        btn_add_option.setBackground(getResources().getDrawable(R.drawable.lines_bg6));
        btn_select_voters.setBackground(getResources().getDrawable(R.drawable.lines_bg6));
        btn_set_reward.setBackground(getResources().getDrawable(R.drawable.lines_bg6));

    }

    public void SelectFragment(int index) {
        fragmentTransaction = fragmentManager.beginTransaction();
//        RemoveFragment(fragmentTransaction);
        hideFragment(fragmentTransaction);
        switch (index) {
            case 0:
                if (svfsf==null) {
                    svfsf = new StartVoteFirstStepFragment();
                    fragmentTransaction.add(R.id.ll_container, svfsf);
                }
                fragmentTransaction.show(svfsf);
                fragmentTransaction.commit();
                btn_create.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
                btn_add_option.setTextColor(Color.argb(255,66,143,219));
                break;
            case 1:
                System.out.println("getIfSetReward="+App.voteInfo.getIfSetReward());


                if (svssf==null) {
                    svssf = new StartVoteSecondStepFragment();
                    fragmentTransaction.add(R.id.ll_container, svssf);
                }
                fragmentTransaction.show(svssf);
                fragmentTransaction.commit();
                tabColorClear();
                btn_add_option.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
                btn_add_option.setTextColor(Color.argb(255,66,143,219));
                //btn_create.setBackgroundColor(Color.argb(255,241,241,241));
                btn_create.setText("✔");
                btn_create.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
                break;
            case 2:
                if (svtsf==null) {
                    svtsf = new StartVoteThirdStepFragment();
                    fragmentTransaction.add(R.id.ll_container, svtsf);
                }
                fragmentTransaction.show(svtsf);
                fragmentTransaction.commit();
                break;
            case 3:
                if (svfosf==null) {
                    svfosf = new StartVoteFouthStepFragment();
                    fragmentTransaction.add(R.id.ll_container, svfosf);
                }
                fragmentTransaction.show(svfosf);
                fragmentTransaction.commit();
                break;

        }

    }

    public void hideFragment(FragmentTransaction fragmentTransaction){
        if (svfsf!=null) {
            fragmentTransaction.hide(svfsf);
        }
        if (svssf!=null) {
            fragmentTransaction.hide(svssf);
        }
        if (svtsf!=null) {
            fragmentTransaction.hide(svtsf);
        }
        if (svfosf!=null) {
            fragmentTransaction.hide(svfosf);
        }

    }

    private void RemoveFragment(FragmentTransaction fragmentTransaction){
        if (svfsf!=null) {
            fragmentTransaction.remove(svfsf);
        }
        if (svssf!=null) {
            fragmentTransaction.remove(svssf);
        }
        if (svtsf!=null) {
            fragmentTransaction.remove(svtsf);
        }
        if (svfosf!=null) {
            fragmentTransaction.remove(svfosf);
        }

    }

}
