package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Contact.AddNewContactActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactDetailActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactRequestActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private View view;
    private Button bt_send;
    private TextView requestCount_tv;
    private LinearLayout request_view;
    private ImageButton addNewContact_btn;

    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private List<String> mDatas;

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
        bt_send=(Button)view.findViewById(R.id.to_send);
        request_view = (LinearLayout)view.findViewById(R.id.id_text_request_layout);
        requestCount_tv = (TextView)view.findViewById(R.id.id_text_request_count);
        addNewContact_btn = (ImageButton)view.findViewById(R.id.id_add_new);
        request_view.setVisibility(View.GONE);


        mDatas = new ArrayList<String>();
        for (int i = 'A'; i < 'z'; i++)
        {
            mDatas.add("" + (char) i);
        }
        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new ContactAdapter());

    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_contact, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position){
            holder.tv.setText(mDatas.get(position));
        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.id_num);
            }
        }
    }


    private void addEvent(){
        addNewContact_btn.setOnClickListener(mIntoContactAdd);
        bt_send.setOnClickListener(showContactDetail);
    }

    private View.OnClickListener mIntoContactAdd = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            startActivity(new Intent(getActivity(),AddNewContactActivity.class));
        }
    };
    private View.OnClickListener showContactDetail = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            startActivity(new Intent(getActivity(), ContactDetailActivity.class));
        }
    };

    private void getData(){
        HttpClient.get(this, Urls.ContactRequestsCount, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                int requestCount = data.getInteger("data");
                if(requestCount != 0){
                    requestCount_tv.setText(data.getString("data"));
                    request_view.setVisibility(View.VISIBLE);
                    request_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(App.getContext(), ContactRequestActivity.class));
                        }
                    });
                }else{
                    Toast.makeText(getContext(),"4444", Toast.LENGTH_SHORT).show();
                }
            }
        });

        HttpClient.get(this, Urls.Contacts, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {

            }
        });
    }

}
