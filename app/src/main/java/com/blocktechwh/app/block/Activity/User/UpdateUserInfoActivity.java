package com.blocktechwh.app.block.Activity.User;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;

import com.blocktechwh.app.block.Bean.User;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.InputDialog;
import com.blocktechwh.app.block.CustomView.SelectDialog;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.blocktechwh.app.block.Utils.ImageUtil;
import com.blocktechwh.app.block.Utils.PreferencesUtils;

import java.util.HashMap;

public class UpdateUserInfoActivity extends TitleActivity {

    private static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    private static final int PHOTO_REQUEST_CUT = 3;// 结果

    private InputDialog inputDialog;
    private SelectDialog selectDialog;
    private TextView userName;
    private TextView userPhone;
    private TextView userAddress;
    private TextView userSex;
    private ImageView userPhoto;

    private String jsonKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_info);

        initTitle("更新用户资料");
        initView();
    }

    private void initView(){
        userName = (TextView)findViewById(R.id.text_user_name);
        userPhone = (TextView)findViewById(R.id.text_user_phone);
        userAddress = (TextView)findViewById(R.id.text_user_address);
        userSex = (TextView)findViewById(R.id.text_user_sex);
        userPhoto = (ImageView)findViewById(R.id.id_user_photo);

        setUserData();

        inputDialog = new InputDialog(this){
            @Override
            public void handle(String string){
                mInputHandler(string);
            }
        };

        selectDialog = new SelectDialog(this){
            @Override
            public void handle(String string){
                mInputHandler(string);
            }
        };

        ((LinearLayout)findViewById(R.id.id_user_name_btn)).setOnClickListener(mClickListener);
        ((LinearLayout)findViewById(R.id.id_user_address_btn)).setOnClickListener(mClickListener);
        ((LinearLayout)findViewById(R.id.id_user_sex_btn)).setOnClickListener(mClickListener);
        ((LinearLayout)findViewById(R.id.id_user_photo_btn)).setOnClickListener(mClickListener);
    }

    private void setUserData(){
        userName.setText(App.userInfo.getName());
        userPhone.setText(App.userInfo.getPhone());
        userAddress.setText(App.userInfo.getAddress());
        userSex.setText(App.userInfo.getSex());
        String url = Urls.HOST + "staticImg" + App.userInfo.getImg();
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                userPhoto.setImageBitmap(bmp);
            }
        });
    }

    private View.OnClickListener mClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.id_user_name_btn:
                    jsonKey = "userName";
                    inputDialog.show();
                    inputDialog.setTitleText("更改用户名");
                    break;
                case R.id.id_user_address_btn:
                    jsonKey = "address";
                    inputDialog.show();
                    inputDialog.setTitleText("更改地区");
                    break;
                case R.id.id_user_sex_btn:
                    jsonKey = "sex";
                    selectDialog.show();
                    HashMap<Integer,String> map = new HashMap<Integer,String>();
                    map.put(1,"男");
                    map.put(2,"女");
                    selectDialog.setChoices(map);
                    selectDialog.setTitleText("更改性别");
                    break;
                case R.id.id_user_photo_btn:
                    Intent intent = new Intent(Intent.ACTION_PICK);
                    intent.setType("image/*");
                    startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
                    break;
            }
        }
    };

    private void mInputHandler(final String string){
        JSONObject json = new JSONObject();
        json.put(jsonKey, string);
        HttpClient.put(this, Urls.UpdateUserInfo, json.toString(), new CallBack<JSONObject>() {
            @Override
            public void onSuccess(JSONObject result) {
                App.userInfo = result.toJavaObject(User.class);
                setUserData();
                PreferencesUtils.putString(App.getContext(),"UserInfo", JSONObject.toJSONString(App.userInfo));
            }
        });
    }

    @Override
    public void onPause(){
        super.onPause();
        if(inputDialog != null){
            inputDialog.dismiss();
        }
        if(selectDialog != null){
            selectDialog.dismiss();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PHOTO_REQUEST_GALLERY) {// 从相册返回的数据
            if (data != null) {
                Uri uri = data.getData();// 得到图片的全路径
                crop(uri);
            }
        } else if (requestCode == PHOTO_REQUEST_CUT) {// 从剪切图片返回的数据
            if (data != null && data.hasExtra("data")) {
                Bitmap bitmap = data.getParcelableExtra("data");
                String base64 = ImageUtil.bitmapToBase64(bitmap);
                jsonKey = "img";
                mInputHandler(base64);
            }
        }
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);

        //SimpleDateFormat sDateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
        //String address = sDateFormat.format(new java.util.Date());
        //imagePath = address + ".JPEG";
        //　Uri imageUri = Uri.parse("保存的文件夹的名称/" + address
        // + ".JPEG");
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);//输出路径

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", true);

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
}
