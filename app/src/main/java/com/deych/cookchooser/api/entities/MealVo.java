package com.deych.cookchooser.api.entities;

/**
 * Created by deigo on 20.12.2015.
 */
public class MealVo {
    private long mId;
    private String mName;
    private long mCategoryId;
    private String mGroup;
    private String mClientId;

    public long getId() {
        return mId;
    }

    public void setId(long id) {
        mId = id;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        mName = name;
    }

    public long getCategoryId() {
        return mCategoryId;
    }

    public void setCategoryId(long categoryId) {
        mCategoryId = categoryId;
    }

    public String getGroup() {
        return mGroup;
    }

    public void setGroup(String group) {
        mGroup = group;
    }

    public String getClientId() {
        return mClientId;
    }

    public void setClientId(String clientId) {
        mClientId = clientId;
    }
}
