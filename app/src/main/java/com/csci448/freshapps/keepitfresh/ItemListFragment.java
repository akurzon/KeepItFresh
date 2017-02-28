package com.csci448.freshapps.keepitfresh;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class ItemListFragment extends Fragment {
    private RecyclerView mCrimeRecyclerView;
    private ItemAdapter mItemAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);

        mCrimeRecyclerView = (RecyclerView) view.findViewById(R.id.items_list_recycler_view);
        mCrimeRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));


        updateUI();

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_items_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch ((item.getItemId())) {
            case R.id.menu_item_new_item:
                Toast.makeText(getActivity(), "Added a new item", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_filter_by:
                Toast.makeText(getActivity(), "will filter", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menu_item_settings:
                Toast.makeText(getActivity(), "will open settings", Toast.LENGTH_SHORT).show();
                Intent i = SettingsActivity.newIntent(getActivity());
                startActivity(i);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        StoredItems storedItems = StoredItems.getInstance();
        List<Item> items = storedItems.getItemList();
        if (mItemAdapter == null) {
            mItemAdapter = new ItemAdapter(items);
            mCrimeRecyclerView.setAdapter(mItemAdapter);
        }
        else {
            mItemAdapter.notifyDataSetChanged();
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mItemNameTextView;
        private Item mItem;

        public ItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mItemNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_grocery_item_name_text_view);
        }

        public void bindItem(Item item) {
            mItem = item;
            mItemNameTextView.setText(mItem.getName());
        }

        @Override
        public void onClick(View v) {
            Intent intent = ItemPagerActivity.newIntent(getActivity(), mItem.getId());
            startActivity(intent);
        }
    }

    private class ItemAdapter extends RecyclerView.Adapter<ItemHolder> {
        private List<Item> mItems;

        public ItemAdapter(List<Item> items) {
            mItems = items;
        }

        @Override
        public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            View view = inflater.inflate(R.layout.list_item_grocery_item, parent, false);

            return new ItemHolder(view);
        }

        @Override
        public void onBindViewHolder(ItemHolder holder, int position) {
            Item item = mItems.get(position);
            holder.bindItem(item);
        }

        @Override
        public int getItemCount() {
            return mItems.size();
        }
    }
}
