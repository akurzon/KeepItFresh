package com.csci448.freshapps.keepitfresh;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class StoredItems {
    private static StoredItems sStoredItems;
    private List<Item> mItemList;
    private List<Item> mShoppingList;
    private List<Item> mHistoryList;

    public static StoredItems getInstance() {
        if (sStoredItems == null) {
            sStoredItems = new StoredItems();
        }
         return sStoredItems;
    }

    private StoredItems() {
        pullListsFromDb();
    }

    private void pullListsFromDb() {
        List<Item> mHistoryList = Item.listAll(Item.class);
        mItemList = new ArrayList<>();
        mShoppingList = new ArrayList<>();

        for (Item item : mHistoryList) {
            if (item.isOnShoppingList()) {
                mShoppingList.add(item);
            }
            if (item.getQuantity() > 0) {
                mItemList.add(item);
            }
        }
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
        i.save();
        mItemList.add(i);
    }

    public void addItemToShoppingList(Item i) {
        i.save();
        mShoppingList.add(i);
    }

    public Item getItem(long id) {
        return Item.findById(Item.class, id);
//        for (Item item : mItemList) {
//            if (item.getId().equals(id)) {
//                return item;
//            }
//        }
//        for (Item item : mShoppingList) {
//            if (item.getId().equals(id)) {
//                return item;
//            }
//        }
//        return null;
    }

    public List<Item> sortByName(ItemType type) {
        String orderBy = "name";
        String whereClause;
        String whereArgs;

        if (type.equals(ItemType.CART)) {
            whereClause = "onShoppingList = ?";
            whereArgs = "true";
        }
        else {
            whereClause = "quantity > ?";
            whereArgs = "0";
        }

        return Item.find(Item.class, whereClause, whereArgs, null, orderBy, null);
//
//        List<Item> sortedList;
//        if (type.equals(ItemType.STORED)) {
//            sortedList = new ArrayList<>(mItemList);
//        }
//        else {
//            sortedList = new ArrayList<>(mShoppingList);
//        }
//        Collections.sort(sortedList, new Comparator<Item>() {
//            @Override
//            public int compare(Item item1, Item item2) {
//                return item1.getName().compareTo(item2.getName());
//            }
//        });
//        return sortedList;
    }

    public List<Item> sortByExpirationDate(ItemType type) {

        String orderBy = "expirationDate";
        String whereClause;
        String whereArgs;

        if (type.equals(ItemType.CART)) {
            whereClause = "where onShoppingList = ?";
            whereArgs = "true";
        }
        else {
            whereClause = "where quantity > ?";
            whereArgs = "0";
        }

        return Item.findWithQuery(Item.class,
                "select * from Item " + whereClause + "order by ?", whereArgs, orderBy);

//        return Item.find(Item.class, whereClause, whereArgs, null, orderBy, null);

//        List<Item> sortedList;
//        if (type.equals(ItemType.STORED)) {
//            sortedList = new ArrayList<>(mItemList);
//        }
//        else {
//            sortedList = new ArrayList<>(mShoppingList);
//        }
//        Collections.sort(sortedList, new Comparator<Item>() {
//            @Override
//            public int compare(Item item1, Item item2) {
//                return item1.getExpirationDate().compareTo(item2.getExpirationDate());
//            }
//        });
//        return sortedList;
    }

    public List<Item> sortByPurchaseDate(ItemType type) {

        String orderBy = "purchaseDate";
        String whereClause;
        String whereArgs;

        if (type.equals(ItemType.CART)) {
            whereClause = "where onShoppingList = ?";
            whereArgs = "true";
        }
        else {
            whereClause = "where quantity > ?";
            whereArgs = "0";
        }

        return Item.findWithQuery(Item.class,
                "select * from Item " + whereClause + "order by ?", whereArgs, orderBy);
//        return Item.find(Item.class, whereClause, whereArgs, null, orderBy, null);
//        List<Item> sortedList;
//        if (type.equals(ItemType.STORED)) {
//            sortedList = new ArrayList<>(mItemList);
//        }
//        else {
//            sortedList = new ArrayList<>(mShoppingList);
//        }
//        Collections.sort(sortedList, new Comparator<Item>() {
//            @Override
//            public int compare(Item item1, Item item2) {
//                return item1.getPurchaseDate().compareTo(item2.getPurchaseDate());
//            }
//        });
//        return sortedList;
    }
}
