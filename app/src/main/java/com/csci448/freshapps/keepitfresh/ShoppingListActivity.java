package com.csci448.freshapps.keepitfresh;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * This class will hold data for items in our shopping list.
 */
public class ShoppingListActivity extends AppCompatActivity {

    private RecyclerView mShoppingRecyclerView;
    private ListAdapter mAdapter;

    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_shopping_list);

        mShoppingRecyclerView = (RecyclerView) findViewById(R.id.shopping_recycler_view);
        //in case we have to turn this into a fragment
        mShoppingRecyclerView.setLayoutManager(new LinearLayoutManager(ShoppingListActivity.this));

        updateUI();
    }

    private void updateUI() {
        StoredItems storedItems = StoredItems.getInstance(this);
        List<Item> items = storedItems.getInstance(this).getShoppingList();

        if (mAdapter == null) {
            mAdapter = new ListAdapter(items);
            mShoppingRecyclerView.setAdapter(mAdapter);
        }
        else {
            mAdapter.updateItems(items);
        }
    }

    private class ListHolder extends RecyclerView.ViewHolder {

        private Item mItem;

        private TextView mName;
        private CheckBox mChecked;

        public ListHolder(View v) {
            super(v);

            mName = (TextView) v.findViewById(R.id.shopping_list_item_name);
            mChecked = (CheckBox) v.findViewById(R.id.shopping_list_item_checkbox);
        }

        public void bindItem(Item item) {
            mItem = item;
            mName.setText(mItem.getName());
            mChecked.setChecked(mItem.isChecked());
        }
    }

    private class ListAdapter extends RecyclerView.Adapter<ListHolder> {
        private List<Item> mItems;

        public ListAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public ListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            //find context for shopping list activity
            LayoutInflater inflater = LayoutInflater.from(ShoppingListActivity.this);
            View view = inflater.inflate(R.layout.list_item_shopping_list, parent, false);
            return new ListHolder(view);
        }

        @Override
        public void onBindViewHolder(ListHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }

        public void updateItems(List<Item> items) {
            mItems = items;
            notifyDataSetChanged();
        }
    }
}
