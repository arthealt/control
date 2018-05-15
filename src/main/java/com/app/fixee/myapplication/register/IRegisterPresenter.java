package com.app.fixee.myapplication.register;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface IRegisterPresenter extends MvpPresenter<IRegisterView> {

    void register(String name, String login, String password, Context ctx);

}
