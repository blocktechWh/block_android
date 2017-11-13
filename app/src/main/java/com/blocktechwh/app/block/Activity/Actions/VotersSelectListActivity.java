package com.blocktechwh.app.block.Activity.Actions;

import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;

import com.blocktechwh.app.block.Adapter.PlayerListViewAdapter;
import com.blocktechwh.app.block.Bean.Players;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/11/10.
 */

public class VotersSelectListActivity extends TitleActivity {
//    private ListView list;
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        super.initTitle("参与人员");
//        setContentView(R.layout.activity_join_select);
//
//        list = (ListView) findViewById(R.id.VotersListView);
//        //组织数据源
//        List<HashMap<String, String>> mylist = new ArrayList<HashMap<String, String>>();
//        for(int i=0;i<20;i++) {
//            HashMap<String, String> map = new HashMap<String, String>();
//            map.put("itemTitle", "This is Title");
//            map.put("itemText", "This is text");
//            mylist.add(map);
//        }
//        //配置适配器
//        SimpleAdapter adapter = new SimpleAdapter(this,
//                mylist,//数据源
//                R.layout.voters_listitem,//显示布局
//                new String[] {"itemTitle", "itemText"}, //数据源的属性字段
//                new int[] {R.id.itemTitle,R.id.itemText}); //布局里的控件id
//        //添加并且显示
//        list.setAdapter(adapter);
//    }

    private ListView listView;  //声明一个ListView对象
    private List<Players> mlistInfo = new ArrayList<Players>();  //声明一个list，动态存储要显示的信息

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_select);

        listView=(ListView)findViewById(R.id.votersListView);    //将listView与布局对象关联

        setInfo();  //给信息赋值函数，用来测试

        listView.setAdapter(new PlayerListViewAdapter(mlistInfo,new VotersSelectListActivity()));//VotersSelectListActivity.this

        //处理Item的点击事件
        listView.setOnItemClickListener(new OnItemClickListener(){
            public void onItemClick(AdapterView<?> parent, View view,int position, long id) {
                Players getObject = mlistInfo.get(position);   //通过position获取所点击的对象
                int infoId = getObject.getId(); //获取信息id
                String infoTitle = getObject.getTitle();    //获取信息标题
                String infoDetails = getObject.getDetails();    //获取信息详情
                int avatar=getObject.getAvatar();    //获取信息详情

                //Toast显示测试
                Toast.makeText(VotersSelectListActivity.this, "信息ID:"+infoId,Toast.LENGTH_SHORT).show();
            }
        });

        //长按菜单显示
        listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {
            public void onCreateContextMenu(ContextMenu conMenu, View view , ContextMenuInfo info) {
                conMenu.setHeaderTitle("菜单");
                conMenu.add(0, 0, 0, "条目一");
                conMenu.add(0, 1, 1, "条目二");
                conMenu.add(0, 2, 2, "条目三");
            }
        });

    }
    //长按菜单处理函数
    public boolean onContextItemSelected(MenuItem aItem) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)aItem.getMenuInfo();
        switch (aItem.getItemId()) {
            case 0:
                Toast.makeText(VotersSelectListActivity.this, "你点击了条目一",Toast.LENGTH_SHORT).show();
                return true;
            case 1:
                Toast.makeText(VotersSelectListActivity.this, "你点击了条目二",Toast.LENGTH_SHORT).show();

                return true;
            case 2:
                Toast.makeText(VotersSelectListActivity.this, "你点击了条目三",Toast.LENGTH_SHORT).show();
                return true;
        }
        return false;
    }

    public void setInfo(){
        mlistInfo.clear();
        int i=0;
        while(i<10){
            Players information = new Players();
            information.setId(1000+i);
            information.setTitle("标题"+i);
            information.setDetails("详细信息"+i);
            information.setAvatar(R.mipmap.ic_launcher);

            mlistInfo.add(information); //将新的info对象加入到信息列表中
            i++;
        }
    }
}
