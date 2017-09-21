package com.parksangeun.water.common.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.Button;

import com.parksangeun.water.R;


/**
 * Created by parksangeun on 2017. 9. 21..
 */

public class MainAlertDialog extends Dialog {
    private static final String TAG = "MainAlertDialog";

    private Button button;


    public MainAlertDialog(@NonNull Context context) {
        super(context);
    }

    public MainAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected MainAlertDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_main);

        button = (Button) findViewById(R.id.buttonSubmit);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
