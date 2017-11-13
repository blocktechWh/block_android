package com.blocktechwh.app.block.RecyclerView;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by eagune on 2017/11/13.
 */

public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter {

    protected Context mContext;
    protected List<T> mDatas;

    public MultiItemTypeAdapter(Context context, List<T> datas) {
        mContext = context;
        mDatas = datas;
    }

    @Override
    public int getItemCount() {
        int itemCount = mDatas.size();
        return itemCount;
    }

}
