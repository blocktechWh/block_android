package com.blocktechwh.app.block.Activity.Actions;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;

/**
 * Created by 跳跳蛙 on 2017/11/18.
 */

public class VoteFillActivity extends TitleActivity {
    private int voteId;
    private Button btFillSure;
    private EditText et_fill_money;
    private TextView tv_name;
    private ImageView iv_img;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill);
        initTitle("加注");

        Bundle bundle=this.getIntent().getExtras();
        voteId=bundle.getInt("voteId");
        //Toast.makeText(VoteFillActivity.this,"voteId="+voteId, Toast.LENGTH_SHORT).show();

        initData();
        addEvent();
    }

    private void initData(){
        btFillSure=(Button) findViewById(R.id.btFillSure);
        et_fill_money=(EditText) findViewById(R.id.et_fill_money);
        tv_name=(TextView) findViewById(R.id.tv_name);
        iv_img=(ImageView) findViewById(R.id.iv_img);

        tv_name.setText(App.userInfo.getName());
        //userPhone.setText(App.userInfo.getPhone());
        String url = Urls.HOST + "staticImg" + App.userInfo.getImg();
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                iv_img.setImageBitmap(bmp);
            }
        });
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
                VoteFillActivity.this.finish();

//                Intent intent = new Intent(VoteFillActivity.this,VoteDetailActivity.class);
//                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
//                startActivity(intent);
            }
        });
            }
        });
    }


}
