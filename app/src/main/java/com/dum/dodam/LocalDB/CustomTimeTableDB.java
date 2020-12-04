package com.dum.dodam.LocalDB;

import io.realm.RealmList;
import io.realm.RealmObject;

public class CustomTimeTableDB extends RealmObject {
    public int lunch_time = 4;
    public RealmList<TimeTableDay> days = new RealmList<>();

    public CustomTimeTableDB() {
    }

    public CustomTimeTableDB(int lunch_time, RealmList<TimeTableDay> days) {
        this.lunch_time = lunch_time;
        this.days = days;
    }
}
