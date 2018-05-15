package com.app.fixee.myapplication.register;

import com.hannesdorfmann.mosby3.mvp.MvpView;

public interface IRegisterView extends MvpView {

    void snackError(String error);

    void goMain();

    void showProgressDialog();

    void hideProgressDialog();

}
