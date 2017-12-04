package com.blocktechwh.app.block.Activity.Actions;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Bean.VoteInfo;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.DoubleDatePickerDialog;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.ImageUtil;

import java.util.Calendar;

/**
 * Created by Administrator on 2017/11/10.
 */

public class StartActivity extends TitleActivity {

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private String theme;//主题
    private String themeImg;//主题图片

    private String voteFee;//奖金总额
    private String content;//投票活动描述
    private String expireTime;//投票到期时间

    private TextView tv_to_add;

    private TextView addPlayersButton;
    private Switch sIfSingle;
    private Switch sIfNoSee;
    private Switch switchifAdd;
    private String isLimited="false";
    private String isAnonymous="true";
    private String isRaise="true";
    private String isReward="false";
    private RecyclerView mRecyclerView;

    //private TextView tvPopupTime;
    private LinearLayout layout;
    private boolean isFold=true;//判断是否显示
    private TextView tv_main=null;//遮罩层
    private PopupWindow taxWindow;// 弹出框  
    private Button btnStartVote;
    private EditText etTheme;
    private TextView tvAddReward;
    //private ImageView iv_optionimg;
    private TextView tv_select_voters;
    private VotesListAdapter mAdapter;
    private Switch sIfReward;
    private ImageView lladdImage;
    private LinearLayout reward_container_layout;
    private Bundle bundle;
    private LinearLayout ll_reward_container;
    private LinearLayout linear;
    private TextView btn_date;
   // private EditText et_data;
    private String endTimeText="";
    private ImageButton titlebar_button_back;
    private LinearLayout ll_voters_container;
    private TextView tv_reward_info;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_vote);

        initTitle("发起投票");
        initView();
        addEvent();


    }
    private void initView(){
        tv_reward_info=(TextView) findViewById(R.id.tv_reward_info);
        ll_voters_container=(LinearLayout) findViewById(R.id.ll_voters_container);
        titlebar_button_back=(ImageButton)findViewById(R.id.titlebar_button_back);
        linear=(LinearLayout) findViewById(R.id.reward_container_layout);
        btn_date=(TextView) findViewById(R.id.btn_date);
        //et_data=(EditText) findViewById(R.id.et_data);

        tv_select_voters=(TextView)findViewById(R.id.tv_select_voters);
        ll_reward_container=(LinearLayout) findViewById(R.id.ll_reward_container);

       // LayoutInflater inflater = getLayoutInflater();
        //View view = inflater.inflate(R.layout.item_vote_active, null);
        addPlayersButton=(TextView) findViewById(R.id.tv_to_add);
        tv_to_add=(TextView) findViewById(R.id.tv_to_add);
        sIfSingle=(Switch) findViewById(R.id.switch_vote);
        sIfNoSee=(Switch) findViewById(R.id.switch_vote2);
        switchifAdd=(Switch) findViewById(R.id.switchifAdd);
        sIfReward=(Switch) findViewById(R.id.sIfReward);
        tvAddReward=(TextView) findViewById(R.id.tvAddReward);
        lladdImage=(ImageView) findViewById(R.id.id_add_image);
        lladdImage.setImageBitmap(VoteInfo.getBitmap());
        mRecyclerView = (RecyclerView)findViewById(R.id.id_votes_option_recycler);

        sIfSingle.setChecked(false);
        sIfNoSee.setChecked(true);
        switchifAdd.setChecked(true);
        sIfReward.setChecked(Boolean.parseBoolean(VoteInfo.getIsReward()));

        //tvPopupTime=(TextView) findViewById(R.id.tv_popup_time);
        layout=(LinearLayout) findViewById(R.id.layout_main);
        btnStartVote=(Button) findViewById(R.id.btnStartVote);

        etTheme=(EditText) findViewById(R.id.etTheme);
        etTheme.setText(VoteInfo.getVoteTheme());

        reward_container_layout=(LinearLayout)findViewById(R.id.reward_container_layout);


        //etTheme.setFocusable(false);
        VoteInfo.setIsLimited(isLimited);
        VoteInfo.setIsAnonymous(isAnonymous);
        VoteInfo.setIsRaise(isRaise);

        //投票人员头像
        GridView gridview = (GridView) findViewById(R.id.gridview_voters);
        gridview.setAdapter(new ImageAdapter(this));



        if(VoteInfo.getVoteTheme()!=null){
            etTheme.setText(VoteInfo.getVoteTheme().toString());
        }


        //设置不显示数据控件隐藏
//        ll_voters_container.setVisibility(View.GONE);
//        tv_reward_info.setVisibility(View.GONE);
//        reward_container_layout.setVisibility(View.GONE);


        //投票想的渲染
        if(VoteInfo.getOptions().size()<=0){
            mRecyclerView.setVisibility(View.GONE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
            mRecyclerView.setAdapter(mAdapter = new VotesListAdapter());
        }

        //判断是否显示奖励
        if(VoteInfo.getIfSetReward()){
            tvAddReward.setText("修改奖励");
            sIfReward.setChecked(true);
            reward_container_layout.setVisibility(View.VISIBLE);
            tv_reward_info.setVisibility(View.VISIBLE);
            ll_reward_container.setOnClickListener(null);
            setRewardList();
        }else{
            sIfReward.setChecked(false);
            tvAddReward.setText("添加奖励+");
            reward_container_layout.setVisibility(View.GONE);
            tv_reward_info.setVisibility(View.GONE);
            ll_reward_container.setOnClickListener(toSetReward);
        }

        //判断是否显示时间
        if(!VoteInfo.getVoteExpireTime().equals("")){
            btn_date.setText(VoteInfo.getVoteExpireTime());

        }


    }

    class VotesListAdapter extends RecyclerView.Adapter<VotesListAdapter.MyViewHolder>{

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
            MyViewHolder holder = new MyViewHolder(LayoutInflater.from(
                    App.getContext()).inflate(R.layout.item_vote_active, parent,
                    false));
            return holder;
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, int position){

            holder.tv_option_text.setText((position+1)+"."+VoteInfo.getOptions().get(position).get("item").toString());
            String url = VoteInfo.getImgUrls().get(position);
            HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                @Override
                public void onSuccess(final Bitmap bmp) {
                    holder.iv_option_img.setImageBitmap(bmp);
                }
            });
            tv_to_add.setText("继续添加投票项");

        }

        @Override
        public int getItemCount(){
            return VoteInfo.getOptions().size();
        }

        class MyViewHolder extends RecyclerView.ViewHolder{
            TextView tv_option_text;
            ImageView iv_option_img;

            public MyViewHolder(View view)
            {
                super(view);
                tv_option_text = (TextView) view.findViewById(R.id.tv_option_text);
                iv_option_img=(ImageView) view.findViewById(R.id.iv_option_img);

            }
        }
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


    private void goBack(){
        Intent intent = new Intent(StartActivity.this,VotesListActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
        finish();
    }

    private void addEvent(){

        //返回
        titlebar_button_back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        //点击开关选择是否单选
        sIfSingle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sIfSingle.setText("单选");
                    isLimited="true";
                }else{
                    sIfSingle.setText("多选");
                    isLimited="false";
                }
                VoteInfo.setIsLimited(isLimited);
                System.out.print(VoteInfo.getIsLimited());

            }
        });

        //点击跳转选择投票人页面
        tv_select_voters.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Bundle bundle=new Bundle();
                bundle.putInt("stateIndex",2);
                Intent intent = new Intent(StartActivity.this,VotersSelectListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        //点击弹出时间选择对话框
        btn_date.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new DoubleDatePickerDialog(StartActivity.this, 0, new DoubleDatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker endDatePicker, int endYear, int endMonthOfYear,
                                          int endDayOfMonth) {
                        endTimeText = String.format("%d-%d-%d", endYear, endMonthOfYear + 1, endDayOfMonth);
                        btn_date.setText(endTimeText);
                        VoteInfo.setVoteExpireTime(endTimeText);
                    }
                }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DATE), true).show();
            }
        });

        //点击跳转设置奖励资金页面
        ll_reward_container.setOnClickListener(toSetReward);

        //点击开关选择是否匿名
        sIfNoSee.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    sIfNoSee.setText("开");
                    isAnonymous="true";
                }else{
                    sIfNoSee.setText("关");
                    isAnonymous="false";
                }
                VoteInfo.setIsAnonymous(isAnonymous);
                System.out.print(VoteInfo.getIsAnonymous());
            }
        });

        //点击开关选择是否添加奖励
        sIfReward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(VoteInfo.getOptions().size()<=0){
                        Toast.makeText(StartActivity.this,"请先添加受益人",Toast.LENGTH_SHORT).show();
                        sIfReward.setChecked(false);
                        return;
                    }

                    if(VoteInfo.getOptions().size()<2){
                        Toast.makeText(StartActivity.this,"受益人不能少于两人，请继续添加",Toast.LENGTH_SHORT).show();
                        sIfReward.setChecked(false);
                        return;
                    }

                    sIfReward.setText("开");
                    tvAddReward.setText("修改奖励");
                    isReward="true";
                    VoteInfo.setIsReward(isReward);
                    //tv_reward_info.setVisibility(View.VISIBLE);
                    linear.setVisibility(View.VISIBLE);
                    tv_reward_info.setVisibility(View.VISIBLE);

                    Intent intent = new Intent(StartActivity.this,SetRewardActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                    startActivity(intent);
                }else{
                    sIfReward.setText("关");
                    VoteInfo.setIfSetReward(false);
                    VoteInfo.setIsReward(isReward);
                    tvAddReward.setText("添加奖励+");

                    linear.setVisibility(View.GONE);
                    //ll_reward_container.setVisibility(View.GONE);
                    reward_container_layout.setVisibility(View.GONE);
                    tv_reward_info.setVisibility(View.GONE);

                    tv_reward_info.setVisibility(View.GONE);
                    ll_reward_container.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(VoteInfo.getOptions().size()<=0){
                                Toast.makeText(StartActivity.this,"请先添加投票项",Toast.LENGTH_SHORT).show();
                                return;
                            }
                            isReward="true";
                            VoteInfo.setIsReward(isReward);
                            Intent intent = new Intent(StartActivity.this,SetRewardActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                            startActivity(intent);

                        }
                    });
                }
                VoteInfo.setIsReward(isReward);

            }
        });

        //点击开关选择是否加注
        switchifAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    switchifAdd.setText("开");
                    isRaise="true";
                }else{
                    switchifAdd.setText("关");
                    isRaise="false";
                }
                VoteInfo.setIsRaise(isRaise);

            }
        });

        addPlayersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String theme=etTheme.getText().toString();
                VoteInfo.setVoteTheme(theme);
                Intent intent = new Intent(StartActivity.this,AddPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });

        tv_to_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,AddPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);
            }
        });

        //点击提交发起投票
        btnStartVote.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                final  JSONObject json = new JSONObject();

                if(VoteInfo.getVoteImg()==""){
                    Toast.makeText(StartActivity.this,"还未添加主题图片",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(etTheme.getText().toString().length()<=0){
                    Toast.makeText(StartActivity.this,"还未填写主题",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(VoteInfo.getVoteExpireTime().length()<=0){
                    Toast.makeText(StartActivity.this,"还未选择结束时间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(VoteInfo.getOptions().size()<=0){
                    Toast.makeText(StartActivity.this,"还未设置投票项目",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(VoteInfo.getVoteTarget().size()<=0){
                    Toast.makeText(StartActivity.this,"还未添加投票参与者",Toast.LENGTH_SHORT).show();
                    return;
                }
                json.put("voteImg",VoteInfo.getVoteImg());
                json.put("voteTheme",etTheme.getText().toString());
                json.put("isLimited",VoteInfo.getIsLimited());
                json.put("isRaise",VoteInfo.getIsRaise());
                json.put("isAnonymous",VoteInfo.getIsAnonymous());
                json.put("voteExpireTime",VoteInfo.getVoteExpireTime());
                json.put("options",VoteInfo.getOptions());
                json.put("voteTarget",VoteInfo.getVoteTarget());
                if(VoteInfo.getIsReward()=="true"){
                    json.put("voteFee",VoteInfo.getVoteFee());
                    json.put("voteRewardRule",VoteInfo.getVoteRewardRule());
                }
                System.out.println("json="+json);

                //提交投票
                HttpClient.post(this, Urls.MakeVote, json.toString(), new CallBack<JSONObject>() {
                    @Override
                    public void onSuccess(JSONObject data) {
                        Toast.makeText(StartActivity.this,"发起投票成功",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(StartActivity.this,VotesListActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        startActivity(intent);
                        finish();

                    }
                });

            }
        });

        lladdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
            }
        });

    }

    private View.OnClickListener toSetReward = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(VoteInfo.getOptions().size()<=0){
                Toast.makeText(StartActivity.this,"请先添加投票项",Toast.LENGTH_SHORT).show();
                return;
            }
            if(VoteInfo.getOptions().size()<2){
                Toast.makeText(StartActivity.this,"受益人不能少于两人，请继续添加",Toast.LENGTH_SHORT).show();
                return;
            }
            isReward="true";
            VoteInfo.setIsReward(isReward);
            Intent intent = new Intent(StartActivity.this,SetRewardActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
            startActivity(intent);
        }
    };

    class ImageAdapter extends BaseAdapter {
        private Context mContext;

        // Constructor
        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return VoteInfo.getPlayerList().size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            final ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            //判断是否有添加voters
            if(VoteInfo.getPlayerList().size()>0){
                ll_voters_container.setVisibility(View.VISIBLE);
                ll_voters_container.setPadding(20,10,20,10);
                String url = VoteInfo.getPlayerList().get(position).get("img").toString();
                HttpClient.getImage(this, url, new CallBack<Bitmap>() {
                    @Override
                    public void onSuccess(final Bitmap bmp) {
                        imageView.setImageBitmap(bmp);
                    }
                });
                tv_select_voters.setText("继续添加投票人员+");
            }else{
                ll_voters_container.setVisibility(View.GONE);
                tv_select_voters.setText("添加投票人员+");
            }

            //imageView.setImageResource(mThumbIds[position]);
            return imageView;
        }

        // Keep all Images in array
        public Integer[] mThumbIds = {
                R.mipmap.icon25,R.mipmap.ic_launcher,R.mipmap.icon24,R.mipmap.icon25,R.mipmap.icon25
        };
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {// 从相册返回的数据
            if (data != null) {
                Uri uri = data.getData();// 得到图片的全路径
                System.out.println(uri);
                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {// 从剪切图片返回的数据
            if (data != null && data.hasExtra("data")) {
                Bitmap bitmap = data.getParcelableExtra("data");
                lladdImage.setImageBitmap(bitmap);
                String base64 = ImageUtil.bitmapToBase64(bitmap);
                VoteInfo.setVoteImg(base64);
                VoteInfo.setBitmap(bitmap);
                System.out.println(base64);
//                mInputHandler(base64);
            }
        }
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }

    private void setRewardList(){

        TextView tv_reward_total_amount=new TextView(this);
        tv_reward_total_amount.setText("奖励资金: "+VoteInfo.getVoteFee()+"元");
        reward_container_layout.addView(tv_reward_total_amount);
        if(VoteInfo.getVoteRewardRule().size()<=0){
            tv_reward_info.setVisibility(View.GONE);
            reward_container_layout.setVisibility(View.GONE);
            return;
        }else{
            tv_reward_info.setVisibility(View.VISIBLE);
            reward_container_layout.setVisibility(View.VISIBLE);
        }
        for(int i=0;i<VoteInfo.getVoteRewardRule().size();i++){
            TextView tv_reward_option1=new TextView(StartActivity.this);
            tv_reward_option1.setText("第"+(i+1)+"名获得: "+VoteInfo.getVoteRewardRule().get(i)+"元");
            tv_reward_option1.setPadding(0, 5, 0, 5);//left, top, right, bottom
            reward_container_layout.addView(tv_reward_option1);
        }
    }


}
