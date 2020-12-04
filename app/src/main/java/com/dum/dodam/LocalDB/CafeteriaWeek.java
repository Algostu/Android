package com.dum.dodam.LocalDB;

import io.realm.RealmObject;

public class CafeteriaWeek extends RealmObject {
    public String monday = "";
    public String tuesday = "";
    public String wednesday = "";
    public String thursday = "";
    public String friday = "";
    public String mondayCal = "";
    public String tuesdayCal = "";
    public String wednesdayCal = "";
    public String thursdayCal = "";
    public String fridayCal = "";
    public String mondayDate = "";
    public String tuesdayDate = "";
    public String wednesdayDate = "";
    public String thursdayDate = "";
    public String fridayDate = "";

    public CafeteriaWeek() {
    }

    public CafeteriaWeek(String monday, String tuesday, String wednesday, String thursday, String friday, String mondayCal, String tuesdayCal, String wednesdayCal, String thursdayCal, String fridayCal, String mondayDate, String tuesdayDate, String wednesdayDate, String thursdayDate, String fridayDate) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.mondayCal = mondayCal;
        this.tuesdayCal = tuesdayCal;
        this.wednesdayCal = wednesdayCal;
        this.thursdayCal = thursdayCal;
        this.fridayCal = fridayCal;
        this.mondayDate = mondayDate;
        this.tuesdayDate = tuesdayDate;
        this.wednesdayDate = wednesdayDate;
        this.thursdayDate = thursdayDate;
        this.fridayDate = fridayDate;
    }
}
