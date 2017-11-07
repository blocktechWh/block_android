package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Activity.SettingActivity;

import org.w3c.dom.Text;

public class UserFragment extends Fragment {

    private View view;
    private TextView userName;
    private TextView userPhone;
    private LinearLayout settingButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        addEvent();

        return view;
    }

    private void initView(){
        settingButton = (LinearLayout)view.findViewById(R.id.id_setting_button);
        userName = (TextView)view.findViewById(R.id.id_text_name);
        userPhone = (TextView)view.findViewById(R.id.id_phone);

        userName.setText(App.userInfo.getName());
        userPhone.setText(App.userInfo.getPhone());
    }

    private void addEvent(){
        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),SettingActivity.class);
                startActivity(intent);
            }
        });
    }


}
