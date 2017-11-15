package com.blocktechwh.app.block.Activity.Actions;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.PagerTabStrip;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Adapter.RedTicketDetailAdapter;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.OnRedPackagePageChangeListener;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private JSONObject dataReturn;
    private TextView tv;
    private RecyclerView ry;
    private List<Map<String,Object>> mDatas;
    private reciveListAdapter mAdapter;
    private String count_recive;
    private String count_send;
    private TextView tv_name;
    private TextView tv_recive;



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

        initView();

        //监听页卡的滑动
        vp.setOnPageChangeListener(new OnRedPackagePageChangeListener(textWidth,titleList1,currIndex));


        getData();
    }

    private void initView(){
        tv_recive=(TextView)findViewById(R.id.tv_recive_total);
        tv_name=(TextView)findViewById(R.id.tv_name);
        tv_name.setText(App.userInfo.getName()+"共发出");

        mDatas = new ArrayList<Map<String,Object>>();
        for (int i = 'A'; i < 'z'; i++)
        {
            Map<String,Object> hm=new HashMap<String, Object>();
            hm.put("geter_name","胡艺瑾");
            hm.put("get_time","10-11");
            hm.put("amount","250.00");

            hm.put("id_img",R.mipmap.image_test);
            mDatas.add(hm);
        }

        //实例化布局
        LayoutInflater inflater = getLayoutInflater();
        View view_send = inflater.inflate(R.layout.view_sended, null);
        ry=(RecyclerView) view_send.findViewById(R.id.id_send_recycler);
        LinearLayoutManager lm=new LinearLayoutManager(App.getContext());
        lm.setOrientation(OrientationHelper.VERTICAL);
        ry.setLayoutManager(lm);

        ry.setAdapter(mAdapter = new reciveListAdapter());


    }

    class reciveListAdapter extends RecyclerView.Adapter<reciveListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.view_sended_item, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            holder.geter_name.setText(mDatas.get(position).get("geter_name").toString());
            holder.get_time.setText(mDatas.get(position).get("get_time").toString());
            holder.amount.setText(mDatas.get(position).get("amount").toString());

            holder.image_layout.setBackgroundResource(Integer.parseInt(mDatas.get(position).get("id_img").toString()));

        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{


            TextView geter_name,get_time,amount;

            ImageView image_layout;

            public MyViewHolder(View view)
            {
                super(view);
                geter_name = (TextView) view.findViewById(R.id.textView_getter);
                get_time = (TextView) view.findViewById(R.id.textView_time);
                amount = (TextView) view.findViewById(R.id.textView_amount);

                image_layout=(ImageView) view.findViewById(R.id.imageView_getter);

            }
        }
    }

    //标题点击监听
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;

        public MyOnClickListener(int i) {
            index = i;
        }

        public void onClick(View v) {
            vp.setCurrentItem(index);
            if(0==index){
                tv_recive.setText("¥ "+count_recive);
            }else if(1==index){
                tv_recive.setText("¥ "+count_send);
            }

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

        //请求发出红包列表请求
        HttpClient.get(this, Urls.GiftSendList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                if(data!=null){
                    //dataReturn=data;
                }
            }
        });

    }
}
