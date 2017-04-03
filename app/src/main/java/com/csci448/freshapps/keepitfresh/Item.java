package com.csci448.freshapps.keepitfresh;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class Item {
    private UUID mId;
    private String mName;
    private Date mExpirationDate, mPurchaseDate;
    private int mQuantity;
    private String mLocation;
    private boolean mOnShoppingList, mIsChecked;

    public int getQuantity() {
        return mQuantity;
    }

    public void setQuantity(int quantity) {
        this.mQuantity = quantity;
    }

    public String getLocation() {
        return mLocation;
    }

    public void setLocation(String location) {
        mLocation = location;
    }

    public boolean isOnShoppingList() {
        return mOnShoppingList;
    }

    public void setOnShoppingList(boolean onShoppingList) {
        this.mOnShoppingList = onShoppingList;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public void setChecked(boolean checked) {
        mIsChecked = checked;
    }


    public Item() {
        this("");
        mId = UUID.randomUUID();
    }

    public Item(UUID id) {
        mId = id;

    }
    /**
     * constructor will set default values for everything other than mName
     * @param name is the item mName
     */
    public Item(String name) {
        this.mName = name;
        mExpirationDate = new Date();
        mPurchaseDate = new Date();
        mQuantity = 0;
        mLocation = null;
        mOnShoppingList = false;
        mIsChecked = false;
    }

    /**
     * item constructor
     * @param name is the item mName
     * @param expirationDate is the item's expiry date
     * @param purchaseDate is the item's purchase date
     */
    public Item(String name, Date expirationDate, Date purchaseDate) {
        this.mName = name;
        this.mExpirationDate = expirationDate;
        this.mPurchaseDate = purchaseDate;
//        mId = UUID.randomUUID();
    }

    public void setName(String name) {
        this.mName = name;
    }

    public void setExpirationDate(Date expirationDate) {
        this.mExpirationDate = expirationDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.mPurchaseDate = purchaseDate;
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

    public void update(Item item) {
        this.mName = item.mName;
        this.mExpirationDate = item.mExpirationDate;
        this.mPurchaseDate = item.mPurchaseDate;
        this.mQuantity = item.mQuantity;
        this.mLocation = item.mLocation;
        this.mOnShoppingList = item.mOnShoppingList;
        this.mIsChecked = item.mIsChecked;
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
