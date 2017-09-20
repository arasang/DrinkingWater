package com.parksangeun.water.common.firebase;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.parksangeun.water.common.Metrics;
import com.parksangeun.water.common.UserData;

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
                    Log.d(TAG, "이거 호출 되니 ? ");
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
                    sendBroad(trigger);
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
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    //TODO: Already exists registered user.

                    Log.d(TAG, "User already exists.");
                    trigger = Metrics.USER_EXIST;

                    if (flag == 0) {
                        setGlobalUser();
                        flag = 1;
                    }
                } else {
                    //TODO: Need login

                    Log.d(TAG, "You need to log in.");
                    trigger = Metrics.USER_NOT_EXIST;
                    if (flag == 0) {
                        flag = 1;
                        sendBroad(trigger);
                    }
                }
            }
        };
    }

    private void sendBroad(int trigger){
        Intent intent = new Intent(Metrics.AUTH_BROAD);
        intent.putExtra("trigger", trigger);

        context.sendBroadcast(intent);
    }

    public FirebaseAuth getAuth(){
        return auth;
    }

    public void setGlobalUser(){
        user = auth.getCurrentUser();
        Log.d(TAG, "user : " + user.getUid());
        String uid = user.getUid();

        FireDB db = new FireDB();
        db.readUserInfo(uid, handler);

    }

    public FirebaseUser getUser(){
        FirebaseUser user = auth.getCurrentUser();

        return user;
    }

    public void addFireAuth(){
        auth.addAuthStateListener(listener);
    }

    public void removeFireAuth(){
        auth.removeAuthStateListener(listener);
    }
}
