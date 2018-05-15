package com.app.fixee.myapplication.add;

import android.net.Uri;

import com.app.fixee.myapplication.models.News;
import com.app.fixee.myapplication.models.Ticket;
import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IAddView extends MvpView {

    void onSend();

    void addPhotoUrl(String link);

    void addTicket(Ticket ticket);

    void addNews(News news);

    void snackError(String error);

    void showProgressDialog();

    void hideProgressDialog();

}
