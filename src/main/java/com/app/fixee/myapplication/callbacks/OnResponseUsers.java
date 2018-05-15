package com.app.fixee.myapplication.callbacks;

import com.app.fixee.myapplication.models.User;

import java.util.List;

public interface OnResponseUsers {

    void onSuccess(List<User> users);

    void onFailure(String error);

}
