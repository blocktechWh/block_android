package com.blocktechwh.app.block.Activity.Contact;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_add);

        initTitle("添加联系人");
        initView();
    }

    private void initView() {
        ((LinearLayout)findViewById(R.id.id_add_by_phone)).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
//                final EditText inputServer = new EditText(AddNewContactActivity.this);
//                AlertDialog.Builder builder = new AlertDialog.Builder(AddNewContactActivity.this);
//                 builder.setTitle("搜索联系人").setIcon(android.R.drawable.ic_dialog_info).setView(inputServer).setNegativeButton("Cancel", null);
//                builder.setPositiveButton("查找", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                         inputServer.getText().toString();
//                    }
//                });
//
//                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                    public void onClick(DialogInterface dialog, int which) {
//                        inputServer.getText().toString();
//                    }
//                });
//            builder.show();
                startActivity(new Intent(AddNewContactActivity.this, AddContactByPhoneActivity.class));
            }
        });
    }


    public static class AddContactByPhoneActivity extends AppCompatActivity {

        private EditText textInput;
        private TextView userName_tv;
        private LinearLayout user_layout;
        private ImageView userPhoto_iv;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_add_contact_by_phone);
            initView();
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

            user_layout = (LinearLayout)findViewById(R.id.id_user_layout);
            user_layout.setVisibility(View.GONE);

            userName_tv = (TextView)findViewById(R.id.id_user_name);
            userPhoto_iv = (ImageView)findViewById(R.id.id_user_photo);
        }

        private void queryUser(){
            String phone = textInput.getText().toString();
            if(!phone.matches( "[1][34578]\\d{9}" )){
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

