package com.app.fixee.myapplication.callbacks;

public interface OnResponseUpdateUser {

    void onSuccess(String accessLevel, int position);

    void onFailure(String error);

}
