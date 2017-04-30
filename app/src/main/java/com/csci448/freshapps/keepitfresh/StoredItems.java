package com.csci448.freshapps.keepitfresh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.csci448.freshapps.keepitfresh.database.ItemCursorWrapper;
import com.csci448.freshapps.keepitfresh.database.ItemDbHelper;
import com.csci448.freshapps.keepitfresh.database.ItemDbSchema;
import com.csci448.freshapps.keepitfresh.database.LocationCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoredItems {
    private static StoredItems sStoredItems;
    private List<Item> mItemList;
    private List<Item> mShoppingList;
    private List<Item> mHistoryList;
    private List<String> mLocations;
    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static StoredItems getInstance(Context context) {
        if (sStoredItems == null) {
            sStoredItems = new StoredItems(context);
        }
        return sStoredItems;
    }

    private StoredItems(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ItemDbHelper(mContext).getWritableDatabase();
        pullListsFromDb();

    }

    private void pullListsFromDb() {

        mHistoryList = new ArrayList<>();
        mItemList = new ArrayList<>();
        mShoppingList = new ArrayList<>();
        mLocations = new ArrayList<>();

        ItemCursorWrapper itemCursor = queryItems(null, null);
        itemCursor.moveToFirst();
        while(!itemCursor.isAfterLast()) {
            mHistoryList.add(itemCursor.getItem());
            itemCursor.moveToNext();
        }
        itemCursor.close();

        for (Item item : mHistoryList) {
            if (item.isOnShoppingList()) {
                mShoppingList.add(item);
            }
            if (item.getQuantity() > 0) {
                mItemList.add(item);
            }
        }

        LocationCursorWrapper locationCursor = queryLocations(null, null);
        locationCursor.moveToFirst();
        while(!locationCursor.isAfterLast()) {
            mLocations.add(locationCursor.getLocation());
            locationCursor.moveToNext();
        }
        itemCursor.close();

        if (mLocations.size() == 0) {
            String fridge = "fridge";
            String pantry = "pantry";
            String freezer = "freezer";

            mLocations.add(fridge);
            mLocations.add(pantry);
            mLocations.add(freezer);

            for (String location : mLocations) {
                addLocation(location);
            }
        }
    }

    public void addLocation(String location) {
        ContentValues values = new ContentValues();
        values.put(ItemDbSchema.LocationTable.Cols.NAME, location);
        mDatabase.insert(ItemDbSchema.LocationTable.NAME, null, values);
    }

    private LocationCursorWrapper queryLocations(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ItemDbSchema.LocationTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new LocationCursorWrapper(cursor);
    }

    private ItemCursorWrapper queryItems(String whereClause, String[] whereArgs) {

        Cursor cursor = mDatabase.query(
                ItemDbSchema.ItemTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null // orderBy
        );

        return new ItemCursorWrapper(cursor);
    }

    private static ContentValues getContentValues(Item item) {
        ContentValues values = new ContentValues();
        values.put(ItemDbSchema.ItemTable.Cols.UUID, item.getId().toString());
        values.put(ItemDbSchema.ItemTable.Cols.NAME, item.getName());
        values.put(ItemDbSchema.ItemTable.Cols.EXPIRE, item.getExpirationDate().getTime());
        values.put(ItemDbSchema.ItemTable.Cols.PURCHASE, item.getPurchaseDate().getTime());
        values.put(ItemDbSchema.ItemTable.Cols.LOCATION, item.getLocation());
        values.put(ItemDbSchema.ItemTable.Cols.QUANTITY, item.getQuantity());
        values.put(ItemDbSchema.ItemTable.Cols.ON_SHOPPING, item.isOnShoppingList() ? 1 : 0);
        values.put(ItemDbSchema.ItemTable.Cols.IS_CHECKED, item.isChecked() ? 1 : 0);

        return values;
    }

    public List<String> getLocations() {
        pullListsFromDb();
        return mLocations;
    }

    public List<Item> getItemList() {
        pullListsFromDb();
        return mItemList;
    }

    public List<Item> getShoppingList() {
        pullListsFromDb();
        return mShoppingList;
    }

    public List<Item> getHistoryList() {
        pullListsFromDb();
        return mHistoryList;
    }

    public void addItemToItemList(Item i) {


        // TODO: 4/2/2017 replace

        mItemList.add(i);
    }

    public void addItemToShoppingList(Item i) {
        // TODO: 4/2/2017 replace

        mShoppingList.add(i);
    }

    public Item getItem(UUID id) {
        ItemCursorWrapper cursor = queryItems(
                ItemDbSchema.ItemTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getItem();

        } finally {
            cursor.close();
        }
    }

    public void addItem(Item item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemDbSchema.ItemTable.NAME, null, values);
    }

    public void deleteItem(Item item) {
        String uuidString = item.getId().toString();
        mDatabase.delete(
                ItemDbSchema.ItemTable.NAME,
                ItemDbSchema.ItemTable.Cols.UUID + " = ?",
                new String[] {uuidString}
        );
    }

    public void updateItem(Item item) {
        String uuidString = item.getId().toString();
        ContentValues values = getContentValues(item);
        String whereClause = ItemDbSchema.ItemTable.Cols.UUID + " = ?";
        String[] whereArgs = new String[] {uuidString};
        mDatabase.update(ItemDbSchema.ItemTable.NAME, values, whereClause, whereArgs);

        getItem(item.getId()).update(item);
    }

    private ItemCursorWrapper querySortedItems(String whereClause, String[] whereArgs,
                                               String sortCol) {
        Cursor cursor = mDatabase.query(
                ItemDbSchema.ItemTable.NAME,
                null, // Columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                sortCol // orderBy
        );

        return new ItemCursorWrapper(cursor);
    }

    private List<Item> sortBy(ItemType type, String sortCol, String location) {
        List<Item> sortedList = new ArrayList<>();
        String orderBy = sortCol;
        String whereClause;
        String[] whereArgs;

        // TODO: 4/3/17 figure out why using whereArgs breaks the query
        if (type.equals(ItemType.CART)) {
            whereClause = "onShoppingList = 1";
            whereArgs = null;
        }
        else {
            whereClause = "quantity > 0";
            whereArgs = null;

        }

        ItemCursorWrapper cursor = querySortedItems(whereClause, whereArgs, orderBy);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()) {
            sortedList.add(cursor.getItem());
            cursor.moveToNext();
        }
        cursor.close();

        if (!location.equals("all")) {
            return filterByLocation(sortedList, location);
        }

        return sortedList;
    }

    private List<Item> filterByLocation(List<Item> items, String location) {
        ArrayList<Item> filteredItems = new ArrayList<>();
        for (Item item : items) {
            if (item.getLocation() == null) continue;
            if (item.getLocation().equals(location)) {
                filteredItems.add(item);
            }
        }
        return filteredItems;
    }

    public List<Item> sortByName(ItemType type, String location) {
        return sortBy(type, "name", location);
    }

    public List<Item> sortByExpirationDate(ItemType type, String location) {
        return sortBy(type, "expirationDate", location);
    }

    public List<Item> sortByPurchaseDate(ItemType type, String location) {
        return sortBy(type, "purchaseDate", location);
    }

    public List<Item> getItemsFromLocation(String location) {
        ItemCursorWrapper itemCursor = queryItems("location = ?", new String[] {location});

        ArrayList<Item> items = new ArrayList<>();
        itemCursor.moveToFirst();
        while(!itemCursor.isAfterLast()) {
            items.add(itemCursor.getItem());
            itemCursor.moveToNext();
        }
        itemCursor.close();

        return items;
    }

}
