package com.parksangeun.water.common.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.parksangeun.water.common.data.Metrics;
import com.parksangeun.water.common.data.UserData;

import java.util.HashMap;

/**
 * Created by parksangeun on 2017. 9. 11..
 */

public class FireAuth {
    private static final String TAG = "FireAuth";

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;

    private Context context;

    private int trigger = -1;

    private int flag = 0;

    private FirebaseUser user;

    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 0:
                    HashMap<String,String> userInfo =
                    (HashMap<String,String>)msg.getData().getSerializable("UserInfo");
                    String uid = user.getUid();
                    String displayName = user.getDisplayName();
                    String userEmail = user.getEmail();
                    Uri userPhoto = user.getPhotoUrl();

                    UserData.setUid(uid);
                    UserData.setDisplayName(displayName);
                    UserData.setUserEmail(userEmail);
                    UserData.setUserPhoto(userPhoto);
                    UserData.setDailyGoal(userInfo.get("dailyGoal"));
                    sendTriggerToAuth(trigger);
                    break;
            }
        }
    };

    public FireAuth(Context context){
        this.context = context;
        initFire();
    }

    private void initFire(){
        auth = FirebaseAuth.getInstance();

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                user = auth.getCurrentUser();

                if (user != null) {
                    //TODO: Already exists registered user.

//                    Log.d(TAG, "User already exists.");
                    trigger = Metrics.USER_EXIST;

                    if (flag == 0) {
                        setGlobalUser();
                        flag = 1;
                    }
                } else {
                    //TODO: Need login

//                    Log.d(TAG, "You need to log in.");
                    trigger = Metrics.USER_NOT_EXIST;
                    if (flag == 0) {
                        flag = 1;
                        sendTriggerToAuth(trigger);
                    }
                }
            }
        };
    }

    private void sendTriggerToAuth(int trigger){
        Intent intent = new Intent(Metrics.AUTH_BROAD);
        intent.putExtra("trigger", trigger);

        context.sendBroadcast(intent);
    }



    public FirebaseAuth getAuth(){
        return auth;
    }

    public void setGlobalUser(){
        user = auth.getCurrentUser();
        String uid = user.getUid();

        FireDB db = new FireDB();
        db.readUserInfo(uid, handler);

    }

    public FirebaseUser getUser(){
        user = auth.getCurrentUser();

        return user;
    }

    public void changeProfile(String name, Uri uri){
        user = auth.getCurrentUser();

        UserProfileChangeRequest request;
        if (name != null || name.length() != 0) {
            request = new UserProfileChangeRequest.Builder()
                    .setDisplayName(name)
                    .build();
        } else {
            request = new UserProfileChangeRequest.Builder()
                    .setPhotoUri(uri)
                    .build();
        }
        user.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    sendToMyInfo(Metrics.PROFILE_CHANGE_SUCCESS);
                } else {
                    sendToMyInfo(Metrics.PROFILE_CHANGE_FAILED);
                }
            }
        });

        UserData.setDisplayName(user.getDisplayName());
        UserData.setUserPhoto(user.getPhotoUrl());
    }

    private void sendToMyInfo(String action) {
        Intent intent = new Intent(action);
        context.sendBroadcast(intent);
    }

    public void addFireAuth(){
        auth.addAuthStateListener(listener);
    }

    public void removeFireAuth(){
        auth.removeAuthStateListener(listener);
    }
}
