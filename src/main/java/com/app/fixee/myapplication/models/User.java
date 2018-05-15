package com.app.fixee.myapplication.models;

public class User {

    private String accessLevel;
    private String name;
    private String docId;

    public User(String accessLevel, String name, String docId) {
        this.accessLevel = accessLevel;
        this.name = name;
        this.docId = docId;
    }

    public String getAccessLevel() {
        return accessLevel;
    }

    public void setAccessLevel(String accessLevel) {
        this.accessLevel = accessLevel;
    }

    public String getName() {
        return name;
    }

    public String getDocId() {
        return docId;
    }
}
