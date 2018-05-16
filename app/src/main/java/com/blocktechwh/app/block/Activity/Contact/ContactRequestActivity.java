package com.blocktechwh.app.block.Activity.Contact;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Base64;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

public class ContactRequestActivity extends TitleActivity {

    private RecyclerView mRecyclerView;
    private JSONArray mDatas = new JSONArray();
    private JSONObject userWaitAddList = new JSONObject();
    private int waitSize;
    private int addSize;
    private int acceptSize;
    private Adapter mAdapter;
    private LinearLayout titlebar_button_back;
    private SwipeRefreshLayout swipeRefreshLayout ;

    /** 获取库Phone表字段 **/
    private static final String[] PHONES_PROJECTION = new String[] {
            Phone.DISPLAY_NAME, Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, Phone.CONTACT_ID};

    /** 联系人显示名称 **/
    private static final int PHONES_DISPLAY_NAME = 0;

    /** 电话号码 **/
    private static final int PHONES_NUMBER = 1;

    /** 头像ID **/
    private static final int PHONES_PHOTO_ID = 2;

    /** 联系人的ID **/
    private static final int PHONES_CONTACT_ID = 3;

    /** 联系人名称 **/
    private ArrayList<String> mContactsName = new ArrayList<String>();

    /** 联系人头像 **/
    private ArrayList<String> mContactsNumber = new ArrayList<String>();

    /** 联系人头像 **/
    private ArrayList<Bitmap> mContactsImg = new ArrayList<Bitmap>();

    ListView mListView = null;

    private RelativeLayout rely;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_new);

        initTitle("新的朋友");

        App.getInstance().addActivity(this);

        initView();
        getData();
        setEvent();
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        getData();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:

                goBack();
                break;

            case KeyEvent.KEYCODE_HOME:
                break;
            case KeyEvent.KEYCODE_MENU:
                break;
        }
        return super.onKeyDown(keyCode,event);
    }

    private void goBack(){
        Bundle bundle = new Bundle();
        bundle.putString("from","ContactRequestActivity");
        Intent intent= new Intent(ContactRequestActivity.this, MainActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void initView(){

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_main);
        swipeRefreshLayout.setRefreshing(true);

        titlebar_button_back=(LinearLayout)findViewById(R.id.titlebar_button_back);

        mRecyclerView = (RecyclerView)findViewById(R.id.id_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new ContactRequestActivity.Adapter());
    }
    // 获取手机联系人
    private void getPhoneContacts() {
        // rely=(RelativeLayout) findViewById(R.id.relationId);
        ContentResolver resolver = this.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);

        // 不为空
        if (phoneCursor != null) {
            String phoneNumber;
            String contactName;
            Long contactid;
            Long imgid;
            //Bitmap bitmap = null;
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                phoneNumber = phoneCursor.getString(PHONES_NUMBER);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                // 得到联系人名称
                contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);

                // 得到联系人ID
                contactid = phoneCursor.getLong(PHONES_CONTACT_ID);

                // 得到联系人头像ID
                imgid = phoneCursor.getLong(PHONES_PHOTO_ID);

                // 得到联系人头像Bitamp
                //Bitmap bitmap = null;

                // photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (imgid > 0) {
                    Uri uri = ContentUris.withAppendedId(
                            ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    //bitmap = BitmapFactory.decodeStream(input);
                } else {
                    // 设置默认
                    //bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
                }

                mContactsName.add(contactName);
                //Log.i("info", "contactName---" + contactName);
                // Log.i("info","mContactsName111"+mContactsName);
                mContactsNumber.add(phoneNumber);
                //mContactsImg.add(bitmap);
            }

            phoneCursor.close();

        }
    }


    /** 得到手机SIM卡联系人人信息 **/
    private void getSIMContacts() {
        ContentResolver resolver = this.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(PHONES_NUMBER);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(PHONES_DISPLAY_NAME);

                // Sim卡中没有联系人头像
                mContactsName.add(contactName);
                mContactsNumber.add(phoneNumber);
            }

            phoneCursor.close();
        }
    }

    private void queryUsersAddWait(){
        final JSONObject json = new JSONObject();
        json.put("phones",mContactsNumber);

        HttpClient.post(this, Urls.QueryUsers, json.toString(), new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                userWaitAddList=data;

                //查询待接收的好友数据
                queryUsersAcceptWait();
            }
        });
    }

    private void queryUsersAcceptWait(){
        HttpClient.get(this, Urls.ContactRequestsList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                mDatas = data;
                acceptSize=data.size();
                System.out.println("好友列表="+userWaitAddList);

                waitSize=userWaitAddList.getJSONArray("wait").size();
                addSize=userWaitAddList.getJSONArray("add").size();
                for(int j=0;j<waitSize;j++){
                    JSONObject jo=new JSONObject();
                    String number=userWaitAddList.getJSONArray("wait").getJSONObject(j).getString("phone");
                    String myNumber=JSONObject.parseObject(PreferencesUtils.getString(ContactRequestActivity.this,"UserInfo",""), User.class).getPhone();
                    System.out.println("myNumber="+myNumber);
                    System.out.println("number="+number);
                    if(number.equals(myNumber)){
                        System.out.println("999999");
                        continue;
                    }
                    jo.put("name",userWaitAddList.getJSONArray("wait").getJSONObject(j).getString("name"));
                    jo.put("img",userWaitAddList.getJSONArray("wait").getJSONObject(j).getString("img"));
                    jo.put("number",number);
                    jo.put("id",userWaitAddList.getJSONArray("wait").getJSONObject(j).getString("id"));
                    mDatas.add(jo);
                }
                for(int j=0;j<addSize;j++){
                    JSONObject jo=new JSONObject();
                    String number=userWaitAddList.getJSONArray("add").getJSONObject(j).getString("phone");
                    String myNumber=JSONObject.parseObject(PreferencesUtils.getString(ContactRequestActivity.this,"UserInfo",""), User.class).getPhone();
                    System.out.println("myNumber="+myNumber);
                    System.out.println("number="+number);
                    if(number.equals(myNumber)){
                        continue;
                    }
                    jo.put("name",userWaitAddList.getJSONArray("add").getJSONObject(j).getString("name"));
                    jo.put("img",userWaitAddList.getJSONArray("add").getJSONObject(j).getString("img"));
                    jo.put("number",number);
                    jo.put("id",userWaitAddList.getJSONArray("add").getJSONObject(j).getString("id"));
                    mDatas.add(jo);
                }
                System.out.println("mDatas.size="+mDatas.size());
                mAdapter.notifyDataSetChanged();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }
    class Adapter extends RecyclerView.Adapter<ContactRequestActivity.Adapter.MyViewHolder>{

        @Override
        public ContactRequestActivity.Adapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            ContactRequestActivity.Adapter.MyViewHolder holder = new ContactRequestActivity.Adapter.MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_friend_new, parent,false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final ContactRequestActivity.Adapter.MyViewHolder holder, final int position){

            holder.userName_tv.setText(mDatas.getJSONObject(position).getString("name"));

            String url = Urls.HOST + "staticImg" + mDatas.getJSONObject(position).getString("img");
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.userPhoto_iv.setImageBitmap(bmp);
                }
            });

            if(position<acceptSize){
                holder.accept_btn.setText("接受");

                holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int index = holder.getAdapterPosition();
                        mAcceptRequest(index, Integer.parseInt(mDatas.getJSONObject(position).getString("id")));
                    }
                });

                holder.ll_show_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(App.getContext(),AcceptNewActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("userId",mDatas.getJSONObject(position).getString("userId"));
                        bundle.putString("id",mDatas.getJSONObject(position).getString("id"));
                        bundle.putString("img",mDatas.getJSONObject(position).getString("img").toString());
                        bundle.putInt("size",mDatas.size()-1);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }else if(position<acceptSize+waitSize){
                holder.accept_btn.setOnClickListener(null);
                holder.accept_btn.setText("等待验证");
                holder.accept_btn.setTextColor(Color.argb(255,120,120,120));
                holder.accept_btn.setBackground(null);

                holder.ll_show_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(App.getContext(),ContactDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id",mDatas.getJSONObject(position).getString("id"));
                        bundle.putString("img",mDatas.getJSONObject(position).getString("img").toString());
                        bundle.putBoolean("isFriend",false);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }else if(position<acceptSize+waitSize+addSize){
                holder.accept_btn.setText("添加");
                holder.accept_btn.setBackgroundColor(Color.argb(255,241,241,241));
                holder.accept_btn.setTextColor(Color.argb(255,120,120,120));
                //holder.userPhoto_iv.setImageBitmap(convertStringToIcon(mDatas.getJSONObject(position).getString("img")));

                holder.accept_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int userId=Integer.parseInt(mDatas.getJSONObject(position).getString("id"));
                        mAddContact(userId,holder.accept_btn);
                    }
                });

                holder.ll_show_detail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(App.getContext(),ContactDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("id",mDatas.getJSONObject(position).getString("id"));
                        bundle.putString("img",mDatas.getJSONObject(position).getString("img").toString());
                        bundle.putBoolean("isFriend",false);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }

        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView userName_tv;
            ImageView userPhoto_iv;
            Button accept_btn;
            LinearLayout ll_show_detail;
            public MyViewHolder(View view)
            {
                super(view);
                userName_tv = (TextView) view.findViewById(R.id.id_user_name);
                userPhoto_iv = (ImageView) view.findViewById(R.id.id_user_photo);
                accept_btn = (Button) view.findViewById(R.id.id_accept_btn);
                ll_show_detail = (LinearLayout) view.findViewById(R.id.ll_show_detail);
            }
        }
    }
    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap)
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }
    private void getData(){
        /** 得到手机通讯录联系人信息 **/
        getPhoneContacts();
        /** 得到手机SIM卡联系人人信息 **/
        getSIMContacts();
        /** 得到待添加好友数据 **/
        queryUsersAddWait();

    }

    private void setEvent(){
        //返回
        titlebar_button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        //
        swipeRefreshLayout.setOnRefreshListener(reFreshData);
    }
    private SwipeRefreshLayout.OnRefreshListener reFreshData = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh(){
            getData();

        }
    };

    private void mAddContact(int userId,final Button btn){
        String urls = Urls.RequestContact + userId;
        HttpClient.post(this, urls, new JSONObject().toString(), new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(App.getContext(),"已发送",Toast.LENGTH_SHORT).show();
                btn.setOnClickListener(null);
                btn.setText("等待验证");
                btn.setBackground(null);

//                Bundle bundle = new Bundle();
//                bundle.putString("from","ContactDetailActivity");
//                Intent intent= new Intent(ContactRequestActivity.this, MainActivity.class);
//                intent.putExtras(bundle);
//                startActivity(intent);
            }
        });
    }
    private void mAcceptRequest(final int position, int id){
        String url = Urls.AgreeContactRequest + id;
        HttpClient.put(this, url, new JSONObject().toString(), new CallBack() {
            @Override
            public void onSuccess(Object result) {
                Toast.makeText(ContactRequestActivity.this,"已同意好友请求",Toast.LENGTH_SHORT).show();
                mDatas.remove(position);
                if(mDatas.size()<=0){

                    Bundle bundle = new Bundle();
                    bundle.putString("from","ContactRequestActivity");
                    Intent intent= new Intent(ContactRequestActivity.this, MainActivity.class);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }else{
                    mAdapter.notifyItemRemoved(position);
                }
            }
        });
    }

}
