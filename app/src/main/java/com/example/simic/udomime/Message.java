package com.example.simic.udomime;

import java.util.Date;

/**
 * Created by Simic on 29.12.2017..
 */

public class Message {

    private String MsgText;
    private String MsgUser;
    private long MsgTime;

    public Message() {
    }

    public Message(String msgText, String msgUser) {
        this.MsgText = msgText;
        this.MsgUser = msgUser;

        MsgTime = new Date().getTime();
    }

    public String getMsgText() {
        return MsgText;
    }

    public void setMsgText(String msgText) {
        MsgText = msgText;
    }

    public String getMsgUser() {
        return MsgUser;
    }

    public void setMsgUser(String msgUserM) {
        MsgUser = msgUserM;
    }

    public long getMsgTime() {
        return MsgTime;
    }

    public void setMsgTime(long msgTime) {
        MsgTime = msgTime;
    }
}
