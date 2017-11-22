package com.blocktechwh.app.block.Bean;

import java.util.List;
import java.util.Map;

/**
 * Created by 跳跳蛙 on 2017/11/19.
 */

public class VotedDetail {
    private static String voteImg;
    private static String voteTheme;
    private static List<Map<String,Object>>voteOptionsList;
    private static List<Map<String,Object>>voteRewardsList;
    private static Double rewardTotal;
    private static List<String> playersList;
    private static int totalCount;

    public VotedDetail(){

    }

    public static String getVoteImg() {
        return voteImg;
    }

    public static void setVoteImg(String voteImg) {
        VotedDetail.voteImg = voteImg;
    }

    public static List<Map<String, Object>> getVoteOptionsList() {
        return voteOptionsList;
    }

    public static void setVoteOptionsList(List<Map<String, Object>> voteOptionsList) {
        VotedDetail.voteOptionsList = voteOptionsList;
    }

    public static List<Map<String, Object>> getVoteRewardsList() {
        return voteRewardsList;
    }

    public static void setVoteRewardsList(List<Map<String, Object>> voteRewardsList) {
        VotedDetail.voteRewardsList = voteRewardsList;
    }

    public static Double getRewardTotal() {
        return rewardTotal;
    }

    public static void setRewardTotal(Double rewardTotal) {
        VotedDetail.rewardTotal = rewardTotal;
    }

    public static List<String> getPlayersList() {
        return playersList;
    }

    public static void setPlayersList(List<String> playersList) {
        VotedDetail.playersList = playersList;
    }

    public static String getVoteTheme() {
        return voteTheme;
    }

    public static void setVoteTheme(String voteTheme) {
        VotedDetail.voteTheme = voteTheme;
    }

    public static int getTotalCount() {
        return totalCount;
    }

    public static void setTotalCount(int totalCount) {
        VotedDetail.totalCount = totalCount;
    }
}
