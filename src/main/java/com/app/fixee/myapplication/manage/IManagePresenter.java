package com.app.fixee.myapplication.manage;

import android.content.Context;

import com.hannesdorfmann.mosby3.mvp.MvpPresenter;

public interface IManagePresenter extends MvpPresenter<IManageView>{

    void getAllUsers(Context ctx);

    void updateUser(String docId, int level, int position, Context ctx);

}
