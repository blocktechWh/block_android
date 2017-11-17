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
    private static boolean isRaise=true;
    private static String voteExpireTime;
    private static List<Map<String,Object>>options=new ArrayList<>();
    private static List<Integer>voteTarget=new ArrayList<>();
    private static List<Integer>voteRewardRule=new ArrayList<>();
    private static Float voteFee;
    private static List<Integer>voterList=new ArrayList<>();
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
        VoteInfo.voteImg = voteImg;
    }

    public static void setVoteTheme(String voteTheme) {
        VoteInfo.voteTheme = voteTheme;
    }

    public static void setIsLimited(boolean limited) {
        VoteInfo.isLimited = limited;
    }

    public static void setIsAnonymous(boolean anonymous) {
        VoteInfo.isAnonymous = anonymous;
    }

    public static void setVoteExpireTime(String voteExpireTime) {
        VoteInfo.voteExpireTime = voteExpireTime;
    }

    public static void setOptions(List<Map<String, Object>> options) {
        VoteInfo.options = options;
    }

    public static void setVoteTarget(List<Integer> voteTarget) {
        VoteInfo.voteTarget = voteTarget;
    }

    public static void setVoteRewardRule(List<Integer> voteRewardRule) {
        VoteInfo.voteRewardRule = voteRewardRule;
    }

    public static boolean getIsRaise() {
        return isRaise;
    }
    public static void setIsRaise(boolean isRaise) {
        VoteInfo.isRaise=isRaise;
    }

    public static Float getVoteFee() {
        return voteFee;
    }

    public static void setVoteFee(Float voteFee) {
        VoteInfo.voteFee = voteFee;
    }

    public static List<Integer> getVoterList() {
        return voterList;
    }

    public static void setVoterList(List<Integer> voterList) {
        VoteInfo.voterList = voterList;
    }
}
