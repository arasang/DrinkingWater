package com.parksangeun.water.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.parksangeun.water.R;
import com.parksangeun.water.common.CommonFunction;
import com.parksangeun.water.common.data.Metrics;
import com.parksangeun.water.fragment.MyInfoFragment;
import com.parksangeun.water.fragment.NameFragment;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

/**
 * Created by parksangeun on 2017. 9. 18..
 */

public class MyInfoActivity extends AppCompatActivity
        implements MyInfoFragment.setChangeFragment,
                    MyInfoFragment.startCamera{
    private static final String TAG = "MyInfoActivity";

    private NameFragment nameFragment;
    private MyInfoFragment myinfoFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private Toolbar toolbar;
    
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (Metrics.PROFILE_CHANGE_FAILED.equals(intent.getAction())) {
                Toast.makeText(context, "이름을 변경하지 못했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);

        /** TitleBar 설정 **/
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        myinfoFragment = new MyInfoFragment();
        nameFragment = new NameFragment();

        transaction.add(R.id.container, myinfoFragment);
        transaction.commit();
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter filter = new IntentFilter(Metrics.PROFILE_CHANGE_FAILED);
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(receiver);
    }

    @Override
    public void onChangeFragment(int position) {
        // TODO: MyInfoFragment Event
        switch (position) {
            case Metrics.CLICK_NAME:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, nameFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;

            case Metrics.CLICK_GOAL:
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.container, nameFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
        }
    }

    @Override
    public void OnStartCamera() {
        CommonFunction function = new CommonFunction(this);

        if (function.checkPermissions()) {
            takePicture();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case Metrics.MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < permissions.length; i++) {
                        if (permissions[i].equals(Metrics.permissions[0])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();
                            }
                        } else if (permissions[i].equals(Metrics.permissions[1])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        } else if (permissions[i].equals(Metrics.permissions[2])) {
                            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                                showNoPermissionToastAndFinish();

                            }
                        }
                    }
                    takePicture();
                } else {
                    showNoPermissionToastAndFinish();
                }
                return;
            }
        }
    }

    private void takePicture(){

        CommonFunction function = new CommonFunction(this);

        File photo = null;

        try {
            photo = function.createImageFile();
        } catch (IOException e) {
            Toast.makeText(this, "이미지 처리 오류! 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (photo != null) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(this, "com.parksangeun.water.provider", photo);
            Log.d(TAG, "Uri : " + photoUri);
            intent.setData(photoUri);
            startActivityForResult(intent, Metrics.CAMERA_START);
        }
    }

    private void showNoPermissionToastAndFinish() {
        Toast.makeText(this, "권한 요청에 동의 해주셔야 이용 가능합니다. 설정에서 권한 허용 하시기 바랍니다.", Toast.LENGTH_SHORT).show();
        finish();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == Metrics.CAMERA_START) {
            if (resultCode == RESULT_OK) {
                Uri uri = data.getData();
                Log.d(TAG, "uri : " + uri);
                try {
                    myinfoFragment.changeProfile(uri);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
