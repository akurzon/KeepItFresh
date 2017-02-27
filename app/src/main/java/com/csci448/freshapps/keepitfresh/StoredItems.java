package com.csci448.freshapps.keepitfresh;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class StoredItems {
    private static StoredItems sStoredItems;
    private List<Item> mItemList;

    public static StoredItems getInstance() {
        if (sStoredItems == null) {
            sStoredItems = new StoredItems();
        }
         return sStoredItems;
    }

    private StoredItems() {
        mItemList = new ArrayList<>();
        // TODO: 2/26/17 remove this shit
        for (int i = 0; i < 10; ++i) {
            mItemList.add(new Item("item: " + i));
        }
    }

    public List<Item> getItemList() {
        return mItemList;
    }

    public void addItem(Item i) {
        mItemList.add(i);
    }

    public Item getItem(UUID id) {
        for (Item item : mItemList) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }
}
