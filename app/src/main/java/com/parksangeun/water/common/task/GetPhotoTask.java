package com.parksangeun.water.common.task;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.parksangeun.water.common.data.Metrics;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by parksangeun on 2017. 9. 16..
 */

public class GetPhotoTask extends AsyncTask {
    private static final String TAG = "GetPhotoTask";

    private URL url;

    private Handler handler;

    public GetPhotoTask(String strURL, Handler handler) throws MalformedURLException {
        url = new URL(strURL);

        this.handler = handler;

    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected Object doInBackground(Object[] params) {
        try {
            HttpsURLConnection connection = (HttpsURLConnection)url.openConnection();
            connection.setDoInput(true);

            InputStream input = connection.getInputStream();
            Bitmap bitmap = BitmapFactory.decodeStream(input);

            return bitmap;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Object o) {
        super.onPostExecute(o);

        Message msg = new Message();

        if (o != null) {
            Bitmap bitmap = (Bitmap)o;

            ByteArrayOutputStream output = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);

            byte[] arrayPhoto = output.toByteArray();

            msg.what = Metrics.GET_IMAGE_SUCCESS;

            Bundle bundle = new Bundle();
            bundle.putByteArray(Metrics.BYTE_ARRAY_PHOTO, arrayPhoto);

            msg.setData(bundle);

            handler.sendMessage(msg);
        } else {
            msg.what = Metrics.GET_IMAGE_FAILED;
            handler.sendMessage(msg);
        }

    }
}
