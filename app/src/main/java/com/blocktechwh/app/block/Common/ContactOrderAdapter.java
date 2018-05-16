package com.blocktechwh.app.block.Common;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AlphabetIndexer;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.blocktechwh.app.block.Activity.Contact.ContactDetailActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by JiaJi on 2015/12/4.
 */
public class ContactOrderAdapter extends BaseAdapter
{
    private Cursor cursor;
    private LayoutInflater inflater;
    private AlphabetIndexer indexer;
    private ViewHolder holder;
    private List<User> mDatas = new ArrayList<User>();
    public ContactOrderAdapter(Context context, List<User> mDatas)
    {
        this.mDatas = mDatas;
        inflater = LayoutInflater.from(context);

    }
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Cursor getItem(int position) {
        cursor.moveToPosition(position);
        return cursor;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        if (convertView == null)
        {
            convertView = inflater.inflate(R.layout.item_express, parent, false);
            holder = new ViewHolder();
            holder.tvLetter = (TextView) convertView.findViewById(R.id.tvLetter_item_express);
            holder.tvCompanyName = (TextView) convertView.findViewById(R.id.tvCompanyName_item_express);
            holder.rlContact = (RelativeLayout) convertView.findViewById(R.id.rl_contact);
            holder.tvContactImg = (ImageView) convertView.findViewById(R.id.iv_contact_img);
            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.tvCompanyName.setText(mDatas.get(position).getName());
        holder.id=mDatas.get(position).getId();
        final String img=mDatas.get(position).getImg();
        String url = Urls.HOST + "staticImg" + img;

        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                holder.tvContactImg.setImageBitmap(bmp);
            }
        });

        holder.rlContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id=holder.id;
                Intent intent = new Intent(App.getContext(), ContactDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id",id+"");
                bundle.putBoolean("isFriend",true);
                bundle.putString("img",img);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                App.getContext().startActivity(intent);
            }
        });

/*        //获取这个位置代表的字符在字符表中的位置
        int section = indexer.getSectionForPosition(position);
        //判断当前位置是否是第一个出现这个字符，indexer.getPositionForSection(section)获取第一次出现的位置
        if (position == indexer.getPositionForSection(section))
        {
            holder.tvLetter.setVisibility(View.VISIBLE);
            holder.tvLetter.setText("A");
        }
        else
        {
            holder.tvLetter.setVisibility(View.GONE);
        }*/
        return convertView;
    }
    private class ViewHolder
    {
        RelativeLayout rlContact;
        TextView tvLetter;
        TextView tvCompanyName;
        ImageView tvContactImg;
        int id;
    }
}
