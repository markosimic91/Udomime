package com.example.simic.udomime;

/**
 * Created by Simic on 26.10.2017..
 */

public class Dog {
    private String mDogPicure;
    private String mDogName;
    private String mDogDescription;
    private String mDogContact;

    public Dog(String mDogPicure, String mDogName, String mDogDescription, String mDogContact) {
        this.mDogPicure = mDogPicure;
        this.mDogName = mDogName;
        this.mDogDescription = mDogDescription;
        this.mDogContact = mDogContact;
    }

    public String getmDogPicure() {
        return mDogPicure;
    }

    public String getmDogName() {
        return mDogName;
    }

    public String getmDogDescription() {
        return mDogDescription;
    }

    public String getmDogContact() {
        return mDogContact;
    }
}



