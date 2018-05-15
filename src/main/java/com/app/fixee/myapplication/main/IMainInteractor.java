package com.app.fixee.myapplication.main;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseNews;
import com.app.fixee.myapplication.callbacks.OnResponseTickets;
import com.app.fixee.myapplication.callbacks.OnResponseWithErr;

public interface IMainInteractor {

    void addFeedback(String docId, String feedback, Context ctx, OnResponseWithErr callback);

    void publishNews(String docId, Context ctx, OnResponseWithErr callback);

    void getTickets(Context ctx, OnResponseTickets callback);

    void getNews(Context ctx, OnResponseNews callback);

    void logOut(OnResponseWithErr callback);

}
