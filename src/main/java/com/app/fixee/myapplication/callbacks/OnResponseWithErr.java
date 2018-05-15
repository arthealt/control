package com.app.fixee.myapplication.callbacks;

public interface OnResponseWithErr {

    void onSuccess();

    void onFailure(String error);

}