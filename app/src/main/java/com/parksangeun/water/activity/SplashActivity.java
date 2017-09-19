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
import com.parksangeun.water.common.ConvertDate;
import com.parksangeun.water.common.Metrics;
import com.parksangeun.water.common.Water;
import com.parksangeun.water.common.firebase.FireAuth;
import com.parksangeun.water.common.firebase.FireDB;

import java.util.HashMap;

/**
 * Created by parksangeun on 2017. 9. 8..
 */

public class SplashActivity extends AppCompatActivity {

    private static final String TAG = "SplashActivity";

    /** UI **/
    private WaveView wave;

    /** Variable **/
    private Context context = SplashActivity.this;
    private CommonFunction function = new CommonFunction(context);


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

                if (trigger == Metrics.USER_EXIST) {
                    Log.d(TAG, "Broad");
                    FireDB firedb = new FireDB(handler);
                    String uid = fireAuth.getUser().getUid();

                    ConvertDate convertDate = new ConvertDate();
                    String year = convertDate.getCurrent(Metrics.YEAR);
                    String month = convertDate.getCurrent(Metrics.MONTH);
                    String day = convertDate.getCurrent(Metrics.DAY);
                    String time = convertDate.getCurrent(Metrics.TIME);

                    firedb.readDayWater(Metrics.WATER, year, month, day, uid);

                } else if (trigger == Metrics.USER_NOT_EXIST) {
                    function.ChangeActivity(context, AuthActivity.class);
                } else {
                    Toast.makeText(context, "비정상적 접근입니다.", Toast.LENGTH_SHORT).show();
                }
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

                case Metrics.GET_WATER_SUCCESS:
                    Bundle bundle = msg.getData();

                    Water.setToday((HashMap<String, String>) bundle.getSerializable(Metrics.WATER));
                    function.ChangeActivity(context, MainActivity.class);
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
        initBroadCast();
    }

    @Override
    protected void onStop() {
        super.onStop();
        fireAuth.removeFireAuth();
        unregisterReceiver(broadReceiver);
    }
}
