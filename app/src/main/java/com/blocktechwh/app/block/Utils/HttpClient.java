package com.blocktechwh.app.block.Utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import com.alibaba.fastjson.JSONObject;
import com.blocktechwh.app.block.Activity.User.LoginActivity;
import com.blocktechwh.app.block.Common.App;

import java.io.IOException;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by eagune on 2017/11/1.
 */
public class HttpClient {
    private static Handler handler = new Handler(Looper.getMainLooper());
    private static OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build();
    private static OkHttpClient.Builder client=new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS);
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public static void getImage(Object tag, String url, final CallBack callBack){
        Request request = new Request.Builder()
                .tag(tag)
                .url(url)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handleError(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                byte[] bytes = response.body().bytes();
                final Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.onSuccess(bmp);
                    }
                });
            }
        });
    }

    public static void delete(Object tag, String url, String json, final CallBack callBack) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        String Cookie_token = PreferencesUtils.getString(App.getContext(), "Cookie_token", UUID.randomUUID().toString());

        Request request = new Request.Builder()
                .addHeader("Cookie_token", Cookie_token)
                .addHeader("Authorization", App.token)
                .tag(tag)
                .url(url)
                .delete(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handleError(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { handleResponse(response, callBack); }
        });
    }

    public static void put(Object tag, String url, String json, final CallBack callBack) {
        System.out.println("urlkizi = " + url);
        RequestBody requestBody = RequestBody.create(JSON, json);

        String Cookie_token = PreferencesUtils.getString(App.getContext(), "Cookie_token", UUID.randomUUID().toString());

        Request request = new Request.Builder()
                .addHeader("Cookie_token", Cookie_token)
                .addHeader("Authorization", App.token)
                .tag(tag)
                .url(url)
                .put(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handleError(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { handleResponse(response, callBack); }
        });
    }

    public static void post(Object tag, String url, String json, final CallBack callBack) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        String Cookie_token = PreferencesUtils.getString(App.getContext(), "Cookie_token", UUID.randomUUID().toString());

        Request request = new Request.Builder()
                .addHeader("Cookie_token", Cookie_token)
                .addHeader("Authorization", App.token)
                .tag(tag)
                .url(url)
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                handleError(e, callBack);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException { handleResponse(response, callBack); }
        });
    }

    public static void get(Object tag, String url, HashMap<String, String> map, final CallBack callBack) {
        if (map != null) {
            url += "?";
            for (String s : map.keySet()) {
                url += s + "=" + map.get(s) + "&";
            }
        }
        System.out.println("urlkizi = " + url);

        String Cookie_token = PreferencesUtils.getString(App.getContext(), "Cookie_token", UUID.randomUUID().toString());

        Request request = new Request.Builder()
                .addHeader("Cookie_token", Cookie_token)
                .addHeader("Authorization", App.token)
                .tag(tag)
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) { handleError(e, callBack); }
            @Override
            public void onResponse(final Call call, Response response) throws IOException { handleResponse(response, callBack); }
        });

    }

    private static void handleResponse(Response response, final CallBack callBack) throws IOException {
        int httpCode = response.code();
        System.out.println("http code is "+httpCode);
        if ((httpCode/100) == 5){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onFailure(2, "服务器内部错误");
                }
            });
        }else if((httpCode/100) == 4){
            handler.post(new Runnable() {
                @Override
                public void run() {
                    callBack.onFailure(2, "网络请求错误");
                }
            });

        }else if((httpCode/100) == 2){
            String json = response.body().string();
            json = json.replace("null", "\"\"");
            final String finalJson = json;
            System.out.println(finalJson);
            final JSONObject jsonObject = JSONObject.parseObject(finalJson);
            final String statusCode = jsonObject.getString("code");
            if(statusCode.equals("000")){
                final String dataStr = jsonObject.getString("data");
                if("".equals(dataStr)){
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.onSuccess(null);
                        }
                    });
                }else{
                    final char fistChar = dataStr.charAt(0);
                    if(fistChar == '{'){
                        handler.post(new Runnable() {
                            @Override
                            public void run() { callBack.onSuccess(JSONObject.parseObject(dataStr)); }
                        });
                    }else if(fistChar == '['){
                        handler.post(new Runnable() {
                            @Override
                            public void run() { callBack.onSuccess(JSONObject.parseArray(dataStr));     }
                        });
                    }else{
                        final JSONObject dataJson = new JSONObject();
                        dataJson.put("data",dataStr);
                        handler.post(new Runnable() {
                            @Override
                            public void run() { callBack.onSuccess(dataJson);     }
                        });
                    }
                }
            }else if(statusCode.equals("403")){
                System.out.println("您未登录，请重新登录！");
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(App.getContext(),"登陆异常，请重新登录！",Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(App.getContext(),LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                        App.getContext().startActivity(intent);
                    }
                });

            }else{
                handler.post(new Runnable() {
                    @Override
                    public void run() { callBack.ErrorHandler(statusCode, jsonObject.getString("msg"));     }
                });
            }
        }
    }

    private static void handleError(final IOException e, final CallBack callBack) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                System.out.println("e.getLocalizedMessage() = " + e.getLocalizedMessage());
                callBack.onFailure(4, e.getLocalizedMessage());
            }
        });
    }

}
