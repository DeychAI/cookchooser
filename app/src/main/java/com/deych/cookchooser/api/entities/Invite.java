package com.deych.cookchooser.api.entities;

/**
 * Created by deigo on 22.01.2016.
 */
public class Invite {

    private long id;
    private String from;

    public Invite() {
    }

    public Invite(long id, String from) {
        this.id = id;
        this.from = from;
    }

    public long getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }
}
