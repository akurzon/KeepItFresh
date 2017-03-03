package com.csci448.freshapps.keepitfresh;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Item {
    private String mName;
    private Date mExpirationDate, mPurchaseDate;
    private UUID mId;

    /**
     * testing constructor generates a random expire date and purchase date
     * @param name is the item name
     */
    public Item(String name) {
        mName = name;
        mExpirationDate = generateRandomDate(2017, 2018);
        mPurchaseDate = generateRandomDate(2015, 2016);
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

    private Date generateRandomDate(int startYear, int endYear) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(startYear, 1, 1);
        long startTime = calendar.getTimeInMillis();
        calendar.set(endYear, 12, 31);
        long endTime = calendar.getTimeInMillis();
        long randomTime = startTime + (long)(Math.random()*(endTime-startTime));

        calendar.setTimeInMillis(randomTime);
        return calendar.getTime();
    }
}
