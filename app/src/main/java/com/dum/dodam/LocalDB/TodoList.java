package com.dum.dodam.LocalDB;

import io.realm.RealmObject;

public class TodoList extends RealmObject {
    public String ID;
    public long start;
    public long end;
    public String title;

    public TodoList() {
    }

    public TodoList(String ID, long start, long end, String title) {
        this.ID = ID;
        this.start = start;
        this.end = end;
        this.title = title;
    }
}
