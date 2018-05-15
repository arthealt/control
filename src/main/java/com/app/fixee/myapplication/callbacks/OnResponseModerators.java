package com.app.fixee.myapplication.callbacks;

import com.app.fixee.myapplication.models.Moderator;

import java.util.List;

public interface OnResponseModerators {

    void onSuccess(List<Moderator> moderators);

    void onNotFound();

    void onFailure(String error);

}
