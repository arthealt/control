package com.app.fixee.myapplication.adminTickets;

import android.content.Context;

import com.app.fixee.myapplication.callbacks.OnResponseBuilder;
import com.app.fixee.myapplication.callbacks.OnResponseModerators;

public interface IFullTicketInteractor {

    void getAllModerator(Context ctx, OnResponseModerators callback);

    void setBuilder(String docId, String builder, String builderUid, Context ctx, OnResponseBuilder callback);

    void setStatus(String docId, int status, Context ctx, OnResponseBuilder callback);

}
