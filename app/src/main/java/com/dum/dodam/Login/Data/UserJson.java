package com.dum.dodam.Login.Data;

import com.dum.dodam.Home.dataframe.MyCommunityFrame;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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
    // community List
    public ArrayList<MyCommunityFrame> comAll;
    public ArrayList<MyCommunityFrame> comRegion;
    public ArrayList<MyCommunityFrame> comSchool;
    // return value
    public String status;


    public String getNickName() {
        return nickName;
    }
}
