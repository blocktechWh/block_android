package com.blocktechwh.app.block.Activity.Actions;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vote_action_add);
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

    private void initData(){
        //从VoteInfo中拿到mDatas
        mDatas=VoteInfo.getOptions();

        ll_option_add_button=(LinearLayout) findViewById(R.id.ll_option_add_button);
        et_action_input=(EditText) findViewById(R.id.et_action_input);
        addSure=(Button) findViewById(R.id.btn_add_sure);
        addPlayer=(LinearLayout) findViewById(R.id.ll_to_add_player);
        ll_player_img=(ImageView) findViewById(R.id.ll_player_img);
        ll_player_img.setVisibility(View.GONE);
        tvToAddAction=(TextView) findViewById(R.id.tv_to_add_action);
        btnAddSure=(Button) findViewById(R.id.btn_add_sure);


        //url = Urls.HOST + "staticImg" + imgUrl.toString();
//        if(imgUrl!=""){
//            imgUrl = imgUrl.toString();
//            Toast.makeText(AddPlayerActivity.this,"11imgUrl="+imgUrl,Toast.LENGTH_SHORT).show();
//        }else{
//            imgUrl = Urls.HOST + "staticImg" + imgUrl.toString();
//            Toast.makeText(AddPlayerActivity.this,"false",Toast.LENGTH_SHORT).show();
//        }



        mRecyclerView = (RecyclerView)findViewById(R.id.id_action_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());
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
            holder.tv.setText(map.get("item").toString());
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


        //添加活动项
        tvToAddAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(et_action_input.getText().toString().trim().equals("")||ll_player_img.getDrawable()==null){
                    Toast.makeText(AddPlayerActivity.this,"还未填写或添加完整信息",Toast.LENGTH_SHORT).show();

                }else{
                    mDatas=VoteInfo.getOptions();

                    System.out.println("长度"+mDatas.size());
                    System.out.println("item"+VoteInfo.getOptions());

                    Toast.makeText(AddPlayerActivity.this,"OK",Toast.LENGTH_LONG).show();

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
                Intent intent = new Intent(AddPlayerActivity.this,VotersSelectListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });

        btnAddSure.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

                if(!et_action_input.getText().toString().trim().equals("")&&ll_player_img.getDrawable()!=null){
                    String item=et_action_input.getText().toString();
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("item",item);
                    map.put("suppler",id);
                    mDatas.add(map);
                    //将活动信息存入VoteInfo
                    VoteInfo.setOptions(mDatas);
                }

                Bundle bundle=new Bundle();
                bundle.putInt("id",id);
                Intent intent=new Intent(AddPlayerActivity.this,StartActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }
    //button19
}
