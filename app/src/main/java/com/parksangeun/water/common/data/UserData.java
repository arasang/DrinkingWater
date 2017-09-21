package com.parksangeun.water.common.data;

import android.net.Uri;

/**
 * Created by parksangeun on 2017. 9. 20..
 */

public class UserData {
    private static String uid;
    private static String userEmail;
    private static String displayName;
    private static Uri userPhoto;
    private static String dailyGoal;

    public static String getUid() {
        return uid;
    }

    public static String getDisplayName() {
        return displayName;
    }

    public static String getUserEmail() {
        return userEmail;
    }

    public static Uri getUserPhoto() {
        return userPhoto;
    }

    public static String getDailyGoal() {
        return dailyGoal;
    }

    public static void setUid(String uid) {
        UserData.uid = uid;
    }

    public static void setDisplayName(String displayName) {
        UserData.displayName = displayName;
    }

    public static void setUserEmail(String userEmail) {
        UserData.userEmail = userEmail;
    }

    public static void setUserPhoto(Uri userPhoto) {
        UserData.userPhoto = userPhoto;
    }

    public static void setDailyGoal(String dailyGoal) {
        UserData.dailyGoal = dailyGoal;
    }
}
