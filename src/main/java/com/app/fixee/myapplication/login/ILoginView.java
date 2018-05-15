package com.app.fixee.myapplication.login;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface ILoginView extends MvpView {

    void snackError(String error);

    void goMain();

    void showProgressDialog();

    void hideProgressDialog();

}
