package com.dum.dodam.LocalDB;

import io.realm.annotations.RealmModule;

@RealmModule(classes = {Todo.class, TodoData.class})
public class TodoModule {
}
