package com.app.fixee.myapplication.models;

public class Ticket {

    private String name;
    private String description;
    private String photo;
    private String date;
    private String builder;
    private String builderUid;
    private String docId;
    private String feedback;
    private String status;

    public Ticket(String name, String description, String photo, String date, String builder, String builderUid, String docId, String feedback, String status) {
        this.name = name;
        this.description = description;
        this.photo = photo;
        this.date = date;
        this.builder = builder;
        this.builderUid = builderUid;
        this.docId = docId;
        this.feedback = feedback;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDate() {
        return date;
    }

    public String getBuilder() {
        return builder;
    }

    public String getBuilderUid() {
        return builderUid;
    }

    public String getDocId() {
        return docId;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getStatus() {
        return status;
    }

}
