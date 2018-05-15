package com.app.fixee.myapplication.manage;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseUpdateUser;
import com.app.fixee.myapplication.callbacks.OnResponseUsers;

public interface IManageInteractor {

    void getAllUsers(Context ctx, OnResponseUsers callback);

    void updateUser(String docId, int level, int position, Context ctx, OnResponseUpdateUser callback);
}
