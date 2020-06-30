package com.example.android.quakereport;

public class Specification {
    private Double mMagnitude;
    private String mLocation;
    private String mDate;
    private long mTimeinMilliSecond;
    private String Url;

    public Specification(Double mMagnitude, String mLocation, long mTimeinMilliSecond,String Url) {
        this.mMagnitude = mMagnitude;
        this.mLocation = mLocation;
        this.mTimeinMilliSecond=mTimeinMilliSecond;
        this.Url=Url;
    }


    public String getUrl() {
        return Url;
    }

    public long getmTimeinMilliSecond() {
        return mTimeinMilliSecond;
    }

    public Double getmMagnitude() {
        return mMagnitude;
    }

    public String getmLocation() {
        return mLocation;
    }

    public String getmDate() {
        return mDate;
    }
}
