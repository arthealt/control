package com.app.fixee.myapplication.callbacks;

public interface OnResponseNameAccess {

    void onSuccess(String name, int accessLevel);

    void onFailure(String error);

}
