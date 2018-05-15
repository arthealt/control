package com.app.fixee.myapplication.login;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface ILoginPresenter extends MvpPresenter<ILoginView> {

    void login(String login, String password, Context ctx);

}
