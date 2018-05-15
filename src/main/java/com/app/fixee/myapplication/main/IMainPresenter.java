package com.app.fixee.myapplication.main;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface IMainPresenter extends MvpPresenter<IMainView> {

    void addFeedback(String docId, String feedback, Context ctx);

    void publishNews(String docId, int position, Context ctx);

    void getTickets(Context ctx);

    void getNews(Context ctx);

    void logOut();

}
