package com.example.simic.udomime;

/**
 * Created by Simic on 26.10.2017..
 */

public class Cat {
    private String mCatPicure;
    private String mCatName;
    private String mCatDescription;
    private String mCatContact;

    public Cat(String mCatPicure, String mCatName, String mCatDescription, String mCatContact) {
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

    public String getmCatContact() {
        return mCatContact;
    }
}
