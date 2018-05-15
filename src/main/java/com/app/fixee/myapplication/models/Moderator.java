package com.app.fixee.myapplication.models;

public class Moderator {

    private String name;
    private String uid;

    public Moderator(String name, String uid) {
        this.name = name;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public String getUid() {
        return uid;
    }
}
