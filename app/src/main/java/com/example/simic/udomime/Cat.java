package com.example.simic.udomime;

/**
 * Created by Simic on 26.10.2017..
 */

public class Cat {
    private String mCatPicure;
    private String mCatName;
    private String mCatDescription;
    private int mCatContact;

    public Cat(String mCatPicure, String mCatName, String mCatDescription, int mCatContact) {
        this.mCatPicure = mCatPicure;
        this.mCatName = mCatName;
        this.mCatDescription = mCatDescription;
        this.mCatContact = mCatContact;
    }

    public String getmCatPicure() {
        return mCatPicure;
    }

    public String getmCatName() {
        return mCatName;
    }

    public String getmCatDescription() {
        return mCatDescription;
    }

    public int getmCatContact() {
        return mCatContact;
    }
}
