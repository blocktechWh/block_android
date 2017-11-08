package com.blocktechwh.app.block.CustomView;

import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Common.App;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */

public class OnRedPackagePageChangeListener implements ViewPager.OnPageChangeListener {
    private int textWidth;
    private List<TextView>titleList1;
    private int currIndex;

    public OnRedPackagePageChangeListener(int textWidth,List<TextView>titleList1,int currIndex){
        this.textWidth=textWidth;
        this.titleList1=titleList1;
        this.currIndex=currIndex;
    }
    @Override
    public void onPageSelected(int position) {
        Toast.makeText(App.getContext(),"position="+position,Toast.LENGTH_SHORT).show();
//        if (textWidth == 0) {
//            textWidth = titleList1.get(0).getWidth();
//        }
//        Animation animation = new TranslateAnimation(textWidth * currIndex,
//                textWidth * position, 0, 0);
//        animation.setFillAfter(true);/* True:图片停在动画结束位置 */
//        animation.setDuration(300);
//        imageView.startAnimation(animation);
//        setTextTitleSelectedColor(position);
//        setImageViewWidth(textWidth);
        for(int i=0;i<titleList1.size();i++){
            titleList1.get(i).setBackgroundColor(Color.parseColor("#FFFFFFFF"));
            //GradientDrawable mGroupDrawable= (GradientDrawable) titleList1.get(i).getBackground();

        }
          titleList1.get(position).setBackgroundColor(Color.parseColor("#f7f7f7"));

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }
}
