package com.blocktechwh.app.block.Activity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.InputDialog;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

public class UpdateUserInfoActivity extends TitleActivity {

    private InputDialog inputDialog;
    private TextView userName;
    private TextView userPhone;
    private TextView userAddress;

    private String jsonKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        initTitle("更新用户资料");
        initView();
    }

    private void initView(){
        userName = (TextView)findViewById(R.id.text_user_name);
        userPhone = (TextView)findViewById(R.id.text_user_phone);
        userAddress = (TextView)findViewById(R.id.text_user_address);

        userName.setText(App.userInfo.getName());
        userPhone.setText(App.userInfo.getPhone());
        userAddress.setText(App.userInfo.getAddress());

        inputDialog = new InputDialog(this){
            @Override
            public void handle(String string){
                mInputHandler(string);
            }
        };

        ((LinearLayout)findViewById(R.id.id_user_name_btn)).setOnClickListener(mClickListener);
        ((LinearLayout)findViewById(R.id.id_user_address_btn)).setOnClickListener(mClickListener);

    }
    private View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            inputDialog.show();
            switch (view.getId()){
                case R.id.id_user_name_btn:
                    jsonKey = "userName";
                    inputDialog.setTitleText("更改用户名");
                    break;
                case R.id.id_user_address_btn:
                    jsonKey = "address";
                    inputDialog.setTitleText("更改地区");
                    break;
            }
        }
    };

    private void mInputHandler(final String string){
        inputDialog.hide();

        JSONObject json = new JSONObject();
        json.put(jsonKey, string);
        HttpClient.put(this, Urls.UpdateUserInfo, json.toString(), new CallBack() {
            @Override
            public void onSuccess(JSONObject result) {
                switch (jsonKey){
                    case "userName":
                        App.userInfo.setName(string);
                        userName.setText(string);
                        break;
                    case "address":
                        App.userInfo.setAddress(string);
                        userAddress.setText(string);
                        break;
                }
                PreferencesUtils.putString(App.getContext(),"UserInfo", JSONObject.toJSONString(App.userInfo));
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        if(inputDialog != null){
            inputDialog.dismiss();
        }
    }
}
