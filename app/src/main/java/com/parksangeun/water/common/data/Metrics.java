package com.parksangeun.water.common.data;

import android.Manifest;

/**
 * Created by parksangeun on 2017. 9. 8..
 **/

public class Metrics {
    /** Used FireAuth **/
    public static final String AUTH_BROAD = "AUTH_BROAD";

    /** Used FireDB **/
    public static final String WATER = "Water";
    public static final String ACTION_READ_WATER = "READ_WATER";

    /** Used SplashActivity **/
    public static final int LOADING = 2;
    public static final int FINISH  = 3;

    public static final int USER_EXIST     = 10;
    public static final int USER_NOT_EXIST = 11;

    public static final int GET_WATER_SUCCESS = 12;
    public static final int GET_WATER_FAILED  = 13;
    public static final int GET_WATER_NULL    = 14;

    /** Used AuthActivity **/
    public static final int RC_SIGN_IN = 100;
    public static final String USER = "User";

    public static final int MULTIPLE_PERMISSIONS = 301;

    public static String[] permissions = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA}; //권한 설정 변수

    /** Used GetPhotoTask **/
    public static final int GET_IMAGE_SUCCESS = 1000;
    public static final int GET_IMAGE_FAILED  = 1001;
    public static final String BYTE_ARRAY_PHOTO = "arrayPhoto";

    /** Used ConvertDate **/
    public static final int YEAR = 1010;
    public static final int MONTH = 1011;
    public static final int DAY = 1012;
    public static final int TIME = 1013;
    public static final int DAYTIME = 1014;
    public static final int ALL = 1015;

    /** Used MainActivity **/
    public static final int START_ANIMATION = 2000;
    public static final int STOP_ANIMATION  = 2001;

    public static final String BROAD_GET_USER_SUCCESS  = "GET_USER_SUCCESS";
    public static final String BROAD_GET_USER_FAILED   = "GET_USER_FAILED";
    public static final String BROAD_GET_WATER_SUCCESS = "GET_WATER_SUCCESS";
    public static final String BROAD_GET_WATER_FAILED  = "GET_WATER_FAILED";

    /** Used MyInActivity **/
    public static final String PROFILE_CHANGE_SUCCESS = "CHANGE_SUCCESS";
    public static final String PROFILE_CHANGE_FAILED  = "CHANGE_FAILED";
    public static final int CLICK_NAME = 2500;
    public static final int CLICK_GOAL = 2501;
    public static final int CAMERA_START = 2502;


}
