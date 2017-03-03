package com.csci448.freshapps.keepitfresh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

public class StoredItems {
    private static StoredItems sStoredItems;
    private List<Item> mItemList;
    private List<Item> mShoppingList;

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

    public List<Item> getShoppingList() {
        return mShoppingList;
    }

    public void addItemToItemList(Item i) {
        mItemList.add(i);
    }

    public void addItemToShoppingList(Item i) {
        mShoppingList.add(i);
    }

    public Item getItem(UUID id) {
        for (Item item : mItemList) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        for (Item item : mShoppingList) {
            if (item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public List<Item> sortByName(ItemType type) {
        List<Item> sortedList;
        if (type.equals(ItemType.STORED)) {
            sortedList = new ArrayList<>(mItemList);
        }
        else {
            sortedList = new ArrayList<>(mShoppingList);
        }
        Collections.sort(sortedList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.getName().compareTo(item2.getName());
            }
        });
        return sortedList;
    }

    public List<Item> sortByExpirationDate(ItemType type) {
        List<Item> sortedList;
        if (type.equals(ItemType.STORED)) {
            sortedList = new ArrayList<>(mItemList);
        }
        else {
            sortedList = new ArrayList<>(mShoppingList);
        }
        Collections.sort(sortedList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.getExpirationDate().compareTo(item2.getExpirationDate());
            }
        });
        return sortedList;
    }

    public List<Item> sortByPurchaseDate(ItemType type) {
        List<Item> sortedList;
        if (type.equals(ItemType.STORED)) {
            sortedList = new ArrayList<>(mItemList);
        }
        else {
            sortedList = new ArrayList<>(mShoppingList);
        }
        Collections.sort(sortedList, new Comparator<Item>() {
            @Override
            public int compare(Item item1, Item item2) {
                return item1.getPurchaseDate().compareTo(item2.getPurchaseDate());
            }
        });
        return sortedList;
    }
}
