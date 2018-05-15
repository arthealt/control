package com.app.fixee.myapplication.adminTickets;

import com.app.fixee.myapplication.models.Moderator;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface IFullTicketView extends MvpView {

    void setModerators(List<Moderator> moderators);

    void notModerators();

    void updateBuilder(String builder);

    void updateStatus(String status);

    void snackError(String error);

    void hideTextNone();

    void hideContent();

    void showContent();

    void showProgressDialog();

    void hideProgressDialog();

    void setTextNone(String text);

    void hideProgressBar();

}
