package com.blocktechwh.app.block.Bean;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 跳跳蛙 on 2017/11/17.
 */

public class VoteInfo {
    private static List<String>imgUrls=new ArrayList<String>();
    private static String voteImg;
    private static String voteTheme;
    private static boolean isLimited=false;
    private static boolean isAnonymous=true;
    private static String voteExpireTime;
    private static List<Map<String,Object>>options=new ArrayList<>();
    private static List<Integer>voteTarget=new ArrayList<>();
    private static List<Integer>voteRewardRule=new ArrayList<>();

    public VoteInfo(){

    }

    public static List<String> getImgUrls() {
        return imgUrls;
    }

    public static String getVoteImg() {
        return voteImg;
    }

    public static String getVoteTheme() {
        return voteTheme;
    }

    public static boolean getIsLimited() {
        return isLimited;
    }

    public static boolean getIsAnonymous() {
        return isAnonymous;
    }

    public static String getVoteExpireTime() {
        return voteExpireTime;
    }

    public static List<Map<String, Object>> getOptions() {
        return options;
    }

    public static List<Integer> getVoteTarget() {
        return voteTarget;
    }

    public static List<Integer> getVoteRewardRule() {
        return voteRewardRule;
    }

    public static void setImgUrls(List<String> imgUrls) {
        VoteInfo.imgUrls = imgUrls;
    }

    public static void setVoteImg(String voteImg) {
        voteImg = voteImg;
    }

    public static void setVoteTheme(String voteTheme) {
        voteTheme = voteTheme;
    }

    public static void setIsLimited(boolean limited) {
        isLimited = limited;
    }

    public static void setIsAnonymous(boolean anonymous) {
        isAnonymous = anonymous;
    }

    public static void setVoteExpireTime(String voteExpireTime) {
        voteExpireTime = voteExpireTime;
    }

    public static void setOptions(List<Map<String, Object>> options) {
        options = options;
    }

    public static void setVoteTarget(List<Integer> voteTarget) {
        voteTarget = voteTarget;
    }

    public static void setVoteRewardRule(List<Integer> voteRewardRule) {
        voteRewardRule = voteRewardRule;
    }
}
