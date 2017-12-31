package com.example.simic.udomime;

/**
 * Created by Simic on 26.10.2017..
 */

public class Dog {
    private String mId;
    private String mDogPicture;
    private String mDogName;
    private String mDogDescription;
    private String mDogContact;

    public Dog() {

    }

    public Dog(String mId, String mDogPicture, String mDogName, String mDogDescription, String mDogContact) {
        this.mId = mId;
        this.mDogPicture = mDogPicture;
        this.mDogName = mDogName;
        this.mDogDescription = mDogDescription;
        this.mDogContact = mDogContact;
    }

    public String getmId() {
        return mId;
    }

    public void setmId(String mId) {
        this.mId = mId;
    }

    public String getmDogPicture() {
        return mDogPicture;
    }

    public void setmDogPicture(String mDogPicture) {
        this.mDogPicture = mDogPicture;
    }

    public String getmDogName() {
        return mDogName;
    }

    public void setmDogName(String mDogName) {
        this.mDogName = mDogName;
    }

    public String getmDogDescription() {
        return mDogDescription;
    }

    public void setmDogDescription(String mDogDescription) {
        this.mDogDescription = mDogDescription;
    }

    public String getmDogContact() {
        return mDogContact;
    }

    public void setmDogContact(String mDogContact) {
        this.mDogContact = mDogContact;
    }
}



