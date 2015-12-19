package com.deych.cookchooser.api.entities;

/**
 * Created by deigo on 19.12.2015.
 */
public class User {
    private long mId;
    private String mName;
    private String mUsername;
    private String mGroup;

    public long getId() {
        return mId;
    }

    public void setId(long aId) {
        mId = aId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String aName) {
        mName = aName;
    }

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String aUsername) {
        mUsername = aUsername;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String aGroup) {
        mGroup = aGroup;
    }
}
