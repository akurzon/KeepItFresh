package com.csci448.freshapps.keepitfresh.database;

public class ItemDbSchema {
    public static final class ItemTable {
        public static final String NAME = "items";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String NAME = "name";
            public static final String EXPIRE = "expirationDate";
            public static final String PURCHASE = "purchaseDate";
            public static final String QUANTITY = "quantity";
            public static final String LOCATION =  "location";
            public static final String ON_SHOPPING = "onShoppingList";
            public static final String IS_CHECKED = "isChecked";
        }
    }

    public static final class LocationTable {
        public static final String NAME = "locations";

        public static final class Cols {
            public static final String NAME = "name";
        }
    }

}
