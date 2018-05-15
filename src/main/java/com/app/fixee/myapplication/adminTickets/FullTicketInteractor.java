package com.app.fixee.myapplication.adminTickets;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.callbacks.OnResponseBuilder;
import com.app.fixee.myapplication.callbacks.OnResponseModerators;
import com.app.fixee.myapplication.models.Moderator;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FullTicketInteractor implements IFullTicketInteractor {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    public void getAllModerator(Context ctx, OnResponseModerators callback) {

        List<Moderator> moderators = new ArrayList<>();

        if (isInternetConnection(ctx)) {

            db.collection("users")
                    .whereEqualTo("access_level",2)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {

                            for (DocumentSnapshot document : task.getResult()) {

                                moderators.add(new Moderator(document.getString("name"), document.getString("uid")));
                            }

                            if (!moderators.isEmpty()) {
                                callback.onSuccess(moderators);
                            } else {
                                callback.onNotFound();
                            }

                        } else {
                            callback.onFailure(task.getException().getLocalizedMessage());
                        }
                    });

        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    @Override
    public void setBuilder(String docId, String builder, String builderUid, Context ctx, OnResponseBuilder callback) {

        Map<String, Object> builderInfo = new HashMap<>();
        builderInfo.put("builder", builder);
        builderInfo.put("builderUid", builderUid);
        builderInfo.put("status", 2 + "");

        if (isInternetConnection(ctx)) {
            db.collection("tickets").document(docId)
                    .update(builderInfo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess(builder);
                        } else {
                            callback.onFailure(task.getException().getLocalizedMessage());
                        }
                    });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    @Override
    public void setStatus(String docId, int status, Context ctx, OnResponseBuilder callback) {
        Map<String, Object> ticketInfo = new HashMap<>();
        ticketInfo.put("status", status + "");

        if (isInternetConnection(ctx)) {
            db.collection("tickets").document(docId)
                    .update(ticketInfo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess(getStatus(status, ctx));
                        } else {
                            callback.onFailure(task.getException().getLocalizedMessage());
                        }
                    });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
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

    // CHECK INTERNET CONNECTION
    private boolean isInternetConnection(Context ctx) {

        ConnectivityManager connectivityManager = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}
