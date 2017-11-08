package com.blocktechwh.app.block.Adapter;

import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class RedTicketDetailAdapter extends PagerAdapter {
    private List<View>viewList;
    private List<String>titleList;

    //构造方法
    public RedTicketDetailAdapter(List<View>viewList,List<String>titleList){
        this.viewList=viewList;
        this.titleList=titleList;
    }

    /*
    * 返回的是页卡的数量
    * */
    @Override
    public int getCount() {
        return viewList.size();
    }

    /*
    * View是否来自于对象
    * */
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }

    /*
    * 实例化一个页卡
    * */
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    /*
    * 销毁一个页卡
    * */
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView(viewList.get(position));
    }

    /*
    * 设置VPager页卡的标题
    * */

    @Override
    public CharSequence getPageTitle(int position) {
        return titleList.get(position);
    }

    @Override
    public int getItemPosition(Object object) {
        return super.getItemPosition(object);
    }
}
