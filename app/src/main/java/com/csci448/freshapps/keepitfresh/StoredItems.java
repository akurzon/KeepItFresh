package com.csci448.freshapps.keepitfresh;

import java.util.ArrayList;
import java.util.List;

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
}
