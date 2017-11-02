package com.blocktechwh.app.block.Common;

import java.util.HashMap;

/**
 * Created by eagune on 2017/11/2.
 */
public class ErrorTip {

    private static HashMap<String, String> reasonMap = new HashMap<String, String>(){
        {
            put("50800001","注册失败");
            put("50800002","未找到用户");
            put("50800003","添加用户失败");
            put("50800004","添加联系人失败");
            put("50800005","联系人未找到");
            put("50800006","usre has created this group");
            put("50800007","usre has the same group name");
            put("50800008","群组不存在");
            put("50800009","密码不一致");
            put("50800010","验证码错误");
            put("50800011","该手机号已注册");
        }
    };

    public static String getReason(String code){
        return reasonMap.get(code)== null?"":reasonMap.get(code);
    }
}
