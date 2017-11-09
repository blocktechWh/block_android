package com.blocktechwh.app.block.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.AddNewContactActivity;
import com.blocktechwh.app.block.Activity.SettingActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

public class ContactFragment extends Fragment {

    private View view;
    private TextView requestCount_tv;
    private LinearLayout request_view;
    private ImageButton addNewContact_btn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView();
        addEvent();
        getData();

        return view;
    }

    private void initView(){
        request_view = (LinearLayout)view.findViewById(R.id.id_text_request_layout);
        requestCount_tv = (TextView)view.findViewById(R.id.id_text_request_count);
        addNewContact_btn = (ImageButton)view.findViewById(R.id.id_add_new);
        request_view.setVisibility(View.GONE);
    }

    private void addEvent(){
        addNewContact_btn.setOnClickListener(mIntoContactAdd);
    }

    private View.OnClickListener mIntoContactAdd = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Intent intent =new Intent(getActivity(),AddNewContactActivity.class);
            startActivity(intent);
        }
    };

    private void getData(){
        HttpClient.get(this, Urls.ContactRequestsCount, null, new CallBack() {
            @Override
            public void onSuccess(JSONObject data) {
                int requestCount = data.getInteger("data");
                if(requestCount != 0){
                    requestCount_tv.setText(data.getString("data"));
                    request_view.setVisibility(View.VISIBLE);
                }
            }
        });

        HttpClient.get(this, Urls.Contacts, null, new CallBack() {
            @Override
            public void onSuccess(JSONObject data) {

            }
        });
    }

}
