package com.dum.dodam.LocalDB;

import io.realm.RealmObject;

public class CafeteriaDB extends RealmObject {
    public String version;
    public CafeteriaWeek week1;
    public CafeteriaWeek week2;
    public CafeteriaWeek week3;
    public CafeteriaWeek week4;
    public CafeteriaWeek week5;
    public CafeteriaWeek week6;

    public CafeteriaDB() {
    }

    public CafeteriaDB(String version, CafeteriaWeek week1, CafeteriaWeek week2, CafeteriaWeek week3, CafeteriaWeek week4, CafeteriaWeek week5, CafeteriaWeek week6) {
        this.version = version;
        this.week1 = week1;
        this.week2 = week2;
        this.week3 = week3;
        this.week4 = week4;
        this.week5 = week5;
        this.week6 = week6;
    }
}
