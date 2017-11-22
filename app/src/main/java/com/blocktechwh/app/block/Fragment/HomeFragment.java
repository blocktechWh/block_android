package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.Actions.VoteDetailActivity;
import com.blocktechwh.app.block.Activity.Actions.VotedDetailActivity;
import com.blocktechwh.app.block.Activity.RedTicket.GiftActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

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
    private LinearLayout lo_gift_sure;
    private int id;
    private String tv_pray_text;
    private List<Map<String,Object>> voteDatas = new ArrayList<Map<String,Object>>();//投票数据列表
    private RecyclerView voteRecyclerView;//投票列表对象
    private VoteListAdapter voteAdapter;

    private List<Map<String,Object>> redTicketDatas = new ArrayList<Map<String,Object>>();//红包数据列表
    private RecyclerView redTicketRecyclerView;//投票列表对象
    private RedTicketListAdapter redTicketAdapter;
    private int voteId;
    private boolean isRaise;





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
        tv_maker_name=view.findViewById(R.id.tv_maker_name);
        tv_create_time=view.findViewById(R.id.tv_create_time);
        id_wallet_image=view.findViewById(R.id.id_wallet_image);
        lo_gift_sure=view.findViewById(R.id.lo_gift_sure);




        //模拟红包列表数据
        for(int i=0;i<1;i++){
            Map<String,Object> map_red_ticket=new HashMap();
            map_red_ticket.put("redTicketTheme","恭喜发财，大吉大利");
            map_red_ticket.put("redTicketMaker","发起人"+"   "+"许巍");
            map_red_ticket.put("redTicketCreateTime","发起时间"+"   "+"2017-11-01");
            redTicketDatas.add(map_red_ticket);
        }
        System.out.print("redTicketDatas="+redTicketDatas);

        //红包列表数据渲染
        redTicketRecyclerView = (RecyclerView)view.findViewById(R.id.id_red_ticket_recive_wait_list);
        redTicketRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        redTicketRecyclerView.setAdapter(redTicketAdapter = new RedTicketListAdapter());
        redTicketAdapter.notifyDataSetChanged();


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
            //点击红包列表项
            holder.ll_vote_detail.setOnClickListener(toGiftSure=new View.OnClickListener() {
                @Override
                public void onClick(View v) {
             voteId=Integer.parseInt(voteDatas.get(position).get("voteId").toString());

                    //isRaise=voteDatas.get(position).get("isRaise");

            //查询投票是否已投
            HttpClient.get(this, Urls.QueryHasVoted+voteId, null, new CallBack<JSONObject>() {
                @Override
                public void onSuccess(JSONObject data) {
                    System.out.print("是否已投返回值="+data);
                    if(data.getString("data")=="false"){
                        //调http://111.231.146.57:20086/front/vote/overview/{voteId}
                        Bundle bundle = new Bundle();
                        bundle.putInt("voteId",voteId);//isRaise
                        bundle.putInt("isRaise",voteId);
                        bundle.putString("tv_active_name",tv_pray_text);
                        Intent intent= new Intent(getActivity(), VoteDetailActivity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        //调http://111.231.146.57:20086/front/vote/statis/{voteId}
                        Bundle bundle = new Bundle();
                        bundle.putInt("voteId",voteId);
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
            holder.redTicketTheme.setText(redTicketDatas.get(position).get("redTicketTheme").toString());
            holder.redTicketTime.setText(redTicketDatas.get(position).get("redTicketMaker").toString());
            holder.redTicketMaker.setText(redTicketDatas.get(position).get("redTicketCreateTime").toString());


        }

        @Override
        public int getItemCount(){
            return redTicketDatas.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView redTicketTheme,redTicketTime,redTicketMaker;

            public MyViewHolder(View view)
            {
                super(view);
                redTicketTheme = (TextView) view.findViewById(R.id.tv_pray);
                redTicketTime = (TextView) view.findViewById(R.id.tv_maker_name);
                redTicketMaker = (TextView) view.findViewById(R.id.tv_create_time);

            }
        }
    }


    private void addEvent(){
        //lo_gift_sure.setOnClickListener(toGiftSure);

    }

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


    private void getData(){
        System.out.print("token="+App.token);
        //查询红包
        HttpClient.get(this, Urls.GiftWaitRecieveList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {

                System.out.print("待接收的红包列表mDatas="+data);
                if(data.size()>0){
                    operateGiftList(data);
                }

            }
        });



        //查询投票
        HttpClient.get(this, Urls.QueryVotesList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                System.out.print("待投票列表mDatas="+data);
                //Toast.makeText(getContext(),data.toString(),Toast.LENGTH_LONG).show();
                operateVotesWait(data);
            }
        });


    }

    private void operateVotesWait(JSONArray data){
        //模拟投票列表数据
        for(int i=0;i<data.size();i++){
            Map<String,Object> map_vote=new HashMap();
            //Object obj=new Object();
            System.out.print("object"+data.get(i));
            map_vote.put("voteTheme",data.getJSONObject(i).getString("voteThrem"));
            map_vote.put("voteMaker","发起人"+"   "+data.getJSONObject(i).getString("voteCreater"));
            map_vote.put("voteCreateTime","发起时间"+"   "+data.getJSONObject(i).getString("createTime"));
            map_vote.put("voteId",data.getJSONObject(i).getString("id"));
            map_vote.put("isRaise",data.getJSONObject(i).getString("isRaise"));
            voteDatas.add(map_vote);
        }
        System.out.print("voteDatas="+voteDatas);

        //投票列表数据渲染
        voteRecyclerView = (RecyclerView)view.findViewById(R.id.id_vote_wait_list);
        voteRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
        voteRecyclerView.setAdapter(voteAdapter = new VoteListAdapter());
        voteAdapter.notifyDataSetChanged();
    }

    private void operateGiftList(JSONArray data){
//        //模拟投票列表数据
//        for(int i=0;i<data.size();i++){
//            Map<String,Object> map_vote=new HashMap();
//            //Object obj=new Object();
//            System.out.print("object"+data.get(i));
//            map_vote.put("voteTheme",data.getJSONObject(i).getString("voteThrem"));
//            map_vote.put("voteMaker","发起人"+"   "+data.getJSONObject(i).getString("voteCreater"));
//            map_vote.put("voteCreateTime","发起时间"+"   "+data.getJSONObject(i).getString("createTime"));
//            map_vote.put("voteId",data.getJSONObject(i).getString("id"));
//
//            voteDatas.add(map_vote);
//        }
//        System.out.print("voteDatas="+voteDatas);
//
//        //投票列表数据渲染
//        redTicketRecyclerView = (RecyclerView)view.findViewById(R.id.id_vote_wait_list);
//        redTicketRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
//        redTicketRecyclerView.setAdapter(redTicketAdapter = new RedTicketListAdapter());
//        redTicketAdapter.notifyDataSetChanged();
    }

//    private void operateGiftList(JSONArray ja){
//        JSONObject jo=(JSONObject)ja.get(0);
//        id= (int) jo.get("id");
//
//        tv_pray.setText(jo.get("sendMsg").toString());
//        tv_create_time.setText(jo.get("createTime").toString());
//        tv_maker_name.setText(jo.get("sendName").toString());
//        tv_pray_text=jo.get("sendMsg").toString();
//
//        for(int i=0;i<mDatas.size();i++){
//
//        }
//    }

    private void operateVoteList(){

    }
}
