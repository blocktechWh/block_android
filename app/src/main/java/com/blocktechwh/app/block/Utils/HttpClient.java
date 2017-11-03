package com.blocktechwh.app.block.Utils;

import android.os.Handler;
import android.os.Looper;

import org.json.JSONObject;

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

import com.blocktechwh.app.block.Common.App;
import com.blocktechwh.app.block.Common.ErrorTip;

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

    public static void post(Object tag, String url, String json, final CallBack callBack) {
        RequestBody requestBody = RequestBody.create(JSON, json);

        String Cookie_token = PreferencesUtils.getString(App.getContext(), "Cookie_token", UUID.randomUUID().toString());
        String token = App.token;

        Request request = new Request.Builder()
                .addHeader("Cookie_token", Cookie_token)
                .addHeader("Authorization", token)
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

        Request request = new Request.Builder()
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
        String json = response.body().string();
        json = json.replace("null", "\"\"");
        final String finalJson = json;
        System.out.println(finalJson);
        handler.post(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject jsonObject = new JSONObject(finalJson);
                    String statusCode = jsonObject.getString("code");
                    if(statusCode.equals("000")){
                        String dataStr = jsonObject.getString("data");
                        if("".equals(dataStr)){
                            callBack.onSuccess(null);
                        }else{
                            final char fistChar = dataStr.charAt(0);
                            if(fistChar == '{'){
                                JSONObject dataJson = new JSONObject(dataStr);
                                callBack.onSuccess(dataJson);
                            }else{
                                JSONObject dataJson = new JSONObject();
                                dataJson.put("data",dataStr);
                                callBack.onSuccess(dataJson);
                            }
                        }

                    }else{
                        String msg = jsonObject.getString("msg");
                        String errMsg =  "".equals(ErrorTip.getReason(statusCode))?msg:ErrorTip.getReason(statusCode);
                        callBack.onFailure(2, errMsg);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    callBack.onFailure(4, e.getLocalizedMessage());
                }
            }
        });
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
