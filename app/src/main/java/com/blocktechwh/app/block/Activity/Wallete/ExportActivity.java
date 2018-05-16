package com.blocktechwh.app.block.Activity.Wallete;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.RedTicket.SendRedTicketActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/12/2.
 */

public class ExportActivity extends TitleActivity {
    private RecyclerView accountRecyclerView;
    private AccountAdapter mAdapter;
    private JSONArray accountArray;
    private TextView tv_wallete_data;
    private EditText et_new_account;
    private EditText et_amount;
    private Button btn_bind_commit;
    private Button btn_commit;
    private int checkedPosition;
    private String recipientId;
    private Button btn_amount_add;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_export);
        initTitle("钱包");

        App.getInstance().addActivity(this);

        initView();
        getData();
        setEvent();
    }


    private void initView(){
        recipientId="";
        accountArray=new JSONArray();
        tv_wallete_data = (TextView) findViewById(R.id.tv_wallete_data);
        accountRecyclerView = (RecyclerView)findViewById(R.id.id_acount_list);
        et_new_account = (EditText) findViewById(R.id.et_new_account);
        et_amount = (EditText) findViewById(R.id.et_amount);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        //btn_bind_commit = (Button) findViewById(R.id.btn_bind_commit);
        btn_amount_add = (Button) findViewById(R.id.btn_amount_add);

    }

    private void setEvent(){
/*        btn_bind_commit.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String newAccount=et_new_account.getText().toString().toUpperCase();
                JSONObject json = new JSONObject();
                json.put("account",newAccount);
                String telRegex = "\\d+L" ;
                if (!newAccount.matches( telRegex )){
                    Toast.makeText(App.getContext(),"无效地址格式",Toast.LENGTH_SHORT).show();
                    return;
                }
                HttpClient.post(this, Urls.AddWalletAccount+newAccount, "", new CallBack<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        Toast.makeText(ExportActivity.this,"添加成功",Toast.LENGTH_SHORT).show();

                        //查询投票列表
                        HttpClient.get(this, Urls.QueryWalleteAddressList, null, new CallBack<JSONArray>() {
                            @Override
                            public void onSuccess(JSONArray data) {
                                System.out.print("账号列表="+data);
                                accountArray=data;
                                accountRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
                                accountRecyclerView.setAdapter(mAdapter = new AccountAdapter());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });

            }
        });*/


        btn_amount_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String amount=et_amount.getText().toString();
                if(amount.equals("")){
                    Toast.makeText(ExportActivity.this,"请输入金额",Toast.LENGTH_SHORT).show();
                    return;
                }
                String telRegex = "^-?[0-9]+" ;
                if (!amount.matches( telRegex )||Double.parseDouble(amount)<=0){
                    Toast.makeText(ExportActivity.this,"请输入有效金额",Toast.LENGTH_SHORT).show();
                    return;
                }

                JSONObject json = new JSONObject();
                json.put("amount",amount);
                json.put("recipientId",recipientId);
                System.out.print("提交="+json);

                HttpClient.post(this, Urls.Commit, json.toString(), new CallBack<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        Toast.makeText(ExportActivity.this,"添加成功",Toast.LENGTH_SHORT).show();

                        //查询投票列表
                        HttpClient.get(this, Urls.QueryWalleteAddressList, null, new CallBack<JSONArray>() {
                            @Override
                            public void onSuccess(JSONArray data) {
                                System.out.print("账号列表="+data);
                                accountArray=data;
                                accountRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
                                accountRecyclerView.setAdapter(mAdapter = new AccountAdapter());
                                mAdapter.notifyDataSetChanged();
                            }
                        });
                    }
                });
            }
        });


    }

    class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(App.getContext()).inflate(R.layout.item_accout_radio_li, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position){
            if(position==0){
                checkedPosition=0;
                holder.tv_wallet_account.setChecked(true);
                recipientId=accountArray.getJSONObject(0).getString("mebAccount");

            }
            holder.tv_wallet_account.setText(accountArray.getJSONObject(position).getString("mebAccount"));
            holder.tv_wallet_account.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if(isChecked){
                        //先前被选中的RadioButton取消选中
                        RecyclerView.ViewHolder viewHolder=accountRecyclerView.findViewHolderForAdapterPosition(checkedPosition);
                        RadioButton rbChecked=viewHolder.itemView.findViewById(R.id.tv_wallet_account);
                        rbChecked.setChecked(false);
                        holder.ll_wallet_account_contaimer.setBackgroundColor(Color.WHITE);
                        checkedPosition=position;//将新的被选中的RadioButton位置赋值给checkedPosition
                        recipientId=accountArray.getJSONObject(position).getString("mebAccount");

                    }else{
                        holder.ll_wallet_account_contaimer.setBackgroundColor(Color.argb(255,241,241,241));
                    }
                }
            });

        }

        @Override
        public int getItemCount(){
            return accountArray.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            RadioButton tv_wallet_account;

            LinearLayout ll_wallet_account_contaimer;

            public MyViewHolder(View view)
            {
                super(view);
                tv_wallet_account = (RadioButton) view.findViewById(R.id.tv_wallet_account);
                ll_wallet_account_contaimer = (LinearLayout) view.findViewById(R.id.ll_wallet_account_contaimer);
            }
        }
    }


    private void getData(){

        //查询钱包余额
//        HttpClient.get(this, Urls.QueryWalleteData, null, new CallBack<JSONObject>() {
//            @Override
//            public void onSuccess(JSONObject data) {
//                tv_wallete_data.setText(data.getInteger("total").toString()+"Block");
//            }
//            @Override
//            public void onFailure(int errorType, String message){
//
//            }
//        });
        //查询投票列表
        HttpClient.get(this, Urls.QueryWalleteAddressList, null, new CallBack<JSONArray>() {
            @Override
            public void onSuccess(JSONArray data) {
                System.out.print("账号列表="+data);
                accountArray=data;
                accountRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
                accountRecyclerView.setAdapter(mAdapter = new AccountAdapter());
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
