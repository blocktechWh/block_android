package com.blocktechwh.app.block.Bean;

/**
 * Created by 跳跳蛙 on 2017/11/16.
 */

public class RedTicketWait {
    private int id;
    private String pray_text;
    private String create_time;
    private String sender_name;
    private String img_url;

    public void setId(int id) {
        this.id = id;
    }

    public void setPray_text(String pray_text) {
        this.pray_text = pray_text;
    }

    public String getSender_name() {
        return sender_name;
    }

    public String getCreate_time() {

        return create_time;
    }

    public String getPray_text() {

        return pray_text;
    }

    public int getId() {

        return id;
    }

    public void setSender_name(String sender_name) {

        this.sender_name = sender_name;
    }

    public void setCreate_time(String create_time) {

        this.create_time = create_time;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getImg_url() {
        return img_url;
    }
}
