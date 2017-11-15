package com.blocktechwh.app.block.Fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.alibaba.fastjson.JSONArray;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        getData();
        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    private void getData(){
        System.out.print("token="+App.token);
        HttpClient.get(this, Urls.GiftWaitRecieveList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {

            }
        });
    }
}
