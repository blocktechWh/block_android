package com.blocktechwh.app.block.Activity.Contact;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.acker.simplezxing.activity.CaptureActivity;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by eagune on 2017/11/7.
 */
public class AddNewContactActivity extends TitleActivity {
    private static final String DECODED_CONTENT_KEY = "codedContent";
    private static final String DECODED_BITMAP_KEY = "codedBitmap";
    private static final int REQUEST_CODE_SCAN = 0x0000;
    private static final int REQ_CODE_PERMISSION = 0x1111;
    private String userInfo;
    private int id;
    private String name;
    private String img;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);
        initTitle("添加联系人");

        App.getInstance().addActivity(this);

        initView();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CaptureActivity.REQ_CODE:
                switch (resultCode) {
                    case RESULT_OK:
                        System.out.println("111EXTRA_SCAN_RESULT="+data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        userInfo=data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT);
                        id= JSONObject.parseObject(userInfo).getInteger("id");
                        name= JSONObject.parseObject(userInfo).getString("name");
                        img= JSONObject.parseObject(userInfo).getString("img");

                        for(int i=0;i<App.contactIdList.size();i++){
                            if(App.contactIdList.get(i)==id){
                                Toast.makeText(this, "你们已经是好友了", Toast.LENGTH_LONG).show();
                                return;
                            }
                        }

                        //跳转
                        Intent intent = new Intent(AddNewContactActivity.this, ContactDetailActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("isFriend",false);
                        bundle.putString("id",id+"");
                        bundle.putString("img",img);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        //showUser();
                        //tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));  //or do sth
                        break;
                    case RESULT_CANCELED:
                        if (data != null) {
                            System.out.println("222EXTRA_SCAN_RESULT="+data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                            Toast.makeText(this, "存在某些原因导致相机功能不可用", Toast.LENGTH_LONG).show();

                            // for some reason camera is not working correctly
                            //tvResult.setText(data.getStringExtra(CaptureActivity.EXTRA_SCAN_RESULT));
                        }
                        break;
                }
                break;
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQ_CODE_PERMISSION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // User agree the permission
                    startCaptureActivityForResult();
                } else {
                    // User disagree the permission
                    //Toast.makeText(this, "You must agree the camera permission request before you use the code scan function", Toast.LENGTH_LONG).show();
                    Toast.makeText(this, "扫码之前必须同意设备调用相机的权限", Toast.LENGTH_LONG).show();
                }
            }
            break;
        }
    }


    private void initView() {
        ((LinearLayout)findViewById(R.id.id_add_by_phone)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                startActivity(new Intent(AddNewContactActivity.this, AddContactByPhoneActivity.class));
            }
        });
        ((LinearLayout)findViewById(R.id.ll_saomiao)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //startActivityForResult(new Intent(AddNewContactActivity.this, CaptureActivity.class), CaptureActivity.REQ_CODE);
                // Open Scan Activity
                if (ContextCompat.checkSelfPermission(AddNewContactActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                    // Do not have the permission of camera, request it.
                    ActivityCompat.requestPermissions(AddNewContactActivity.this, new String[]{Manifest.permission.CAMERA}, REQ_CODE_PERMISSION);
                } else {
                    // Have gotten the permission
                    startCaptureActivityForResult();
                }

            }
        });
    }
    private void startCaptureActivityForResult() {
        Intent intent = new Intent(AddNewContactActivity.this, CaptureActivity.class);
        Bundle bundle = new Bundle();
        bundle.putBoolean(CaptureActivity.KEY_NEED_BEEP, CaptureActivity.VALUE_BEEP);
        bundle.putBoolean(CaptureActivity.KEY_NEED_VIBRATION, CaptureActivity.VALUE_VIBRATION);
        bundle.putBoolean(CaptureActivity.KEY_NEED_EXPOSURE, CaptureActivity.VALUE_NO_EXPOSURE);
        bundle.putByte(CaptureActivity.KEY_FLASHLIGHT_MODE, CaptureActivity.VALUE_FLASHLIGHT_OFF);
        bundle.putByte(CaptureActivity.KEY_ORIENTATION_MODE, CaptureActivity.VALUE_ORIENTATION_AUTO);
        bundle.putBoolean(CaptureActivity.KEY_SCAN_AREA_FULL_SCREEN, CaptureActivity.VALUE_SCAN_AREA_FULL_SCREEN);
        bundle.putBoolean(CaptureActivity.KEY_NEED_SCAN_HINT_TEXT, CaptureActivity.VALUE_SCAN_HINT_TEXT);
        intent.putExtra(CaptureActivity.EXTRA_SETTING_BUNDLE, bundle);
        startActivityForResult(intent, CaptureActivity.REQ_CODE);
    }


    public static class AddContactByPhoneActivity extends AppCompatActivity {

        private EditText textInput;
        private TextView userName_tv;
        private LinearLayout user_layout;
        private ImageView userPhoto_iv;
        private SwipeRefreshLayout mSwipeLayout;
        private int REFRESH_UI=1;
        private Handler mHandler = new Handler()
        {
            public void handleMessage(android.os.Message msg)
            {
                switch (msg.what)
                {
                    case 1:

                        if(mSwipeLayout.isRefreshing()){
                            //关闭刷新动画
                            mSwipeLayout.setRefreshing(false);
                        }
                        break;

                }
            };
        };
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_contact_by_phone);
            initView();
/*        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.refreshable_view);

            mSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    //这里可以做一下下拉刷新的操作
                    //例如下面代码，在方法中发送一个handler模拟延时操作
                    mHandler.sendEmptyMessageDelayed(REFRESH_UI, 2000);
                }
            });*/

        }

        private void initView() {
            ((ImageButton)findViewById(R.id.titlebar_button_back)).setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    AddContactByPhoneActivity.this.finish();
                }
            });

            textInput = (EditText)findViewById(R.id.editText);
            textInput.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                        queryUser();
                        return true;
                    }
                    return false;
                }
            });
            textInput.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    // et.getCompoundDrawables()得到一个长度为4的数组，分别表示左右上下四张图片
                    Drawable drawable = textInput.getCompoundDrawables()[2];
                    //如果右边没有图片，不再处理
                    if (drawable == null)
                        return false;
                    //如果不是按下事件，不再处理
                    if (event.getAction() != MotionEvent.ACTION_UP)
                        return false;
                    if (event.getX() > textInput.getWidth() - textInput.getPaddingRight() - drawable.getIntrinsicWidth()){
                        queryUser();
                    }
                    return false;
                }
            });
            user_layout = (LinearLayout)findViewById(R.id.id_user_layout);
            user_layout.setVisibility(View.GONE);

            userName_tv = (TextView)findViewById(R.id.id_user_name);
            userPhoto_iv = (ImageView)findViewById(R.id.id_user_photo);
        }

        private void queryUser(){
            String phone = textInput.getText().toString().trim();
            if(phone.length()!=11){
                Toast.makeText(App.getContext(),"无效手机号",Toast.LENGTH_SHORT).show();
                return;
            }
            if(phone.equals(App.userInfo.getPhone())){
                Toast.makeText(App.getContext(),"不允许添加本人手机号",Toast.LENGTH_SHORT).show();
                return;
            }
            String url = Urls.SearchContact + phone + '/';
            HttpClient.get(this, url, null, new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject result) {
                    JSONObject userJson = result.getJSONObject("user");
                    setUser(JSONObject.toJavaObject(userJson,User.class),result.getBoolean("isContact"));
                }
            });
        }

        private void setUser(final User user, final Boolean isFriend){
            userName_tv.setText(user.getName());

            String url = Urls.HOST + "staticImg" + user.getImg();
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    userPhoto_iv.setImageBitmap(bmp);
                }
            });

            user_layout.setVisibility(View.VISIBLE);
            user_layout.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view){
                    Intent intent = new Intent(App.getContext(),ContactDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isFriend",isFriend);
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
        }

    }

}

