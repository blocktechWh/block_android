package com.blocktechwh.app.block.CustomView;

import android.support.v7.widget.RecyclerView;

/**
 * Created by 跳跳蛙 on 2017/11/30.
 */

public class MaxRecyclerView extends RecyclerView {


    public MaxRecyclerView(android.content.Context context, android.util.AttributeSet attrs){
        super(context, attrs);
    }
    public MaxRecyclerView(android.content.Context context){
        super(context);
    }

    /**
     * 设置不滚动
     */
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
