package com.cz3002.shopfunding.Model;

public class Transaction {
    private String mName;
    private String mDate;
    private int mAmount;

    public Transaction(String name, String date, int amount) {
        mName = name;
        mDate = date;
        mAmount = amount;
    }

    public String getName() {
        return mName;
    }

    public String getDate() {
        return mDate;
    }

    public int getAmount()
    {
        return mAmount;
    }
}
