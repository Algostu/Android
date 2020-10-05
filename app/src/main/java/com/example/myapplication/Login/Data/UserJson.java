package com.example.myapplication.Login.Data;

import java.io.Serializable;

@SuppressWarnings("serial")
public class UserJson implements Serializable {
    public long userID;
    public int schoolID;
    public int regionID;
    public String email;
    public int gender;
    public int age;
    public int grade;
    public String nickName;
    public String status;

    public String getNickName() {
        return nickName;
    }
}
