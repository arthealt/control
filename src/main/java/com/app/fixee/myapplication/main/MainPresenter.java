package com.app.fixee.myapplication.main;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseNews;
import com.app.fixee.myapplication.callbacks.OnResponseTickets;
import com.app.fixee.myapplication.callbacks.OnResponseWithErr;
import com.app.fixee.myapplication.models.News;
import com.app.fixee.myapplication.models.Ticket;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

public class MainPresenter extends MvpBasePresenter<IMainView> implements IMainPresenter {

    private IMainInteractor mainInteractor;

    MainPresenter() {
        mainInteractor = new MainInteractor();
    }

    @Override
    public void addFeedback(String docId, String feedback, Context ctx) {
        ifViewAttached(IMainView::showProgressDialog);

        mainInteractor.addFeedback(docId, feedback, ctx, new OnResponseWithErr() {
            @Override
            public void onSuccess() {
                ifViewAttached(IMainView::hideProgressDialog);
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
    public void publishNews(String docId, int position, Context ctx) {
        ifViewAttached(IMainView::showProgressDialog);

        mainInteractor.publishNews(docId, ctx, new OnResponseWithErr() {
            @Override
            public void onSuccess() {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.updateNews(position);
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
    public void getTickets(Context ctx) {
       mainInteractor.getTickets(ctx, new OnResponseTickets() {
           @Override
           public void onSuccessUser(List<Ticket> tickets) {
               ifViewAttached(view -> {
                   view.setTicketsErr("-user");
                   view.loadCurrentItemMenu();
                   view.addTickets(tickets);
               });
           }

           @Override
           public void onSuccessAdmin(List<Ticket> tickets) {
               ifViewAttached(view -> {
                   view.setTicketsErr("-");
                   view.loadCurrentItemMenu();
                   view.addTickets(tickets);
               });
           }

           @Override
           public void setAccessLevel(int accessLevel) {
               ifViewAttached(view -> view.setAccessLevel(accessLevel));
           }

           @Override
           public void onLogOut() {
               logOut();
           }

           @Override
           public void onFailure(String error) {
               ifViewAttached(view -> {
                   view.setTicketsErr(error);
                   view.loadCurrentItemMenu();
               });
           }
       });
    }

    @Override
    public void getNews(Context ctx) {
        mainInteractor.getNews(ctx, new OnResponseNews() {
            @Override
            public void onSuccess(List<News> news) {
                ifViewAttached(view -> {
                    view.setNewsErr("-");
                    view.loadCurrentItemMenu();
                    view.addNews(news);
                });
            }

            @Override
            public void setAccessLevel(int accessLevel) {
                ifViewAttached(view -> view.setAccessLevel(accessLevel));
            }

            @Override
            public void onFailure(String error) {
                ifViewAttached(view -> {
                    view.setNewsErr(error);
                    view.loadCurrentItemMenu();
                });
            }
        });
    }

    @Override
    public void logOut() {
        mainInteractor.logOut(new OnResponseWithErr() {
            @Override
            public void onSuccess() {
                ifViewAttached(IMainView::openLoginActivity);
            }

            @Override
            public void onFailure(String error) {

            }
        });
    }
}
