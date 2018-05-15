package com.app.fixee.myapplication.callbacks;

import com.app.fixee.myapplication.models.News;

public interface OnResponseNewsOne {

    void onSuccess(News news);

    void onSend();

    void onFailure(String error);

}
