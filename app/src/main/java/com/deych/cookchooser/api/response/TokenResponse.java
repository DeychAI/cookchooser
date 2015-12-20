package com.deych.cookchooser.api.response;

import com.deych.cookchooser.db.entities.User;

/**
 * Created by deigo on 13.12.2015.
 */
public class TokenResponse {
    private String mToken;
    private User mUser;

    public String getToken() {
        return mToken;
    }

    public void setToken(String aToken) {
        mToken = aToken;
    }

    public User getUser() {
        return mUser;
    }

    public void setUser(User aUser) {
        mUser = aUser;
    }
}
