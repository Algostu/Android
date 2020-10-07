package com.example.myapplication.Login.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserJson implements Serializable {
    // school information
    public String regionName;
    public String townName;
    public String schoolName;
    public int schoolGender;
    public String email;
    public String contact;
    public String homePage;
    // user information
    public int gender;
    public int age;
    public int grade;
    public String nickName;
    // return value
    public String status;

    public String getNickName() {
        return nickName;
    }
}
