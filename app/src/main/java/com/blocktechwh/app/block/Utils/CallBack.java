package com.blocktechwh.app.block.Utils;

import android.text.TextUtils;
import android.widget.Toast;

import com.blocktechwh.app.block.Common.App;

/**
 * Created by eagune on 2017/11/1.
 */
public abstract class CallBack<T>{

    public abstract void onSuccess(T result);

    public void onFailure(int errorType, String message) {
        if (errorType != 0) {
            if(!TextUtils.isEmpty(message)){
                Toast.makeText(App.getContext(), message, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(App.getContext(), "系统异常！", Toast.LENGTH_SHORT).show();
        }
        System.out.println("errorType = [" + errorType + "], message = [" + message + "]");
    }

    public void ErrorHandler(String statusCode, String msg){
        ErrorPromptPropertiesUtil eppu=new ErrorPromptPropertiesUtil();
        String errMsg =  "".equals(eppu.getProperty(statusCode))?msg:eppu.getProperty(statusCode);
        this.onFailure(2, errMsg);
    }
}
