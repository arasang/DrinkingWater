package com.parksangeun.water.common.firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by parksangeun on 2017. 9. 13..
 */

public class FireDB {
    private static final String TAG = "FireDB";

    private FirebaseDatabase db;
    private DatabaseReference ref;

    public FireDB(){
        db = FirebaseDatabase.getInstance();
        ref = db.getReference();
    }

    public void insertStringDB(String tableName, HashMap<String,String> name){
        Iterator iterator = name.keySet().iterator();

        while (iterator.hasNext()) {
            String key = iterator.next().toString();
            String value = name.get(key);

            ref.child(tableName).child(key).push().setValue(value);

            Log.d(TAG, "Insert to Database");
        }
    }

}
