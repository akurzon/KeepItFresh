package com.csci448.freshapps.keepitfresh;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

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

    /**
     * returns the instance of the singleton
     * @param context
     * @return
     */
    public static StoredItems getInstance(Context context) {
        if (sStoredItems == null) {
            sStoredItems = new StoredItems(context);
        }
        return sStoredItems;
    }

    /**
     * private constructor to adhere to singleton design patter
     * @param context is requesting context
     */
    private StoredItems(Context context) {
        mContext = context.getApplicationContext();
        mDatabase = new ItemDbHelper(mContext).getWritableDatabase();
        pullListsFromDb();

    }

    /**
     * this will populate the sub-lists from the database
     */
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

    /**
     * adds the string to the table of existing locations
     * @param location
     */
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

    /**
     * creates an ItemCursorWrapper to navigate the database with the given where-clause
     * @param whereClause is a string describing the where clause
     * @param whereArgs is a string array containing the arguments that fill in wildcards for the
     *                  where clause
     * @return an ItemCursorWrapper pointing to the queried subset of the database
     */
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

    /**
     * wraps an item into a ContentValues object to insert it into the database
     * @param item is the item to be wrapped up
     * @return a ContentValues object containing the item's member variables
     */
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

    // getters
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

    /**
     * queries the database for an item with the given ID
     * @param id the ID with which to search the database
     * @return an item if one is found, null otherwise
     */
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

    /**
     * adds an item to the database
     * @param item is the item to be added
     */
    public void addItem(Item item) {
        ContentValues values = getContentValues(item);
        mDatabase.insert(ItemDbSchema.ItemTable.NAME, null, values);
    }

    /**
     * deletes an item from the database
     * @param item is the item to be deleted
     */
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

    /**
     * filters the item list by a given location
     * @param items is the list of items to be filtered
     * @param location is the location
     * @return a filtered list of items
     */
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

    /**
     * sorts items by name and filters by location
     * @param type defines whether the item is in the shopping list or item list
     * @param location is the location
     * @return a sorted and filtered item list
     */
    public List<Item> sortByName(ItemType type, String location) {
        return sortBy(type, "name", location);
    }

    /**
     * sorts items by expiration date and filters by location
     * @param type defines whether the item is in the shopping list or item list
     * @param location is the location
     * @return a sorted and filtered item list
     */
    public List<Item> sortByExpirationDate(ItemType type, String location) {
        return sortBy(type, "expirationDate", location);
    }

    /**
     * sorts items by purchase date and filters by location
     * @param type defines whether the item is in the shopping list or item list
     * @param location is the location
     * @return a sorted and filtered item list
     */
    public List<Item> sortByPurchaseDate(ItemType type, String location) {
        return sortBy(type, "purchaseDate", location);
    }


    /**
     * queries database for all items that are in a given location
     * @param location is the location
     * @return a list of items in the given location
     */
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

    public void clearHistory() {
        Toast.makeText(mContext, "everything is gone", Toast.LENGTH_SHORT).show();
        mDatabase.delete(ItemDbSchema.ItemTable.NAME, null, null);
    }

    public List<String> getHistoryListNames() {
        ArrayList<String> names = new ArrayList<>();
        for (Item item : mHistoryList) {
            names.add(item.getName());
        }
        return names;
    }
}
