package com.dum.dodam.LocalDB;

import io.realm.RealmList;
import io.realm.RealmObject;

public class TimeTableDay extends RealmObject {
    public int date;
    public RealmList<String> subject;

    public TimeTableDay() {
    }

    public TimeTableDay(RealmList<String> subject) {
        this.subject = subject;
    }
}
