package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Activity.Actions.PlayersSelectListActivity;
import com.blocktechwh.app.block.Activity.Actions.StartActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartVoteSecondStepFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private PlayerListAdapter mAdapter;
    private Button addSure;
    private LinearLayout addPlayer;
    private Button btnAddSure;
    private Integer id=0;
    private String imgUrl="";
    private ImageView ll_player_img;
    private RelativeLayout tvToAddAction;
    private List<Integer> ids=new ArrayList<Integer>();
    private List<Map<String,Object>> mDatas = new ArrayList<Map<String,Object>>();
    private List<String> imgUrls = App.voteInfo.getImgUrls();
    private EditText et_action_input;
    private Switch sIfSingle;
    private Switch sIfNoSee;
    private boolean isLimited=false;
    private boolean isAnonymous=true;
    private LinearLayout ll_option_add_button;
    private Bundle bundle;
    private LinearLayout titlebar_button_back;
    private LinearLayout ll_next_container;
    private TextView tv_clear;

    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    private TextView tv_vote_list;
    private RelativeLayout rl_vote_list;

    private StartVoteFirstStepFragment svfsf;
    private StartVoteSecondStepFragment svssf;
    private StartVoteThirdStepFragment svtsf;
    private StartVoteFouthStepFragment svfosf;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_start_vote_2, container, false);
        initData();

        bundle=getActivity().getIntent().getExtras();
        if(bundle!=null){
            id=bundle.getInt("id");
            imgUrl=bundle.getString("imgUrl");
            imgUrls.add(imgUrl);
            App.voteInfo.setImgUrls(imgUrls);

            if(!imgUrl.equals("")){
                imgUrl = imgUrl.toString();
                HttpClient.getImage(this, imgUrl, new CallBack<Bitmap>() {
                    @Override
                    public void onSuccess(final Bitmap bmp) {
                        ll_player_img.setImageBitmap(bmp);
                    }
                });
                ll_option_add_button.setVisibility(View.GONE);
                addPlayer.setOnClickListener(null);
                ll_player_img.setVisibility(View.VISIBLE);
            }

        }else{
            addPlayer.setOnClickListener(getPlayer);
        }

        addEvent();

        return view;
    }

    private void initData(){

        //从VoteInfo中拿到mDatas
        mDatas=App.voteInfo.getOptions();
        tv_clear=(TextView) view.findViewById(R.id.tv_clear);
        titlebar_button_back=(LinearLayout)view.findViewById(R.id.titlebar_button_back);
        ll_option_add_button=(LinearLayout) view.findViewById(R.id.ll_option_add_button);
        et_action_input=(EditText) view.findViewById(R.id.et_action_input);
        et_action_input.setText(PreferencesUtils.getString(getContext(),"optionName",""));
        addSure=(Button) view.findViewById(R.id.btn_add_sure);
        addPlayer=(LinearLayout) view.findViewById(R.id.ll_to_add_player);
        ll_next_container=(LinearLayout) view.findViewById(R.id.ll_next_container);
        ll_player_img=(ImageView) view.findViewById(R.id.ll_player_img);
        ll_player_img.setVisibility(View.GONE);
        tvToAddAction=(RelativeLayout) view.findViewById(R.id.rl_to_add_action);
        btnAddSure=(Button) view.findViewById(R.id.btn_add_sure);
        tv_vote_list=(TextView) view.findViewById(R.id.tv_vote_list);
        rl_vote_list=(RelativeLayout) view.findViewById(R.id.rl_vote_list);


        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_action_recycler);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());

        fragmentManager = getActivity().getSupportFragmentManager();
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

                HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                    @Override
                    public void onSuccess(final Bitmap bmp) {
                        holder.playerImg1.setImageBitmap(bmp);
                    }
                });
            }

            holder.tv_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mDatas=App.voteInfo.getOptions();
                    mDatas.remove(position);
                    imgUrls.remove(position);
                    App.voteInfo.setImgUrls(imgUrls);
                    //将活动信息存入VoteInfo
                    App.voteInfo.setOptions(mDatas);
                    mDatas=App.voteInfo.getOptions();
                    mAdapter.notifyDataSetChanged();
                    //判断是否有投票项列表
                    if(mDatas.size()>=1){
                        rl_vote_list.setVisibility(View.VISIBLE);
                    }else{
                        rl_vote_list.setVisibility(View.GONE);
                    }
                    //判断是否达到进行下一步的条件
                    if(mDatas.size()>=2){
                        ll_next_container.setVisibility(View.VISIBLE);
                    }else{
                        ll_next_container.setVisibility(View.GONE);

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
            TextView tv_remove;
            ImageView playerImg1;

            public MyViewHolder(View view)
            {
                super(view);
                tv=view.findViewById(R.id.tv_input1);
                tv_remove=view.findViewById(R.id.tv_remove);
                playerImg1=view.findViewById(R.id.ll_player_img1);
            }
        }
    }


    private void addEvent(){

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
                    List<Integer>checkedRadioButtonList=App.voteInfo.getCheckedRadioButtonList();
                    System.out.println("checkedRadioButtonList"+checkedRadioButtonList);
                    checkedRadioButtonList.remove(checkedRadioButtonList.size()-1);
                    App.voteInfo.setCheckedRadioButtonList(checkedRadioButtonList);
                }


            }
        });

        //添加活动项
        tvToAddAction.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                if(et_action_input.getText().toString().trim().equals("")){
                    Toast.makeText(getContext(),"还未填写投票项描述",Toast.LENGTH_SHORT).show();

                }else if(ll_player_img.getVisibility()==View.GONE){
                    Toast.makeText(getContext(),"还未选择受益人",Toast.LENGTH_SHORT).show();

                }else{
                    mDatas=App.voteInfo.getOptions();
                    ll_option_add_button.setVisibility(View.VISIBLE);
                    ll_player_img.setVisibility(View.GONE);

                    String item=et_action_input.getText().toString().trim();
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("item",item);
                    map.put("suppler",id);
                    mDatas.add(map);

                    //将活动信息存入VoteInfo
                    App.voteInfo.setOptions(mDatas);

                    //重新配置列表
                    mDatas=App.voteInfo.getOptions();
                    mAdapter.notifyDataSetChanged();

                    //清空输入框和图片
                    et_action_input.setText("");
                    ll_player_img.setImageResource(0);

                    //判断是否有投票项列表
                    if(mDatas.size()>=1){
                        rl_vote_list.setVisibility(View.VISIBLE);
                    }else{
                        rl_vote_list.setVisibility(View.GONE);

                    }

                    //判断是否达到进行下一步的条件
                    if(mDatas.size()>=2){
                        ll_next_container.setVisibility(View.VISIBLE);
                    }else{
                        ll_next_container.setVisibility(View.GONE);

                    }

                }
            }


        });


        //跳转到选择受益者页面
        addPlayer.setOnClickListener(getPlayer);


        //确定添加(下一步)
        btnAddSure.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {

/*                if(!et_action_input.getText().toString().trim().equals("")&&ll_player_img.getVisibility()==View.VISIBLE){
                    String item=et_action_input.getText().toString().trim();
                    Map<String,Object> map=new HashMap<String, Object>();
                    map.put("item",item);
                    map.put("suppler",id);
                    mDatas.add(map);
                    //将活动信息存入VoteInfo
                    App.voteInfo.setOptions(mDatas);
                }
                if(et_action_input.getText().toString().trim().equals("")&&ll_player_img.getVisibility()==View.VISIBLE){
                    Toast.makeText(getContext(),"还未填写投票项描述",Toast.LENGTH_SHORT).show();
                    return;

                }
                if(!et_action_input.getText().toString().trim().equals("")&&ll_player_img.getVisibility()==View.GONE){
                    Toast.makeText(getContext(),"还未选择受益人",Toast.LENGTH_SHORT).show();
                    return;
                }*/
                if(App.voteInfo.getOptions().size()<=0){
                    Toast.makeText(getContext(),"还未设置投票项目",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(App.voteInfo.getOptions().size()<2){
                    Toast.makeText(getContext(),"投票项目数不能少于2，请继续添加",Toast.LENGTH_SHORT).show();
                    return;
                }

                //标记第二部完成
                App.voteInfo.setSecondStepFinished(true);

                //跳转
                StartActivity sa=(StartActivity)getActivity();
                sa.SelectFragment(2);

                //清空缓存
                PreferencesUtils.putString(App.getContext(),"optionName","");

                //标记完成
                TextView btn_add_option=(TextView)getActivity().findViewById(R.id.btn_add_option);
                btn_add_option.setBackground(getResources().getDrawable(R.drawable.lines_bg6));
                btn_add_option.setText("✔");
                TextView btn_select_voters=(TextView)getActivity().findViewById(R.id.btn_select_voters);
                btn_select_voters.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
               // btn_select_voters.setTextColor(Color.argb(255,66,143,219));
            }
        });

    }
    private void hideFragment(FragmentTransaction fragmentTransaction){
        if (svfsf!=null) {
            fragmentTransaction.hide(svfsf);
        }
        if (svssf!=null) {
            fragmentTransaction.hide(svssf);
        }
        if (svtsf!=null) {
            fragmentTransaction.hide(svtsf);
        }
        if (svfosf!=null) {
            fragmentTransaction.hide(svfosf);
        }

    }
    private View.OnClickListener getPlayer = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            String etActionInput=et_action_input.getText().toString().trim();
            PreferencesUtils.putString(App.getContext(),"optionName",etActionInput);

            Bundle bundle=new Bundle();
            bundle.putInt("stateIndex",1);
            Intent intent = new Intent(getContext(),PlayersSelectListActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };


}
