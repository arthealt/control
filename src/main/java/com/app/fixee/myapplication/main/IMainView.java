package com.app.fixee.myapplication.main;

import com.app.fixee.myapplication.models.News;
import com.app.fixee.myapplication.models.Ticket;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface IMainView extends MvpView {

    void openLoginActivity();

    void updateNews(int position);

    void setAccessLevel(int accessLevel);

    void addTickets(List<Ticket> tickets);

    void addNews(List<News> news);

    void setNewsErr(String error);

    void setTicketsErr(String error);

    void loadCurrentItemMenu();

    void snackError(String error);

    void hideTextNone();

    void setTextNone(String text);

    void showProgressBar();

    void hideProgressBar();

    void showProgressDialog();

    void hideProgressDialog();

}
