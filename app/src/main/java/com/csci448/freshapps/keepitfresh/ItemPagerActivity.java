package com.csci448.freshapps.keepitfresh;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Nate on 2/27/2017.
 */

public class ItemPagerActivity extends AppCompatActivity {
    private static final String EXTRA_ITEM_ID = "item_id";

    private ViewPager mViewPager;
    private List<Item> mItems;

    public static Intent newIntent(Context context, UUID itemId) {
        Intent intent = new Intent(context, ItemPagerActivity.class);
        intent.putExtra(EXTRA_ITEM_ID, itemId);
        return intent;
    }

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_item_pager);

        long itemId = getIntent().getLongExtra(EXTRA_ITEM_ID, 0);

        mViewPager = (ViewPager) findViewById(R.id.activity_item_pager_view_pager);

        // TODO: 3/31/17 check the bundle for sort information, and get the properly sorted list
        mItems = StoredItems.getInstance(getApplicationContext()).getItemList();
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
