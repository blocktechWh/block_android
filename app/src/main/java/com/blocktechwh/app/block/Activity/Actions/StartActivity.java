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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.EditChangedListener;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.DoubleDatePickerDialog;
import com.blocktechwh.app.block.CustomView.ImageViewPlus;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
    private String isRaise="false";
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
    private TextView tv_select_voters;
    private VotesListAdapter mAdapter;
    private Switch sIfReward;
    private ImageView lladdImage;
    private LinearLayout reward_container_layout;
    private Bundle bundle;
    private LinearLayout ll_reward_container;
    private LinearLayout linear;
    private TextView btn_date;
    private String endTimeText="";
    private LinearLayout titlebar_button_back;
    private LinearLayout ll_voters_container;
    private TextView tv_reward_info;
    private RelativeLayout rl_limit;
    private RelativeLayout rl_no_see;
    private RelativeLayout rl_fill;
    private RelativeLayout rl_reward;


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
        titlebar_button_back=(LinearLayout)findViewById(R.id.titlebar_button_back);
        linear=(LinearLayout) findViewById(R.id.reward_container_layout);
        rl_limit=(RelativeLayout) findViewById(R.id.rl_limit);
        rl_no_see=(RelativeLayout) findViewById(R.id.rl_no_see);
        rl_fill=(RelativeLayout) findViewById(R.id.rl_fill);
        rl_reward=(RelativeLayout) findViewById(R.id.rl_reward);
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
        lladdImage.setImageBitmap(App.voteInfo.getBitmap());
        mRecyclerView = (RecyclerView)findViewById(R.id.id_votes_option_recycler);

        sIfSingle.setChecked(false);
        sIfNoSee.setChecked(true);
        switchifAdd.setChecked(false);
        sIfReward.setChecked(Boolean.parseBoolean(App.voteInfo.getIsReward()));

        //tvPopupTime=(TextView) findViewById(R.id.tv_popup_time);
        layout=(LinearLayout) findViewById(R.id.layout_main);
        btnStartVote=(Button) findViewById(R.id.btnStartVote);

        etTheme=(EditText) findViewById(R.id.etTheme);
        etTheme.setSingleLine(false);
        etTheme.setText(App.voteInfo.getVoteTheme());

        reward_container_layout=(LinearLayout)findViewById(R.id.reward_container_layout);


        //etTheme.setFocusable(false);
        App.voteInfo.setIsLimited(isLimited);
        App.voteInfo.setIsAnonymous(isAnonymous);
        App.voteInfo.setIsRaise(isRaise);

        //投票人员头像
        GridView gridview = (GridView) findViewById(R.id.gridview_voters);
        gridview.setAdapter(new ImageAdapter(this));



        if(App.voteInfo.getVoteTheme()!=null){
            etTheme.setText(App.voteInfo.getVoteTheme().toString());
        }


        //设置不显示数据控件隐藏
//        ll_voters_container.setVisibility(View.GONE);
//        tv_reward_info.setVisibility(View.GONE);
//        reward_container_layout.setVisibility(View.GONE);


        //投票想的渲染
        if(App.voteInfo.getOptions().size()<=0){
            mRecyclerView.setVisibility(View.GONE);
        }else{
            mRecyclerView.setVisibility(View.VISIBLE);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(App.getContext()));
            mRecyclerView.setAdapter(mAdapter = new VotesListAdapter());
        }

        //判断是否显示奖励
        if(App.voteInfo.getIfSetReward()){
            tvAddReward.setText("修改奖励");
            sIfReward.setChecked(true);
            sIfReward.setText("开");
            reward_container_layout.setVisibility(View.VISIBLE);
            tv_reward_info.setVisibility(View.VISIBLE);
            ll_reward_container.setOnClickListener(null);
            setRewardList();
        }else{
            sIfReward.setChecked(false);
            sIfReward.setText("关");
            tvAddReward.setText("添加奖励+");
            reward_container_layout.setVisibility(View.GONE);
            tv_reward_info.setVisibility(View.GONE);
            ll_reward_container.setOnClickListener(toSetReward);
        }

        //判断是否显示时间
        if(!App.voteInfo.getVoteExpireTime().equals("")){
            btn_date.setText(App.voteInfo.getVoteExpireTime());

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

            holder.tv_option_text.setText((position+1)+"."+App.voteInfo.getOptions().get(position).get("item").toString());
            String url = App.voteInfo.getImgUrls().get(position);
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
            return App.voteInfo.getOptions().size();
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
                App.voteInfo.setIsLimited(isLimited);

            }
        });

        //监听主题输入框输入
        etTheme.addTextChangedListener(new EditChangedListener(80));


        //点击开关外层控件选择是否单选
        rl_limit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sIfSingle.isChecked()){
                    sIfSingle.setChecked(false);
                    sIfSingle.setText("多选");
                    isLimited="false";
                }else{
                    sIfSingle.setChecked(true);
                    sIfSingle.setText("单选");
                    isLimited="true";
                }
                App.voteInfo.setIsLimited(isLimited);
            }
        });


        //点击跳转选择投票人页面
        tv_select_voters.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String theme=etTheme.getText().toString();
                App.voteInfo.setVoteTheme(theme);
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
                        SimpleDateFormat currentTime= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                        String nowTimeText = currentTime.format(new Date());// new Date()为获取当前系统时间

                        try{

                            Date endTime=currentTime.parse(endTimeText+" 11:59:59");
                            Date nowTime=currentTime.parse(nowTimeText);
                            if(endTime.before(nowTime)){
                                Toast.makeText(StartActivity.this,"结束时间选择有误",Toast.LENGTH_SHORT).show();

                            }else{
                                btn_date.setText(endTimeText);
                                App.voteInfo.setVoteExpireTime(endTimeText);
                            }

                        }catch (Exception e){
                            e.printStackTrace();
                        }

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
                App.voteInfo.setIsAnonymous(isAnonymous);
                System.out.print(App.voteInfo.getIsAnonymous());
            }
        });
        //点击开关外层控件选择是否匿名
        rl_no_see.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sIfNoSee.isChecked()){
                    sIfNoSee.setChecked(false);
                    sIfNoSee.setText("关");
                    isAnonymous="false";
                }else{
                    sIfNoSee.setChecked(true);
                    sIfNoSee.setText("开");
                    isAnonymous="true";
                }
                App.voteInfo.setIsAnonymous(isAnonymous);
            }
        });

        //点击开关选择是否添加奖励
        sIfReward.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    operateRewardChecked();
                }else{
                    operateRewardNoChecked();
                }
                App.voteInfo.setIsReward(isReward);

            }
        });
        //点击开关外层控件选择是否添加奖励
        rl_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sIfReward.isChecked()){
                    operateRewardNoChecked();
                }else{
                    operateRewardChecked();
                }
                App.voteInfo.setIsReward(isReward);
            }
        });



        //点击开关选择是否加注
        switchifAdd.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(!App.voteInfo.getIfSetReward()){
                        Toast.makeText(StartActivity.this,"请先确认添加奖励",Toast.LENGTH_SHORT).show();
                        switchifAdd.setChecked(false);
                        isRaise="false";
                    }else{
                        switchifAdd.setText("开");
                        isRaise="true";
                    }
                }else{
                    switchifAdd.setText("关");
                    isRaise="false";
                }
                App.voteInfo.setIsRaise(isRaise);

            }
        });
        //点击开关外层控件选择是否加注
        rl_fill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(switchifAdd.isChecked()){
                    switchifAdd.setChecked(false);
                    switchifAdd.setText("关");
                    isRaise="true";
                }else{
                    if(!App.voteInfo.getIfSetReward()){
                        Toast.makeText(StartActivity.this,"请先确认添加奖励",Toast.LENGTH_SHORT).show();
                        switchifAdd.setChecked(false);
                        isRaise="false";
                    }else{
                        switchifAdd.setChecked(true);
                        switchifAdd.setText("开");
                        isRaise="false";
                    }

                }
                App.voteInfo.setIsRaise(isRaise);
            }
        });


        addPlayersButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String theme=etTheme.getText().toString();
                App.voteInfo.setVoteTheme(theme);
                Intent intent = new Intent(StartActivity.this,AddPlayerActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });

        tv_to_add.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String theme=etTheme.getText().toString();
                App.voteInfo.setVoteTheme(theme);
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

                if(etTheme.getText().toString().length()<2){
                    Toast.makeText(StartActivity.this,"主题描述字数在2-80个字之间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(App.voteInfo.getVoteExpireTime().length()<=0){
                    Toast.makeText(StartActivity.this,"还未选择结束时间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(App.voteInfo.getOptions().size()<=0){
                    Toast.makeText(StartActivity.this,"还未设置投票项目",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(App.voteInfo.getOptions().size()<2){
                    Toast.makeText(StartActivity.this,"投票项目数不能少于2，请继续添加",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(App.voteInfo.getVoteTarget().size()<=0){
                    Toast.makeText(StartActivity.this,"还未添加投票参与者",Toast.LENGTH_SHORT).show();
                    return;
                }
                json.put("voteImg",App.voteInfo.getVoteImg());
                json.put("voteTheme",etTheme.getText().toString());
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

    private void operateRewardChecked(){
        if(App.voteInfo.getOptions().size()<=0){
            Toast.makeText(StartActivity.this,"请先添加受益人",Toast.LENGTH_SHORT).show();
            sIfReward.setChecked(false);
            return;
        }

        if(App.voteInfo.getOptions().size()<2){
            Toast.makeText(StartActivity.this,"受益人不能少于两人，请继续添加",Toast.LENGTH_SHORT).show();
            sIfReward.setChecked(false);
            return;
        }

        sIfReward.setText("开");
        sIfReward.setChecked(true);
        tvAddReward.setText("修改奖励");
        App.voteInfo.setIfSetReward(true);
        App.voteInfo.setIsReward(isReward);
        //tv_reward_info.setVisibility(View.VISIBLE);
        linear.setVisibility(View.VISIBLE);
        tv_reward_info.setVisibility(View.VISIBLE);

        String theme=etTheme.getText().toString();
        App.voteInfo.setVoteTheme(theme);
        App.voteInfo.setIfSetReward(true);
        Intent intent = new Intent(StartActivity.this,SetRewardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        startActivity(intent);
    }
    private void operateRewardNoChecked(){
        //关掉奖励开关
        sIfReward.setText("关");
        sIfReward.setChecked(false);

        //关掉加注开关
        switchifAdd.setText("关");
        isRaise="false";

        App.voteInfo.setIfSetReward(false);
        App.voteInfo.setIsReward(isReward);
        tvAddReward.setText("添加奖励+");

        linear.setVisibility(View.GONE);
        //ll_reward_container.setVisibility(View.GONE);
        reward_container_layout.setVisibility(View.GONE);
        tv_reward_info.setVisibility(View.GONE);

        tv_reward_info.setVisibility(View.GONE);
        ll_reward_container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(App.voteInfo.getOptions().size()<=0){
                    Toast.makeText(StartActivity.this,"请先添加投票项",Toast.LENGTH_SHORT).show();
                    return;
                }
                isReward="true";
                App.voteInfo.setIsReward(isReward);
                App.voteInfo.setIfSetReward(true);
                Intent intent = new Intent(StartActivity.this,SetRewardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                startActivity(intent);

            }
        });
    }

    private View.OnClickListener toSetReward = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            if(App.voteInfo.getOptions().size()<=0){
                Toast.makeText(StartActivity.this,"请先添加投票项",Toast.LENGTH_SHORT).show();
                return;
            }
            if(App.voteInfo.getOptions().size()<2){
                Toast.makeText(StartActivity.this,"受益人不能少于两人，请继续添加",Toast.LENGTH_SHORT).show();
                return;
            }
            isReward="true";
            String theme=etTheme.getText().toString();
            App.voteInfo.setVoteTheme(theme);
            App.voteInfo.setIsReward(isReward);
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
            return App.voteInfo.getPlayerList().size();
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
                imageView = new ImageViewPlus(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(0, 0, 0, 0);
            } else {
                imageView = (ImageView) convertView;
            }

            //判断是否有添加voters
            if(App.voteInfo.getPlayerList().size()>0){
                ll_voters_container.setVisibility(View.VISIBLE);
                ll_voters_container.setPadding(20,10,20,10);
                String url = App.voteInfo.getPlayerList().get(position).get("img").toString();
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
                App.voteInfo.setVoteImg(base64);
                App.voteInfo.setBitmap(bitmap);
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
        tv_reward_total_amount.setText("奖励资金: "+App.voteInfo.getVoteFee()+"元");
        reward_container_layout.addView(tv_reward_total_amount);
        if(App.voteInfo.getVoteRewardRule().size()<=0){
            tv_reward_info.setVisibility(View.GONE);
            reward_container_layout.setVisibility(View.GONE);
            return;
        }else{
            tv_reward_info.setVisibility(View.VISIBLE);
            reward_container_layout.setVisibility(View.VISIBLE);
        }
        for(int i=0;i<App.voteInfo.getVoteRewardRule().size();i++){
            TextView tv_reward_option1=new TextView(StartActivity.this);
            tv_reward_option1.setText("第"+(i+1)+"名获得: "+App.voteInfo.getVoteRewardRule().get(i)+"元");
            tv_reward_option1.setPadding(0, 5, 0, 5);//left, top, right, bottom
            reward_container_layout.addView(tv_reward_option1);
        }
    }


}
