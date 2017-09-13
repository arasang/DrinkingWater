package com.parksangeun.water.common;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by parksangeun on 2017. 9. 12..
 */

public class User {
    private String uid;
    private String familyName;
    private String givenName;
    private String email;
    private String goal;

    public User(){

    }

    public User(String uid, String fname, String gname, String email, String goal){
        this.uid = uid;
        this.familyName = fname;
        this.givenName = gname;
        this.email = email;
        this.goal = goal;
    }

    public Map<String, Object> toMap(){
        HashMap<String,Object> result = new HashMap<String,Object>();

        result.put("uid", uid);
        result.put("familyName", familyName);
        result.put("givenName", givenName);
        result.put("email", email);
        result.put("goal", goal);

        return result;
    }
}
