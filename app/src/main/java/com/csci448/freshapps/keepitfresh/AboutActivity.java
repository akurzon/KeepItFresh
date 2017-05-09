package com.csci448.freshapps.keepitfresh;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutCompat;

public class AboutActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        return new AboutFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
