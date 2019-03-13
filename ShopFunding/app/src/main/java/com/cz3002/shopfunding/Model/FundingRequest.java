package com.cz3002.shopfunding.Model;

public class FundingRequest {
    private int mID;
    private String mAuthor;
    private String mProductName;
    private String mDescription;
    private String mUrl;
    private float mGoal;
    private String mCreationDate;
    private float mRemainingFund = 0;

    public FundingRequest(
            int id, String author, String productName, String description, String creationDate, String url,
            float goal, float remainingFund
    ) {
        mID = id;
        mAuthor = author;
        mProductName = productName;
        mDescription = description;
        mCreationDate = creationDate;
        mUrl = url;
        mGoal = goal;
        mRemainingFund = remainingFund;
    }

    public int getID() {
        return mID;
    }

    public String getAuthor() {
        return mAuthor;
    }

    public String getProductName() {
        return mProductName;
    }

    public String getCreationDate()
    {
        return mCreationDate;
    }

    public String getDescription() {
        return mDescription;
    }

    public String getUrl()
    {
        return mUrl;
    }

    public float getmRemainingFund()
    {
        return mRemainingFund;
    }

    public float getGoal()
    {
        return mGoal;
    }
}
