package com.app.fixee.myapplication.login;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseWithErr;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

public class LoginPresenter extends MvpBasePresenter<ILoginView> implements ILoginPresenter {

    private ILoginAuth loginAuth;

    LoginPresenter() {
        loginAuth = new LoginAuth();
    }

    @Override
    public void login(String login, String password, Context ctx) {
        loginAuth.login(login, password, ctx, new OnResponseWithErr() {
            @Override
            public void onSuccess() {
                ifViewAttached(view -> {
                    view.hideProgressDialog();
                    view.goMain();
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
