package com.app.fixee.myapplication.register;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseWithErr;

public interface IRegisterReg {

    void register(String name, String login, String password, Context ctx, OnResponseWithErr callback);

}
