package com.blocktechwh.app.block.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.User.LoginActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.BaseActivity;
import com.blocktechwh.app.block.Fragment.ContactFragment;
import com.blocktechwh.app.block.Fragment.HomeFragment;
import com.blocktechwh.app.block.Fragment.UserFragment;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;
import com.blocktechwh.app.block.Utils.SupportMultipleScreensUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity{

    private TabLayout mTabHost;
    private List<String> tab_texts = Arrays.asList("主页","联系人","我的");
    private List<Integer> tab_icons = Arrays.asList( R.mipmap.tab_home, R.mipmap.tab_contact, R.mipmap.tab_user );
    private List<Integer> tab_active_icons = Arrays.asList( R.mipmap.tab_home_pressed, R.mipmap.tab_contact_pressed, R.mipmap.tab_user_pressed );
    private List<String> lists_indicators = Arrays.asList("","","");
    private List<Fragment> lists_fragment = new ArrayList<>();
    private ViewPager mViewPager;
    private ContentPagerAdapter contentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }
    // 在onKeyDown(int keyCode, KeyEvent event)方法中调用此方法
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            onBackPressed();
        }
        return false;
    }

    public void onBackPressed() {
        new AlertDialog.Builder(this).setTitle("确认退出吗？")
                .setIcon(android.R.drawable.ic_dialog_info)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“确认”后的操作
                        //SettingActivity.this.finish();
                        logOut();

                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 点击“返回”后的操作,这里不设置没有任何操作
                    }
                }).show();
    }

    private void logOut(){
        String url = Urls.Logout;
        HttpClient.get(this, url, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
            }
            @Override
            public void onFailure(int errorType, String message){
            }
        });
        App.token = "";
        PreferencesUtils.putString(App.getContext(),"Token","");

        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }

    private void initView() {
        lists_fragment.add(new HomeFragment());
        lists_fragment.add(new ContactFragment());
        lists_fragment.add(new UserFragment());

        mViewPager=(ViewPager) findViewById(R.id.container1);
        mTabHost=(TabLayout)findViewById(R.id.tabHost);

        SupportMultipleScreensUtil.init(getApplicationContext());
        SupportMultipleScreensUtil.scale(mTabHost);
        setContent();
    }

    private void setContent() {
        contentAdapter = new ContentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(contentAdapter);
        mTabHost.setTabMode(TabLayout.MODE_SCROLLABLE);
        ViewCompat.setElevation(mTabHost,10);
        mTabHost.setupWithViewPager(mViewPager);


        for(int i=0;i<mTabHost.getTabCount();i++){
            mTabHost.getTabAt(i).setCustomView(getTabItemView(i));
        }
        mTabHost.setOnClickListener(mTabOnClickListener);
        mTabHost.setTabMode(TabLayout.MODE_FIXED);
    }

    class ContentPagerAdapter extends FragmentPagerAdapter {

        public ContentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return lists_fragment.get(position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return lists_indicators.get(position);

        }
    }

    private View.OnClickListener mTabOnClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            for(int i=0;i<mTabHost.getTabCount();i++){
                inactiveTab(mTabHost.getTabAt(i).getCustomView());
            }
            activeTab(view);
            int pos = (int) view.getTag();
            TabLayout.Tab tab = mTabHost.getTabAt(pos);
            tab.select();
        }
    };

    private View getTabItemView(int index) {
        View view = getLayoutInflater().inflate(R.layout.item_tab_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.item_icon);
        TextView textView=(TextView)view.findViewById(R.id.item_text);
        textView.setText(tab_texts.get(index));
        view.setTag(index);
        if(mTabHost.getSelectedTabPosition() == index){
            activeTab(view);
        }else{
            inactiveTab(view);
        }
        view.setOnClickListener(mTabOnClickListener);
        SupportMultipleScreensUtil.scale(view);
        return view;
    }

    private void activeTab(View view){
        ImageView imageView = (ImageView) view.findViewById(R.id.item_icon);
        TextView textView=(TextView)view.findViewById(R.id.item_text);
        int index = (int) view.getTag();
        imageView.setImageResource(tab_active_icons.get(index));
        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tabActive));
    }

    private void inactiveTab(View view){
        ImageView imageView = (ImageView) view.findViewById(R.id.item_icon);
        TextView textView=(TextView)view.findViewById(R.id.item_text);
        int index = (int) view.getTag();
        imageView.setImageResource(tab_icons.get(index));
        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.tab));
    }

}
