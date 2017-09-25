package com.parksangeun.water.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.parksangeun.water.R;
import com.parksangeun.water.common.ConvertDate;
import com.parksangeun.water.common.data.Metrics;
import com.parksangeun.water.common.data.UserData;
import com.parksangeun.water.common.firebase.FireDB;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by parksangeun on 2017. 9. 25..
 */

public class GridAdapter extends BaseAdapter {

    private static final String TAG = "GridAdapter";

    private Context context;
    private LayoutInflater inflater;

    private int textArray[] = {
            R.string._150,
            R.string._250,
            R.string._330,
            R.string._500,
            R.string._750,
            R.string._1,
    };

    private String arrayAmount[] = {
            "150",
            "250",
            "330",
            "500",
            "750",
            "1000",
    };

    private int imageArray[] = {

    };

    public GridAdapter(Context context){
        this.context = context;

        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return textArray.length;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;

        if (convertView == null) {
            holder = new ViewHolder();

            convertView = inflater.inflate(R.layout.content_grid, null);

            holder.imageCup = (ImageView) convertView.findViewById(R.id.imageCup);
            holder.textAmount = (TextView) convertView.findViewById(R.id.textAmount);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }

        String title = context.getString(textArray[position]);

        holder.textAmount.setText(title);

        //ImageSetting
//        Bitmap bitmap = null;
//        holder.imageCup.setImageBitmap(bitmap);

        return convertView;
    }

    private class ViewHolder{
        ImageView imageCup;
        TextView textAmount;
    }

    public void onItemClick(int position, Context context){
        String amount = arrayAmount[position];
        String uid = UserData.getUid();

        FireDB fireDB = new FireDB(context);

        ConvertDate date = new ConvertDate();
        String year = date.getCurrent(Metrics.YEAR);
        String month = date.getCurrent(Metrics.MONTH);
        String day = date.getCurrent(Metrics.DAY);
        String time = date.getCurrent(Metrics.TIME);

        HashMap<String,String> params = new HashMap<String,String>();
        params.put(time, amount);

        fireDB.insertStringDB(Metrics.WATER, uid, year, month, day, params);
    }
}
