package com.csci448.freshapps.keepitfresh;

import java.util.Date;
import java.util.UUID;

public class Item {
    private String mName;
    private Date mExpirationDate, mPurchaseDate;
    private UUID mId;

    /**
     * testing constructor
     * @param name is the item name
     */
    public Item(String name) {
        mName = name;
        mExpirationDate = new Date();
        mPurchaseDate = new Date();
        mId = UUID.randomUUID();
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
        mId = UUID.randomUUID();
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

    public UUID getId() {
        return mId;
    }
}
