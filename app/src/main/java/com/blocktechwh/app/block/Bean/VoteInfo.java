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
    private static String isLimited="false";
    private static String isAnonymous="true";
    private static String isRaise="true";
    private static String isReward="false";
    private static String voteExpireTime;
    private static List<Map<String,Object>>options=new ArrayList<>();
    private static List<Integer>voteTarget=new ArrayList<>();
    private static List<Double>voteRewardRule=new ArrayList<>();
    private static Double voteFee;
    private static List<Integer>voterList=new ArrayList<>();
    private static List<Map<String,Object>>playerList=new ArrayList<>();
    private static List<Integer>voterTargetList=new ArrayList<>();


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



    public static String getVoteExpireTime() {
        return voteExpireTime;
    }

    public static List<Map<String, Object>> getOptions() {
        return options;
    }

    public static List<Integer> getVoteTarget() {
        return voteTarget;
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



    public static void setVoteExpireTime(String voteExpireTime) {
        VoteInfo.voteExpireTime = voteExpireTime;
    }

    public static void setOptions(List<Map<String, Object>> options) {
        VoteInfo.options = options;
    }

    public static void setVoteTarget(List<Integer> voteTarget) {
        VoteInfo.voteTarget = voteTarget;
    }

    public static List<Double> getVoteRewardRule() {
        return voteRewardRule;
    }

    public static void setVoteRewardRule(List<Double> voteRewardRule) {
        VoteInfo.voteRewardRule = voteRewardRule;
    }

    public static Double getVoteFee() {
        return voteFee;
    }

    public static void setVoteFee(Double voteFee) {
        VoteInfo.voteFee = voteFee;
    }

    public static List<Integer> getVoterList() {
        return voterList;
    }

    public static void setVoterList(List<Integer> voterList) {
        VoteInfo.voterList = voterList;
    }


    public static List<Map<String, Object>> getPlayerList() {
        return playerList;
    }

    public static void setPlayerList(List<Map<String, Object>> playerList) {
        VoteInfo.playerList = playerList;
    }

    public static String getIsLimited() {
        return isLimited;
    }

    public static void setIsLimited(String isLimited) {
        VoteInfo.isLimited = isLimited;
    }

    public static String getIsAnonymous() {
        return isAnonymous;
    }

    public static void setIsAnonymous(String isAnonymous) {
        VoteInfo.isAnonymous = isAnonymous;
    }

    public static String getIsRaise() {
        return isRaise;
    }

    public static void setIsRaise(String isRaise) {
        VoteInfo.isRaise = isRaise;
    }

    public static String getIsReward() {
        return isReward;
    }

    public static void setIsReward(String isReward) {
        VoteInfo.isReward = isReward;
    }

    public static List<Integer> getVoterTargetList() {
        return voterTargetList;
    }

    public static void setVoterTargetList(List<Integer> voterTargetList) {
        VoteInfo.voterTargetList = voterTargetList;
    }
}
