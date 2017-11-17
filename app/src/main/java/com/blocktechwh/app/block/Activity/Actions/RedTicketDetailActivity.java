package com.blocktechwh.app.block.Activity.Actions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.Fragment.RedTiketGetFragment;
import com.blocktechwh.app.block.Fragment.RedTiketSentFragment;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class RedTicketDetailActivity extends TitleActivity {

    private Integer count_recive = 0;
    private Integer count_send = 0;

    private TextView tv_name;
    private TextView tv_recive;
    private TabLayout mTabLayout;

    private ViewPager mViewPager;
    private List<Fragment>viewList = new ArrayList<Fragment>();
    private List<String>titleList= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redticket_detail);

        initTitle("红包");
        initView();

        getData();
    }

    private void initView(){
        tv_recive = (TextView)findViewById(R.id.tv_recive_total);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_name.setText(App.userInfo.getName()+"共发出");
        mViewPager = (ViewPager) findViewById(R.id.id_redPacket);
        mTabLayout = (TabLayout)findViewById(R.id.tabHost);

        initViewPager();
        initTab();
    }

    private LinearLayout getTextView(final Integer index,String text){
        LinearLayout view = new LinearLayout(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT
        );
        view.setLayoutParams(p);
        view.setGravity(Gravity.CENTER);
        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(int i=0;i<mTabLayout.getTabCount();i++){
                    inactiveTab(mTabLayout.getTabAt(i).getCustomView());
                }
                activeTab(view);
                mTabLayout.getTabAt(index).select();
            }
        });
        TextView textView = new TextView(this);
        textView.setText(text);
        view.addView(textView);
        return view;
    }

    private void activeTab(View view){
        view.setBackgroundColor(Color.parseColor("#F6F6F6"));
    }

    private void inactiveTab(View view){
        view.setBackgroundColor(Color.parseColor("#FFFFFF"));
    }

    private void initTab(){
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.getTabAt(0).setCustomView(getTextView(0,"我发出的"));
        mTabLayout.getTabAt(1).setCustomView(getTextView(1,"我收到的"));
        activeTab(mTabLayout.getTabAt(0).getCustomView());
    }

    private void initViewPager(){
        viewList.add(new RedTiketGetFragment());
        viewList.add(new RedTiketSentFragment());
        titleList.add("我收到的");
        titleList.add("我发出的");
        mViewPager.setAdapter(new RedTicketDetailAdapter(getSupportFragmentManager()));
    }

    class RedTicketDetailAdapter extends FragmentPagerAdapter {

        public RedTicketDetailAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return viewList.size();
        }

        @Override
        public Fragment getItem(int position) {
            return viewList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titleList.get(position);
        }

    }

    private void getData(){
        HttpClient.get(this, Urls.GiftSendTotal, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(final JSONObject data) {
                count_send = data.getInteger("data");
                Integer index = mViewPager.getCurrentItem();
                if(index==0){
                    tv_recive.setText("¥ "+count_send);
                }
            }
        });

        HttpClient.get(this, Urls.GiftReciveTotal, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(final JSONObject data) {
                count_recive = data.getInteger("data");
                Integer index = mViewPager.getCurrentItem();
                if(index==1){
                    tv_recive.setText("¥ "+count_recive);
                }

            }
        });

    }
}
