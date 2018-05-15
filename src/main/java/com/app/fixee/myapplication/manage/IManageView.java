package com.app.fixee.myapplication.manage;

import com.app.fixee.myapplication.models.User;
import com.hannesdorfmann.mosby3.mvp.MvpView;

import java.util.List;

public interface IManageView extends MvpView {

    void loadUsers(List<User> users);

    void updateUser(String accessLevel, int position);

    void snackError(String error);

    void hideTextNone();

    void setTextNone(String text);

    void hideProgressBar();

    void showProgressDialog();

    void hideProgressDialog();

}
