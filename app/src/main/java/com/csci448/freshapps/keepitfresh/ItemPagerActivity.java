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

public class ItemPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_ID = "item_id";
    private static final String EXTRA_SORT_OPTION = "sort_option";
    private static final String EXTRA_ITEM_LIST = "item_list";
    private static final String EXTRA_LOCATION = "location";
    private static final String EXTRA_NEW_ITEM = "new_item";

    private ViewPager mViewPager;
    private List<Item> mItems;
    private boolean mIsNewItem;

    /**
     * creates an intent for this activity which will contain an item ID, a list of items, and a
     * boolean value for whether or not this activity has been started for a new item or an existing
     * item
     * @param context is the requesting context
     * @param itemId is the item ID
     * @param items is the list of items in which the item ID sits
     * @param isNewItem is whether or not the item is new or old and edited
     * @return an Intent to start this activity
     */
    public static Intent newIntent(Context context, UUID itemId,
                                   ArrayList<Item> items, boolean isNewItem) {
        Intent intent = new Intent(context, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        intent.putExtra(EXTRA_ITEM_LIST, items);
        intent.putExtra(EXTRA_NEW_ITEM, isNewItem);

        return intent;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_item_pager);

        //this could be null, if we are calling a new item to be made
        UUID itemId = (UUID) getIntent().getSerializableExtra(EXTRA_ITEM_ID);
        mIsNewItem = (boolean) getIntent().getSerializableExtra(EXTRA_NEW_ITEM);
        mItems = getIntent().getParcelableArrayListExtra(EXTRA_ITEM_LIST);
        mViewPager = (ViewPager) findViewById(R.id.activity_item_pager_view_pager);

        FragmentManager fragmentManager = getSupportFragmentManager();
        /**
         * If we are creating a new item, we don't want a pager, we just want to open a single item detail
         */
        if (!mIsNewItem) {
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
        else {
            //creating a new item, so we use null item id from
            fragmentManager.beginTransaction()
                    .replace(android.R.id.content, ItemDetailFragment.newInstance(itemId))
                    .commit();
        }
    }
}
