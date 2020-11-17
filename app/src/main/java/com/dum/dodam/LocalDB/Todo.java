package com.dum.dodam.LocalDB;

import io.realm.RealmObject;

public class Todo extends RealmObject {
    public String ID;
    public int color;
    public long start;
    public long end;
    public String title;
    public boolean visible;
    public boolean done;

    public Todo() {
    }

    public Todo(String ID, long start, long end, String title, boolean visible, int color) {
        this.ID = ID;
        this.start = start;
        this.end = end;
        this.title = title;
        this.visible = visible;
        this.color = color;
    }
}
