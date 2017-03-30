package com.csci448.freshapps.keepitfresh;

import android.support.v4.app.Fragment;

/**
 * Created by nate on 3/29/17.
 */

public class ItemEditActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        //TODO: 3/29/17 call the new instance function, I don't believe my implementation works because of database changes
        //return ItemEditFragment.newInstance();
        return new ItemEditFragment();
    }
}
