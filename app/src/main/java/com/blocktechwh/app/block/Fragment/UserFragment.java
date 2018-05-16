package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Actions.VotesListActivity;
import com.blocktechwh.app.block.Activity.RedTicket.RedTicketDetailActivity;
import com.blocktechwh.app.block.Activity.User.QrCodeActivity;
import com.blocktechwh.app.block.Activity.User.SettingActivity;
import com.blocktechwh.app.block.Activity.Wallete.WalleteDetailActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;


public class UserFragment extends Fragment {

    private View view;
    private TextView userName;
    private TextView userPhone;
    private ImageView userPhoto;
    private LinearLayout settingButton;
    private LinearLayout redPacketButton;
    private LinearLayout voteButton;
    private LinearLayout id_wallet;
    private TextView tv_wallete_data;
    private RelativeLayout  rl_qr_code;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_user, container, false);
        initView();
        getData();
        addEvent();
        return view;
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==0&&resultCode==1){//判断响应码和请求码
            System.out.println("55555555555555");
//            MainActivity mainActivity=(MainActivity) getActivity();
//            FragmentManager fragmentManager=mainActivity.getSupportFragmentManager();
//            FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
//            fragmentTransaction.replace(R.id.container1,new ContactFragment());
//            fragmentTransaction.commit();
            ViewPager mViewPager;
            mViewPager=(ViewPager) getActivity().findViewById(R.id.container1);
            mViewPager.setCurrentItem(2);
        }
    }

    private void initView(){
        tv_wallete_data = (TextView) view.findViewById(R.id.tv_wallete_data);
        userName = (TextView)view.findViewById(R.id.id_text_name);
        userPhone = (TextView)view.findViewById(R.id.id_phone);
        userPhoto = (ImageView) view.findViewById(R.id.id_user_photo);
        rl_qr_code = (RelativeLayout) view.findViewById(R.id.rl_qr_code);

        settingButton = (LinearLayout)view.findViewById(R.id.id_setting_button);
        redPacketButton = (LinearLayout)view.findViewById(R.id.id_red_package);
        voteButton = (LinearLayout) view.findViewById(R.id.id_vote);
        id_wallet = (LinearLayout) view.findViewById(R.id.id_wallet);
        setUserData();
    }

    private void setUserData(){
        System.out.println("userName="+App.userInfo.getName());

        userName.setText(JSONObject.parseObject(PreferencesUtils.getString(getActivity(),"UserInfo",""), User.class).getName());
        userPhone.setText(App.userInfo.getPhone());
        String url = Urls.HOST + "staticImg" + App.userInfo.getImg();
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                userPhoto.setImageBitmap(bmp);
            }
        });
    }

    private void addEvent(){
        settingButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),SettingActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
        redPacketButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(),RedTicketDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
        voteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent intent =new Intent(getActivity(),VotesListActivity.class);
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivityForResult(intent,0);

            }
        });

        id_wallet.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),WalleteDetailActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivityForResult(intent,2);

            }
        });

        rl_qr_code.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(getActivity(),QrCodeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivityForResult(intent,2);

            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        setUserData();
    }


    private void getData(){
        //查询钱包余额
        HttpClient.get(this, Urls.QueryWalleteData, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                tv_wallete_data.setText("余额："+data.getInteger("total").toString());
            }
            @Override
            public void onFailure(int errorType, String message){

            }
        });
    }
}
