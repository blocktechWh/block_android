
package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.List;

public class PlayersSelectListActivity extends TitleActivity {


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
    private ScrollView scr_contact_container;



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_player_select);
            initTitle("参与人员");

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
        scr_contact_container=(ScrollView) findViewById(R.id.scr_contact_container);
        ll_no_contact_container=(LinearLayout) findViewById(R.id.ll_no_contact_container);
        bt_back=(Button) findViewById(R.id.bt_back);
        titlebar_button_back=(LinearLayout)findViewById(R.id.titlebar_button_back);
        mDatas= new ArrayList<User>();
        checkdeArray=new ArrayList<Integer>();
        playerAddSure=(Button) findViewById(R.id.btn_add_sure);
        mRecyclerView = (RecyclerView)findViewById(R.id.id_players_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());



    }
    private void goBack(){
        Intent intent = new Intent(PlayersSelectListActivity.this,AddPlayerActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
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
            holder.tv.setText(mDatas.get(position).getName());
            holder.id=mDatas.get(position).getId();
            String url = Urls.HOST + "staticImg" + mDatas.get(position).getImg();
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.iv.setImageBitmap(bmp);
                }
            });

//            if(position==0){
//                checkedPosition=0;
//                holder.rb.setChecked(true);
//                id=holder.id;
//                img=Urls.HOST + "staticImg" + mDatas.get(position).getImg();
//            }

            //去掉已被选择的项
            for(int i=0;i<App.voteInfo.getCheckedRadioButtonList().size();i++){
                if(App.voteInfo.getCheckedRadioButtonList().get(i)==position){
                    holder.rl_radio_contaner.setVisibility(View.GONE);
                }
            }




            holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        if(id>0){
                            //先前被选中的RadioButton取消选中
                            RecyclerView.ViewHolder viewHolder=mRecyclerView.findViewHolderForAdapterPosition(checkedPosition);
                            RadioButton rbChecked=viewHolder.itemView.findViewById(R.id.rb_voter);
                            rbChecked.setChecked(false);
                        }

                        checkedPosition=position;//将新的被选中的RadioButton位置赋值给checkedPosition
                        id=holder.id;
                        img=Urls.HOST + "staticImg" + mDatas.get(position).getImg();

                    }
                }
            });

        }

        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            RadioButton rb;
            ImageView iv;
            Integer id;
            RelativeLayout rl_radio_contaner;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.itemText_player);
                iv=(ImageView) view.findViewById(R.id.itemImg);
                rb=(RadioButton) view.findViewById(R.id.rb_voter);
                rl_radio_contaner=(RelativeLayout) view.findViewById(R.id.rl_radio_contaner);
                id=0;
            }
        }
    }

    private void addEvent(){
        //返回
        titlebar_button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        bt_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        //确定添加
        playerAddSure.setOnClickListener(addSure);
    }
    private View.OnClickListener addSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            if(id>0){
                System.out.println("id="+id);
                App.voteInfo.getCheckedRadioButtonList().add(checkedPosition);
                Bundle bundle=new Bundle();
                bundle.putInt("id",id);
                bundle.putString("imgUrl",img);
                Intent intent=new Intent(PlayersSelectListActivity.this,AddPlayerActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
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
                mDatas = data.toJavaList(User.class);

                System.out.println("getCheckedRadioButtonList="+App.voteInfo.getCheckedRadioButtonList());
                //去点之前选中的收益人
                for(int i=0;i< App.voteInfo.getCheckedRadioButtonList().size();i++){
                    mAdapter.notifyItemRemoved(App.voteInfo.getCheckedRadioButtonList().get(i));

                    //mDatas.remove(App.voteInfo.getCheckedRadioButtonList().get(i));
                }
                mAdapter.notifyDataSetChanged();

                //判断已选列表长度是否与好友列表长度相等
                if(App.voteInfo.getCheckedRadioButtonList().size()==mDatas.size()){
                    ll_no_contact_container.setVisibility(View.VISIBLE);
                    scr_contact_container.setVisibility(View.GONE);
                }else{
                    ll_no_contact_container.setVisibility(View.GONE);
                    scr_contact_container.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        getData();
    }
}

