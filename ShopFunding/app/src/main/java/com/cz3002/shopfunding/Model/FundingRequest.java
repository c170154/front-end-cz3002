package com.cz3002.shopfunding.Model;

public class FundingRequest {
    private String mProductName;
    private String mDescription;
    private String mUrl;
    private float mGoal;
    private String mCreationDate;
    private float mProgress = 0;

    public FundingRequest(String productName, String description, String creationDate, String url, float goal) {
        mProductName = productName;
        mDescription = description;
        mCreationDate = creationDate;
        mUrl = url;
        mGoal = goal;
    }

    public String getProductName() {
        return mProductName;
    }

    public String getCreationDate()
    {
        return mCreationDate;
    }

    public String getmDescription() {
        return mDescription;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public float getProgress()
    {
        return mProgress;
    }

    public float getGoal()
    {
        return mGoal;
    }
}
