package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Activity.SettingActivity;

public class UserFragment extends Fragment {

    private View view;
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
