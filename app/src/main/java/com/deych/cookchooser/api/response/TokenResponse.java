package com.deych.cookchooser.api.response;

import com.deych.cookchooser.db.entities.User;

/**
 * Created by deigo on 13.12.2015.
 */
public class TokenResponse {
    private String token;
    private User user;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
