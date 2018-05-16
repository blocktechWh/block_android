
package com.blocktechwh.app.block.Activity.Actions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.List;

public class PlayersSelectListActivity extends TitleActivity{


    private RecyclerView mRecyclerView;
    private PlayerListAdapter mAdapter;
    //private List<Map<String,Object>> mDatas;
    private List<User> mDatas ;
    private ArrayList<Integer>checkdeArray;
    private Integer id;
    private Button playerAddSure;
    private String img;
    private int checkedPosition;
    private LinearLayout titlebar_button_back;
    private LinearLayout ll_no_contact_container;
    private Button bt_back;
    private LinearLayout scr_contact_container;
    private FragmentManager manager;
    private FragmentTransaction transaction;
    private JSONArray dataList;
    private SwipeRefreshLayout swipeRefreshLayout ;
    private TextView tv_to_top ;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_player_select);
            initTitle("参与人员");
        App.getInstance().addActivity(this);
        initView();
        getData();
        addEvent();

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
    private void initView(){
        id=-1;
        scr_contact_container=(LinearLayout) findViewById(R.id.scr_contact_container);
        tv_to_top=(TextView) findViewById(R.id.tv_to_top);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.srl_main);
        swipeRefreshLayout.setRefreshing(true);

        ll_no_contact_container=(LinearLayout) findViewById(R.id.ll_no_contact_container);
        bt_back=(Button) findViewById(R.id.bt_back);
        titlebar_button_back=(LinearLayout)findViewById(R.id.titlebar_button_back);
        mDatas= new ArrayList<User>();
        dataList=new JSONArray();
        checkdeArray=new ArrayList<Integer>();
        playerAddSure=(Button) findViewById(R.id.btn_add_sure);
        mRecyclerView = (RecyclerView)findViewById(R.id.id_players_recycler);
        MyLinearLayoutManager myLinearLayoutManager = new MyLinearLayoutManager (App.getContext());
        mRecyclerView.setLayoutManager(myLinearLayoutManager );

    }
    private void goBack(){
        Bundle bundle=new Bundle();
        bundle.putInt("id",0);
        bundle.putInt("index",1);
        bundle.putString("imgUrl","");
        Intent intent=new Intent(PlayersSelectListActivity.this,StartActivity.class);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_player_contact, parent,
                    false));
            return holder;
        }


        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position){
            //holder.tv.setText(mDatas.get(position).getName());
            String url;

            holder.tv.setText(dataList.getJSONObject(position).getString("name"));
            holder.id=dataList.getJSONObject(position).getInteger("id");

            url = Urls.HOST + "staticImg" + dataList.getJSONObject(position).getString("img");
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.iv.setImageBitmap(bmp);
                }
            });
            if(position==0){
                holder.rb.setChecked(true);
            }
            holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!buttonView.isPressed()){
                        //加这一条，否则当我setChecked()时会触发此listener
                        return;
                    }
                    if(isChecked){
                        if(id>0){
                            System.out.println("checkedPosition="+checkedPosition);
                            //先前被选中的RadioButton取消选中
                            RecyclerView.ViewHolder viewHolder=mRecyclerView.findViewHolderForAdapterPosition(checkedPosition);
                            if(viewHolder!=null){
                                RadioButton rbChecked=viewHolder.itemView.findViewById(R.id.rb_voter);
                                rbChecked.setChecked(false);
                            }
                        }else{

                        }

                        checkedPosition=position;//将新的被选中的RadioButton位置赋值给checkedPosition
                        id=holder.id;
                        img=Urls.HOST + "staticImg" + dataList.getJSONObject(position).getString("img");

                    }
                }
            });

            holder.myListItem.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    holder.rb.setChecked(true);
                    if(id>0){
                        System.out.println("checkedPosition="+checkedPosition);
                       // mRecyclerView.setItemViewCacheSize(dataList.size());
                        //先前被选中的RadioButton取消选中
                        MyViewHolder viewHolder=(MyViewHolder)mRecyclerView.findViewHolderForAdapterPosition(checkedPosition);
                        //RadioButton rbChecked=viewHolder.itemView.findViewById(R.id.rb_voter);
                        if(viewHolder!=null){
                            viewHolder.rb.setChecked(false);
                        }

                    }

                    checkedPosition=position;//将新的被选中的RadioButton位置赋值给checkedPosition
                    id=holder.id;
                    img=Urls.HOST + "staticImg" + dataList.getJSONObject(position).getString("img");
                }
            });

        }

        @Override
        public int getItemCount(){
            return dataList.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            RadioButton rb;
            ImageView iv;
            Integer id;
            FrameLayout rl_radio_contaner;
            RelativeLayout myListItem;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.itemText_player);
                iv=(ImageView) view.findViewById(R.id.itemImg);
                rb=(RadioButton) view.findViewById(R.id.rb_voter);
                rl_radio_contaner=(FrameLayout) view.findViewById(R.id.rl_radio_contaner);
                myListItem=(RelativeLayout) view.findViewById(R.id.myListItem);
                id=0;
            }
        }
    }

    private void addEvent(){


        bt_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        //确定添加
        playerAddSure.setOnClickListener(addSure);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //实际项目中这里一般是用网络请求获取数据
                getData();

            }
        });
        //设置列表滑动事件,阻止滑动过程中RadioButton选中的错乱问题，向下滑动提供回到顶部功能
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                LinearLayoutManager manager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int firstVisibleItemPosition = manager.findFirstVisibleItemPosition();
                // 当不滚动时
                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    // 判断是否滚动超过一屏
                    if (firstVisibleItemPosition == 0) {
                        tv_to_top.setVisibility(View.GONE);
                    } else {
                        tv_to_top.setVisibility(View.VISIBLE);
                    }

                } else if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {//拖动中
                    tv_to_top.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                for(int i=0;i<dataList.size();i++){
                    PlayerListAdapter.MyViewHolder viewHolder=(PlayerListAdapter.MyViewHolder)mRecyclerView.findViewHolderForAdapterPosition(i);
                    if(viewHolder!=null){
                        RadioButton rbChecked=viewHolder.itemView.findViewById(R.id.rb_voter);

                        if(checkedPosition==i){
                            rbChecked.setChecked(true);
                        }else{
                            rbChecked.setChecked(false);
                        }
                    }


                }
            }
        });

        //点击回到顶部按钮回到顶部
        tv_to_top.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRecyclerView.smoothScrollToPosition(0);
            }
        });

    }

    private View.OnClickListener addSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            if(id>0){
                System.out.println("id="+id);
                App.voteInfo.getCheckedRadioButtonList().add(checkedPosition);
                Bundle bundle=new Bundle();
                bundle.putInt("id",id);
                bundle.putInt("index",1);
                bundle.putString("imgUrl",img);
                Intent intent=new Intent(PlayersSelectListActivity.this,StartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                PlayersSelectListActivity.this.finish();
            }else{
                Toast.makeText(PlayersSelectListActivity.this,"请先选择受益人",Toast.LENGTH_SHORT).show();
            }

        }
    };

    private void getData(){
        //请求查询联系人
        HttpClient.get(this, Urls.Contacts, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                dataList=data;
                JSONObject jo_user=new JSONObject();
                jo_user.put("name", JSONObject.parseObject(PreferencesUtils.getString(PlayersSelectListActivity.this,"UserInfo",""), User.class).getName());
                jo_user.put("id",App.userInfo.getId());
                jo_user.put("img",App.userInfo.getImg());
                dataList.add(0,jo_user);
                System.out.println("受益人列表="+dataList);

                id=dataList.getJSONObject(0).getInteger("id");
                img=Urls.HOST + "staticImg" + dataList.getJSONObject(0).getString("img");
                checkedPosition=0;
                swipeRefreshLayout.setRefreshing(false);//取消刷新效果
                mRecyclerView.setLayoutManager(new LinearLayoutManager(PlayersSelectListActivity.this));
                mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());

                mAdapter.notifyDataSetChanged();
            }
        });
    }

    public class MyLinearLayoutManager extends LinearLayoutManager {
        public MyLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public boolean supportsPredictiveItemAnimations() {
            return false;
        }

        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();

            }
        }

        @Override
        public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                return super.scrollVerticallyBy(dy, recycler, state);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //getData();
    }
}

