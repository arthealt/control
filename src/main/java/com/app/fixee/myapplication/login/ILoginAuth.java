package com.app.fixee.myapplication.login;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseWithErr;

public interface ILoginAuth {

    void login(String login, String password, Context ctx, OnResponseWithErr callback);

}
