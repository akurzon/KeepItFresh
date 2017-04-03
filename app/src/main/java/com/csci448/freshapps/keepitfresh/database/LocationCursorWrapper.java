package com.csci448.freshapps.keepitfresh.database;

import android.database.Cursor;
import android.database.CursorWrapper;



public class LocationCursorWrapper extends CursorWrapper {

    public LocationCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public String getLocation() {
        String name = getString(getColumnIndex(ItemDbSchema.ItemTable.Cols.NAME));

        return name;
    }
}
