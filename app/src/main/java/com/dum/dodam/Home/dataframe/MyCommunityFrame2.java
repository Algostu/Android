package com.dum.dodam.Home.dataframe;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MyCommunityFrame2 implements Serializable {
    public int communityID;
    @SerializedName("communityName")
    public String title;
}
