package com.csci448.freshapps.keepitfresh;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;


public class ItemListActivity extends SingleFragmentActivity {

    private ListView mDrawerList;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;

    private String mActivityTitle;
    private List<String> mLocations;
    private List<String> mStores;



    @Override
    protected Fragment createFragment() {
        return new ItemListFragment();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items);

        ExpirationService.setServiceAlarm(this);

        mLocations = StoredItems.getInstance(this).getLocations();
        mLocations.add(0, getString(R.string.all));

        /**
         * This code is for the Navigation drawer
         */
        final NavigationView navView = (NavigationView) findViewById(R.id.main_navigation);
        Menu menu = navView.getMenu();
        SubMenu storedLocations = menu.addSubMenu("Stored Food");
        for (String location : mLocations) {
            storedLocations.add(location);
        }
        SubMenu stores = menu.addSubMenu("Shopping Lists");
        //for now stores is does not have sub categories
//        for (String store : mStores) {
//            stores.add(store);
//        }
        stores.add(getString(R.string.shopping_list));

        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                //check if the menu item hit was the shopping list
                if (menuItem.getTitle() == getString(R.string.shopping_list)) {
                    Intent shoppingIntent = new Intent(navView.getContext(), ShoppingListActivity.class);
                    startActivity(shoppingIntent);
                }
                else {
                    ItemListFragment f = (ItemListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    //find the item that we want to filter by
                    for (String location : mLocations) {
                        if (menuItem.getTitle() == location) {
                            f.filterListByLocation(location);
                            break;
                        }
                    }
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    private void showToast(String string) {
        Toast.makeText(this, string, Toast.LENGTH_SHORT).show();
    }

    /**
     * Code for the side bar.
     * http://blog.teamtreehouse.com/add-navigation-drawer-android
     */
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {
            public void onDrawerOpened(View v) {
                super.onDrawerOpened(v);
                getSupportActionBar().setTitle("Navigation");
                invalidateOptionsMenu();
            }
            public void onDrawerClosed(View v) {
                super.onDrawerClosed(v);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.addDrawerListener(mDrawerToggle);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle bundle) {
        super.onPostCreate(bundle);
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration config) {
        super.onConfigurationChanged(config);
        mDrawerToggle.onConfigurationChanged(config);
    }

    public static Intent newIntent(Context context) {
        return new Intent(context, ItemListActivity.class);

    }

    /**
     * This code may become useful later
     * Any item selected will open a new fragment depending on its position
     * https://developer.android.com/training/implementing-navigation/nav-drawer.html
     */
    /*
    //Swaps fragments in the main content view
    private void selectItem(int position) {
        // Create a new fragment and specify the planet to show based on position
        Fragment fragment = new PlanetFragment();
        Bundle args = new Bundle();
        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
        fragment.setArguments(args);

        // Insert the fragment by replacing any existing fragment
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();

        // Highlight the selected item, update the title, and close the drawer
        mDrawerList.setItemChecked(position, true);
        setTitle(mPlanetTitles[position]);
        mDrawerLayout.closeDrawer(mDrawerList);
    }
    */
}
