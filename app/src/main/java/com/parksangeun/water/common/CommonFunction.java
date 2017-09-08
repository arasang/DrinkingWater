package com.parksangeun.water.common;

import android.content.Context;
import android.content.Intent;

/**
 * Created by parksangeun on 2017. 9. 8..
 */

public class CommonFunction {
    private static final String TAG = "CommonFunction";

    private Context context;

    public CommonFunction(Context context){
        this.context = context;
    }

    public void ChangeActivity(Context current, Class after){
        Intent intent = new Intent(current, after);
        context.startActivity(intent);
    }
}
