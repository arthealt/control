package com.app.fixee.myapplication.callbacks;

import com.app.fixee.myapplication.models.News;

import java.util.List;

public interface OnResponseNews {

    void onSuccess(List<News> news);

    void setAccessLevel(int accessLevel);

    void onFailure(String error);

}
