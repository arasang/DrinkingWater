package com.parksangeun.water.common;

/**
 * Created by parksangeun on 2017. 9. 18..
 */

public class User {
    private String DailyGoal;
    private String UserFamily;
    private String UserGiven;
    private String UserEmail;

    public User(){}

    public User(String DailyGoal, String UserFamily, String UserGiven, String UserEmail){
        this.DailyGoal = DailyGoal;
        this.UserFamily = UserFamily;
        this.UserGiven = UserGiven;
        this.UserEmail = UserEmail;
    }

    public void setDailyGoal(String DailyGoal) {
        this.DailyGoal = DailyGoal;
    }

    public void setUserFamily(String UserFamily) {
        this.UserFamily = UserFamily;
    }

    public void setUserGiven(String UserGiven) {
        this.UserGiven = UserGiven;
    }

    public void setUserEmail(String UserEmail) {
        this.UserEmail = UserEmail;
    }

    public String getDailyGoal() {
        return DailyGoal;
    }

    public String getUserFamily() {
        return UserFamily;
    }

    public String getUserGiven() {
        return UserGiven;
    }

    public String getUserEmail() {
        return UserEmail;
    }
}
