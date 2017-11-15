package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Contact.AddNewContactActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactDetailActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactDetailForSendActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactRequestActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class ContactFragment extends Fragment {

    private View view;
    private TextView requestCount_tv;
    private LinearLayout request_view;
    private ImageButton addNewContact_btn;

    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private List<User> mDatas = new ArrayList<User>();

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
        public void onBindViewHolder(final MyViewHolder holder, int position){
            holder.userName_tv.setText(mDatas.get(position).getName());
            holder.user_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int index = holder.getAdapterPosition();
                    User user = mDatas.get(index);
                    Intent intent = new Intent(App.getContext(), ContactDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isFriend",true);
                    bundle.putString("name",user.getName());
                    bundle.putString("email",user.getEmail());
                    bundle.putString("phone",user.getPhone());
                    bundle.putString("address",user.getAddress());
                    bundle.putString("sex",user.getSex());
                    bundle.putString("img",user.getImg());
                    bundle.putString("id",user.getId().toString());
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
            String url = Urls.HOST + "staticImg" + mDatas.get(position).getImg();
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.userPhoto_iv.setImageBitmap(bmp);
                }
            });
        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView userName_tv;
            ImageView userPhoto_iv;
            LinearLayout user_btn;
            public MyViewHolder(View view)
            {
                super(view);
                user_btn = (LinearLayout) view.findViewById(R.id.id_user_btn);
                userName_tv = (TextView) view.findViewById(R.id.id_user_name);
                userPhoto_iv = (ImageView) view.findViewById(R.id.id_user_photo);
            }
        }
    }

    private void addEvent(){
        addNewContact_btn.setOnClickListener(mIntoContactAdd);
    }

    private View.OnClickListener mIntoContactAdd = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            startActivity(new Intent(getActivity(),AddNewContactActivity.class));
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
                    request_view.setVisibility(View.GONE);
                }
            }
        });

        HttpClient.get(this, Urls.Contacts, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                mDatas = data.toJavaList(User.class);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}
