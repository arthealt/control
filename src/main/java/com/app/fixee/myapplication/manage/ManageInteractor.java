package com.app.fixee.myapplication.manage;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.callbacks.OnResponseUpdateUser;
import com.app.fixee.myapplication.callbacks.OnResponseUsers;
import com.app.fixee.myapplication.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ManageInteractor implements IManageInteractor {

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();


    // GET ALL USERS
    @Override
    public void getAllUsers(Context ctx, OnResponseUsers callback) {

        if (isInternetConnection(ctx)) {

            List<User> users = new ArrayList<>();

            db.collection("users")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            for (DocumentSnapshot document : task.getResult()) {

                                if (!document.getString("uid").equals(auth.getUid())) {
                                    users.add(new User(getAccessLevel(document.getLong("access_level").intValue(), ctx), document.getString("name"), document.getId()));
                                }
                            }

                            if (!users.isEmpty()) {
                                callback.onSuccess(users);
                            } else {
                                callback.onFailure(ctx.getString(R.string.empty_users));
                            }
                        } else {
                            callback.onFailure(task.getException().toString());
                        }
                    });
        } else  {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    // UPDATE USER INFO
    @Override
    public void updateUser(String docId, int level, int position, Context ctx, OnResponseUpdateUser callback) {

        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("access_level", level);

        if (isInternetConnection(ctx)) {
            db.collection("users").document(docId)
                    .update(userInfo)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            callback.onSuccess(getAccessLevel(level, ctx), position);
                        } else {
                            callback.onFailure(task.getException().getLocalizedMessage());
                        }
                    });
        } else {
            callback.onFailure(ctx.getString(R.string.check_your_internet_connection));
        }
    }

    // GET ACCESS LEVEL TEXT
    private String getAccessLevel(int accessLevel, Context ctx) {

        switch (accessLevel) {
            case 1:
                return ctx.getString(R.string.access_level_1);

            case 2:
                return ctx.getString(R.string.access_level_2);

            case 3:
                return ctx.getString(R.string.access_level_3);

            default:
                return "";
        }
    }

    // CHECK INTERNET CONNECTION
    private boolean isInternetConnection(Context ctx) {

        ConnectivityManager connectivityManager = (ConnectivityManager)ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        return connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED;
    }
}
