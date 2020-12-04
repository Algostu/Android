package com.dum.dodam.LocalDB;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TimeTableDB extends RealmObject {
    public String version;
    public boolean custom = false;
    public int lunch_time = 4;
    public RealmList<TimeTableDay> days = new RealmList<>();

    public TimeTableDB() {
    }

    public TimeTableDB(String version, boolean custom, int lunch_time, RealmList<TimeTableDay> days) {
        this.version = version;
        this.custom = custom;
        this.lunch_time = lunch_time;
        this.days = days;
    }
}
