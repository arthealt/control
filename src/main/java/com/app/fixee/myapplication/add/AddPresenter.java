package com.app.fixee.myapplication.add;

import android.content.Context;
import android.net.Uri;

import com.app.fixee.myapplication.callbacks.OnResponseTicket;
import com.app.fixee.myapplication.callbacks.OnResponseNewsOne;
import com.app.fixee.myapplication.callbacks.OnResponse;
import com.app.fixee.myapplication.models.News;
import com.app.fixee.myapplication.models.Ticket;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

public class AddPresenter extends MvpBasePresenter<IAddView> implements IAddPresenter{

    private IAddInteractor addInteractor;

    AddPresenter() {
        addInteractor = new AddInteractor();
    }

    @Override
    public void addTicket(String name, String description, String linkPhoto, Context ctx) {
        ifViewAttached(IAddView::showProgressDialog);

        addInteractor.addTicket(name, description, linkPhoto, ctx, new OnResponseTicket() {
            @Override
            public void onSuccess(Ticket ticket) {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.addTicket(ticket);
                });
            }

            @Override
            public void onFailure(String error) {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.snackError(error);
                });
            }
        });
    }

    @Override
    public void addNews(String name, String description, String linkPhoto, Context ctx) {
        ifViewAttached(IAddView::showProgressDialog);

        addInteractor.addNews(name, description, linkPhoto, ctx, new OnResponseNewsOne() {
            @Override
            public void onSuccess(News news) {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.addNews(news);
                });
            }

            @Override
            public void onSend() {
                ifViewAttached(IAddView::onSend);
            }

            @Override
            public void onFailure(String error) {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.snackError(error);
                });
            }
        });
    }

    @Override
    public void uploadPhoto(Uri uri) {
        ifViewAttached(IAddView::showProgressDialog);

        addInteractor.uploadPhoto(uri, new OnResponse() {
            @Override
            public void onSuccess(String link) {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.addPhotoUrl(link);
                });
            }

            @Override
            public void onFailure(String error) {
                ifViewAttached(view ->  {
                    view.hideProgressDialog();
                    view.snackError(error);
                });
            }
        });
    }
}
