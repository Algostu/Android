package com.dum.dodam.LocalDB;

import io.realm.RealmObject;

public class TodoData extends RealmObject {
    public long start;
    public long end;
    public String title;

    public TodoData() {
    }

    public TodoData(long start, long end, String title) {
        this.start = start;
        this.end = end;
        this.title = title;
    }
}
