package com.parksangeun.water.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;

import com.parksangeun.water.R;
import com.parksangeun.water.common.data.Metrics;

/**
 * Created by parksangeun on 2017. 9. 22..
 */

public class PhotoDialog extends Dialog implements View.OnClickListener{
    private static final String TAG = "PhotoDialog";

    private Button buttonCamera;
    private Button buttonAlbum;
    private Button buttonDefault;

    private Handler handler;

    public PhotoDialog(@NonNull Context context) {
        super(context);
        setView();
    }

    public PhotoDialog(@NonNull Context context, Handler handler){
        super(context);
        this.handler = handler;

        setView();
    }

    public PhotoDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setView();
    }

    protected PhotoDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        setView();
    }

    private void setView(){
        setContentView(R.layout.dialog_photo);

        buttonCamera = (Button)findViewById(R.id.buttonCamera);
        buttonAlbum = (Button)findViewById(R.id.buttonAlbum);
        buttonDefault = (Button)findViewById(R.id.buttonDefault);

        buttonCamera.setOnClickListener(this);
        buttonAlbum.setOnClickListener(this);
        buttonDefault.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonCamera) {
            handler.sendEmptyMessage(Metrics.CAMERA_START);
        }

        if (v == buttonAlbum) {

        }

        if (v == buttonDefault) {

        }
        dismiss();
    }
}
