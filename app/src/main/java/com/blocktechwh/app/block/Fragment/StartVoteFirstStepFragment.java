package com.blocktechwh.app.block.Fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.blocktechwh.app.block.Activity.Actions.StartActivity;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.EditChangedListener;
import com.blocktechwh.app.block.CustomView.DoubleDatePickerDialog;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.ImageUtil;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class StartVoteFirstStepFragment extends Fragment {
    private View view;
    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果
    private String theme;//主题
    private String themeImg;//主题图片
    private EditText etTheme;

    private Switch sIfReward;
    private Switch sIfSingle;
    private Switch sIfNoSee;
    private Switch switchifAdd;

    private LinearLayout titlebar_button_back;
    private String isLimited="false";

    private RelativeLayout rl_limit;
    private RelativeLayout rl_no_see;
    private RelativeLayout rl_fill;
    private RelativeLayout rl_reward;
    private Button btn_next_step;

    private TextView btn_date;
    private String endTimeText="";
    private String isAnonymous="true";
    private String isReward="false";
    private String isRaise="false";

    private FragmentManager fragmentManager;
    //private FragmentTransaction fragmentTransaction;

    private StartVoteFirstStepFragment svfsf;
    private StartVoteSecondStepFragment svssf;
    private StartVoteThirdStepFragment svtsf;
    private StartVoteFouthStepFragment svfosf;
    private ImageView lladdImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_start_vote_1, container, false);

        initView();
        addEvent();
        return view;
    }

    private void initView(){
        lladdImage=(ImageView) view.findViewById(R.id.id_add_image);
        lladdImage.setImageBitmap(App.voteInfo.getBitmap());
        etTheme=(EditText) view.findViewById(R.id.etTheme);
        etTheme.clearFocus();
        etTheme.setSingleLine(false);
        etTheme.setText(App.voteInfo.getVoteTheme());

        sIfSingle=(Switch) view.findViewById(R.id.switch_vote);
        sIfNoSee=(Switch) view.findViewById(R.id.switch_vote2);
        sIfReward=(Switch) view.findViewById(R.id.sIfReward);

        titlebar_button_back=view.findViewById(R.id.titlebar_button_back);

        rl_no_see=view.findViewById(R.id.rl_no_see);
        rl_limit=view.findViewById(R.id.rl_limit);
        rl_fill=view.findViewById(R.id.rl_fill);
        rl_reward=view.findViewById(R.id.rl_reward);
        btn_next_step=view.findViewById(R.id.btn_next_step);

        sIfSingle.setChecked(false);
        sIfNoSee.setChecked(true);
        btn_date=view.findViewById(R.id.btn_date);
        sIfReward.setChecked(Boolean.parseBoolean(App.voteInfo.getIsReward()));
        App.voteInfo.setIsLimited(isLimited);
        App.voteInfo.setIsAnonymous(isAnonymous);
        App.voteInfo.setIsRaise(isRaise);

        //判断是否显示时间
        if(!App.voteInfo.getVoteExpireTime().equals("")){
            btn_date.setText(App.voteInfo.getVoteExpireTime());

        }

        fragmentManager = getActivity().getSupportFragmentManager();
    }



    private void addEvent(){

        lladdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
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
        etTheme.addTextChangedListener(new EditChangedListener(25));


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


        //点击弹出时间选择对话框
        btn_date.setOnClickListener(new View.OnClickListener() {
            Calendar c = Calendar.getInstance();

            @Override
            public void onClick(View v) {
                // 最后一个false表示不显示日期，如果要显示日期，最后参数可以是true或者不用输入
                new DoubleDatePickerDialog(getContext(), 0, new DoubleDatePickerDialog.OnDateSetListener() {

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
                                Toast.makeText(getContext(),"结束时间应晚于当前时间",Toast.LENGTH_SHORT).show();

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
                    sIfReward.setText("开");
                    App.voteInfo.setIfSetReward(true);
                    StartActivity sa=(StartActivity)getActivity();
                    sa.showTab4();
                }else{
                    sIfReward.setText("关");
                    App.voteInfo.setIfSetReward(false);
                    StartActivity sa=(StartActivity)getActivity();
                    sa.hideTab4();
                }
                App.voteInfo.setIsReward(isReward);

            }
        });
        //点击开关外层控件选择是否添加奖励
        rl_reward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sIfReward.isChecked()){
                    //关掉奖励开关
                    sIfReward.setText("关");
                    sIfReward.setChecked(false);
                    App.voteInfo.setIfSetReward(false);
                    StartActivity sa=(StartActivity)getActivity();
                    sa.hideTab4();
                }else{
                    sIfReward.setText("开");
                    sIfReward.setChecked(true);
                    App.voteInfo.setIfSetReward(true);
                    StartActivity sa=(StartActivity)getActivity();
                    sa.showTab4();
                }
                App.voteInfo.setIsReward(isReward);
            }
        });


        //点击“下一步”
        btn_next_step.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String theme=etTheme.getText().toString().trim();
                if(theme.length()<2){
                    Toast.makeText(getContext(),"主题描述字数在2-25个字之间",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(App.voteInfo.getVoteExpireTime().length()<=0){
                    Toast.makeText(getContext(),"还未选择结束时间",Toast.LENGTH_SHORT).show();
                    return;
                }
                App.voteInfo.setVoteTheme(theme);
                App.voteInfo.setFirstStepFinished(true);

                //跳转
                StartActivity sa=(StartActivity)getActivity();
                sa.SelectFragment(1);

                //标记完成
                TextView btn_create=(TextView)getActivity().findViewById(R.id.btn_create);
                btn_create.setBackground(getResources().getDrawable(R.drawable.lines_bg6));
                btn_create.setText("✔");
                TextView btn_add_option=(TextView)getActivity().findViewById(R.id.btn_add_option);
                btn_add_option.setBackground(getResources().getDrawable(R.drawable.lines_bg5));
                btn_add_option.setTextColor(Color.argb(255,66,143,219));
            }
        });



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
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



    //@Override
  /*  public void onActivityResult(int requestCode, int resultCode, Intent data) {
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
    }*/




/*    private void operateRewardChecked(){
        if(App.voteInfo.getOptions().size()<=0){
            Toast.makeText(getContext(),"请先添加受益人",Toast.LENGTH_SHORT).show();
            sIfReward.setChecked(false);
            return;
        }

        if(App.voteInfo.getOptions().size()<2){
            Toast.makeText(getContext(),"受益人不能少于两人，请继续添加",Toast.LENGTH_SHORT).show();
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
    }*/


}
