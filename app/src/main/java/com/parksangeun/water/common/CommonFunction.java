package com.parksangeun.water.common;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import com.parksangeun.water.common.data.Metrics;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by parksangeun on 2017. 9. 8..
 */

public class CommonFunction {
    private static final String TAG = "CommonFunction";


    private Context context;

    public CommonFunction(Context context){
        this.context = context;
    }

    public static void ChangeActivity(Context current, Class after){
        Intent intent = new Intent(current, after);
        current.startActivity(intent);
    }

    public File createImageFile() throws IOException {
        ConvertDate convertDate = new ConvertDate();
        String time = convertDate.getCurrent(Metrics.ALL);

        String fileName = "Water" + time;

        File storeDir = new File(context.getFilesDir() + "/waterApp/");
//        File storeDir = new File(Environment.getExternalStorageDirectory() + "/waterApp/");

        if (!storeDir.exists()) {
            storeDir.mkdir();
        }

        File image = File.createTempFile(fileName, ".jpg", storeDir);

        return image;
    }

    public boolean checkPermissions() {
        int result;
        List<String> permissionList = new ArrayList<>();
        for (String pm : Metrics.permissions) {
            result = ContextCompat.checkSelfPermission(context, pm);
            if (result != PackageManager.PERMISSION_GRANTED) { //사용자가 해당 권한을 가지고 있지 않을 경우 리스트에 해당 권한명 추가
                permissionList.add(pm);
            }
        }
        if (!permissionList.isEmpty()) { //권한이 추가되었으면 해당 리스트가 empty가 아니므로 request 즉 권한을 요청합니다.
            ActivityCompat.requestPermissions(
                    (Activity) context,
                    permissionList.toArray(new String[permissionList.size()]),
                    Metrics.MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

}
