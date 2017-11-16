package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.RedTicket.GiftActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

public class HomeFragment extends Fragment {
    private JSONArray mDatas = new JSONArray();
    private TextView tv_pray;
    private TextView tv_maker_name;
    private TextView tv_create_time;
    private ImageButton id_wallet_image;
    private View view;
    private LinearLayout lo_gift_sure;
    private int id;
    private String tv_pray_text;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        initData();
        getData();
        addEvent();
        return view;
    }

    private void initData(){
        tv_pray=view.findViewById(R.id.tv_pray);
        tv_maker_name=view.findViewById(R.id.tv_maker_name);
        tv_create_time=view.findViewById(R.id.tv_create_time);
        id_wallet_image=view.findViewById(R.id.id_wallet_image);
        lo_gift_sure=view.findViewById(R.id.lo_gift_sure);

    }

    private void addEvent(){
        lo_gift_sure.setOnClickListener(toGiftSure);

    }

    private View.OnClickListener toGiftSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            bundle.putString("tv_pray",tv_pray_text);
            Intent intent= new Intent(getActivity(), GiftActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };


    private void getData(){
        System.out.print("token="+App.token);
        HttpClient.get(this, Urls.GiftWaitRecieveList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {

                mDatas = data;
                System.out.print("待接收的红包列表mDatas="+data);
                if(mDatas.size()>0){
                    operateGiftList(mDatas);
                }

            }
        });
    }

    private void operateGiftList(JSONArray ja){
        JSONObject jo=(JSONObject)ja.get(0);
        id= (int) jo.get("id");

        tv_pray.setText(jo.get("sendMsg").toString());
        tv_create_time.setText(jo.get("createTime").toString());
        tv_maker_name.setText(jo.get("sendName").toString());
        tv_pray_text=jo.get("sendMsg").toString();

        for(int i=0;i<mDatas.size();i++){

        }
    }
}
