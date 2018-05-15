package com.app.fixee.myapplication.add;

import android.content.Context;
import android.net.Uri;

import com.app.fixee.myapplication.callbacks.OnResponseNewsOne;
import com.app.fixee.myapplication.callbacks.OnResponseTicket;
import com.app.fixee.myapplication.callbacks.OnResponse;

public interface IAddInteractor {

    void addTicket(String name, String description, String linkPhoto, Context ctx, OnResponseTicket callback);

    void addNews(String name, String description, String linkPhoto, Context ctx, OnResponseNewsOne callback);

    void uploadPhoto(Uri uri, OnResponse callback);

}
