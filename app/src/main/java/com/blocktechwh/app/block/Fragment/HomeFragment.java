package com.blocktechwh.app.block.Fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Actions.StartActivity;
import com.blocktechwh.app.block.Activity.Actions.VoteDetailActivity;
import com.blocktechwh.app.block.Activity.Actions.VotedDetailActivity;
import com.blocktechwh.app.block.Activity.MainActivity;
import com.blocktechwh.app.block.Activity.RedTicket.GiftActivity;
import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.MaxRecyclerView;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import org.java_websocket.client.WebSocketClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HomeFragment extends Fragment {
    private JSONArray mDatas = new JSONArray();
    private TextView tv_pray;
    private TextView tv_maker_name;
    private TextView tv_create_time;
    private ImageButton id_wallet_image;
    private View view;
    private int id;
    private String tv_pray_text;
    private List<Map<String,Object>> voteDatas = new ArrayList<Map<String,Object>>();//投票数据列表
    private RecyclerView voteRecyclerView=new MaxRecyclerView(App.getContext());//投票列表对象
    private VoteListAdapter voteAdapter;

    private List<Map<String,Object>> redTicketDatas = new ArrayList<Map<String,Object>>();//红包数据列表
    private RecyclerView redTicketRecyclerView=new MaxRecyclerView(App.getContext());//投票列表对象
    private RedTicketListAdapter redTicketAdapter;
    private int voteId;
    private String creater;
    private boolean isRaise;
    private static WebSocketClient client;
    private JSONArray voteWaitData;
    private JSONArray giftWaiteData;
    private TextView tv_start_vote;
    private TextView tv_send_gift;
    private LinearLayout sv_data_list;
    private LinearLayout ll_no_data;
    private SwipeRefreshLayout swipeRefreshLayout ;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_home, container, false);
        initData();
        getData();
        addEvent();

        return view;
    }

    private void initData(){
        tv_pray=view.findViewById(R.id.tv_pray);
        sv_data_list=view.findViewById(R.id.sv_data_list);
        ll_no_data=view.findViewById(R.id.ll_no_data);
        tv_start_vote=view.findViewById(R.id.tv_start_vote);
        tv_send_gift=view.findViewById(R.id.tv_send_gift);
        tv_start_vote.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_start_vote.getPaint().setAntiAlias(true);//抗锯齿
        tv_send_gift.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG); //下划线
        tv_send_gift.getPaint().setAntiAlias(true);//抗锯齿
        tv_maker_name=view.findViewById(R.id.tv_maker_name);
        tv_create_time=view.findViewById(R.id.tv_create_time);
        id_wallet_image=view.findViewById(R.id.id_wallet_image);
        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.srl_main);
        swipeRefreshLayout.setRefreshing(true);
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



    class VoteListAdapter extends RecyclerView.Adapter<VoteListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(App.getContext()).inflate(R.layout.item_vote_wait, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position){
            holder.tvVoteTheme.setText(voteDatas.get(position).get("voteTheme").toString());
            holder.tvCreateTime.setText(voteDatas.get(position).get("voteCreateTime").toString());
            holder.tvVoteMaker.setText(voteDatas.get(position).get("voteMaker").toString());

            //点击投票列表项
            holder.ll_vote_detail.setOnClickListener(toGiftSure=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             voteId=Integer.parseInt(voteDatas.get(position).get("voteId").toString());

            //查询投票是否已投
            HttpClient.get(this, Urls.QueryHasVoted+voteId, null, new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    System.out.print("是否已投返回值="+data);
                    if(data.getString("data")=="false"){
                        //调http://111.231.146.57:20086/front/vote/overview/{voteId}
                        Bundle bundle = new Bundle();
                        bundle.putInt("voteId",voteId);//isRaise
                        bundle.putString("tv_active_name",tv_pray_text);
                        bundle.putString("from","homeFragment");
                        Intent intent= new Intent(getActivity(), VoteDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        //调http://111.231.146.57:20086/front/vote/statis/{voteId}
                        Bundle bundle = new Bundle();
                        bundle.putInt("voteId",voteId);
                        bundle.putBoolean("isOver",false);
                        Intent intent= new Intent(getActivity(), VotedDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                }
            });

                }
            });

        }

        @Override
        public int getItemCount(){
            return voteDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tvVoteTheme,tvCreateTime,tvVoteMaker;
            LinearLayout ll_vote_detail;

            public MyViewHolder(View view)
            {
                super(view);
                tvVoteTheme = (TextView) view.findViewById(R.id.tvVoteTheme);
                tvCreateTime = (TextView) view.findViewById(R.id.tvCreateTime);
                tvVoteMaker = (TextView) view.findViewById(R.id.tvVoteMaker);
                ll_vote_detail = (LinearLayout) view.findViewById(R.id.ll_vote_detail);

            }
        }
    }

    class RedTicketListAdapter extends RecyclerView.Adapter<RedTicketListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(App.getContext()).inflate(R.layout.item_red_ticket_wait, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, final int position){
            if(position<giftWaiteData.size()){
                holder.redTicketTheme.setText(redTicketDatas.get(position).get("redTicketTheme").toString());
                holder.redTicketMaker.setText(redTicketDatas.get(position).get("redTicketMaker").toString());
                holder.redTicketTime.setText(redTicketDatas.get(position).get("redTicketTime").toString());
                holder.id_sender_image.setImageResource(R.mipmap.icon24);

                holder.lo_gift_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //GiftDetail
                        int ticketId=Integer.parseInt(redTicketDatas.get(position).get("ticketId").toString());
                        String tv_pray=redTicketDatas.get(position).get("redTicketTheme").toString();
                        Bundle bundle=new Bundle();
                        bundle.putInt("id",ticketId);
                        bundle.putString("tv_pray",tv_pray);

                        Intent intent=new Intent(getContext(),GiftActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                });
            }else{
                holder.redTicketTheme.setText(redTicketDatas.get(position).get("voteTheme").toString());
                holder.redTicketMaker.setText(redTicketDatas.get(position).get("voteMaker").toString());
                holder.redTicketTime.setText(redTicketDatas.get(position).get("voteCreateTime").toString());
                holder.id_sender_image.setImageResource(R.mipmap.icon25);

                String myName=JSONObject.parseObject(PreferencesUtils.getString(getActivity(),"UserInfo",""), User.class).getName();
                String voteMaker=redTicketDatas.get(position).get("creater").toString();
                if(myName.equals(voteMaker)){
                    holder.tv_my_self.setVisibility(View.VISIBLE);
                }else{
                    holder.tv_my_self.setVisibility(View.GONE);
                }

                holder.lo_gift_sure.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        voteId=Integer.parseInt(redTicketDatas.get(position).get("voteId").toString());
                        creater=redTicketDatas.get(position).get("creater").toString();

                        //查询投票是否已投
                        HttpClient.get(this, Urls.QueryHasVoted+voteId, null, new CallBack<JSONObject>() {
                            @Override
                            public void onSuccess(JSONObject data) {
                                System.out.println("是否已投返回值="+data);
                                if(data.getString("data")=="false"){
                                    //调http://111.231.146.57:20086/front/vote/overview/{voteId}
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("voteId",voteId);//isRaise
                                    bundle.putString("tv_active_name",tv_pray_text);
                                    bundle.putString("creater",creater);
                                    bundle.putString("from","homeFragment");
                                    Intent intent= new Intent(getActivity(), VoteDetailActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }else{
                                    //调http://111.231.146.57:20086/front/vote/statis/{voteId}
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("voteId",voteId);
                                    bundle.putBoolean("isOver",false);
                                    bundle.putString("creater",creater);
                                    Intent intent= new Intent(getActivity(), VotedDetailActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                            }
                        });
                    }
                });


//                map_vote.put("voteTheme",voteWaitData.getJSONObject(i).getString("voteThrem"));
//                map_vote.put("voteMaker","发起人"+"   "+voteWaitData.getJSONObject(i).getString("voteCreater"));
//                map_vote.put("voteCreateTime","发起时间"+"   "+voteWaitData.getJSONObject(i).getString("createTime").substring(0,11));
//                map_vote.put("voteId",voteWaitData.getJSONObject(i).getString("id"));
//                map_vote.put("isRaise",voteWaitData.getJSONObject(i).getString("isRaise"));
            }

        }

        @Override
        public int getItemCount(){
            return redTicketDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView redTicketTheme,redTicketTime,redTicketMaker,tv_my_self;
            RelativeLayout lo_gift_sure;
            ImageView id_sender_image;
            public MyViewHolder(View view)
            {
                super(view);
                redTicketTheme = (TextView) view.findViewById(R.id.tv_pray);
                redTicketTime = (TextView) view.findViewById(R.id.tv_create_time);
                tv_my_self = (TextView) view.findViewById(R.id.tv_my_self);
                redTicketMaker = (TextView) view.findViewById(R.id.tv_maker_name);
                lo_gift_sure= (RelativeLayout) view.findViewById(R.id.lo_gift_sure);
                id_sender_image= (ImageView) view.findViewById(R.id.id_sender_image);
            }
        }
    }


    private void addEvent(){
        //lo_gift_sure.setOnClickListener(toGiftSure);
        tv_start_vote.setOnClickListener(startVote);
        tv_send_gift.setOnClickListener(sendGift);
        swipeRefreshLayout.setOnRefreshListener(reFreshData);

    }
    private SwipeRefreshLayout.OnRefreshListener reFreshData = new SwipeRefreshLayout.OnRefreshListener(){
        @Override
        public void onRefresh(){
            getData();
        }
    };

    private View.OnClickListener toGiftSure = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Bundle bundle = new Bundle();
            bundle.putInt("id",id);
            bundle.putString("tv_pray",tv_pray_text);
            Intent intent= new Intent(getActivity(), GiftActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };

    private View.OnClickListener startVote = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            clearVoteInfo();
            Intent intent = new Intent(getActivity(),StartActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);

        }
    };

    private View.OnClickListener sendGift = new View.OnClickListener(){
        @Override
        public void onClick(View view){
            Bundle bundle = new Bundle();
            bundle.putString("from","HomeFragment");
            Intent intent= new Intent(getActivity(), MainActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    };

    private void queryGifts(){
        //查询红包
        HttpClient.get(this, Urls.GiftWaitRecieveList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {

                System.out.print("待接收的红包列表="+data);
                giftWaiteData=data;
                //查询投票列表
                HttpClient.get(this, Urls.QueryVotesList, null, new CallBack<JSONArray>() {
                    @Override
                    public void onSuccess(JSONArray data) {
                        System.out.println("待投票列表="+data);
                        swipeRefreshLayout.setRefreshing(false);//取消刷新效果
                        voteWaitData=data;
                        if(giftWaiteData.size()>0||voteWaitData.size()>0){
                            sv_data_list.setVisibility(View.VISIBLE);
                            ll_no_data.setVisibility(View.GONE);
                            operateGiftList(giftWaiteData);
                        }else{

                            sv_data_list.setVisibility(View.GONE);
                            ll_no_data.setVisibility(View.VISIBLE);
                        }


                    }
                });


            }
        });
    }

    private void clearVoteInfo(){
        App.voteInfo.setIsAnonymous("true");
        App.voteInfo.setBitmap(null);
        App.voteInfo.getVoteRewardRule().clear();
        App.voteInfo.setIsReward("false");
        App.voteInfo.getPlayerList().clear();
        App.voteInfo.setVoteExpireTime("");
        App.voteInfo.getVoteTarget().clear();
        App.voteInfo.getImgUrls().clear();
        App.voteInfo.setVoteTheme("");
        App.voteInfo.getOptions().clear();
        App.voteInfo.setIsLimited("false");
        App.voteInfo.getVoterTargetList().clear();
        App.voteInfo.setIsRaise("true");
        App.voteInfo.getCheckedRadioButtonList().clear();
        App.voteInfo.setVoteExpireTime("");
        App.voteInfo.getCheckedPositionList().clear();
        App.voteInfo.setIfSetReward(false);
        App.voteInfo.setFirstStepFinished(false);
        App.voteInfo.setSecondStepFinished(false);
        App.voteInfo.setThirdStepFinished(false);
        App.voteInfo.setFouthStepFinished(false);
        PreferencesUtils.putString(App.getContext(),"optionName","");
    }


    private void getData(){
        //查询红包
        queryGifts();


        //查询投票
//        HttpClient.get(this, Urls.QueryVotesList, null, new CallBack<JSONArray>() {
//            @Override
//            public void onSuccess(JSONArray data) {
//                System.out.println("待投票列表="+data);
//                operateVotesWait(data);
//            }
//        });


    }

    //处理代投票列表
    private void operateVotesWait(JSONArray data){
        voteDatas.clear();
        //模拟投票列表数据
        for(int i=0;i<data.size();i++){
            Map<String,Object> map_vote=new HashMap();
            //Object obj=new Object();
            map_vote.put("voteTheme",data.getJSONObject(i).getString("voteThrem"));
            map_vote.put("voteMaker","发起人"+"   "+data.getJSONObject(i).getString("voteCreater"));
            map_vote.put("voteCreateTime","发起时间"+"   "+data.getJSONObject(i).getString("createTime").substring(0,11));
            map_vote.put("voteId",data.getJSONObject(i).getString("id"));
            map_vote.put("isRaise",data.getJSONObject(i).getString("isRaise"));
            voteDatas.add(map_vote);
        }


        //投票列表数据渲染
        //voteRecyclerView = (RecyclerView)view.findViewById(R.id.id_vote_wait_list);
        //voteRecyclerView.setNestedScrollingEnabled(false);
        voteRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        voteRecyclerView.setAdapter(voteAdapter = new VoteListAdapter());
        voteAdapter.notifyDataSetChanged();
    }

    private void operateGiftList(JSONArray data){
        redTicketDatas.clear();
        //模拟红包列表数据
        for(int i=0;i<data.size();i++){
            Map<String,Object> map_ticket=new HashMap();
            //Object obj=new Object();
            map_ticket.put("redTicketTheme",data.getJSONObject(i).getString("sendMsg"));
            map_ticket.put("redTicketMaker","发起人"+"   "+data.getJSONObject(i).getString("sendName"));
            map_ticket.put("redTicketTime","发起时间"+"   "+data.getJSONObject(i).getString("createTime").substring(0,11));
            map_ticket.put("ticketId",data.getJSONObject(i).getString("id"));
            redTicketDatas.add(map_ticket);

        }

        for(int i=0;i<voteWaitData.size();i++){
            Map<String,Object> map_vote=new HashMap();
            //Object obj=new Object();
            map_vote.put("voteTheme",voteWaitData.getJSONObject(i).getString("voteThrem"));
            map_vote.put("voteMaker","发起人"+"   "+voteWaitData.getJSONObject(i).getString("voteCreater"));
            map_vote.put("voteCreateTime","发起时间"+"   "+voteWaitData.getJSONObject(i).getString("createTime").substring(0,11));
            map_vote.put("voteId",voteWaitData.getJSONObject(i).getString("id"));
            map_vote.put("isRaise",voteWaitData.getJSONObject(i).getString("isRaise"));
            map_vote.put("creater",voteWaitData.getJSONObject(i).getString("voteCreater"));
            redTicketDatas.add(map_vote);
        }

        //投票列表数据渲染
        redTicketRecyclerView = (RecyclerView)view.findViewById(R.id.id_red_ticket_recive_wait_list);
        //redTicketRecyclerView.setNestedScrollingEnabled(false);
        redTicketRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        redTicketRecyclerView.setAdapter(redTicketAdapter = new RedTicketListAdapter());
        redTicketAdapter.notifyDataSetChanged();
    }

    //弹出红包模态框
    public void showPopupWindow(Context context, View parent){
//        LayoutInflater inflater = (LayoutInflater)
//                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View vPopupWindow=inflater.inflate(R.layout.view_redticket_popup, null, false);
//        final PopupWindow pw= new PopupWindow(vPopupWindow, LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT,true);
//
//        //OK按钮及其处理事件
//        Button btnOK=(Button)vPopupWindow.findViewById(R.id.dialog_ok);
//        btnOK.setOnClickListener(new OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                //设置文本框内容
//            }
//        });
//
//        //Cancel按钮及其处理事件
//        Button btnCancel=(Button)vPopupWindow.findViewById(R.id.dialog_cancel);
//        btnCancel.setOnClickListener(new OnClickListener(){
//            @Override
//            public void onClick(View v) {
//                pw.dismiss();//关闭
//            }
//        });
//        //显示popupWindow对话框
//        pw.showAtLocation(parent, Gravity.CENTER, 0, 0);
    }


}
