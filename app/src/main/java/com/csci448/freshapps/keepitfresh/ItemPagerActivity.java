package com.csci448.freshapps.keepitfresh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Nate on 2/27/2017.
 */

public class ItemPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_ID = "item_id";
    private static final String EXTRA_SORT_OPTION = "sort_option";
    private static final String EXTRA_ITEM_LIST = "item_list";
    private static final String EXTRA_LOCATION = "location";

    private ViewPager mViewPager;
    private List<Item> mItems;
    private SortOptions mSortOption;
    private String mLocation;

    public static Intent newIntent(Context context, UUID itemId,
                                   ArrayList<Item> items) {
        Intent intent = new Intent(context, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        intent.putExtra(EXTRA_ITEM_LIST, items);

        return intent;
    }

    public static Intent newIntent(Context context, UUID itemId, SortOptions sortOption,
                                   String location) {
        Intent intent = new Intent(context, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        intent.putExtra(EXTRA_SORT_OPTION, sortOption);
        intent.putExtra(EXTRA_LOCATION, location);

        return intent;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_item_pager);

        UUID itemId = (UUID) getIntent().getSerializableExtra(EXTRA_ITEM_ID);
        mItems = getIntent().getParcelableArrayListExtra(EXTRA_ITEM_LIST);
//        mSortOption = (SortOptions)getIntent().getSerializableExtra(EXTRA_SORT_OPTION);
//        mLocation = getIntent().getStringExtra(EXTRA_LOCATION);
        mViewPager = (ViewPager) findViewById(R.id.activity_item_pager_view_pager);

//        switch (mSortOption) {
//            case EXPIRE:
//                mItems = StoredItems.getInstance(this).sortByExpirationDate(ItemType.STORED);
//                break;
//            case NAME:
//                mItems = StoredItems.getInstance(this).sortByName(ItemType.STORED);
//                break;
//            case PURCHASE:
//                mItems = StoredItems.getInstance(this).sortByPurchaseDate(ItemType.STORED);
//                break;
//        }

        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {
            @Override
            public Fragment getItem(int position) {
                Item item = mItems.get(position);
                return ItemDetailFragment.newInstance(item.getId());
            }

            @Override
            public int getCount() {
                return mItems.size();
            }
        });

        for (int i = 0; i < mItems.size(); i++) {
            if (mItems.get(i).getId().equals(itemId)) {
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
