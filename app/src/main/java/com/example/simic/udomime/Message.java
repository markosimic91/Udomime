package com.example.simic.udomime;

/**
 * Created by Simic on 29.12.2017..
 */

public class Message {

    private String mId;
    private String mBody;
    private String mAuthor;

    public Message() {
    }

    public Message(String id, String body, String author) {
        mId = id;
        mBody = body;
        mAuthor = author;
    }

    public String getId() {
        return mId;
    }

    public void setId(String id) {
        mId = id;
    }

    public String getBody() {
        return mBody;
    }

    public void setBody(String body) {
        mBody = body;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public void setAuthor(String author) {
        mAuthor = author;
    }

    @Override
    public String toString() {
        return this.mAuthor + ": " + this.mBody;
    }

}
