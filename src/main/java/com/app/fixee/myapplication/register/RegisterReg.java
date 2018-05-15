package com.app.fixee.myapplication.register;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.app.fixee.myapplication.R;
import com.app.fixee.myapplication.callbacks.OnResponseWithErr;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterReg implements IRegisterReg {

    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // REGISTER
    @Override
    public void register(String name, String login, String password, Context ctx, OnResponseWithErr callback) {
        auth = FirebaseAuth.getInstance();

        auth.createUserWithEmailAndPassword(login, password)
                .addOnCompleteListener((Activity) ctx, task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = auth.getCurrentUser();
                        sendEmailVerification(user, ctx, callback);
                        addAccountDB(name, ctx, callback);
                    } else {
                        callback.onFailure(task.getException().getLocalizedMessage());
                    }
                });
    }

    // ADD DATA FOR USER
    private void addAccountDB(String name, Context ctx, OnResponseWithErr callback) {
        Map<String, Object> userInfo = new HashMap<>();
        userInfo.put("uid", auth.getCurrentUser().getUid());
        userInfo.put("name", name);
        userInfo.put("access_level", 1);

        db.collection("users").add(userInfo).addOnCompleteListener(task -> callback.onSuccess()).addOnFailureListener(e -> callback.onFailure(e.getLocalizedMessage()));
    }

    // SEND EMAIL VERIFICATION
    private void sendEmailVerification(FirebaseUser user, Context ctx, OnResponseWithErr callback) {

        user.sendEmailVerification()
                .addOnCompleteListener((Activity) ctx, task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ctx, ctx.getString(R.string.send_email), Toast.LENGTH_LONG).show();
                    } else {
                        callback.onFailure(task.getException().getLocalizedMessage());
                    }
                });
    }
}
