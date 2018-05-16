package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
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
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Actions.StartActivity;
import com.blocktechwh.app.block.Activity.Actions.VotesListActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.FastScrollManger;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StartVoteThirdStepFragment extends Fragment {

    private View view;
    private RecyclerView mRecyclerView;
    private PlayerListAdapter mAdapter;
    private JSONArray dataList;
    private ArrayList<Integer> checkdeArray;
    private Button votersAddSure;
    private static List<Map<String,Object>>playerList;
    private List<Integer> votersTargetList;
    private List<Integer>checkedPositionList;
    private CheckBox cb_select_all;
    private LinearLayout titlebar_button_back;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    private boolean isAllSelect;
    private boolean isSelectAllCheched;
    private StartVoteFirstStepFragment svfsf;
    private StartVoteSecondStepFragment svssf;
    private StartVoteThirdStepFragment svtsf;
    private StartVoteFouthStepFragment svfosf;
    private int selectedCount;
    private int totalCount;
    private TextView tv_vote_percent;
    private TextView tv_to_top ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_start_vote_3, container, false);
        initView();
        getData();
        addEvent();

        return view;
    }


    private void initView(){
        isAllSelect=false;
        isSelectAllCheched=false;
        playerList=new ArrayList<>();
        votersTargetList=new ArrayList<>();
        checkedPositionList=new ArrayList<>();
        dataList=new JSONArray();

        checkdeArray=new ArrayList<Integer>();
        votersAddSure=(Button) view.findViewById(R.id.btn_add_sure);
        if(!App.voteInfo.getIfSetReward()){
            votersAddSure.setText("发起投票");
        }
        selectedCount=0;//已投人数初始化为0

        tv_to_top=(TextView) view.findViewById(R.id.tv_to_top);
        titlebar_button_back=(LinearLayout)view.findViewById(R.id.titlebar_button_back);
        //cb_select_all=(CheckBox) view.findViewById(R.id.cb_select_all);
        tv_vote_percent=(TextView) view.findViewById(R.id.tv_vote_percent);

        fragmentManager = getActivity().getSupportFragmentManager();
        mRecyclerView = (RecyclerView)view.findViewById(R.id.id_players_recycler);

    }

    class PlayerListAdapter extends RecyclerView.Adapter<PlayerListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_voters_contact, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position){
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

            //去掉已被选择的项
//            for(int i=0;i<App.voteInfo.getCheckedPositionList().size();i++){
//                if(App.voteInfo.getCheckedPositionList().get(i)==position){
//                    holder.rl_radio_contaner.setVisibility(View.GONE);
//                    totalCount-=1;
//                }
//            }

/*            if(isSelectAllCheched){
                //判断是否全选
                if(cb_select_all.isChecked()){
                    holder.rb.setChecked(true);

                }else{
                    holder.rb.setChecked(false);
                }

            }else{

            }*/


            holder.rb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(!buttonView.isPressed()){
                        return;
                    }	//加这一条，否则当我setChecked()时会触发此listener

                    if(isChecked){
                        selectedCount+=1;
                        System.out.println("selectedCount="+selectedCount);
                        checkedPositionList.add(position);
                        votersTargetList.add(dataList.getJSONObject(position).getInteger("id"));
                        Map<String,Object> map_player=new HashMap<>();
                        map_player.put("id",dataList.getJSONObject(position).getInteger("id"));
                        map_player.put("img",Urls.HOST + "staticImg" + dataList.getJSONObject(position).getString("img"));
                        playerList.add(map_player);

                    }else{
                        selectedCount-=1;
                        System.out.println("selectedCount="+selectedCount);
                        for(int i=0;i<checkedPositionList.size();i++){
                            if(position==checkedPositionList.get(i)){
                                votersTargetList.remove(i);
                                checkedPositionList.remove(i);
                                playerList.remove(i);
                            }
                        }
                    }
                    tv_vote_percent.setText("已选 "+selectedCount+"/"+totalCount+"");

                }
            });

            holder.myListItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.rb.isChecked()){
                        holder.rb.setChecked(false);
                        selectedCount-=1;
                        System.out.println("selectedCount="+selectedCount);
                        for(int i=0;i<checkedPositionList.size();i++){
                            if(position==checkedPositionList.get(i)){
                                votersTargetList.remove(i);
                                checkedPositionList.remove(i);
                                playerList.remove(i);
                            }
                        }
                        tv_vote_percent.setText("已选 "+selectedCount+"/"+totalCount+"");

                    }else{
                        holder.rb.setChecked(true);
                        selectedCount+=1;
                        System.out.println("selectedCount="+selectedCount);
                        checkedPositionList.add(position);
                        votersTargetList.add(dataList.getJSONObject(position).getInteger("id"));
                        Map<String,Object> map_player=new HashMap<>();
                        map_player.put("id",dataList.getJSONObject(position).getInteger("id"));
                        map_player.put("img",Urls.HOST + "staticImg" + dataList.getJSONObject(position).getString("img"));
                        playerList.add(map_player);
                        tv_vote_percent.setText("已选 "+selectedCount+"/"+totalCount+"");
                    }
                }
            });

        }

        @Override
        public int getItemCount(){
            return dataList.size();

        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv;
            CheckBox rb;
            ImageView iv;
            RelativeLayout rl_radio_contaner;
            RelativeLayout myListItem;
            Integer id;

            public MyViewHolder(View view)
            {
                super(view);
                tv = (TextView) view.findViewById(R.id.itemText_player);
                iv=(ImageView) view.findViewById(R.id.itemImg);
                rb=(CheckBox) view.findViewById(R.id.rb_voter);
                rl_radio_contaner=(RelativeLayout) view.findViewById(R.id.rl_radio_contaner);
                myListItem=(RelativeLayout) view.findViewById(R.id.myListItem);
                id=0;
            }
        }
    }



    private void addEvent(){
        //确定添加
        votersAddSure.setOnClickListener(addSure);

        //全选
        //cb_select_all.setOnCheckedChangeListener(selsectAll);

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
                        CheckBox rbChecked=viewHolder.itemView.findViewById(R.id.rb_voter);
                        rbChecked.setChecked(false);
                    }
                }

                for(int j=0;j<checkedPositionList.size();j++){
                    PlayerListAdapter.MyViewHolder viewHolder=(PlayerListAdapter.MyViewHolder)mRecyclerView.findViewHolderForAdapterPosition(checkedPositionList.get(j));
                    if(viewHolder!=null){
                        CheckBox rbChecked=viewHolder.itemView.findViewById(R.id.rb_voter);
                        rbChecked.setChecked(true);
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
    private CompoundButton.OnCheckedChangeListener selsectAll=new CompoundButton.OnCheckedChangeListener(){
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            isSelectAllCheched=true;
            if(isChecked){
                selectedCount=0;
                tv_vote_percent.setText("已选 "+totalCount+"/"+totalCount+"");
                isAllSelect=true;
                mAdapter.notifyDataSetChanged();

            }else{
                selectedCount=0;
                tv_vote_percent.setText("已选 0/"+totalCount+"");
                isAllSelect=false;
                votersTargetList.clear();
                checkedPositionList.clear();
                playerList.clear();
                mAdapter.notifyDataSetChanged();
            }


        }
    };
    private View.OnClickListener addSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            if(playerList.size()>0){
                //选取投票者渲染列表

                App.voteInfo.setPlayerList(playerList);

                //更新选取checkBox列表
                App.voteInfo.setCheckedPositionList(checkedPositionList);

                //更新投票者列表
                App.voteInfo.setVoteTarget(votersTargetList);
            }else{
                Toast.makeText(getContext(),"请选择投票人",Toast.LENGTH_SHORT).show();
                return;
            }


            //跳转
            if(App.voteInfo.getIfSetReward()){
                App.voteInfo.setThirdStepFinished(true);
                //跳转
                StartActivity sa=(StartActivity)getActivity();
                sa.SelectFragment(3);
            }else{
                StartVoteSubmit();
            }
            
            //标记完成
            TextView btn_select_voters=(TextView)getActivity().findViewById(R.id.btn_select_voters);
            btn_select_voters.setBackground(getResources().getDrawable(R.drawable.lines_bg6));
            btn_select_voters.setText("✔");
            TextView btn_set_reward=(TextView)getActivity().findViewById(R.id.btn_set_reward);
            btn_set_reward.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
            btn_set_reward.setTextColor(Color.argb(255,66,143,219));
        }
    };

    private void StartVoteSubmit(){
        if(playerList.size()>0){
            //选取投票者渲染列表
            App.voteInfo.setPlayerList(playerList);

            //更新选取checkBox列表
            App.voteInfo.setCheckedPositionList(checkedPositionList);

            //更新投票者列表
            App.voteInfo.setVoteTarget(votersTargetList);
        }else{
            Toast.makeText(getContext(),"请选择投票人",Toast.LENGTH_SHORT).show();
            return;
        }

        if(playerList.size()<=0){
            Toast.makeText(getContext(),"请选择投票人",Toast.LENGTH_SHORT).show();
            return;
        }
        final JSONObject json = new JSONObject();

        json.put("voteImg",App.voteInfo.getVoteImg());
//        json.put("voteImg","123");
        json.put("voteTheme",App.voteInfo.getVoteTheme());
        json.put("isLimited",App.voteInfo.getIsLimited());
        json.put("isRaise",App.voteInfo.getIsRaise());
        json.put("isAnonymous",App.voteInfo.getIsAnonymous());
        json.put("voteExpireTime",App.voteInfo.getVoteExpireTime());
        json.put("options",App.voteInfo.getOptions());
        json.put("voteTarget",App.voteInfo.getVoteTarget());

        json.put("voteFee",App.voteInfo.getVoteFee());
        json.put("voteRewardRule",App.voteInfo.getVoteRewardRule());
        System.out.println("json="+json);

        //提交投票
        HttpClient.post(this, Urls.MakeVote, json.toString(), new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                Toast.makeText(getContext(),"发起投票成功",Toast.LENGTH_SHORT).show();
                App.voteInfo.setThirdStepFinished(true);
                Intent intent = new Intent(getContext(),VotesListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
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
    private void getData(){
        //请求查询联系人
        HttpClient.get(this, Urls.Contacts, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                dataList = data;
                JSONObject jo_user=new JSONObject();
                jo_user.put("name",JSONObject.parseObject(PreferencesUtils.getString(getActivity(),"UserInfo",""), User.class).getName());
                jo_user.put("id",App.userInfo.getId());
                jo_user.put("img",App.userInfo.getImg());
                dataList.add(0,jo_user);
                System.out.println("dataList.size()="+dataList.size());

                LinearLayoutManager layout = new FastScrollManger(getActivity(), LinearLayoutManager.VERTICAL, false);
                mRecyclerView.setLayoutManager(layout);//竖直放置
                
                //mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
                mRecyclerView.setAdapter(mAdapter = new PlayerListAdapter());

                mAdapter.notifyDataSetChanged();
                totalCount=dataList.size()-App.voteInfo.getCheckedPositionList().size();//投票者总人数人数为初始化为联系人总数
                tv_vote_percent.setText("已选 "+selectedCount+"/"+totalCount);

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }


}
