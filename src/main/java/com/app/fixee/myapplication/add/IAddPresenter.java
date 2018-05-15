package com.app.fixee.myapplication.add;

import android.content.Context;
import android.net.Uri;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface IAddPresenter extends MvpPresenter<IAddView> {

    void addTicket(String name, String description, String linkPhoto, Context ctx);

    void addNews(String name, String description, String linkPhoto, Context ctx);

    void uploadPhoto(Uri uri);

}
