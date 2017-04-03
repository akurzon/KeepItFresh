package com.csci448.freshapps.keepitfresh.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.csci448.freshapps.keepitfresh.Item;

import java.util.Date;
import java.util.UUID;

public class ItemCursorWrapper extends CursorWrapper {

    public ItemCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Item getItem() {
        String uuidString = getString(getColumnIndex(ItemDbSchema.ItemTable.Cols.UUID));
        String name = getString(getColumnIndex(ItemDbSchema.ItemTable.Cols.NAME));
        long expireDate = getLong(getColumnIndex(ItemDbSchema.ItemTable.Cols.EXPIRE));
        long purchaseDate = getLong(getColumnIndex(ItemDbSchema.ItemTable.Cols.PURCHASE));
        int quantity = getInt(getColumnIndex(ItemDbSchema.ItemTable.Cols.QUANTITY));
        String location = getString(getColumnIndex(ItemDbSchema.ItemTable.Cols.LOCATION));
        int onShoppingList = getInt(getColumnIndex(ItemDbSchema.ItemTable.Cols.ON_SHOPPING));
        int isChecked = getInt(getColumnIndex(ItemDbSchema.ItemTable.Cols.IS_CHECKED));

        Item item = new Item(UUID.fromString(uuidString));
        item.setName(name);
        item.setExpirationDate(new Date(expireDate));
        item.setPurchaseDate(new Date(purchaseDate));
        item.setQuantity(quantity);
        item.setLocation(location);
        item.setOnShoppingList(onShoppingList != 0);
        item.setChecked(isChecked != 0);

        return item;
    }
}
