package com.app.fixee.myapplication.add;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.widget.Toast;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.callbacks.OnResponseNameAccess;
import com.app.fixee.myapplication.callbacks.OnResponseNewsOne;
import com.app.fixee.myapplication.callbacks.OnResponseTicket;
import com.app.fixee.myapplication.callbacks.OnResponse;
import com.app.fixee.myapplication.models.News;
import com.app.fixee.myapplication.models.Ticket;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AddInteractor implements IAddInteractor {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // ADD NEW TICKET
    @Override
    public void addTicket(String name, String description, String linkPhoto, Context ctx, OnResponseTicket callback) {

        long unixTime = System.currentTimeMillis() / 1000L;

        Map<String, Object> ticketInfo = new HashMap<>();
        ticketInfo.put("name", name);
        ticketInfo.put("description", description);
        ticketInfo.put("photo", linkPhoto);
        ticketInfo.put("status", "1");
        ticketInfo.put("builder", ctx.getString(R.string.builder_none));
        ticketInfo.put("builderUid", "");
        ticketInfo.put("feedback", "");
        ticketInfo.put("uid", auth.getCurrentUser().getUid());
        ticketInfo.put("timestamp", unixTime);

        if (isInternetConnection(ctx)) {
            db.collection("tickets")
                    .add(ticketInfo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess(new Ticket(name, description, linkPhoto, getDate(unixTime), ctx.getString(R.string.builder_none), "", task.getResult().getId(), "", getStatus(Integer.parseInt("1"), ctx)));
                        } else {
                            callback.onFailure(task.getException().getLocalizedMessage());
                        }
                    });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    @Override
    public void addNews(String name, String description, String linkPhoto, Context ctx, OnResponseNewsOne callback) {

        if (isInternetConnection(ctx)) {

            getInfo(new OnResponseNameAccess() {
                @Override
                public void onSuccess(String author, int accessLevel) {

                    long unixTime = System.currentTimeMillis() / 1000L;

                    Map<String, Object> news = new HashMap<>();
                    news.put("title", name);
                    news.put("description", description);
                    news.put("timestamp", unixTime);
                    news.put("photo", linkPhoto);
                    news.put("author", author);
                    news.put("uid", auth.getCurrentUser().getUid());
                    news.put("status", accessLevel == 1 ? 1 : 2);

                    db.collection("news")
                            .add(news)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    if (accessLevel == 1) {
                                        Toast.makeText(ctx, ctx.getString(R.string.send_news), Toast.LENGTH_LONG).show();
                                        callback.onSend();
                                    } else {
                                        callback.onSuccess(new News(name, description, getDate(unixTime), linkPhoto, task.getResult().getId(), author, 2));
                                    }
                                } else {
                                    callback.onFailure(task.getException().getLocalizedMessage());
                                }
                            });
                }

                @Override
                public void onFailure(String error) {
                    callback.onFailure(error);
                }
            });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    // UPLOAD PHOTO
    @Override
    public void uploadPhoto(Uri uri, OnResponse callback) {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference().child(auth.getCurrentUser().getUid() + "/" + UUID.randomUUID().toString());
        UploadTask uploadTask = storageReference.putFile(uri);

        uploadTask.addOnSuccessListener(taskSnapshot ->
                callback.onSuccess(taskSnapshot.getDownloadUrl().toString()))
                .addOnFailureListener(e -> callback.onFailure(e.getLocalizedMessage()));
    }

    private void getInfo(OnResponseNameAccess callback) {

        db.collection("users")
                .whereEqualTo("uid", auth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            callback.onSuccess(document.getString("name"), document.getLong("access_level").intValue());
                            return;
                        }
                    } else {
                        callback.onFailure(task.getException().getLocalizedMessage());
                    }
                });
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
