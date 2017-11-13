package com.blocktechwh.app.block.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;

import com.blocktechwh.app.block.Activity.VotersSelectListActivity;
import com.blocktechwh.app.block.Bean.Players;
import com.blocktechwh.app.block.R;

import java.util.List;

/**
 * Created by Administrator on 2017/11/11.
 */

public class PlayerListViewAdapter extends BaseAdapter {
    private View[] itemViews;
    private VotersSelectListActivity votersSelectListActivity;

    public PlayerListViewAdapter(List<Players> mlistInfo,VotersSelectListActivity votersSelectListActivity) {
        // TODO Auto-generated constructor stub
        this.itemViews = new View[mlistInfo.size()];
        this.votersSelectListActivity=votersSelectListActivity;
        for(int i=0;i<mlistInfo.size();i++){
            Players getInfo=(Players)mlistInfo.get(i);    //获取第i个对象
            //调用makeItemView，实例化一个Item
            itemViews[i]=makeItemView(
                    getInfo.getTitle(), getInfo.getDetails(),getInfo.getAvatar(),votersSelectListActivity
            );
        }
    }

    public int getCount() {
        return itemViews.length;
    }

    public View getItem(int position) {
        return itemViews[position];
    }

    public long getItemId(int position) {
        return position;
    }

    //绘制Item的函数
    private View makeItemView(String strTitle, String strText, int resId,VotersSelectListActivity votersSelectListActivity) {

        LayoutInflater inflater = (LayoutInflater)votersSelectListActivity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 使用View的对象itemView与R.layout.item关联
        View itemView = inflater.inflate(R.layout.voters_listitem, null);

        // 通过findViewById()方法实例R.layout.item内各组件
//        TextView title = (TextView) itemView.findViewById(R.id.title);
//        title.setText(strTitle);    //填入相应的值
        CheckedTextView text = (CheckedTextView) itemView.findViewById(R.id.itemText);
        text.setText(strText);
        ImageView image = (ImageView) itemView.findViewById(R.id.itemImg);
        image.setImageResource(resId);

        return itemView;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            return itemViews[position];
        return convertView;
    }

}


