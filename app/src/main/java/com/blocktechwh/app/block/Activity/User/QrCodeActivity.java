package com.blocktechwh.app.block.Activity.User;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.Urls;
import com.blocktechwh.app.block.CustomView.TitleActivity;
import com.blocktechwh.app.block.R;
import com.blocktechwh.app.block.Utils.CallBack;
import com.blocktechwh.app.block.Utils.HttpClient;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by 跳跳蛙 on 2017/12/21.
 */

public class QrCodeActivity extends TitleActivity{
    private ImageView iv_user_img;
    private TextView tv_user_name;
    private TextView tv_user_phone;
    private ImageView iv_qr_code;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        initTitle("扫我加好友");

        App.getInstance().addActivity(this);

        initView();
        getData();
    }

    private void initView(){
        tv_user_name = (TextView)findViewById(R.id.tv_user_name);
        tv_user_phone = (TextView)findViewById(R.id.tv_user_phone);
        iv_user_img = (ImageView) findViewById(R.id.iv_user_img);
        iv_qr_code = (ImageView) findViewById(R.id.iv_qr_code);
        tv_user_name.setText(App.userInfo.getName());
        tv_user_phone.setText(App.userInfo.getPhone());

        String url = Urls.HOST + "staticImg" + App.userInfo.getImg();
        HttpClient.getImage(this, url, new CallBack<Bitmap>() {
            @Override
            public void onSuccess(final Bitmap bmp) {
                iv_user_img.setImageBitmap(bmp);
            }
        });

        JSONObject json = new JSONObject();
        json.put("id",App.userInfo.getId());
        json.put("name",App.userInfo.getName());
        System.out.println("name="+App.userInfo.getName());
        json.put("img",App.userInfo.getImg());
        iv_qr_code.setImageBitmap(generateBitmap(json.toJSONString(),400,400));
    }

    private void getData(){

    }

    private Bitmap generateBitmap(String content, int width, int height){
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }
}
