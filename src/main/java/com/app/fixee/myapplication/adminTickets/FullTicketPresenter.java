package com.app.fixee.myapplication.adminTickets;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseBuilder;
import com.app.fixee.myapplication.callbacks.OnResponseModerators;
import com.app.fixee.myapplication.models.Moderator;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

public class FullTicketPresenter extends MvpBasePresenter<IFullTicketView> implements IFullTicketPresenter{

    private IFullTicketInteractor fullTicketInteractor;

    FullTicketPresenter() {
        fullTicketInteractor = new FullTicketInteractor();
    }

    @Override
    public void getAllModerator(Context ctx) {
        fullTicketInteractor.getAllModerator(ctx, new OnResponseModerators() {
            @Override
            public void onSuccess(List<Moderator> moderators) {
                ifViewAttached(view -> {
                    view.hideProgressBar();
                    view.hideTextNone();
                    view.showContent();
                    view.setModerators(moderators);
                });
            }

            @Override
            public void onNotFound() {
                ifViewAttached(view -> {
                    view.hideProgressBar();
                    view.hideTextNone();
                    view.showContent();
                    view.notModerators();
                });
            }

            @Override
            public void onFailure(String error) {
                ifViewAttached(view -> {
                    view.hideProgressBar();
                    view.hideContent();
                    view.setTextNone(error);
                });
            }
        });
    }

    @Override
    public void setBuilder(String docId, String builder, String builderUid, Context ctx) {
        ifViewAttached(IFullTicketView::showProgressDialog);
        fullTicketInteractor.setBuilder(docId, builder, builderUid, ctx, new OnResponseBuilder() {
            @Override
            public void onSuccess(String builder) {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.updateBuilder(builder);
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
    public void setStatus(String docId, int status, Context ctx) {
        ifViewAttached(IFullTicketView::showProgressDialog);
        fullTicketInteractor.setStatus(docId, status, ctx, new OnResponseBuilder() {
            @Override
            public void onSuccess(String status) {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.updateStatus(status);
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
}
