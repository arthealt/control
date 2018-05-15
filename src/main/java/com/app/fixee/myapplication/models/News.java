package com.app.fixee.myapplication.models;

public class News {

    private String title;
    private String description;
    private String date;
    private String photo;
    private String docId;
    private String author;
    private int status;

    public News(String title, String description, String date, String photo, String docId, String author, int status) {
        this.title = title;
        this.description = description;
        this.date = date;
        this.photo = photo;
        this.docId = docId;
        this.author = author;
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

    public String getPhoto() {
        return photo;
    }

    public String getDocId() {
        return docId;
    }

    public String getAuthor() {
        return author;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
