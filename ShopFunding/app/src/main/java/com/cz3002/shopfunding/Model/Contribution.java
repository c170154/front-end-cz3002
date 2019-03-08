package com.cz3002.shopfunding.Model;

public class Contribution {
    private int mID;
    private String mContributor;
    private float mAmount;

    public Contribution(int id, String contributor, float amount) {
        mID = id;
        mContributor = contributor;
        mAmount = amount;
    }

    public int getID() {
        return mID;
    }

    public String getUsername() {
        return mContributor;
    }

    public float getAmount() {
        return mAmount;
    }
}
