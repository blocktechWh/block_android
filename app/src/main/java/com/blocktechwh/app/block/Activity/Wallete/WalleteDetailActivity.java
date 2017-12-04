package com.blocktechwh.app.block.Activity.Wallete;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/12/2.
 */

public class WalleteDetailActivity extends TitleActivity {
    private RecyclerView accountRecyclerView;
    private AccountAdapter mAdapter;
    private JSONArray accountArray;
    private TextView tv_wallete_data;
    private EditText et_new_account;
    private Button btn_commit;
    private Button btn_import;
    private Button btn_export;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallete_detail);
        initTitle("钱包");

        initView();
        getData();
        setEvent();
    }


    private void initView(){
        accountArray=new JSONArray();
        tv_wallete_data = (TextView) findViewById(R.id.tv_wallete_data);
        accountRecyclerView = (RecyclerView)findViewById(R.id.id_acount_list);
        et_new_account = (EditText) findViewById(R.id.et_new_account);
        btn_commit = (Button) findViewById(R.id.btn_commit);
        btn_import = (Button) findViewById(R.id.btn_import);
        btn_export = (Button) findViewById(R.id.btn_export);

    }

    private void setEvent(){
        btn_commit.setOnClickListener(new View.OnClickListener(){
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
                        Toast.makeText(WalleteDetailActivity.this,"添加成功",Toast.LENGTH_SHORT).show();

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

        btn_import.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalleteDetailActivity.this,ImpotActivity.class);
                startActivity(intent);

            }
        });
        btn_export.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WalleteDetailActivity.this,ExportActivity.class);
                startActivity(intent);
                //Toast.makeText(WalleteDetailActivity.this,"开发中。。。",Toast.LENGTH_SHORT).show();

            }
        });
    }

    class AccountAdapter extends RecyclerView.Adapter<AccountAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(App.getContext()).inflate(R.layout.item_accout_li, parent, false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position){
            holder.tv_wallet_account.setText(accountArray.getJSONObject(position).getString("mebAccount"));

        }

        @Override
        public int getItemCount(){
            return accountArray.size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_wallet_account;

            public MyViewHolder(View view)
            {
                super(view);
                tv_wallet_account = (TextView) view.findViewById(R.id.tv_wallet_account);

            }
        }
    }


    private void getData(){

        //查询钱包余额
        HttpClient.get(this, Urls.QueryWalleteData, null, new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                tv_wallete_data.setText(data.getInteger("total").toString()+"Block");
            }
            @Override
            public void onFailure(int errorType, String message){

            }
        });
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
