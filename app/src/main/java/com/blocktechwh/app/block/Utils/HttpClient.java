package com.blocktechwh.app.block.Utils;

import android.os.Handler;
import android.os.Looper;

import java.io.IOException;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
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
            public void onFailure(Call call, final IOException e) {
                System.out.println("onFail+++");
//                handleError(e, callBack);
            }
            @Override
            public void onResponse(final Call call, Response response) throws IOException {
                String json = response.body().string();
                json = json.replace("null", "\"\"");
                callBack.onSuccess(json);
//                handleResponse(response, callBack);
            }
        });

    }

}
