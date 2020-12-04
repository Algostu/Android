package com.dum.dodam.Cafeteria.dataframe;

import java.util.ArrayList;

public class MealInfo {
    public ArrayList<MIFR> mealServiceDietInfo;

    public class MIFR {
        public ArrayList<MIF> row;
    }

    public class MIF{
        public String MLSV_YMD;
        public String DDISH_NM;
        public String CAL_INFO;
    }
}
