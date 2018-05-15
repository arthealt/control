package com.app.fixee.myapplication.callbacks;

import com.app.fixee.myapplication.models.Ticket;

import java.util.List;

public interface OnResponseTickets {

    void onSuccessUser(List<Ticket> tickets);

    void onSuccessAdmin(List<Ticket> tickets);

    void setAccessLevel(int accessLevel);

    void onLogOut();

    void onFailure(String error);

}
