package com.parksangeun.water.common;

import java.util.HashMap;

/**
 * Created by parksangeun on 2017. 9. 19..
 */

public class Water {
    public static HashMap<String,String> today = new HashMap<String,String>();

    public static void setToday(HashMap<String, String> today) {
        Water.today = today;
    }

    public static HashMap<String, String> getToday() {
        return today;
    }
}
