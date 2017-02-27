package com.csci448.freshapps.keepitfresh;

import java.util.Date;

public class Item {
    private String mName;
    private Date mExpirationDate, mPurchaseDate;

    /**
     * testing constructor
     * @param name is the item name
     */
    public Item(String name) {
        mName = name;
        mExpirationDate = new Date();
        mPurchaseDate = new Date();
    }

    /**
     * item constructor
     * @param name is the item name
     * @param expirationDate is the item's expiry date
     * @param purchaseDate is the item's purchase date
     */
    public Item(String name, Date expirationDate, Date purchaseDate) {
        mName = name;
        mExpirationDate = expirationDate;
        mPurchaseDate = purchaseDate;
    }

    public void setName(String name) {
        mName = name;
    }

    public void setExpirationDate(Date expirationDate) {
        mExpirationDate = expirationDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        mPurchaseDate = purchaseDate;
    }

    public String getName() {
        return mName;
    }

    public Date getExpirationDate() {
        return mExpirationDate;
    }

    public Date getPurchaseDate() {
        return mPurchaseDate;
    }
}
