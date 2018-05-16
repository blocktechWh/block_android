package com.blocktechwh.app.block.Bean;

import java.util.List;
import java.util.Map;

/**
 * Created by 跳跳蛙 on 2017/11/19.
 */

public class VoteDetail {
    private static int voteId;
    private static String voteImg;
    private static String voteTheme;
    private static List<Map<String,Object>>voteOptionsList;
    private static List<Map<String,Object>>voteRewardsList;
    private static Double rewardTotal;
    private static List<String> playersList;
    private static List<String> checkedOptionIds;
    private static String creater;

    public VoteDetail(){

    }

    public static String getVoteImg() {
        return voteImg;
    }

    public static void setVoteImg(String voteImg) {
        VoteDetail.voteImg = voteImg;
    }

    public static List<Map<String, Object>> getVoteOptionsList() {
        return voteOptionsList;
    }

    public static void setVoteOptionsList(List<Map<String, Object>> voteOptionsList) {
        VoteDetail.voteOptionsList = voteOptionsList;
    }

    public static List<Map<String, Object>> getVoteRewardsList() {
        return voteRewardsList;
    }

    public static void setVoteRewardsList(List<Map<String, Object>> voteRewardsList) {
        VoteDetail.voteRewardsList = voteRewardsList;
    }

    public static Double getRewardTotal() {
        return rewardTotal;
    }

    public static void setRewardTotal(Double rewardTotal) {
        VoteDetail.rewardTotal = rewardTotal;
    }

    public static List<String> getPlayersList() {
        return playersList;
    }

    public static void setPlayersList(List<String> playersList) {
        VoteDetail.playersList = playersList;
    }

    public static String getVoteTheme() {
        return voteTheme;
    }

    public static void setVoteTheme(String voteTheme) {
        VoteDetail.voteTheme = voteTheme;
    }

    public static int getVoteId() {
        return voteId;
    }

    public static void setVoteId(int voteId) {
        VoteDetail.voteId = voteId;
    }

    public static List<String> getCheckedOptionIds() {
        return checkedOptionIds;
    }

    public static void setCheckedOptionIds(List<String> checkedOptionIds) {
        VoteDetail.checkedOptionIds = checkedOptionIds;
    }

    public static String getCreater() {
        return creater;
    }

    public static void setCreater(String creater) {
        VoteDetail.creater = creater;
    }
}
