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
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Contact.AddNewContactActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactDetailActivity;
import com.blocktechwh.app.block.Activity.Contact.ContactRequestActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.NotYetConnectedException;
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
    private static WebSocketClient client;
    private static final int NO_1 = 0x1;
    int num =1;//初始通知数量为1

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contact, container, false);
        initView();
        addEvent();
        getData();
        try
        {
            webSocketConnect();
        }
        catch(Exception e)
        {
            return view;
        }

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
        //获得好友请求数量
        qureyContactsCount();

        //获得联系人列表
        queryContactsList();

    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }

    private void webSocketConnect()throws URISyntaxException, NotYetConnectedException, UnsupportedEncodingException {

        System.out.println("new client.");
        client = new WebSocketClient(new URI("ws://111.231.146.57:20086/ws?token="+App.token),new Draft_17()) {

            @Override
            public void onOpen(ServerHandshake arg0) {
                System.out.println("打开链接");
            }

            @Override
            public void onMessage(String arg0) {
                System.out.println("收到消息"+arg0);
                if(arg0.contains("contact")){
                    //requestCount_tv.setText(Integer.parseInt(requestCount_tv.getText().toString())+1);
                    qureyContactsCount();

                }

                showInfo();
            }

            @Override
            public void onError(Exception arg0) {
                arg0.printStackTrace();
                System.out.println("发生错误已关闭");
            }

            @Override
            public void onClose(int arg0, String arg1, boolean arg2) {
                System.out.println(client.getURI());
                System.out.println("链接已关闭");
            }

            @Override
            public void onMessage(ByteBuffer bytes) {
                try {
                    System.out.println(new String(bytes.array(),"utf-8"));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }


        };

        client.connect();

//        while(!client.getReadyState().equals(READYSTATE.OPEN)){
//            System.out.println("还没有打开");
//        }
//        System.out.println("打开了");
//        send("hello world".getBytes("utf-8"));
        client.send("hello world");


    }

    public static void send(byte[] bytes){
        client.send(bytes);
    }


    private void qureyContactsCount(){
        //获得好友请求数量
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
    }

    private void queryContactsList(){
        HttpClient.get(this, Urls.Contacts, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                mDatas = data.toJavaList(User.class);
                mAdapter.notifyDataSetChanged();
            }
        });
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
