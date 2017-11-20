package com.blocktechwh.app.block.Activity.Actions;

import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TableLayout;

import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

/**
 * Created by 跳跳蛙 on 2017/11/18.
 */

public class FillInfoActivity extends TitleActivity {
    private TableLayout tableLayout;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filling_table);
        initTitle("加注信息");


        initData();
    }

    private void initData(){
        //设置表格样式
        tableLayout = (TableLayout) findViewById(R.id.table_risk_profile);
        tableLayout.setStretchAllColumns(true);//设置所有的item都可伸缩扩展
        tableLayout.setDividerDrawable(getResources().getDrawable(R.drawable.lines_bg2));//这个就是中间的虚线
        tableLayout.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);//设置分割线为中间显示
    }

//    public void buildTable(){
//        int length = riskProfile.dataList.size();//根据数据，判断行数
//        for (int i = 0; i < length;i++){
//            RiskItem item = riskProfile.dataList.get(i);//获取单行数据
//            View layout = LayoutInflater.from(getApplicationContext()).inflate(R.layout.item_filling_tablerow,null);//布局打气筒获取单行对象
//            TextView name = (TextView) layout.findViewById(R.id.name);
//            TextView value = (TextView) layout.findViewById(R.id.value);
//            TextView statue = (TextView) layout.findViewById(R.id.statue);
//            name.setText(item.name);
//            value.setText(item.show_value);
//            /*根据状态字段，判断显示内容与颜色*/
//            if ("偏高".equals(item.status_value) || "异常".equals(item.status_value)){
//                statue.setTextColor(getResources().getColor(R.color.light_red));
//            }else if("正常".equals(item.status_value)){
//                statue.setTextColor(getResources().getColor(R.color.light_green));
//            }else{
//                statue.setTextColor(getResources().getColor(R.color.dark_yellow));
//            }
//            statue.setText(item.status_value);
//            tableLayout.addView(layout);//将这一行加入表格中
//        }
//    }
}
