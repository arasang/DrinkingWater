package com.parksangeun.water.common;

import java.util.HashMap;
import java.util.Iterator;

/**
 * Created by parksangeun on 2017. 9. 19..
 */

public class WaterData {
    private static HashMap<String,String> today = new HashMap<String,String>();

    public static void setToday(HashMap<String, String> today) {
        WaterData.today = today;
    }

    public static HashMap<String, String> getToday() {
        return today;
    }

    public static String getTotalToday(){
        Iterator<String> i = today.keySet().iterator();

        int result = 0;

        while (i.hasNext()) {
            String key = i.next();
            String value = today.get(key);

            result += Integer.parseInt(value);
        }

        return String.valueOf(result);
    }
}
