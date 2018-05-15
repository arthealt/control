package com.app.fixee.myapplication.callbacks;

import com.app.fixee.myapplication.models.Ticket;

public interface OnResponseTicket {

    void onSuccess(Ticket ticket);

    void onFailure(String error);

}
