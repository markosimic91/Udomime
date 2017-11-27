package com.example.simic.udomime;

import android.net.Uri;

/**
 * Created by Simic on 26.10.2017..
 */

public class Cat {
    private String mId;
    private String mCatPicure;
    private String mCatName;
    private String mCatDescription;
    private String mCatContact;

    public Cat(String mId, String mCatPicure, String mCatName, String mCatDescription, String mCatContact) {
        this.mId = mId;
        this.mCatPicure = mCatPicure;
        this.mCatName = mCatName;
        this.mCatDescription = mCatDescription;
        this.mCatContact = mCatContact;
    }

    public Cat() {
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmCatPicure() {
        return mCatPicure;
    }

    public void setmCatPicure(String mCatPicure) {
        this.mCatPicure = mCatPicure;
    }

    public String getmCatName() {
        return mCatName;
    }

    public void setmCatName(String mCatName) {
        this.mCatName = mCatName;
    }

    public String getmCatDescription() {
        return mCatDescription;
    }

    public void setmCatDescription(String mCatDescription) {
        this.mCatDescription = mCatDescription;
    }

    public String getmCatContact() {
        return mCatContact;
    }

    public void setmCatContact(String mCatContact) {
        this.mCatContact = mCatContact;
    }
}