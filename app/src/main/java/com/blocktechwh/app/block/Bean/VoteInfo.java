package com.blocktechwh.app.block.Bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by 跳跳蛙 on 2017/11/17.
 */

public class VoteInfo implements Serializable{
    private  List<String>imgUrls=new ArrayList<String>();
    private  String voteImg;
    private  String voteTheme;
    private  String isLimited;
    private  String isAnonymous;
    private  String isRaise;
    private  String isReward;
    private  String voteExpireTime;
    private  List<Map<String,Object>>options;
    private  List<Integer>voteTarget;
    private  List<Integer>voteRewardRule;
    private  int voteFee;
    private  List<Integer>voterList;
    private  List<Map<String,Object>>playerList;
    private  List<Integer>voterTargetList;
    private  Bitmap bitmap;
    private  List<Integer>checkedRadioButtonList;
    private  List<Integer>checkedPositionList;
    private  boolean ifSetReward;
    private  boolean isFirstStepFinished;
    private  boolean isSecondStepFinished;
    private  boolean isThirdStepFinished;
    private  boolean isFouthStepFinished;



    public VoteInfo(){
        this.isLimited="false";
        this.isAnonymous="true";
        this.isRaise="false";
        this.isReward="false";
        this.ifSetReward=false;
        this.voteFee=0;
        this.bitmap=null;
        this.ifSetReward=false;
        this.options=new ArrayList<>();
        this.voteTarget=new ArrayList<>();
        this.voteRewardRule=new ArrayList<>();
        this.voterList=new ArrayList<>();
        this.playerList=new ArrayList<>();
        this.voterTargetList=new ArrayList<>();
        this.checkedRadioButtonList=new ArrayList<>();
        this.checkedPositionList=new ArrayList<>();
        this.imgUrls=new ArrayList<>();
        this.isFirstStepFinished=false;
        this.isSecondStepFinished=false;
        this.isThirdStepFinished=false;
        this.isFouthStepFinished=false;
    }

    public  List<String> getImgUrls() {
        return imgUrls;
    }

    public  String getVoteImg() {
        return voteImg;
    }

    public  String getVoteTheme() {
        return voteTheme;
    }



    public  String getVoteExpireTime() {
        return voteExpireTime;
    }

    public  List<Map<String, Object>> getOptions() {
        return options;
    }

    public  List<Integer> getVoteTarget() {
        return voteTarget;
    }


    public  void setImgUrls(List<String> imgUrls) {
        this.imgUrls = imgUrls;
    }

    public  void setVoteImg(String voteImg) {
        this.voteImg = voteImg;
    }

    public  void setVoteTheme(String voteTheme) {
        this.voteTheme = voteTheme;
    }



    public  void setVoteExpireTime(String voteExpireTime) {
        this.voteExpireTime = voteExpireTime;
    }

    public  void setOptions(List<Map<String, Object>> options) {
        this.options = options;
    }

    public  void setVoteTarget(List<Integer> voteTarget) {
        this.voteTarget = voteTarget;
    }

    public  List<Integer> getVoteRewardRule() {
        return voteRewardRule;
    }

    public  void setVoteRewardRule(List<Integer> voteRewardRule) {
        this.voteRewardRule = voteRewardRule;
    }

    public  int getVoteFee() {
        return voteFee;
    }

    public  void setVoteFee(int voteFee) {
        this.voteFee = voteFee;
    }

    public  List<Integer> getVoterList() {
        return voterList;
    }

    public  void setVoterList(List<Integer> voterList) {
        this.voterList = voterList;
    }


    public  List<Map<String, Object>> getPlayerList() {
        return playerList;
    }

    public  void setPlayerList(List<Map<String, Object>> playerList) {
        this.playerList = playerList;
    }

    public  String getIsLimited() {
        return isLimited;
    }

    public  void setIsLimited(String isLimited) {
        this.isLimited = isLimited;
    }

    public  String getIsAnonymous() {
        return isAnonymous;
    }

    public  void setIsAnonymous(String isAnonymous) {
        this.isAnonymous = isAnonymous;
    }

    public  String getIsRaise() {
        return isRaise;
    }

    public  void setIsRaise(String isRaise) {
        this.isRaise = isRaise;
    }

    public  String getIsReward() {
        return isReward;
    }

    public  void setIsReward(String isReward) {
        this.isReward = isReward;
    }

    public  List<Integer> getVoterTargetList() {
        return voterTargetList;
    }

    public  void setVoterTargetList(List<Integer> voterTargetList) {
        this.voterTargetList = voterTargetList;
    }

    public  Bitmap getBitmap() {
        return bitmap;
    }

    public  void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public  List<Integer> getCheckedRadioButtonList() {
        return checkedRadioButtonList;
    }

    public  void setCheckedRadioButtonList(List<Integer> checkedRadioButtonList) {
        this.checkedRadioButtonList = checkedRadioButtonList;
    }

    public  List<Integer> getCheckedPositionList() {
        return checkedPositionList;
    }

    public  void setCheckedPositionList(List<Integer> checkedPositionList) {
        this.checkedPositionList = checkedPositionList;
    }

    public  boolean getIfSetReward() {
        return ifSetReward;
    }

    public  void setIfSetReward(boolean ifSetReward) {
        this.ifSetReward = ifSetReward;
    }

    public boolean isIfSetReward() {
        return ifSetReward;
    }

    public boolean isFirstStepFinished() {
        return isFirstStepFinished;
    }

    public void setFirstStepFinished(boolean firstStepFinished) {
        isFirstStepFinished = firstStepFinished;
    }

    public boolean isSecondStepFinished() {
        return isSecondStepFinished;
    }

    public void setSecondStepFinished(boolean secondStepFinished) {
        isSecondStepFinished = secondStepFinished;
    }

    public boolean isThirdStepFinished() {
        return isThirdStepFinished;
    }

    public void setThirdStepFinished(boolean thirdStepFinished) {
        isThirdStepFinished = thirdStepFinished;
    }

    public boolean isFouthStepFinished() {
        return isFouthStepFinished;
    }

    public void setFouthStepFinished(boolean fouthStepFinished) {
        isFouthStepFinished = fouthStepFinished;
    }

}
