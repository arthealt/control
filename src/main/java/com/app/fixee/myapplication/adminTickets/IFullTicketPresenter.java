package com.app.fixee.myapplication.adminTickets;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface IFullTicketPresenter extends MvpPresenter<IFullTicketView> {

    void getAllModerator(Context ctx);

    void setBuilder(String docId, String builder, String builderUid, Context ctx);

    void setStatus(String docId, int status, Context ctx);

}
