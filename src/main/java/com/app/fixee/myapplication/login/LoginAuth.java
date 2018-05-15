package com.app.fixee.myapplication.login;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.app.fixee.myapplication.callbacks.OnResponseWithErr;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginAuth implements ILoginAuth {

    private FirebaseAuth auth;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    // LOG IN
    @Override
    public void login(String login, String password, Context ctx, OnResponseWithErr callback) {

        auth = FirebaseAuth.getInstance();

        auth.signInWithEmailAndPassword(login, password)
                .addOnCompleteListener((Activity) ctx, task -> {
                    if (task.isSuccessful()) {

                        callback.onSuccess();
                    } else {
                        callback.onFailure(task.getException().getLocalizedMessage());
                    }
                });

    }
}
