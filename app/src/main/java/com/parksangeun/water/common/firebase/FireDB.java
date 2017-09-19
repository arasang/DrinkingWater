package com.parksangeun.water.common.firebase;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.parksangeun.water.common.Metrics;
import com.parksangeun.water.common.User;

import org.json.JSONException;
import org.json.JSONObject;

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

    public FireDB(){
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
    }

    public FireDB(Handler handler){
        this.handler = handler;

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
                Log.d(TAG, "snap : " +dataSnapshot.getValue());

                String data = dataSnapshot.getValue().toString();

                data = data.replace("{", "");
                data = data.replace("}", "");

                StringTokenizer token = new StringTokenizer(data, ",");

                while (token.hasMoreElements()) {
                    Log.d(TAG, "token : " + token.nextElement());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void readUserInfo(String title, final String uid){

        ref.child(title).child(uid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: 지속적으로 호출
                User user = dataSnapshot.getValue(User.class);

                String userEmail = user.getUserEmail();
                String userFamily = user.getUserFamily();
                String userGiven = user.getUserGiven();
                String dailyGoal = user.getDailyGoal();

                Message msg = new Message();
                msg.what = Metrics.GET_USER_SUCCESS;

                Bundle bundle = new Bundle();
                bundle.putString("userEmail", userEmail);
                bundle.putString("userFamily", userFamily);
                bundle.putString("userGiven", userGiven);
                bundle.putString("dailyGoal", dailyGoal);

                msg.setData(bundle);

                handler.sendMessage(msg);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // TODO: 로딩이 중단됐을 경우 호출

                handler.sendEmptyMessage(Metrics.GET_USER_FAILED);
            }
        });
    }
}
