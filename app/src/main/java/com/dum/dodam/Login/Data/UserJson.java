package com.dum.dodam.Login.Data;

import com.dum.dodam.Home.dataframe.MyCommunityFrame2;
import com.dum.dodam.httpConnection.BaseResponse;

import java.io.Serializable;
import java.util.ArrayList;

@SuppressWarnings("serial")
public class UserJson extends BaseResponse implements Serializable {
    // cafeteria_ information
    public String regionName;
    public String townName;
    public String schoolName;
    public int schoolGender;
    public String email;
    public String contact;
    public String homePage;
    public String authorized;
    public int firstLogin;
    // user information
    public int gender;
    public int age;
    public int grade;
    public String nickName;
    public String userName;
    public String signupDate;
    // community List
    public ArrayList<MyCommunityFrame2> comAll;
    public ArrayList<MyCommunityFrame2> comRegion;
    public ArrayList<MyCommunityFrame2> comSchool;

    public String getNickName() {
        return nickName;
    }
}
