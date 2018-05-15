package com.app.fixee.myapplication.register;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseWithErr;
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter;

public class RegisterPresenter extends MvpBasePresenter<IRegisterView> implements IRegisterPresenter{

    private IRegisterReg registerReg;

    RegisterPresenter() {
        registerReg = new RegisterReg();
    }

    @Override
    public void register(String name, String login, String password, Context ctx) {
        registerReg.register(name, login, password, ctx, new OnResponseWithErr() {
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
