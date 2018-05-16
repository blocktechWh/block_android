package com.blocktechwh.app.block.Activity.Actions;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/11/18.
 */

public class FillInfoActivity extends TitleActivity {
    private int voteId;

    private TableLayout tableLayout;
    private JSONArray fillInfoList;
    private LinearLayout ll_fill_container;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_table);
        initTitle("加注信息");
        App.getInstance().addActivity(this);
        Bundle bundle=this.getIntent().getExtras();
        voteId=bundle.getInt("voteId");
        initData();
        getData();
    }

    private void initData(){
        //设置表格样式
        tableLayout = (TableLayout) findViewById(R.id.table_risk_profile);
        ll_fill_container = (LinearLayout) findViewById(R.id.ll_fill_container);
        tableLayout.setStretchAllColumns(true);//设置所有的item都可伸缩扩展
        tableLayout.setDividerDrawable(getResources().getDrawable(R.drawable.lines_bg2));//这个就是中间的虚线
        tableLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);//设置分割线为中间显示
    }

    public void getData(){
        //查询投票列表
        HttpClient.get(this, Urls.QueryRaiseLIst+voteId, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                fillInfoList=data;
                if(fillInfoList.size()<=0){
                    ll_fill_container.setVisibility(View.VISIBLE);
                    tableLayout.setVisibility(View.GONE);
                }else{
                    ll_fill_container.setVisibility(View.GONE);
                    tableLayout.setVisibility(View.VISIBLE);
                    System.out.println("加注列表="+fillInfoList);
                    operateFillInfo();
                }

            }
        });
    }

    private void operateFillInfo(){
        for (int i = 0; i < fillInfoList.size();i++){
              View rowLayout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_filling_tablerow,null);//布局打气筒获取单行对象
              TextView tv_filler_name = (TextView) rowLayout.findViewById(R.id.tv_filler_name);
              TextView tv_fill_amount = (TextView) rowLayout.findViewById(R.id.tv_fill_amount);
              TextView tv_fill_time = (TextView) rowLayout.findViewById(R.id.tv_fill_time);
              final ImageView img_filler= (ImageView) rowLayout.findViewById(R.id.img_filler);

              tv_filler_name.setText(fillInfoList.getJSONObject(i).getString("userName"));
              tv_fill_amount.setText(fillInfoList.getJSONObject(i).getString("amount"));
              tv_fill_time.setText(fillInfoList.getJSONObject(i).getString("cerateTime").substring(5,fillInfoList.getJSONObject(i).getString("cerateTime").length()));
                String url = Urls.HOST + "staticImg" + fillInfoList.getJSONObject(i).getString("img");
                System.out.println("ImgUrl="+url);
                HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                    @Override
                    public void onSuccess(final Bitmap bmp) {
                        img_filler.setImageBitmap(bmp);
                    }
                });
               tableLayout.addView(rowLayout);//将这一行加入表格中

    }
}

}
