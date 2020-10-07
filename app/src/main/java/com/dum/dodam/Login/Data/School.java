package com.dum.dodam.Login.Data;

public class School {
    public int schoolID;
    public int regionID;
    public String schoolName;
    public String regionName;
    public String townName;
    public int gender;
    public String contact;
    public String homePage;

    public String getRegion(){
        return regionName + ", " + townName;
    }
    public boolean is_okay_to_enroll(String gender){
        if(this.gender == 0){
            return true;
        } else if (this.gender == 1 &&  gender.equals("male")){
            return true;
        } else if (this.gender == 2 && gender.equals("female")){
            return true;
        }else {
            return false;
        }
    }
}
