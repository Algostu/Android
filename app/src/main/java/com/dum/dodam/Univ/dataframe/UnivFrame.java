package com.dum.dodam.Univ.dataframe;

public class UnivFrame {
    public String univName;
    public String subRegion;
    public String homePage;
    public String eduHomePage;
    public int univID;
//    public ImageView logo;

    @Override
    public boolean equals(Object obj){
        if (((UnivFrame)obj).univName.equals(univName)){
            return true;
        } else {
            return false;
        }
    }
}
