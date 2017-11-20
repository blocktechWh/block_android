package com.blocktechwh.app.block.Activity.Actions;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/11/18.
 */

public class VoteFillActivity extends TitleActivity {
    private Integer voteId;
    private Button btFillSure;
    private EditText et_fill_money;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill);
        initTitle("加注");

        Bundle bundle=this.getIntent().getExtras();
        voteId=bundle.getInt("voteId");

        initData();
        addEvent();
    }

    private void initData(){
        btFillSure=(Button) findViewById(R.id.btFillSure);
        et_fill_money=(EditText) findViewById(R.id.et_fill_money);
    }


    private void addEvent(){
        btFillSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
         //给投票加注
        JSONObject json_raise=new JSONObject();
         json_raise.put("voteId",voteId);//投票id
         json_raise.put("amount",et_fill_money.getText());//加注金额

        HttpClient.post(this, Urls.MakeRaise, json_raise.toString(), new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject data) {
                System.out.print("投票加注返回="+data);
                Toast.makeText(VoteFillActivity.this,"加注成功",Toast.LENGTH_SHORT).show();
            }
        });
            }
        });
    }


}
