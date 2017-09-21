package com.parksangeun.water.common;


import com.parksangeun.water.common.data.Metrics;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by parksangeun on 2017. 9. 19..
 */

public class ConvertDate {
    private static final String TAG = "ConvertDate";
    private Date current;

    public ConvertDate(){
        long longCurrent = System.currentTimeMillis();

        current = new Date(longCurrent);
    }

    public String getCurrent(int what){
        String returnValue = "";
        SimpleDateFormat format = new SimpleDateFormat();

        switch(what){
            case Metrics.YEAR:
                format = new SimpleDateFormat("yyyy");
                break;

            case Metrics.MONTH:
                format = new SimpleDateFormat("MM");
                break;

            case Metrics.DAY:
                format = new SimpleDateFormat("dd");
                break;

            case Metrics.TIME:
                format = new SimpleDateFormat("HHmmss");
                break;

            case Metrics.DAYTIME:
                format = new SimpleDateFormat("ddHHmmss");
                break;

            case Metrics.ALL:
                format = new SimpleDateFormat("yyyyMMddHHmmss");
                break;
        }

        returnValue = format.format(current);
        return returnValue;
    }
}
