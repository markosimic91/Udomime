package com.example.simic.udomime;

import java.util.Date;

/**
 * Created by Simic on 11.1.2018..
 */

public class Message {

    private String mMessageText;
    private String mMessageUser;
    private long mMessageTime;


    public Message(String mMessageText, String mMessageUser) {
        this.mMessageText = mMessageText;
        this.mMessageUser = mMessageUser;

        mMessageTime = new Date().getTime();
    }

    public Message(){
    }


    public String getmMessageText() {
        return mMessageText;
    }

    public void setmMessageText(String mMessageText) {
        this.mMessageText = mMessageText;
    }

    public String getmMessageUser() {
        return mMessageUser;
    }

    public void setmMessageUser(String mMessageUser) {
        this.mMessageUser = mMessageUser;
    }

    public long getmMessageTime() {
        return mMessageTime;
    }

    public void setmMessageTime(long mMessageTime) {
        this.mMessageTime = mMessageTime;
    }
}
