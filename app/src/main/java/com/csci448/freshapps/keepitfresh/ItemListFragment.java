package com.csci448.freshapps.keepitfresh;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemListFragment extends Fragment {
    private static final String TAG = "ItemListFragment";

    private static final int REQUEST_OPTION = 0;
    private static final int REQUEST_NEW_ITEM = 1;
    private static final int REQUEST_ITEM_DETAIL = 2;
    private static final String DIALOG_OPTION = "option";
    private RecyclerView mRecyclerView;
    private ItemAdapter mItemAdapter;
    private SortOptions mSortOption = SortOptions.EXPIRE;
    private SimpleDateFormat mDateFormat;
    private String mLocation = "all";
    private List<Item> mItems;

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
        setUpItemTouchHelper();

        Drawable divider = ContextCompat.getDrawable(getActivity(), R.drawable.bar_divider);
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(divider);
        mRecyclerView.addItemDecoration(dividerItemDecoration);

        updateUI();

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        updateUI();
        if (resultCode != Activity.RESULT_OK) {
            updateUI();
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
                Intent newItemIntent = ItemPagerActivity.newIntent(
                        getActivity(), null, (ArrayList<Item>) mItems, true);
                startActivityForResult(newItemIntent, REQUEST_NEW_ITEM);
                return true;
            case R.id.menu_item_sort_by:
                SortOptionsDialogFragment dialog = new SortOptionsDialogFragment();
                dialog.setTargetFragment(ItemListFragment.this, REQUEST_OPTION);
                dialog.show(getFragmentManager(), DIALOG_OPTION);
                return true;
            case R.id.menu_item_settings:
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
        switch (mSortOption) {
            case EXPIRE:
                mItems = storedItems.sortByExpirationDate(ItemType.STORED, mLocation);
                break;
            case NAME:
                mItems = storedItems.sortByName(ItemType.STORED, mLocation);
                break;
            case PURCHASE:
                mItems = storedItems.sortByPurchaseDate(ItemType.STORED, mLocation);
                break;
            default:
                mItems = storedItems.sortByExpirationDate(ItemType.STORED, mLocation);
        }

        if (mItemAdapter == null) {
            mItemAdapter = new ItemAdapter(mItems);
            mRecyclerView.setAdapter(mItemAdapter);
//            mRecyclerView.addItemDecoration(new RecyclerViewDivider(getActivity()));
        }
        else {
            mItemAdapter.updateItems(mItems);
            mItemAdapter.notifyDataSetChanged();
        }
    }

    public void filterListByLocation(String location) {
        //if the string is equal to R.string.all ("All") then we want to show all items
        mLocation = location;
        if (location.equals(getString(R.string.all))) {
            mItems = StoredItems.getInstance(getActivity()).getItemList();
//            StoredItems.getInstance(getActivity()).sortByExpirationDate(ItemType.STORED);
        }
        else {
            mItems = StoredItems.getInstance(getActivity()).getItemsFromLocation(location);
        }
        mItemAdapter.updateItems(mItems);
    }

    /**
     * This touch helper is used to allow us to swipe on items in our recycler view to delete them.
     */
    private void setUpItemTouchHelper() {
        ItemTouchHelper.SimpleCallback simpleItemTouchCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                int swipedPosition = viewHolder.getAdapterPosition();
                ItemAdapter adapter = (ItemAdapter) mRecyclerView.getAdapter();
                Item undoItem = adapter.remove(swipedPosition);
                String snackbarMessage = undoItem.getName() + " " + getString(R.string.deleted_snackbar);
                Snackbar sb = Snackbar.make(getActivity().findViewById(R.id.fragment_container), snackbarMessage, Snackbar.LENGTH_LONG);
                sb.setAction(R.string.undo_button, new UndoListener(undoItem));
                sb.show();


            }

        };
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleItemTouchCallback);
        itemTouchHelper.attachToRecyclerView(mRecyclerView);
    }

    /**
     * Undo Listener that will save the item back to the database if it is deleted
     */
    private class UndoListener implements View.OnClickListener {
        private Item mUndoItem;

        UndoListener(Item item) {
            mUndoItem = item;
        }

        @Override
        public void onClick(View v) {
            StoredItems.getInstance(getActivity()).addItem(mUndoItem);
            updateUI();
        }
    }
    
    private class ItemHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        // TODO: 4/29/2017 change this view to look better and have more detail; use list_item_grocery_item.xml
        private TextView mItemNameTextView;
        private TextView mItemLocationTextView;
        private TextView mExpireDateTextView;
        private TextView mPurchaseDateTextView;

        private Item mItem;

        public ItemHolder(View itemView) {
            super(itemView);

            itemView.setOnClickListener(this);

            mItemNameTextView = (TextView)
                    itemView.findViewById(R.id.list_item_grocery_item_name_text_view);
            mItemLocationTextView = (TextView)
                    itemView.findViewById(R.id.list_item_grocery_item_location);
            mExpireDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_grocery_item_expire_date_text_view);
            mPurchaseDateTextView = (TextView)
                    itemView.findViewById(R.id.list_item_grocery_item_purchase_date_text_view);
        }

        public void bindItem(Item item) {
            mItem = item;
            mItemNameTextView.setText(mItem.getName());
            mItemLocationTextView.setText(mItem.getLocation());
            mExpireDateTextView.setText(getResources().getString(R.string.expire_date_label,
                    mDateFormat.format(mItem.getExpirationDate())));
            mPurchaseDateTextView.setText(getResources().getString(R.string.purchase_date_label,
                    mDateFormat.format(mItem.getPurchaseDate())));
        }

        @Override
        public void onClick(View v) {
            Log.i(TAG, mItem.getName() + " clicked");
            Intent intent = ItemPagerActivity.newIntent(getActivity(), mItem.getId(),
                    (ArrayList<Item>) mItems, false);
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
            holder.itemView.setBackgroundColor(Color.WHITE);

            // TODO: 5/7/17 decide on proper highlight values for levels of expiration
//            if (item.isExpired()) {
//                holder.itemView.setBackgroundResource(R.color.itemExpired);
//            }
//            else if (item.expiresInDays(1)) {
//                holder.itemView.setBackgroundResource(R.color.itemExpiresSoon1);
//            }
//            else if (item.expiresInDays(3)) {
//                holder.itemView.setBackgroundResource(R.color.itemExpiresSoon2);
//            }

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

        /**
         * Used with the swipe code to delete an item
         * @param position
         */
        public Item remove(int position) {
            Item item = mItems.get(position);
            StoredItems.getInstance(getActivity()).deleteItem(item);
            updateUI();
            return item;
        }

    }

    /**
     * This class adds a divider to the recycler view in between items
     *
     * This helps visibility, and makes items look distinct from each other
     */
//    private class RecyclerViewDivider extends RecyclerView.ItemDecoration {
//        private Drawable mDivider;
//
//        public RecyclerViewDivider(Context context) {
//            mDivider = ContextCompat.getDrawable(context, R.drawable.line_divider);
//        }
//
//        @Override
//        public void onDrawOver(Canvas c, RecyclerView parent, RecyclerView.State state) {
//            int left = parent.getPaddingLeft();
//            int right = parent.getWidth() - parent.getPaddingRight();
//
//            int childCount = parent.getChildCount();
//            for (int i = 0; i < childCount; i++) {
//                View child = parent.getChildAt(i);
//
//                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child.getLayoutParams();
//
//                int top = child.getBottom() + params.bottomMargin;
//                int bottom = top + mDivider.getIntrinsicHeight();
//
//                mDivider.setBounds(left, top, right, bottom);
//                mDivider.draw(c);
//            }
//        }
//    }
}
