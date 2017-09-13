package com.parksangeun.water.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.john.waveview.WaveView;
import com.parksangeun.water.R;
import com.parksangeun.water.common.CommonFunction;
import com.parksangeun.water.common.Metrics;
import com.parksangeun.water.common.firebase.FireAuth;

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

    /** Firebase Setting **/
    private FireAuth fireAuth;

    /** BroadCast **/
    private BroadcastReceiver broadReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Metrics.AUTH_BROAD.equals(intent.getAction())) {
                trigger = intent.getIntExtra("trigger", 0);

                CommonFunction function = new CommonFunction(context);

                if (trigger == Metrics.USER_EXIST) {
                    function.ChangeActivity(context, MainActivity.class);
                } else if (trigger == Metrics.USER_NOT_EXIST) {
                    function.ChangeActivity(context, AuthActivity.class);
                } else {
                    Toast.makeText(context, "비정상적 접근입니다.", Toast.LENGTH_SHORT).show();
                }

                finish();
            }
        }
    };

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
                    // TODO: Nothing
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        initBroadCast();

        initView();

        initFirebase();
    }

    private void initBroadCast(){
        IntentFilter filter = new IntentFilter(Metrics.AUTH_BROAD);
        registerReceiver(broadReceiver,filter);
    }

    private void initView(){
        wave = (WaveView)findViewById(R.id.wave);

        handler.sendEmptyMessage(Metrics.LOADING);
    }

    private void initFirebase(){
        fireAuth = new FireAuth(context);
    }

    @Override
    protected void onStart() {
        super.onStart();
        fireAuth.addFireAuth();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireAuth.removeFireAuth();
        unregisterReceiver(broadReceiver);
    }
}
