package com.app.fixee.myapplication.callbacks;

import com.app.fixee.myapplication.models.Ticket;

public interface OnResponseClickTicket {

    void onSuccess(Ticket ticket, int position);

    void onSuccessFeedback(String docId);

}
