package com.dum.dodam.Scheduler.dataframe;

import java.util.ArrayList;

public class SchoolTimeTable {
    public ArrayList<HTT> hisTimetable;

    public class HTT {
        public ArrayList<TimeTable> row;
    }

    public class TimeTable {
        public String ITRT_CNTNT;
        public String PERIO;
        public String ALL_TI_YMD;
    }
}