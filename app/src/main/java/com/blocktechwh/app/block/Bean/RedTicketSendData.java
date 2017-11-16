package com.blocktechwh.app.block.Bean;

import com.blocktechwh.app.block.Utils.DateUtil;

import java.util.Date;

/**
 * Created by eagune on 2017/11/16.
 */

public class RedTicketSendData{
    private String _name;
    private String _img;
    private Integer _amount;
    private Date createTime;

    public void setName(String name){this._name=name;}
    public String getName(){return this._name;}

    public void setImg(String img){this._img=img;}
    public String getImg(){return this._img;}

    public void setAmount(Integer amount){this._amount=amount;}
    public Integer getAmount(){return this._amount;}

    public String getCreateTimeString() {
        return DateUtil.formatDatetime(getCreateTime());
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Date getCreateTime() {
        return this.createTime;
    }
}
