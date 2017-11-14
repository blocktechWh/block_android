package com.blocktechwh.app.block.Activity.Actions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Adapter.RedTicketDetailAdapter;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.OnRedPackagePageChangeListener;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class RedTicketDetailActivity extends TitleActivity {
    private List<View>viewList;
    private ViewPager vp;
    private PagerTabStrip tab;
    private List<String>titleList;
    private List<TextView>titleList1;
    private int textWidth=0;
    private TextView textView_got;
    private TextView textView_sent;
    private int currIndex = 0;// 当前页卡编号
    //private ImageView imageView;// 页卡标题动画图片

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_redticket_detail);
        initTitle("红包");

        textView_got=(TextView)findViewById(R.id.textView66);
        textView_sent=(TextView)findViewById(R.id.textView65);
        titleList1=new ArrayList<TextView>();
        titleList1.add(textView_got);
        titleList1.add(textView_sent);

        //为标题添加点击事件
        textView_got.setOnClickListener(new MyOnClickListener(0));
        textView_sent.setOnClickListener(new MyOnClickListener(1));

        /*
        * 使用View作为ViewPager的数据源
        * */
        viewList=new ArrayList<View>();
        //将布局对象转换成View对象
        View view_got=View.inflate(this,R.layout.view_got,null);//null表示父组件为空
        View view_sended=View.inflate(this,R.layout.view_sended,null);//null表示父组件为空

        viewList.add(view_got);
        viewList.add(view_sended);

        //给ViewPager添加标题
        titleList=new ArrayList<String>();
        titleList.add("我收到的");
        titleList.add("我发出的");

        //为pAgerTabTrip设置属性
        tab=(PagerTabStrip) findViewById(R.id.id_redPacket_tab);
        tab.setBackgroundColor(Color.parseColor("#f1f1f1"));
        tab.setTextColor(Color.BLACK);
        tab.setDrawFullUnderline(false);
        tab.setTabIndicatorColor(Color.GREEN);
        tab.setClickable(true);


        //初始化ViewPager
        vp=(ViewPager) findViewById(R.id.id_redPacket);


        //创建PagerAdapter适配器
        RedTicketDetailAdapter pa=new RedTicketDetailAdapter(viewList,titleList);

        //加载PagerAdaoter适配器
        vp.setAdapter(pa);

        //监听页卡的滑动
        vp.setOnPageChangeListener(new OnRedPackagePageChangeListener(textWidth,titleList1,currIndex));

        getData();
    }

    //标题点击监听
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            vp.setCurrentItem(index);
        }
    }

    private void getData(){
        //请求收到红包列表请求
        HttpClient.get(this, Urls.GiftGetList, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {

            }
        });

        //请求发出红包列表请求
        HttpClient.get(this, Urls.GiftSendList, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {

            }
        });

    }
}
