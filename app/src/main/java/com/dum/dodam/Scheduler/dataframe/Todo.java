package com.dum.dodam.Scheduler.dataframe;

public class Todo {
    public String content;
    public boolean done;
    public boolean visible;

    public Todo(String content, boolean done){
        visible = false;
        this.content = content;
        this.done = done;
    }
}
