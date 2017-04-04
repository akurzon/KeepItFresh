package com.csci448.freshapps.keepitfresh;
import android.app.Activity;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ItemListFragment extends Fragment {

    private static final int REQUEST_OPTION = 0;
    private static final int REQUEST_NEW_ITEM = 1;
    private static final int REQUEST_ITEM_DETAIL = 2;
    private static final String DIALOG_OPTION = "option";
    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private SortOptions mSortOption = SortOptions.EXPIRE;
    private SimpleDateFormat mDateFormat;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.US);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle bundle) {
        View view = inflater.inflate(R.layout.fragment_items_list, container, false);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.items_list_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
        if (resultCode != Activity.RESULT_OK) {
            return;
        }

        switch (requestCode) {
            case REQUEST_OPTION:
                mSortOption = (SortOptions)
                        data.getSerializableExtra(SortOptionsDialogFragment.EXTRA_SORT_OPTION);
                updateUI();
                break;
            case REQUEST_NEW_ITEM:
                updateUI();
                break;
            case REQUEST_ITEM_DETAIL:
                updateUI();
            default:
                updateUI();
                break;
        }
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
                //Toast.makeText(getActivity(), "Added a new item", Toast.LENGTH_SHORT).show();
                Intent newItemIntent = new Intent(getActivity(), ItemEditActivity.class);
                Item newItem = new Item();
                StoredItems.getInstance(getContext()).addItem(newItem);
                newItemIntent.putExtra(ItemEditActivity.ITEM_ID, newItem.getId());
                startActivityForResult(newItemIntent, REQUEST_NEW_ITEM);
                return true;
            case R.id.menu_item_shopping_list:
                Intent shoppingIntent = new Intent(getActivity(), ShoppingListActivity.class);
                startActivity(shoppingIntent);
                return true;
            case R.id.menu_item_sort_by:
                Toast.makeText(getActivity(), "will filter", Toast.LENGTH_SHORT).show();
                SortOptionsDialogFragment dialog = new SortOptionsDialogFragment();
                dialog.setTargetFragment(ItemListFragment.this, REQUEST_OPTION);
                dialog.show(getFragmentManager(), DIALOG_OPTION);
                return true;
            case R.id.menu_item_settings:
                Toast.makeText(getActivity(), "will open settings", Toast.LENGTH_SHORT).show();
                Intent i = SettingsActivity.newIntent(getActivity());
                startActivity(i);
                return true;
            case R.id.menu_item_about:
                Intent a = new Intent(getActivity(), AboutActivity.class);
                startActivity(a);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void updateUI() {
        StoredItems storedItems = StoredItems.getInstance(getContext());
        List<Item> items;
        switch (mSortOption) {
            case EXPIRE:
                items = storedItems.sortByExpirationDate(ItemType.STORED);
                break;
            case NAME:
                items = storedItems.sortByName(ItemType.STORED);
                break;
            case PURCHASE:
                items = storedItems.sortByPurchaseDate(ItemType.STORED);
                break;
            default:
                items = storedItems.sortByExpirationDate(ItemType.STORED);
        }

        if (mItemAdapter == null) {
            mItemAdapter = new ItemAdapter(items);
            mRecyclerView.setAdapter(mItemAdapter);
        }
        else {
            mItemAdapter.updateItems(items);
            mItemAdapter.notifyDataSetChanged();
        }
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mItemNameTextView;
        private TextView mExpireDateTextView;
        private TextView mPurchaseDateTextView;
        private Item mItem;

        public ItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mItemNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_grocery_item_name_text_view);
            mExpireDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_grocery_item_expire_date_text_view);
            mPurchaseDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_grocery_item_purchase_date_text_view);
        }

        public void bindItem(Item item) {
            mItem = item;
            mItemNameTextView.setText(mItem.getName());
            // TODO: 3/2/17 get better date formatting
            // TODO: 3/2/17 change to string resource with insert formatting
            mExpireDateTextView.setText(getResources().getString(R.string.expire_date_label,
                    mDateFormat.format(mItem.getExpirationDate())));
            mPurchaseDateTextView.setText(getResources().getString(R.string.purchase_date_label,
                    mDateFormat.format(mItem.getPurchaseDate())));
        }

        @Override
        public void onClick(View v) {
            Intent intent = ItemPagerActivity.newIntent(getActivity(), mItem.getId(), mSortOption);
            startActivityForResult(intent, REQUEST_ITEM_DETAIL);
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

        public void updateItems(List<Item> items) {
            mItems = items;
            notifyDataSetChanged();
        }
    }
}
