package com.dum.dodam.LocalDB;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {TimeTableDB.class, TimeTableDay.class})
public class TimeTableModule {
}
