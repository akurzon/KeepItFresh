package com.csci448.freshapps.keepitfresh;

import com.orm.SugarRecord;

import java.util.Calendar;
import java.util.Date;


public class Item extends SugarRecord {
//    private UUID mId;
    private String name;
    private Date expirationDate, purchaseDate;
    private int quantity;
    private int location;
    private boolean onShoppingList, isChecked;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getLocation() {
        return location;
    }

    public void setLocation(int location) {
        this.location = location;
    }

    public boolean isOnShoppingList() {
        return onShoppingList;
    }

    public void setOnShoppingList(boolean onShoppingList) {
        this.onShoppingList = onShoppingList;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }


    /**
     * testing constructor generates a random expire date and purchase date
     * @param name is the item name
     */
    public Item(String name) {
        this.name = name;
        expirationDate = generateRandomDate(2017, 2018);
        purchaseDate = generateRandomDate(2015, 2016);
//        mId = UUID.randomUUID();
    }

    /**
     * item constructor
     * @param name is the item name
     * @param expirationDate is the item's expiry date
     * @param purchaseDate is the item's purchase date
     */
    public Item(String name, Date expirationDate, Date purchaseDate) {
        this.name = name;
        this.expirationDate = expirationDate;
        this.purchaseDate = purchaseDate;
//        mId = UUID.randomUUID();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setExpirationDate(Date expirationDate) {
        this.expirationDate = expirationDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

    public String getName() {
        return name;
    }

    public Date getExpirationDate() {
        return expirationDate;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

//    public UUID getId() {
//        return mId;
//    }

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
