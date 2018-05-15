package com.app.fixee.myapplication.main;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.callbacks.OnResponseCheckAuth;
import com.app.fixee.myapplication.callbacks.OnResponseNews;
import com.app.fixee.myapplication.callbacks.OnResponseTickets;
import com.app.fixee.myapplication.callbacks.OnResponseWithErr;
import com.app.fixee.myapplication.models.News;
import com.app.fixee.myapplication.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainInteractor implements IMainInteractor {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void addFeedback(String docId, String feedback, Context ctx, OnResponseWithErr callback) {

        Map<String, Object> ticketFeedback = new HashMap<>();
        ticketFeedback.put("feedback", feedback);

        if (isInternetConnection(ctx)) {

            db.collection("tickets").document(docId)
                    .update(ticketFeedback)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(task.getException().getLocalizedMessage());
                        }
                    });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    // PUBLISH NEWS
    @Override
    public void publishNews(String docId, Context ctx, OnResponseWithErr callback) {

        Map<String, Object> newsOne = new HashMap<>();
        newsOne.put("status", 2);

        if (isInternetConnection(ctx)) {

            db.collection("news").document(docId)
                    .update(newsOne)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess();
                        } else {
                            callback.onFailure(task.getException().getLocalizedMessage());
                        }
                    });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    // GET ALL TICKET'S
    @Override
    public void getTickets(Context ctx, OnResponseTickets callback) {

        List<Ticket> tickets = new ArrayList<>();

        if (isInternetConnection(ctx)) {

            getAccessLevel(accessLevel -> {
                callback.setAccessLevel(accessLevel);
                switch (accessLevel) {
                    case 0:
                        callback.onLogOut();
                        break;
                    case 1:
                        db.collection("tickets")
                                .whereEqualTo("uid", auth.getCurrentUser().getUid())
                                .whereGreaterThan("timestamp", 0)
                                .orderBy("timestamp")
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {

                                            String status = getStatus(Integer.parseInt(document.getString("status")), ctx);
                                            tickets.add(new Ticket(document.getString("name"), document.getString("description"), document.getString("photo"), getDate(document.getLong("timestamp")), document.getString("builder"), document.getString("builderUid"), document.getId(), document.getString("feedback"), status));
                                        }

                                        if (!tickets.isEmpty()) {
                                            callback.onSuccessUser(tickets);
                                        } else {
                                            callback.onFailure(ctx.getString(R.string.empty_tickets));
                                        }
                                    } else {
                                        callback.onFailure(task.getException().getLocalizedMessage());
                                    }
                                });
                        break;

                    case 2:

                        db.collection("tickets")
                                .whereEqualTo("builderUid", auth.getCurrentUser().getUid())
                                .whereGreaterThan("timestamp", 0)
                                .orderBy("timestamp")
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {

                                            String status = getStatus(Integer.parseInt(document.getString("status")), ctx);
                                            tickets.add(new Ticket(document.getString("name"), document.getString("description"), document.getString("photo"), getDate(document.getLong("timestamp")), document.getString("builder"), document.getString("builderUid"), document.getId(), document.getString("feedback"), status));
                                        }

                                        if (!tickets.isEmpty()) {
                                            callback.onSuccessAdmin(tickets);
                                        } else {
                                            callback.onFailure(ctx.getString(R.string.empty_tickets_all));
                                        }
                                    } else {
                                        callback.onFailure(task.getException().getLocalizedMessage());
                                    }
                                });
                        break;

                    case 3:
                        db.collection("tickets")
                                .whereGreaterThan("timestamp", 0)
                                .orderBy("timestamp")
                                .get()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        for (DocumentSnapshot document : task.getResult()) {

                                            String status = getStatus(Integer.parseInt(document.getString("status")), ctx);
                                            tickets.add(new Ticket(document.getString("name"), document.getString("description"), document.getString("photo"), getDate(document.getLong("timestamp")), document.getString("builder"), document.getString("builderUid"), document.getId(), document.getString("feedback"), status));
                                        }

                                        if (!tickets.isEmpty()) {
                                            callback.onSuccessAdmin(tickets);
                                        } else {
                                            callback.onFailure(ctx.getString(R.string.empty_tickets_all));
                                        }
                                    } else {
                                        callback.onFailure(task.getException().getLocalizedMessage());
                                    }
                                });
                        break;

                    default:
                        break;
                }
            });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    // GET ALL NEWS
    @Override
    public void getNews(Context ctx, OnResponseNews callback) {

        List<News> news = new ArrayList<>();

        if (isInternetConnection(ctx)) {

            getAccessLevel(accessLevel -> {
                callback.setAccessLevel(accessLevel);

                db.collection("news")
                        .whereGreaterThan("timestamp", 0)
                        .orderBy("timestamp")
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                for (DocumentSnapshot document : task.getResult()) {

                                    if (document.getLong("status").intValue() > 1) {
                                        news.add(new News(document.getString("title"), document.getString("description"), getDate(document.getLong("timestamp")), document.getString("photo"), document.getId(), document.getString("author"), document.getLong("status").intValue()));
                                    } else if (accessLevel == 3) {
                                        news.add(new News(document.getString("title"), document.getString("description"), getDate(document.getLong("timestamp")), document.getString("photo"), document.getId(), document.getString("author"), document.getLong("status").intValue()));
                                    }
                                }

                                if (!news.isEmpty()) {
                                    callback.onSuccess(news);
                                } else {
                                    callback.onFailure(ctx.getString(R.string.empty_news));
                                }
                            } else {
                                callback.onFailure(task.getException().getLocalizedMessage());
                            }
                        });
            });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }

    }

    // LOG OUT
    @Override
    public void logOut(OnResponseWithErr callback) {

        if (auth.getCurrentUser() != null) {
            auth.signOut();
        }

        callback.onSuccess();
    }

    // GET ACCESS LEVEL ACCOUNT
    private void getAccessLevel(OnResponseCheckAuth callback) {

        if (auth.getCurrentUser() != null) {
            db.collection("users")
                    .whereEqualTo("uid", auth.getCurrentUser().getUid())
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                callback.onSuccess(document.getLong("access_level").intValue());
                            }
                        } else {
                            callback.onSuccess(0);
                        }
                    });
        } else {
            callback.onSuccess(0);
        }
    }

    // GET STATUS
    private String getStatus(int statusNum, Context ctx) {

        switch (statusNum) {
            case 1:
                return ctx.getString(R.string.status_1);
            case 2:
                return ctx.getString(R.string.status_2);
            case 3:
                return ctx.getString(R.string.status_3);
            case 4:
                return ctx.getString(R.string.status_4);
            default:
                return ctx.getString(R.string.status_error);
        }
    }

    // GET DATE
    private String getDate(long timeStamp){
        timeStamp *= 1000L;
        try{
            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            Date netDate = (new Date(timeStamp));
            return sdf.format(netDate);
        } catch(Exception ignored){
            return "error";
        }
    }

    // CHECK INTERNET CONNECTION
    private boolean isInternetConnection(Context ctx) {

        ConnectivityManager connectivityManager = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}
