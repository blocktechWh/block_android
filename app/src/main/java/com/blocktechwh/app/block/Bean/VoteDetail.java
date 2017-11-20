package com.blocktechwh.app.block.Bean;

import java.util.List;
import java.util.Map;

/**
 * Created by 跳跳蛙 on 2017/11/19.
 */

public class VoteDetail {
    private static int voteImg;
    private static String voteTheme;
    private static List<Map<String,Object>>voteOptionsList;
    private static List<String>voteRewardsList;
    private static Double rewardTotal;
    private static List<Integer> playersList;

    public VoteDetail(){

    }

    public static int getVoteImg() {
        return voteImg;
    }

    public static void setVoteImg(int voteImg) {
        VoteDetail.voteImg = voteImg;
    }

    public static List<Map<String, Object>> getVoteOptionsList() {
        return voteOptionsList;
    }

    public static void setVoteOptionsList(List<Map<String, Object>> voteOptionsList) {
        VoteDetail.voteOptionsList = voteOptionsList;
    }

    public static List<String> getVoteRewardsList() {
        return voteRewardsList;
    }

    public static void setVoteRewardsList(List<String> voteRewardsList) {
        VoteDetail.voteRewardsList = voteRewardsList;
    }

    public static Double getRewardTotal() {
        return rewardTotal;
    }

    public static void setRewardTotal(Double rewardTotal) {
        VoteDetail.rewardTotal = rewardTotal;
    }

    public static List<Integer> getPlayersList() {
        return playersList;
    }

    public static void setPlayersList(List<Integer> playersList) {
        VoteDetail.playersList = playersList;
    }

    public static String getVoteTheme() {
        return voteTheme;
    }

    public static void setVoteTheme(String voteTheme) {
        VoteDetail.voteTheme = voteTheme;
    }
}
