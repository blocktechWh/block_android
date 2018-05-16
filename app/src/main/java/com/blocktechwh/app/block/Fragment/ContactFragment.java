package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Contact.AddNewContactActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactDetailActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactRequestActivity;
import com.blocktechwh.app.block.Bean.Person;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public class ContactFragment extends Fragment {

    private View view;
    private TextView requestCount_tv;
    private LinearLayout request_view;
    private LinearLayout ll_no_contact;
    private ImageButton addNewContact_btn;

    private RecyclerView mRecyclerView;
    private ContactAdapter mAdapter;
    private List<User> mDatas = new ArrayList<User>();
    private static WebSocketClient client;
    private JSONArray dataList;
    private TextView tv_add_contact;
    private SwipeRefreshLayout swipeRefreshLayout ;

    private HashMap<String, Integer> selector;// 存放含有索引字母的位置
    private LinearLayout layoutIndex;
    private ListView listView;
    private TextView tv_show;
    private String[] indexStr = { "#", "A", "B", "C", "D", "E", "F", "G", "H",
            "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
            "V", "W", "X", "Y", "Z" };
    private List<User> newPersons = new ArrayList<User>();
    private List<Person> persons = null;
    private int height;// 字体高度
    private boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView();
        addEvent();
        getData();

        return view;
    }

    private void initView(){

        request_view = (LinearLayout)view.findViewById(R.id.id_text_request_layout);
        ll_no_contact = (LinearLayout)view.findViewById(R.id.ll_no_contact);
        tv_add_contact = (TextView) view.findViewById(R.id.tv_add_contact);
        tv_add_contact.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_add_contact.getPaint().setAntiAlias(true);//抗锯齿
        requestCount_tv = (TextView)view.findViewById(R.id.id_text_request_count);
        addNewContact_btn = (ImageButton)view.findViewById(R.id.id_add_new);
        //request_view.setVisibility(View.GONE);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_main);
        swipeRefreshLayout.setRefreshing(true);
        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new ContactAdapter());

    }

    class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_contact, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position){
            //holder.userName_tv.setText(mDatas.get(position).getName());
            holder.userName_tv.setText(dataList.getJSONObject(position).getString("name"));
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
        tv_add_contact.setOnClickListener(mIntoContactAdd);
        swipeRefreshLayout.setOnRefreshListener(reFreshData);
    }

    private View.OnClickListener mIntoContactAdd = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            startActivity(new Intent(getActivity(),AddNewContactActivity.class));
        }
    };

    private SwipeRefreshLayout.OnRefreshListener reFreshData = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh(){
            getData();
        }
    };

    private void getData(){
        //获得好友请求数量
        qureyContactsCount();



    }

    @Override
    public void onResume() {
        super.onResume();
    }


    private void qureyContactsCount(){
        //获得好友请求数量
        HttpClient.get(this, Urls.ContactRequestsCount, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {

                Integer requestCount = data.getInteger("data");
                if(requestCount != 0){
                    requestCount_tv.setText(data.getString("data"));
                    //request_view.setVisibility(View.VISIBLE);
                    requestCount_tv.setVisibility(View.VISIBLE);
                    request_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(App.getContext(), ContactRequestActivity.class));
                        }
                    });
                }else{
                    //request_view.setVisibility(View.GONE);
                    requestCount_tv.setVisibility(View.GONE);
                    request_view.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            startActivity(new Intent(App.getContext(), ContactRequestActivity.class));
                        }
                    });
                }

                //获得联系人列表
                queryContactsList();

            }
        });
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            getData();
        } else {
            // 相当于Fragment的onPause

        }
    }
    /**
     * 获取排序后的新数据
     *
     * @param persons
     * @return
     */
    public String[] sortIndex(List<Person> persons) {
        TreeSet<String> set = new TreeSet<String>();
        // 获取初始化数据源中的首字母，添加到set中
        for (Person person : persons) {
            set.add(StringHelper.getPinYinHeadChar(person.getName()).substring(
                    0, 1));
        }
        // 新数组的长度为原数据加上set的大小
        String[] names = new String[persons.size() + set.size()];
        int i = 0;
        for (String string : set) {
            names[i] = string;
            i++;
        }
        String[] pinYinNames = new String[persons.size()];
        for (int j = 0; j < persons.size(); j++) {
            persons.get(j).setPinYinName(
                    StringHelper
                            .getPingYin(persons.get(j).getName().toString()));
            pinYinNames[j] = StringHelper.getPingYin(persons.get(j).getName()
                    .toString());
        }
        // 将原数据拷贝到新数据中
        System.arraycopy(pinYinNames, 0, names, set.size(), pinYinNames.length);
        // 自动按照首字母排序
        Arrays.sort(names, String.CASE_INSENSITIVE_ORDER);
        return names;
    }


    private void queryContactsList(){
        HttpClient.get(this, Urls.Contacts, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                swipeRefreshLayout.setRefreshing(false);//取消刷新效果

                dataList=data;
                mDatas = data.toJavaList(User.class);
                if(mDatas.size()<=0){
                    swipeRefreshLayout.setVisibility(View.GONE);
                    ll_no_contact.setVisibility(View.VISIBLE);
                }else{
                    swipeRefreshLayout.setVisibility(View.VISIBLE);
                    ll_no_contact.setVisibility(View.GONE);
                    System.out.println("联系人列表="+data);
                    List<Integer>contactIdList=new ArrayList<>();

                    persons = new ArrayList<Person>();
                    for(int i=0;i<data.size();i++){
                        contactIdList.add(data.getJSONObject(i).getInteger("id"));

                        Person p = new Person(data.getJSONObject(i).getString("name"));
                        persons.add(p);
                    }
                    App.contactIdList=contactIdList;

                    String[] allNames = sortIndex(persons);
                    sortList(allNames);

                    selector = new HashMap<String, Integer>();
                    for (int j = 0; j < indexStr.length; j++) {// 循环字母表，找出newPersons中对应字母的位置
                        for (int i = 0; i < newPersons.size(); i++) {
                            if (newPersons.get(i).getName().equals(indexStr[j])) {
                                selector.put(indexStr[j], i);
                            }
                        }

                    }
                    mAdapter.notifyDataSetChanged();
                }

                //new Thread(new ThreadShow()).start();

            }
        });
    }

    // 线程类
    class ThreadShow implements Runnable {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (true) {
                try {
                    Thread.sleep(1000);

                    System.out.println("send...");
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("thread error...");
                }
            }
        }
    }


    //按钮点击事件（通知栏）
    private void showInfo(){
        System.out.println("调用了");
//        NotificationCompat.Builder builder = new NotificationCompat.Builder(getContext());
//        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setContentTitle("新消息");
//        builder.setContentText("你有一条新的消息");
//        builder.setNumber(num++);
        //设置点击通知跳转页面后，通知消失
//        builder.setAutoCancel(true);
//        Intent intent = new Intent(getContext(),ContactFragment.class);
//        PendingIntent pi = PendingIntent.getActivity(getContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
//        builder.setContentIntent(pi);
//        Notification notification = builder.build();
//        NotificationManager notificationManager = (NotificationManager)getActivity().getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationManager.notify(NO_1,notification);
    }



}
