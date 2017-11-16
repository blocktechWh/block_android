package com.blocktechwh.app.block.Activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.blocktechwh.app.block.CustomView.BaseActivity;
import com.blocktechwh.app.block.Fragment.ContactFragment;
import com.blocktechwh.app.block.Fragment.HomeFragment;
import com.blocktechwh.app.block.Fragment.UserFragment;
import com.blocktechwh.app.block.R;
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
