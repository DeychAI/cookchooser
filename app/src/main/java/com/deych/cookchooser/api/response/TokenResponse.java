package com.deych.cookchooser.api.response;

import com.deych.cookchooser.api.entities.UserVo;

/**
 * Created by deigo on 13.12.2015.
 */
public class TokenResponse {
    private String mToken;
    private UserVo mUser;

    public String getToken() {
        return mToken;
    }

    public void setToken(String aToken) {
        mToken = aToken;
    }

    public UserVo getUser() {
        return mUser;
    }

    public void setUser(UserVo aUser) {
        mUser = aUser;
    }
}
