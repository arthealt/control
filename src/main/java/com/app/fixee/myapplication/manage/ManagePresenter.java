package com.app.fixee.myapplication.manage;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseUpdateUser;
import com.app.fixee.myapplication.callbacks.OnResponseUsers;
import com.app.fixee.myapplication.models.User;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

import java.util.List;

public class ManagePresenter extends MvpBasePresenter<IManageView> implements IManagePresenter {

    private IManageInteractor manageInteractor;

    ManagePresenter() {
        manageInteractor = new ManageInteractor();
    }

    @Override
    public void getAllUsers(Context ctx) {
        manageInteractor.getAllUsers(ctx, new OnResponseUsers() {
            @Override
            public void onSuccess(List<User> users) {
                ifViewAttached(view -> {
                    view.hideProgressBar();
                    view.hideTextNone();
                    view.loadUsers(users);
                });
            }

            @Override
            public void onFailure(String error) {
                ifViewAttached(view ->  {
                    view.hideProgressBar();
                    view.setTextNone(error);
                });
            }
        });
    }

    @Override
    public void updateUser(String docId, int level, int position, Context ctx) {
        ifViewAttached(IManageView::showProgressDialog);

        manageInteractor.updateUser(docId, level, position, ctx, new OnResponseUpdateUser() {
            @Override
            public void onSuccess(String accessLevel, int position) {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.updateUser(accessLevel, position);
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
