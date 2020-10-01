package com.example.myapplication.Login.Data;

public class UserInfo {
    public long userID;
    public String email;
    public String gender;
    public String ageRange;
    public String accessToken;
    public long expTime;

    public UserInfo(long userID, String accessToken, String email, String gender, String ageRange, long expTime){
        this.userID = userID;
        this.accessToken = accessToken;
        this.email = email;
        this.gender = gender;
        this.ageRange = ageRange;

        this.expTime = expTime;
    }

}
