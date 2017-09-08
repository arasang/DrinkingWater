package com.parksangeun.water.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.john.waveview.WaveView;
import com.parksangeun.water.R;
import com.parksangeun.water.common.CommonFunction;
import com.parksangeun.water.common.Metrics;

/**
 * Created by parksangeun on 2017. 9. 8..
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    /** UI **/
    private WaveView wave;

    /** Variable **/
    private Context context = SplashActivity.this;

    private int load    = 50;
    private int delay   = 100;
    private int trigger = -1;

    /** Firebase **/
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener listener;

    /** Handler Setting **/
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Metrics.LOADING:
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            load++;
                            wave.setProgress(load);

                            if(load == 100)
                                handler.sendEmptyMessage(Metrics.FINISH);

                            handler.sendEmptyMessage(Metrics.LOADING);
                        }
                    }, delay);
                    break;

                case Metrics.FINISH:

                    CommonFunction function = new CommonFunction(context);

                    if (trigger == Metrics.USER_EXIST) {
                        function.ChangeActivity(context, MainActivity.class);
                    } else if (trigger == Metrics.USER_NOT_EXIST) {
                        function.ChangeActivity(context, AuthActivity.class);
                    } else {
                        function.ChangeActivity(context, AuthActivity.class);
                    }
                    finish();

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initView();

        initFirebase();
    }

    private void initView(){
        wave = (WaveView)findViewById(R.id.wave);

        handler.sendEmptyMessage(Metrics.LOADING);
    }

    private void initFirebase(){
        auth = FirebaseAuth.getInstance();

        listener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = auth.getCurrentUser();

                if (user != null) {
                    //TODO: Already exists registered user.

                    Log.d(TAG, "onAuthState : signed_in = " + user.getUid());
                    trigger = Metrics.USER_EXIST;
                } else {
                    //TODO: Need login

                    Log.d(TAG, "onAuthState : signed_out");
                    trigger = Metrics.USER_NOT_EXIST;
                }
            }
        };
    }
}
