package com.csci448.freshapps.keepitfresh;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;


public class Item implements Parcelable {
    private UUID mId;
    private String mName;
    private Date mExpirationDate, mPurchaseDate;
    private int mQuantity;
    private String mLocation;
    private boolean mOnShoppingList, mIsChecked;

    protected Item(Parcel in) {
        mId = UUID.fromString(in.readString());
        mName = in.readString();
        mExpirationDate = new Date(in.readLong());
        mPurchaseDate = new Date(in.readLong());
        mQuantity = in.readInt();
        mLocation = in.readString();
        mOnShoppingList = in.readByte() != 0;
        mIsChecked = in.readByte() != 0;
    }

    public static final Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId.toString());
        dest.writeString(mName);
        dest.writeLong(mExpirationDate.getTime());
        dest.writeLong(mPurchaseDate.getTime());
        dest.writeInt(mQuantity);
        dest.writeString(mLocation);
        dest.writeByte((byte) (mOnShoppingList ? 1 : 0));
        dest.writeByte((byte) (mIsChecked ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return this.hashCode();
    }

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
        mName = "";
        mExpirationDate = new Date(System.currentTimeMillis());
        mPurchaseDate = new Date(System.currentTimeMillis());
        mQuantity = 1;
        mLocation = null;
        mOnShoppingList = false;
        mIsChecked = false;
        mId = UUID.randomUUID();
    }
    public Item(UUID id) {
//        this("");
        mName = "";
        mExpirationDate = new Date(System.currentTimeMillis());
        mPurchaseDate = new Date(System.currentTimeMillis());
        mQuantity = 0;
        mLocation = null;
        mOnShoppingList = false;
        mIsChecked = false;
        mId = id;

    }

    /**
     * constructor will set default values for everything other than mName
     * @param name is the item mName
     */
    public Item(String name) {
        this.mName = name;
        mExpirationDate = new Date(System.currentTimeMillis());
        mPurchaseDate = new Date(System.currentTimeMillis());
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
        mId = UUID.randomUUID();
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
