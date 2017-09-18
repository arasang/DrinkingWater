package com.parksangeun.water.common.firebase;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by parksangeun on 2017. 9. 13..
 */

public class FireDB{
    private static final String TAG = "FireDB";

    private FirebaseDatabase db;
    private DatabaseReference ref;

    public FireDB(){
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
    }

    public void insertStringDB(String first, HashMap<String,String> param){
        Iterator iterator = param.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            String value = param.get(key);

            ref.child(first).child(key).setValue(value);
        }
    }

    public void insertAmount(String first/**uid**/, String second/**amount**/, HashMap<String,String> param){
        Iterator<String> iterator = param.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next();
            String value = param.get(key);

            ref.child(first).child(second).child(key).setValue(value);
        }
    }

    public void readStringDB(){
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // TODO: 최초 생성자에서 한 번만 호출되는 메서드

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }





}
