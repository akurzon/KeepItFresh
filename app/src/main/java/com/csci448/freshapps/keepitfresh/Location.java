package com.csci448.freshapps.keepitfresh;

import com.orm.SugarRecord;

import java.util.ArrayList;
import java.util.List;

// had to create a wrapper to let it extend sugarRecord and become useful
public class Location extends SugarRecord {
    private String name;

    public Location(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static List<Location> getLocations() {
        return Location.listAll(Location.class);
//        return Location.find(Location.class, null, null);
    }

    public static List<String> getLocationsAsStrings() {
        List<String> locations = new ArrayList<>();
        for (Location location : getLocations()) {
            locations.add(location.name);
        }
        return locations;
    }
}
