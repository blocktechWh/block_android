package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Bean.VoteInfo;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/11/11.
 */

public class AddPlayerActivity extends TitleActivity {
    private RecyclerView mRecyclerView;
    private PlayerListAdapter mAdapter;
    private Button addSure;
    private LinearLayout addPlayer;
    private Button btnAddSure;
    private Integer id=0;
    private String imgUrl="";
    private ImageView ll_player_img;
    private TextView tvToAddAction;
    private List<Integer>ids=new ArrayList<Integer>();
    private List<Map<String,Object>> mDatas = new ArrayList<Map<String,Object>>();
    private List<String> imgUrls = VoteInfo.getImgUrls();
    private EditText et_action_input;
    private Switch sIfSingle;
    private Switch sIfNoSee;
    private boolean isLimited=false;
    private boolean isAnonymous=true;
    private LinearLayout ll_option_add_button;
    private Bundle bundle;
    private ImageButton titlebar_button_back;
    private TextView tv_clear;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_action_add);
        initTitle("添加投票项");
        initData();

        bundle=this.getIntent().getExtras();
        if(bundle!=null){
            id=bundle.getInt("id");
            imgUrl=bundle.getString("imgUrl");
            imgUrls.add(imgUrl);
            VoteInfo.setImgUrls(imgUrls);

            imgUrl = imgUrl.toString();
            //Toast.makeText(AddPlayerActivity.this,"imgUrl="+imgUrl,Toast.LENGTH_SHORT).show();
            HttpClient.getImage(this, imgUrl, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    //Toast.makeText(AddPlayerActivity.this,"url="+url,Toast.LENGTH_SHORT).show();
                    ll_player_img.setImageBitmap(bmp);
                }
            });
            ll_option_add_button.setVisibility(View.GONE);
            ll_player_img.setVisibility(View.VISIBLE);
        }

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

    private void initData(){

        //从VoteInfo中拿到mDatas
        mDatas=VoteInfo.getOptions();
        tv_clear=(TextView) findViewById(R.id.tv_clear);
        titlebar_button_back=(ImageButton)findViewById(R.id.titlebar_button_back);
        ll_option_add_button=(LinearLayout) findViewById(R.id.ll_option_add_button);
        et_action_input=(EditText) findViewById(R.id.et_action_input);
        addSure=(Button) findViewById(R.id.btn_add_sure);
        addPlayer=(LinearLayout) findViewById(R.id.ll_to_add_player);
        ll_player_img=(ImageView) findViewById(R.id.ll_player_img);
        ll_player_img.setVisibility(View.GONE);
        tvToAddAction=(TextView) findViewById(R.id.tv_to_add_action);
        btnAddSure=(Button) findViewById(R.id.btn_add_sure);



        mRecyclerView = (RecyclerView)findViewById(R.id.id_action_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());
    }
    private void goBack(){
        Intent intent = new Intent(AddPlayerActivity.this,StartActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
        finish();
    }


    class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(App.getContext()).inflate(R.layout.item_action, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position){
            Map<String,Object> map=mDatas.get(position);
            holder.tv.setText((position+1)+"."+map.get("item").toString());
            System.out.println("holder.tv="+holder.tv.getText());

            if(!imgUrls.isEmpty()){
                String url = imgUrls.get(position).toString();

               // Toast.makeText(AddPlayerActivity.this,"222url="+url,Toast.LENGTH_SHORT).show();

                HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                    @Override
                    public void onSuccess(final Bitmap bmp) {
                        holder.playerImg1.setImageBitmap(bmp);
                    }
                });
            }


        }



        @Override
        public int getItemCount(){
            return mDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
           TextView tv;
           ImageView playerImg1;

            public MyViewHolder(View view)
            {
                super(view);
                tv=view.findViewById(R.id.tv_input1);
                playerImg1=view.findViewById(R.id.ll_player_img1);
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
        //清除内容
        tv_clear.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                //清空输入框和图片
                if(!et_action_input.getText().toString().trim().equals("")){
                    et_action_input.setText("");
                }
                if(ll_player_img.getVisibility()==View.VISIBLE){
                    ll_player_img.setImageResource(0);
                    ll_option_add_button.setVisibility(View.VISIBLE);
                    ll_player_img.setVisibility(View.GONE);

                    //从checkedRadioButtonList删除所清除的对象
                    List<Integer>checkedRadioButtonList=VoteInfo.getCheckedRadioButtonList();
                    System.out.println("checkedRadioButtonList"+checkedRadioButtonList);
                    checkedRadioButtonList.remove(checkedRadioButtonList.size()-1);
                    VoteInfo.setCheckedRadioButtonList(checkedRadioButtonList);
                }


            }
        });

        //添加活动项
        tvToAddAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(et_action_input.getText().toString().trim().equals("")){
                    Toast.makeText(AddPlayerActivity.this,"还未填写投票项描述",Toast.LENGTH_SHORT).show();

                }else if(ll_player_img.getVisibility()==View.GONE){
                    Toast.makeText(AddPlayerActivity.this,"还未选择受益人",Toast.LENGTH_SHORT).show();

                }else{
                    mDatas=VoteInfo.getOptions();
                    ll_option_add_button.setVisibility(View.VISIBLE);
                    ll_player_img.setVisibility(View.GONE);

                    String item=et_action_input.getText().toString();
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("item",item);
                    map.put("suppler",id);
                    mDatas.add(map);

                    //将活动信息存入VoteInfo
                    VoteInfo.setOptions(mDatas);

                    //重新配置列表
                    mAdapter.notifyDataSetChanged();

                    //清空输入框和图片
                    et_action_input.setText("");
                    ll_player_img.setImageResource(0);
                }
            }


        });


        //跳转到选择受益者页面
        addPlayer.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Bundle bundle=new Bundle();
                bundle.putInt("stateIndex",1);
                Intent intent = new Intent(AddPlayerActivity.this,PlayersSelectListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //确定添加
        btnAddSure.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(!et_action_input.getText().toString().trim().equals("")&&ll_player_img.getVisibility()==View.VISIBLE){
                    String item=et_action_input.getText().toString();
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("item",item);
                    map.put("suppler",id);
                    mDatas.add(map);
                    //将活动信息存入VoteInfo
                    VoteInfo.setOptions(mDatas);
                }else if(et_action_input.getText().toString().trim().equals("")&&ll_player_img.getVisibility()==View.VISIBLE){
                    Toast.makeText(AddPlayerActivity.this,"还未填写投票项描述",Toast.LENGTH_SHORT).show();
                    return;

                }else if(!et_action_input.getText().toString().trim().equals("")&&ll_player_img.getVisibility()==View.GONE){
                    Toast.makeText(AddPlayerActivity.this,"还未选择受益人",Toast.LENGTH_SHORT).show();
                    return;
                }

                Bundle bundle=new Bundle();
                bundle.putInt("id",id);
                Intent intent=new Intent(AddPlayerActivity.this,StartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();

            }
        });

    }

}
