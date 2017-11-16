package com.blocktechwh.app.block.Activity.Actions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.Fragment.RedTiketSentFragment;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/7.
 */

public class RedTicketDetailActivity extends TitleActivity {

    private RecyclerView ry;
    private List<Map<String,Object>> mDatas;

    private String count_recive;
    private String count_send;

    private TextView tv_name;
    private TextView tv_recive;

    private ViewPager vp;
    private List<Fragment>viewList = new ArrayList<Fragment>();
    private PagerTabStrip tab;
    private List<String>titleList= new ArrayList<String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redticket_detail);

        initTitle("红包");
        initView();
        initEvent();
        getData();
    }

    private void initView(){
        tv_recive = (TextView)findViewById(R.id.tv_recive_total);
        tv_name = (TextView)findViewById(R.id.tv_name);
        tv_name.setText(App.userInfo.getName()+"共发出");
        initTab();
        initViewPager();
    }

    private void initEvent(){
        ((TextView)findViewById(R.id.id_user_get)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(0);
                tv_recive.setText("¥ "+count_recive);
            }
        });
        ((TextView)findViewById(R.id.id_user_send)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                vp.setCurrentItem(1);
                tv_recive.setText("¥ "+count_send);
            }
        });
    }

    private void initTab(){
        tab = (PagerTabStrip) findViewById(R.id.id_redPacket_tab);
        tab.setBackgroundColor(Color.parseColor("#f1f1f1"));
        tab.setTextColor(Color.BLACK);
        tab.setDrawFullUnderline(false);
        tab.setTabIndicatorColor(Color.GREEN);
        tab.setClickable(true);
    }

    private void initViewPager(){
        viewList.add(new RedTiketSentFragment());
        viewList.add(new RedTiketSentFragment());
        titleList.add("我收到的");
        titleList.add("我发出的");
        vp = (ViewPager) findViewById(R.id.id_redPacket);
        vp.setAdapter(new RedTicketDetailAdapter(getSupportFragmentManager()));
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

        //请求发送红包总金额
        HttpClient.get(this, Urls.GiftSendTotal, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(final JSONObject data) {
                Toast.makeText(RedTicketDetailActivity.this,"444444",Toast.LENGTH_SHORT).show();

                System.out.println("data="+data);
                    tv_recive.setText("¥ "+data.getInteger("data").toString());
                count_send="¥ "+data.getString("data").toString();
            }
        });

        //请求收到红包总金额
        HttpClient.get(this, Urls.GiftReciveTotal, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(final JSONObject data) {

                    System.out.print("data="+data);
                count_recive="¥ "+data.getString("data").toString();
            }
        });

        //请求收到红包列表请求
        HttpClient.get(this, Urls.GiftGetList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {

            }
        });

    }
}
