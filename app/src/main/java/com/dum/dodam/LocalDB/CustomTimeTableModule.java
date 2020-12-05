package com.dum.dodam.LocalDB;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {CustomTimeTableDB.class, TimeTableDay.class})
public class CustomTimeTableModule {
}
