package com.parksangeun.water.common.firebase;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parksangeun.water.common.data.Metrics;
import com.parksangeun.water.common.data.User;
import com.parksangeun.water.common.data.WaterData;

import java.util.HashMap;
import java.util.Iterator;
import java.util.StringTokenizer;

/**
 * Created by parksangeun on 2017. 9. 13..
 */

public class FireDB{
    private static final String TAG = "FireDB";

    private FirebaseDatabase db;
    private DatabaseReference ref;
    private Handler handler;
    private Context context = null;

    public FireDB(){
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
    }

    public FireDB(Context context){
        Log.d(TAG, "Created by Context");
        this.context = context;

        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
    }

    public void insertStringDB(String title, String uid, String year, String month,
                               String day, HashMap<String,String> param){
        Iterator<String> iterator = param.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = param.get(key);

            if ("User".equals(title)) {
                ref.child(title).child(uid).child(key).setValue(value);

            } else if ("Water".equals(title)) {
                ref.child(title).child(uid).child(year).child(month).child(day).child(key).setValue(value);
            }
        }
    }


    public void readDayWater(String title, String year, String month, String day, String uid){
        ref.child(title).child(uid).child(year).child(month).child(day).
                addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue() != null) {
                            String data = dataSnapshot.getValue().toString();

                            data = data.replace("{", "");
                            data = data.replace("}", "");
                            data = data.replace(" ", "");

                            StringTokenizer token = new StringTokenizer(data, ",");

                            HashMap<String,String> tempHash = new HashMap<String, String>();

                            String key = "";
                            String strValue = "";

                            int i=0;

                            while (token.hasMoreElements()) {
                                String value = token.nextToken().toString();
                                StringTokenizer tokenizer = new StringTokenizer(value, "=");

                                while (tokenizer.hasMoreElements()) {
                                    if (i % 2 == 0) {
                                        key = tokenizer.nextToken().toString();
                                    } else {
                                        strValue = tokenizer.nextToken().toString();

                                        tempHash.put(key, strValue);
                                    }
                                    i++;
                                }
                            }

                            WaterData.setToday(tempHash);

                            Intent intent = new Intent(Metrics.ACTION_READ_WATER);
                            context.sendBroadcast(intent);

                        } else {
                            Intent intent = new Intent(Metrics.ACTION_READ_WATER);
                            context.sendBroadcast(intent);
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        handler.sendEmptyMessage(Metrics.GET_WATER_FAILED);
                    }
                });
    }

    public void readUserInfo(String uid, final Handler handler){
        final HashMap<String,String> result = new HashMap<String,String>();
        ref.child(Metrics.USER).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                String userEmail = user.getUserEmail();
                String userFamily = user.getUserFamily();
                String userGiven = user.getUserGiven();
                String dailyGoal = user.getDailyGoal();

                result.put("userEmail", userEmail);
                result.put("userFamily", userFamily);
                result.put("userGiven", userGiven);
                result.put("dailyGoal", dailyGoal);

                Bundle bundle = new Bundle();
                bundle.putSerializable("UserInfo", result);

                Message msg = new Message();
                msg.what = 0;
                msg.setData(bundle);

                handler.sendMessage(msg);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
}
