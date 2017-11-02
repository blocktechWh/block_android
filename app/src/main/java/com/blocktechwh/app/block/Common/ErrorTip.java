package com.blocktechwh.app.block.Common;

import java.util.HashMap;

/**
 * Created by eagune on 2017/11/2.
 */
public class ErrorTip {

    private static HashMap<String, String> reasonMap = new HashMap<String, String>(){
        {
            put("50800001","注册失败");
            put("50800002","user not found");
            put("50800003","add user error");
            put("50800004","add contact error");
            put("50800005","contact not found");
            put("50800006","usre has created this group");
            put("50800007","usre has the same group name");
            put("50800008","group not exist");
            put("50800009","password not same");
            put("50800010","identify code error.");
            put("50800011","user phone has exist");
        }
    };

    public static String getReason(String code){
        return reasonMap.get(code);
    }
}
